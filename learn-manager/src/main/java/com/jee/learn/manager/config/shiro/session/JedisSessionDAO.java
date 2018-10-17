package com.jee.learn.manager.config.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import com.jee.learn.manager.config.SystemConfig;
import com.jee.learn.manager.support.cache.RedisService;
import com.jee.learn.manager.util.Constants;
import com.jee.learn.manager.util.net.ServletUtil;

/**
 * shiro-redis session管理器<br/>
 * 参考: https://www.jianshu.com/p/f85e50f41100
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月13日 下午5:38:17 ccp 新建
 */
public class JedisSessionDAO extends AbstractSessionDAO implements SessionDAO {

    private static final String SESSION_REQUEST_ATTRIBUTE_PREFIX = "session_";
    private static final String SESSION_UPDATE_PARAMETER = "updateSession";
    private static final String ASTERISK = "*";

    @Autowired
    private SystemConfig systemConfig;

    @Autowired
    private RedisService redisService;

    @Override
    protected Serializable doCreate(Session session) {
        HttpServletRequest request = ServletUtil.getRequest();

        if (request != null) {
            String uri = request.getServletPath();
            // 如果是静态文件，则不创建SESSION
            if (ServletUtil.isStaticFile(uri)) {
                return null;
            }
        }

        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        this.saveSession(session);

        logger.debug("create {} {}", session.getId(), request != null ? request.getRequestURI() : StringUtils.EMPTY);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }

        HttpServletRequest request = ServletUtil.getRequest();

        Session s = null;
        if (request != null) {
            String uri = request.getServletPath();
            // 如果是静态文件，则不获取SESSION
            if (ServletUtil.isStaticFile(uri)) {
                return null;
            }
            s = (Session) request.getAttribute(SESSION_REQUEST_ATTRIBUTE_PREFIX + sessionId);
        }
        if (s != null) {
            return s;
        }

        Session session = null;

        Object obj = redisService.getShiroValue(systemConfig.getShiroKeyPrefix() + sessionId.toString());
        if (obj != null) {
            session = (Session) obj;
            logger.debug("read {} {}", sessionId, request != null ? request.getRequestURI() : StringUtils.EMPTY);
        }

        if (request != null && session != null) {
            request.setAttribute(SESSION_REQUEST_ATTRIBUTE_PREFIX + sessionId, session);
        }

        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            return;
        }

        HttpServletRequest request = ServletUtil.getRequest();

        if (request != null) {
            String uri = request.getServletPath();
            // 如果是静态文件，则不更新SESSION
            if (ServletUtil.isStaticFile(uri)) {
                return;
            }
            // 如果是静态视图文件，则不更新SESSION
            if (ServletUtil.isViewFile(uri)) {
                return;
            }
            // 手动控制不更新SESSION
            if (Constants.N.equals(request.getParameter(SESSION_UPDATE_PARAMETER))) {
                return;
            }
        }
        this.saveSession(session);
        logger.debug("update {} {}", session.getId(), request != null ? request.getRequestURI() : StringUtils.EMPTY);

    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }

        String redisKey = systemConfig.getShiroKeyPrefix() + session.getId().toString();
        redisService.flushShiroExpire(redisKey, 1L, TimeUnit.MILLISECONDS);
        logger.debug("delete {} ", session.getId());

    }

    @Override
    public Collection<Session> getActiveSessions() {
        return getActiveSessions(true);
    }

    //////// SessionDAO interface ////////
    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave) {
        return getActiveSessions(includeLeave, null, null);
    }

    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
        Set<Session> sessions = new HashSet<Session>();
        Set<String> keys = redisService.getShiroRedisTemplate().keys(systemConfig.getShiroKeyPrefix() + ASTERISK);

        if (CollectionUtils.isNotEmpty(keys)) {
            Long minute = systemConfig.getSessionTimeoutClean() / 60000L;
            for (String key : keys) {
                Session session = (Session) redisService.getShiroValue(key);
                if (session == null) {
                    continue;
                }

                boolean isActive = isActiveSession(session, includeLeave, principal, filterSession, minute.intValue());
                if (isActive) {
                    sessions.add(session);
                }
            }
        }

        logger.info("getActiveSessions size: {} ", sessions.size());
        return sessions;
    }

    /////// custom ///////

    private void saveSession(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            logger.debug("session or session id is null");
            return;
        }
        String key = systemConfig.getShiroKeyPrefix() + session.getId().toString();
        session.setTimeout(systemConfig.getSessionTimeout());
        redisService.putShiroValue(key, session, systemConfig.getSessionTimeout(), TimeUnit.MILLISECONDS);
    }

}

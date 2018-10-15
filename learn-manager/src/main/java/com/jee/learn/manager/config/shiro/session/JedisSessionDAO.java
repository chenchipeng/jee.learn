package com.jee.learn.manager.config.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import com.jee.learn.manager.support.cache.RedisService;
import com.jee.learn.manager.util.Constants;
import com.jee.learn.manager.util.net.ServletUtil;

/**
 * shiro-redis session管理器<br/> 没用!!!!!!!!!!!!!!!!!!!
 * 参考: https://www.jianshu.com/p/f85e50f41100
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月13日 下午5:38:17 ccp 新建
 */
public class JedisSessionDAO extends AbstractSessionDAO implements SessionDAO {

    private static final String sessionKeyPrefix = "shiro:session_";

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

        String redisKey = sessionKeyPrefix + session.getId().toString();
        redisService.setShiroValue(redisKey, sessionKeyPrefix, session, session.getTimeout());

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
            s = (Session) request.getAttribute("session_" + sessionId);
        }
        if (s != null) {
            return s;
        }

        Session session = null;

        Object sessionByte = redisService.getShiroValue(sessionKeyPrefix + sessionId.toString(), sessionKeyPrefix);
        if (sessionByte != null) {
            session = (Session) sessionByte;
            logger.debug("read {} {}", sessionId, request != null ? request.getRequestURI() : StringUtils.EMPTY);
        }

        if (request != null && session != null) {
            request.setAttribute("session_" + sessionId, session);
        }

        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            return;
        }

        HttpServletRequest request = ServletUtil.getRequest();
        logger.debug("update {} {}", session.getId(), request != null ? request.getRequestURI() : StringUtils.EMPTY);
        
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
            if (Constants.N.equals(request.getParameter("updateSession"))) {
                return;
            }
        }

        String redisKey = sessionKeyPrefix + session.getId().toString();
        redisService.flushShiroExpire(redisKey, session.getTimeout());

    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        logger.debug("delete {} ", session.getId());

        String redisKey = sessionKeyPrefix + session.getId().toString();
        redisService.flushShiroExpire(redisKey, 1L);
    }

    /**
     * 获取活动会话
     * 
     * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
     * @param principal 根据登录者对象获取活动会话
     * @param filterSession 不为空，则过滤掉（不包含）这个会话。
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
        Set<Session> sessions = new HashSet<>();

        Set<String> keys = redisService.getKeys(sessionKeyPrefix + "*");
        for (String key : keys) {
            Session session = (Session) redisService.getShiroValue(key, sessionKeyPrefix);
            if (session != null) {
                sessions.add(session);
            }
        }

        logger.info("getActiveSessions size: {} ", sessions.size());
        return sessions;
    }

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        try {
            return super.readSession(sessionId);
        } catch (UnknownSessionException e) {
            return null;
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return getActiveSessions(true);
    }

    /**
     * 获取活动会话
     * 
     * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave) {
        return getActiveSessions(includeLeave, null, null);
    }

}

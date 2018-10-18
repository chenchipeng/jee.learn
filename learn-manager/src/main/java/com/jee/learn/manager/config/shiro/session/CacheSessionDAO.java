package com.jee.learn.manager.config.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import com.jee.learn.manager.config.SystemConfig;
import com.jee.learn.manager.config.shiro.ShiroContants;
import com.jee.learn.manager.support.cache.CacheConstants;
import com.jee.learn.manager.support.cache.EhcacheService;
import com.jee.learn.manager.util.Constants;
import com.jee.learn.manager.util.base.ObjectUtil;
import com.jee.learn.manager.util.net.ServletUtil;

/**
 * ehcache session dao
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月16日 上午9:56:49 ccp 新建
 */
public class CacheSessionDAO extends CachingSessionDAO implements CustomSessionDAO {

    private static final String SESSION_CACHE_HASHKEY = "sessionCache";

    @Autowired
    private SystemConfig systemConfig;

    @Autowired
    private EhcacheService ehcacheService;

    public CacheSessionDAO() {
        super();
    }

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
        logger.debug("doCreate {} {}", session, request != null ? request.getRequestURI() : StringUtils.EMPTY);
        return session.getId();
    }

    @Override
    protected void doUpdate(Session session) {
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
            if (Constants.N.equals(request.getParameter(ShiroContants.SESSION_UPDATE_PARAMETER))) {
                return;
            }
        }
        this.saveSession(session);
        logger.debug("update {} {}", session.getId(), request != null ? request.getRequestURI() : StringUtils.EMPTY);
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
            s = (Session) request.getAttribute(ShiroContants.SESSION_REQUEST_ATTRIBUTE_PREFIX + sessionId);
        }
        if (s != null) {
            return s;
        }

        Session session = null;

        byte[] data = (byte[]) ehcacheService.get(CacheConstants.EHCACHE_SHIRO, SESSION_CACHE_HASHKEY,
                sessionId.toString());
        if (data != null && data.length > 0) {
            session = (Session) ObjectUtil.unserialize(data);
            logger.debug("read {} {}", sessionId, request != null ? request.getRequestURI() : StringUtils.EMPTY);
        }

        if (request != null && session != null) {
            request.setAttribute(ShiroContants.SESSION_REQUEST_ATTRIBUTE_PREFIX + sessionId, session);
        }
        return session;
    }

    @Override
    protected void doDelete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }

        ehcacheService.delete(CacheConstants.EHCACHE_SHIRO, SESSION_CACHE_HASHKEY, session.getId().toString());
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
    @SuppressWarnings("unchecked")
    public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
        Set<Session> sessions = new HashSet<Session>();
        Map<String, Object> map = (Map<String, Object>) ehcacheService.get(CacheConstants.EHCACHE_SHIRO,
                SESSION_CACHE_HASHKEY);

        if (map != null && !map.isEmpty()) {
            Long minute = systemConfig.getSessionTimeoutClean() / 60000L;

            for (Entry<String, Object> entity : map.entrySet()) {
                byte[] data = (byte[]) entity.getValue();
                if (data == null || data.length == 0) {
                    continue;
                }
                Session session = (Session) ObjectUtil.unserialize(data);
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
        session.setTimeout(systemConfig.getSessionTimeout());
        ehcacheService.put(CacheConstants.EHCACHE_SHIRO, SESSION_CACHE_HASHKEY, session.getId().toString(),
                ObjectUtil.serialize(session));
    }

}

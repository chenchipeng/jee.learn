package com.jee.learn.manager.config.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import com.jee.learn.manager.config.SystemConfig;
import com.jee.learn.manager.config.shiro.ShiroContants;
import com.jee.learn.manager.util.Constants;
import com.jee.learn.manager.util.net.ServletUtil;

/**
 * ehcache session dao
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月16日 上午9:56:49 ccp 新建
 */
public class CacheSessionDAO extends EnterpriseCacheSessionDAO implements SessionDAO {

    @Autowired
    private SystemConfig systemConfig;

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
        super.doCreate(session);
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
            if (Constants.N.equals(request.getParameter(ShiroContants.UPDATE_SESSION_PARAM))) {
                return;
            }
        }
        super.doUpdate(session);
        logger.debug("update {} {}", session.getId(), request != null ? request.getRequestURI() : StringUtils.EMPTY);
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return super.doReadSession(sessionId);
    }

    @Override
    protected void doDelete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }

        super.doDelete(session);
        logger.debug("delete {} ", session.getId());
    }

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        try {
            Session s = null;
            HttpServletRequest request = ServletUtil.getRequest();
            if (request != null) {
                String uri = request.getServletPath();
                // 如果是静态文件，则不获取SESSION
                if (ServletUtil.isStaticFile(uri)) {
                    return null;
                }
                s = (Session) request.getAttribute(ShiroContants.SESSION_PARAM_PREFIX + sessionId);
            }
            if (s != null) {
                return s;
            }

            Session session = super.readSession(sessionId);
            logger.debug("readSession {} {}", sessionId, request != null ? request.getRequestURI() : StringUtils.EMPTY);

            if (request != null && session != null) {
                request.setAttribute(ShiroContants.SESSION_PARAM_PREFIX + sessionId, session);
            }

            return session;
        } catch (UnknownSessionException e) {
            return null;
        }
    }

    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave) {
        return getActiveSessions(includeLeave, null, null);
    }

    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
        // 如果包括离线，并无登录者条件。
        if (includeLeave && principal == null) {
            return getActiveSessions();
        }
        Long minute = systemConfig.getSessionTimeoutClean() / 60000L;
        Set<Session> sessions = new HashSet<>();
        for (Session session : getActiveSessions()) {
            boolean isActive = isActiveSession(session, includeLeave, principal, filterSession, minute.intValue());
            if (isActive) {
                sessions.add(session);
            }
        }
        return sessions;
    }

}

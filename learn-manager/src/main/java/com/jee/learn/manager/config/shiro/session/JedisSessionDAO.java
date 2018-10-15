package com.jee.learn.manager.config.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.jee.learn.manager.support.cache.RedisService;
import com.jee.learn.manager.util.Constants;
import com.jee.learn.manager.util.net.ServletUtil;
import com.jee.learn.manager.util.time.DateUtil;

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

    private static final String sessionKeyPrefix = "shiro:session_";
    private static final String sessionKey = "session";

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
        Serializable sessionId = this.generateSessionId(session);
        assignSessionId(session, sessionId);

        String redisKey = sessionKeyPrefix + session.getId().toString();
        // 获取登录者编号
        PrincipalCollection pc = (PrincipalCollection) session
                .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        String principalId = pc != null ? pc.getPrimaryPrincipal().toString() : StringUtils.EMPTY;

        String hashValue = session.getId().toString() + "|" + principalId + "|" + session.getTimeout() + "|"
                + session.getLastAccessTime().getTime();
        redisService.setShiroValue(redisKey, sessionKey, hashValue, session.getTimeout());
        redisService.setShiroValue(redisKey, session.getId().toString(), session, session.getTimeout());
        logger.debug("create {} {}", session.getId(), request != null ? request.getRequestURI() : StringUtils.EMPTY);

        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {

        if (sessionId == null) {
            return null;
        }

        Session s = null;
        HttpServletRequest request = ServletUtil.getRequest();
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

        Object sessionByte = redisService.getShiroValue(sessionKeyPrefix + sessionId.toString(), sessionId.toString());
        if (sessionByte != null) {
            logger.debug("read {} {}", sessionId, request != null ? request.getRequestURI() : StringUtils.EMPTY);
            session = (Session) sessionByte;
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
        logger.debug("update {} {}", session.getId(), request != null ? request.getRequestURI() : StringUtils.EMPTY);

    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }

        String redisKey = sessionKeyPrefix + session.getId().toString();
        redisService.flushShiroExpire(redisKey, 1L);
        logger.debug("delete {} ", session.getId());
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
            String str = (String) redisService.getShiroValue(key, sessionKey);
            if (StringUtils.isNotBlank(str)) {
                String[] ss = str.split("|");

                if (ss != null && ss.length == 4) {
                    SimpleSession session = new SimpleSession();
                    session.setId(ss[0]);
                    session.setAttribute("principalId", ss[1]);
                    session.setTimeout(Long.valueOf(ss[2]));
                    session.setLastAccessTime(new Date(Long.valueOf(ss[3])));
                    try {
                        // 验证SESSION
                        session.validate();
                    } catch (Exception e) {
                        // SESSION验证失败
                        redisService.flushShiroExpire(key, 1L);
                    }

                    boolean isActiveSession = false;
                    // 不包括离线并符合最后访问时间小于等于3分钟条件。
                    if (includeLeave
                            || DateUtil.isSameTime(DateUtil.subMinutes(new Date(), 3), session.getLastAccessTime())) {
                        isActiveSession = true;
                    }
                    // 符合登陆者条件。
                    if (principal != null) {
                        PrincipalCollection pc = (PrincipalCollection) session
                                .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                        if (principal.toString()
                                .equals(pc != null ? pc.getPrimaryPrincipal().toString() : StringUtils.EMPTY)) {
                            isActiveSession = true;
                        }
                    }
                    // 过滤掉的SESSION
                    if (filterSession != null && filterSession.getId().equals(session.getId())) {
                        isActiveSession = false;
                    }
                    if (isActiveSession) {
                        sessions.add(session);
                    }

                } else {
                    // 存储的SESSION不符合规则
                    redisService.flushShiroExpire(key, 1L);
                }

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
        return this.getActiveSessions(true);
    }

    /**
     * 获取活动会话
     * 
     * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave) {
        return this.getActiveSessions(includeLeave, null, null);
    }

}

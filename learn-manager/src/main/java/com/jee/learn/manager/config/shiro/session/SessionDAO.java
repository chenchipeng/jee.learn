package com.jee.learn.manager.config.shiro.session;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jee.learn.manager.config.SystemConfig;
import com.jee.learn.manager.util.time.DateUtil;

public interface SessionDAO extends org.apache.shiro.session.mgt.eis.SessionDAO {

    public Logger logger = LoggerFactory.getLogger(SessionDAO.class);

    /**
     * 获取活动会话
     * 
     * @param includeLeave 是否包括离线（最后访问时间大于 {@link SystemConfig#getSessionTimeoutClean()} 为离线会话）
     * @return
     */
    public Collection<Session> getActiveSessions(boolean includeLeave);

    /**
     * 获取活动会话
     * 
     * @param includeLeave 是否包括离线（最后访问时间大于 {@link SystemConfig#getSessionTimeoutClean()} 为离线会话）
     * @param principal 根据登录者对象获取活动会话
     * @param filterSession 不为空，则过滤掉（不包含）这个会话。
     * @return
     */
    public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession);

    /**
     * 检查session是否活跃
     * 
     * @param session 需要校验的sesion
     * @param includeLeave 是否包括离线（最后访问时间大于 {@link SystemConfig#getSessionTimeoutClean()} 为离线会话）
     * @param principal 根据登录者对象获取活动会话
     * @param filterSession 不为空，则过滤掉（不包含）这个会话。
     * @param amount 离线时间
     * @return
     */
    default boolean isActiveSession(Session session, boolean includeLeave, Object principal, Session filterSession,
            int amount) {

        boolean isActiveSession = false;
        // 不包括离线并符合最后访问时间小于等于离线时间
        if (includeLeave || DateUtil.isSameTime(DateUtil.subMinutes(new Date(), amount), session.getLastAccessTime())) {
            isActiveSession = true;
        }
        // 符合登陆者条件。
        if (principal != null) {
            PrincipalCollection pc = (PrincipalCollection) session
                    .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (principal.toString().equals(pc != null ? pc.getPrimaryPrincipal().toString() : StringUtils.EMPTY)) {
                isActiveSession = true;
            }
        }
        // 过滤掉的SESSION
        if (filterSession != null && filterSession.getId().equals(session.getId())) {
            isActiveSession = false;
        }

        return isActiveSession;
    }

}

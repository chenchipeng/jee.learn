/**
 * Copyright &copy; 2015-2020 <a href="http://www.chinaskin.net/">chnskin</a> All rights reserved.
 */
package com.jee.learn.component.security.realm;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.jee.learn.common.security.Encodes;
import com.jee.learn.common.util.Constant;
import com.jee.learn.component.security.CustomCredentialsMatcher;
import com.jee.learn.component.security.CustomUsernamePasswordToken;
import com.jee.learn.component.security.Principal;
import com.jee.learn.component.session.SessionDAO;
import com.jee.learn.component.util.UserUtils;
import com.jee.learn.component.util.ValidateCodeUtils;
import com.jee.learn.component.web.ValidateCodeServlet;
import com.jee.learn.config.SysConfigBean;
import com.jee.learn.model.sys.SysMenu;
import com.jee.learn.model.sys.SysUser;
import com.jee.learn.service.sys.SysUserService;

/**
 * 系统安全认证实现类
 * 
 * @author chnskin
 * @version 2014-7-5
 */
@Component
@DependsOn({ "sessionDAO" })
public class SystemAuthorizingRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysConfigBean configBean;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SessionDAO sessionDao;
    @Autowired
    private ValidateCodeUtils validateCodeUtils;
    @Autowired
    private UserUtils userUtils;

    /**
     * 认证回调函数, 登录时调用<br/>
     * 主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
        CustomUsernamePasswordToken token = (CustomUsernamePasswordToken) authcToken;
        int activeSessionSize = sessionDao.getActiveSessions(false).size();
        if (logger.isDebugEnabled()) {
            logger.debug("login submit, active session size: {}, username: {}", activeSessionSize, token.getUsername());
        }

        boolean mobile = false;

        // 校验登录验证码(手机不做处理)
        if (!mobile && validateCodeUtils.isValidateCodeLogin(token.getUsername(), false, false)) {
            Session session = userUtils.getSession();
            String code = (String) session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
            if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)) {
                throw new AuthenticationException("msg:验证码错误, 请重试.");
            }
        }

        // 校验用户名密码
        SysUser user = userService.findByLoginName(token.getUsername());
        if (user != null) {
            logger.debug("user id = {}", user.getId());
            if (Constant.NO_0.equals(user.getLoginFlag())) {
                throw new AuthenticationException("msg:该帐号已禁止登录.");
            }
            byte[] salt = Encodes.decodeHex(user.getPassword().substring(0, 16));
            return new SimpleAuthenticationInfo(new Principal(user, token.isMobileLogin(), token.isApiLogin()),
                    user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
        } else {
            logger.debug("user == null");
            return null;
        }
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        Principal principal = null;
        try {
            principal = (Principal) getAvailablePrincipal(principals);
        } catch (Exception e) {
            logger.info("principal 转换异常", e);
            userUtils.getSubject().logout();
            throw new AuthenticationException("msg: principal 转换异常，请重新登录或联系管理员。");
        }

        // 获取当前已登录的用户 ,并判断是否允许多账号登陆
        if (!configBean.isMultiAccountLogin()) {
            Collection<Session> sessions = sessionDao.getActiveSessions(true, principal, userUtils.getSession());
            if (sessions.size() > 0) {
                if (userUtils.getSubject().isAuthenticated()) {
                    // 如果是登录进来的，则踢出已在线用户
                    for (Session session : sessions) {
                        sessionDao.delete(session);
                    }
                } else {
                    // 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
                    userUtils.getSubject().logout();
                    throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
                }
            }
        }
        SysUser user = userService.findByLoginName(principal.getLoginName());
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            List<SysMenu> list = userUtils.getMenuList();
            for (SysMenu menu : list) {
                if (StringUtils.isNotBlank(menu.getPermission())) {
                    // 添加基于Permission的权限信息
                    for (String permission : StringUtils.split(menu.getPermission(), ",")) {
                        info.addStringPermission(permission);
                    }
                }
            }
            // 添加用户权限
            info.addStringPermission("user");
            // 添加用户角色信息
            // for (SysRole role : user.getRoleList()) {
            // info.addRole(role.getEnname());
            // }
            // 更新登录IP和时间
            // userService.updateUserLoginInfo(user);
            // 记录登录日志
            // LogUtils.saveLog(Servlets.getRequest(), "系统登录");
            return info;
        } else {
            return null;
        }
    }

    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        authorizationValidate(permission);
        super.checkPermission(permission, info);
    }

    @Override
    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermitted(permissions, info);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        authorizationValidate(permission);
        return super.isPermitted(principals, permission);
    }

    @Override
    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermittedAll(permissions, info);
    }

    /**
     * 授权验证方法
     * 
     * @param permission
     */
    private void authorizationValidate(Permission permission) {
        // 模块授权预留接口
    }

    /**
     * 设定密码校验的Hash算法与迭代次数
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        // HashedCredentialsMatcher matcher = new
        // HashedCredentialsMatcher(EntryptUtils.HASH_ALGORITHM);
        // matcher.setHashIterations(EntryptUtils.HASH_INTERATIONS);
        // matcher.setStoredCredentialsHexEncoded(true);
        // setCredentialsMatcher(matcher);
        setCredentialsMatcher(CustomCredentialsMatcher.initHashedCredentialsMatcher());
    }

}

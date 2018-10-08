package com.jee.learn.manager.config.shiro;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jee.learn.manager.domain.sys.SysUser;
import com.jee.learn.manager.service.sys.SysUserService;
import com.jee.learn.manager.util.Constants;

@Component
public class CustomRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomRealm.class);
    private static final String ENTITY_PROPERTIES_LOGIN_NAME = "loginName";

    private static final String MSG_DISABLED = "账号禁止登录";
    private static final String MSG_INVALID_USERNAME = "账号不存在";
    private static final String MSG_INVALID_PASSWORD = "账号密码错误";

    @Autowired
    private SysUserService userService;

    /**
     * 获取授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) throws AuthenticationException {
        LOGGER.debug("————权限认证————");

        CustomPrincipal principal = (CustomPrincipal) getAvailablePrincipal(principals);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 获得该用户角色
        SysUser user = userService.findOne(ENTITY_PROPERTIES_LOGIN_NAME, principal.getLoginName());

        return info;

    }

    /**
     * 获取身份验证信息 Shiro中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的。
     *
     * @param authenticationToken 用户身份信息 token
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        LOGGER.debug("————身份认证————");
        CustomToken customToken = (CustomToken) token;

        // 验证账号密码
        SysUser user = userService.findOne(ENTITY_PROPERTIES_LOGIN_NAME, customToken.getUsername());
        if (user == null) {
            throw new AccountException(MSG_INVALID_USERNAME);
        }
        if (!Constants.Y.equals(user.getLoginFlag())) {
            throw new AccountException(MSG_DISABLED);
        }
        if (!user.getPassword().equals(new String(customToken.getPassword()))) {
            throw new AccountException(MSG_INVALID_PASSWORD);
        }

        return new SimpleAuthenticationInfo(customToken.getPrincipal(), user.getPassword(), getName());
    }

}

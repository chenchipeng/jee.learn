package com.jee.learn.manager.security.shiro;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jee.learn.manager.config.SystemConfig;
import com.jee.learn.manager.config.shiro.ShiroContants;
import com.jee.learn.manager.config.shiro.security.CustomCredentialsMatcher;
import com.jee.learn.manager.config.shiro.security.CustomPrincipal;
import com.jee.learn.manager.config.shiro.security.CustomToken;
import com.jee.learn.manager.config.shiro.session.CustomSessionDAO;
import com.jee.learn.manager.domain.sys.SysMenu;
import com.jee.learn.manager.domain.sys.SysRole;
import com.jee.learn.manager.domain.sys.SysUser;
import com.jee.learn.manager.security.UserUtil;
import com.jee.learn.manager.support.servlet.captcha.CaptchaUtil;
import com.jee.learn.manager.util.Constants;
import com.jee.learn.manager.util.text.EncodeUtil;

/**
 * 自定义shiro realm<br/>
 * 注意: 在操作业务对象时, 不要注入service层! https://blog.csdn.net/libraryhu/article/details/50224269
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月25日 下午5:41:40 ccp 新建
 */
@Component
public class CustomRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomRealm.class);

    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private CustomSessionDAO sessionDao;
    @Autowired
    private CaptchaUtil captchaUtil;
    @Autowired
    private UserUtil userUtil;

    /**
     * 获取授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) throws AuthenticationException {
        LOGGER.debug("------权限认证------");
        CustomPrincipal principal = (CustomPrincipal) getAvailablePrincipal(principals);

        // 获取当前已登录的用户
        if (!systemConfig.isMultiAccountLogin()) {
            Collection<Session> sessions = sessionDao.getActiveSessions(true, principal, ShiroUtil.getSession());
            if (sessions.size() > 0) {
                if (ShiroUtil.getSubject().isAuthenticated()) {
                    // 如果是登录进来的，则踢出已在线用户
                    for (Session session : sessions) {
                        sessionDao.delete(session);
                    }
                } else {
                    // 记住我进来的，并且当前用户已登录，则退出当前用户提示信息
                    ShiroUtil.getSubject().logout();
                    throw new AuthenticationException(ShiroContants.MESSAGE_PREFIX + ShiroContants.MULTI_LOGIN_ERROR);
                }
            }
        }

        // 获得该用户角色
        SysUser user = userUtil.findOneByLoginName(principal.getLoginName());
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

            // 添加用户权限
            info.addStringPermission("user");

            // 添加基于Permission的用户菜单权限
            List<SysMenu> menuList = userUtil.getMenuList();
            for (SysMenu sysMenu : menuList) {
                if (StringUtils.isNotBlank(sysMenu.getPermission())) {
                    for (String permission : StringUtils.split(sysMenu.getPermission(), Constants.COMMA)) {
                        info.addStringPermission(permission);
                    }
                }
            }

            // 添加用户角色
            List<SysRole> roleList = userUtil.getRoleList();
            for (SysRole sysRole : roleList) {
                info.addRole(sysRole.getEnname());
            }

            return info;
        }

        return null;
    }

    /**
     * 获取身份验证信息 Shiro中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的。
     *
     * @param authenticationToken 用户身份信息 token
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        LOGGER.debug("------身份认证------");
        CustomToken customToken = (CustomToken) token;

        // 统计活跃数
        if (LOGGER.isDebugEnabled()) {
            int activeSessionSize = sessionDao.getActiveSessions(false).size();
            LOGGER.debug("login submit, active session size: {}, username: {}", activeSessionSize,
                    customToken.getUsername());
        }

        // 校验登录验证码
        if (captchaUtil.isCaptchaLogin(customToken.getUsername(), false, false)) {
            Session session = ShiroUtil.getSession();
            String code = (String) session.getAttribute(CustomToken.DEFAULT_CAPTCHA_PARAM);
            if (customToken.getCaptcha() == null
                    || !customToken.getCaptcha().toUpperCase().equals(code.toUpperCase())) {
                throw new AuthenticationException(ShiroContants.MESSAGE_PREFIX + ShiroContants.CAPTCH_ERROR);
            }
        }

        // 校验账号密码
        SysUser user = userUtil.findOneByLoginName(customToken.getUsername());
        if (user != null) {
            if (!Constants.Y.equals(user.getLoginFlag())) {
                throw new AuthenticationException(ShiroContants.MESSAGE_PREFIX + ShiroContants.INVALID_USERNAME_ERROR);
            }
            // 密码前八位作为盐值
            byte[] salt = EncodeUtil.decodeHex(user.getPassword().substring(0, ShiroContants.SALT_SIZE));
            return new SimpleAuthenticationInfo(new CustomPrincipal(user.getId(), user.getLoginName(), user.getName()),
                    user.getPassword().substring(ShiroContants.SALT_SIZE), ByteSource.Util.bytes(salt), getName());
        } else {
            throw new AuthenticationException(ShiroContants.MESSAGE_PREFIX + ShiroContants.USERNAME_PASSWORD_ERROR);
        }
    }

    /**
     * 设定Password校验.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        // 该句作用是重写shiro的密码验证，让shiro用我自己的验证
        setCredentialsMatcher(new CustomCredentialsMatcher());
    }

}

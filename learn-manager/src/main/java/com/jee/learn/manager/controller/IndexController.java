package com.jee.learn.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.jee.learn.manager.config.SystemConfig;
import com.jee.learn.manager.config.shiro.ShiroContants;
import com.jee.learn.manager.config.shiro.security.CustomFormAuthenticationFilter;
import com.jee.learn.manager.config.shiro.security.CustomPrincipal;
import com.jee.learn.manager.config.shiro.security.CustomToken;
import com.jee.learn.manager.config.shiro.session.CustomSessionDAO;
import com.jee.learn.manager.security.ShiroUtil;
import com.jee.learn.manager.service.sys.SysUserService;
import com.jee.learn.manager.support.servlet.captcha.CaptchaUtil;
import com.jee.learn.manager.util.Constants;
import com.jee.learn.manager.util.net.CookieUtils;

/**
 * 登陆/首页 页面controller
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月8日 上午11:19:05 ccp 新建
 */
@Controller
public class IndexController extends BaseController {

    private static final String LOGINED_COOKIE_NAME_SUFFIX = ".isLogined";

    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private CaptchaUtil captchaUtil;
    @Autowired
    private CustomSessionDAO sessionDao;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录页面
     * 
     * @param model
     * @return
     */
    @GetMapping("${system.authc-path}/login")
    public String loginPage(Model model) {

        // 统计session活跃数
        if (logger.isDebugEnabled()) {
            logger.debug("login, active session size: {}", sessionDao.getActiveSessions(false).size());
        }

        // 如果已登录，再次访问登录页，则退出原账号。
        if (systemConfig.isNotAllowRefreshIndex()) {
            CookieUtils.setCookie(systemConfig.getApplicationName() + LOGINED_COOKIE_NAME_SUFFIX, Constants.N,
                    CookieUtils.THREE_MINUTE_COOKIE);
        }

        // 如果已经登录，则跳转到管理首页
        CustomPrincipal principal = ShiroUtil.getPrincipal();
        if (principal != null) {
            return REDIRECT + systemConfig.getAuthcPath();
        }

        model.addAttribute("name", systemConfig.getName());
        return "main/login";
    }

    /**
     * 通过shiro拦截器执行登录验证, 登录失败执行此方法
     * 
     * @param request
     * @param model
     * @return
     */
    @PostMapping("${system.authc-path}/login")
    public String loginFail(HttpServletRequest request, Model model) {

        // 如果已经登录，则跳转到管理首页
        CustomPrincipal principal = ShiroUtil.getPrincipal();
        if (principal != null) {
            return REDIRECT + systemConfig.getAuthcPath();
        }

        // 获取并设置登录参数信息
        String username = WebUtils.getCleanParam(request, CustomFormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        boolean rememberMe = WebUtils.isTrue(request, CustomFormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
        String exception = (String) request
                .getAttribute(CustomFormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String message = (String) request.getAttribute(CustomFormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
        if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")) {
            message = ShiroContants.USERNAME_PASSWORD_ERROR;
        }
        model.addAttribute(CustomFormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
        model.addAttribute(CustomFormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
        model.addAttribute(CustomFormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
        model.addAttribute(CustomFormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

        // 统计session活跃数
        if (logger.isDebugEnabled()) {
            logger.debug("login fail, active session size: {} message: {} exception: {}",
                    sessionDao.getActiveSessions(false).size(), message, exception);
        }

        // 非授权异常，登录失败，验证码加1。
        if (!UnauthorizedException.class.getName().equals(exception)) {
            model.addAttribute("isCaptchaLogin", captchaUtil.isCaptchaLogin(username, true, false));
        }

        // 验证失败清空验证码
        request.getSession().setAttribute(CustomToken.DEFAULT_CAPTCHA_PARAM, StringUtils.EMPTY);
        return "main/login";
    }

    /**
     * 打开首页
     * 
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @GetMapping("${system.authc-path}")
    public String indexPage(Model model) {

        // 统计session活跃人数
        if (logger.isDebugEnabled()) {
            logger.debug("show index, active session size: {}", sessionDao.getActiveSessions(false).size());
        }

        // 登录成功后，验证码计算器清零
        CustomPrincipal principal = ShiroUtil.getPrincipal();
        captchaUtil.isCaptchaLogin(principal.getLoginName(), false, true);

        // 如果已登录，再次访问登录页，则退出原账号
        if (systemConfig.isNotAllowRefreshIndex()) {
            String logined = CookieUtils.getCookie(systemConfig.getApplicationName() + LOGINED_COOKIE_NAME_SUFFIX);

            if (StringUtils.equals(logined, Constants.Y)) {
                ShiroUtil.getSubject().logout();
                return REDIRECT + systemConfig.getAuthcPath() + "/login";
            }
            if (StringUtils.isBlank(logined) || Constants.N.equals(logined)) {
                CookieUtils.setCookie(systemConfig.getApplicationName() + LOGINED_COOKIE_NAME_SUFFIX, Constants.Y,
                        CookieUtils.THREE_MINUTE_COOKIE);
            }
        }

        // 更新登录IP和时间
        sysUserService.updateUserLoginInfo(principal.getId());

        // 记录登录日志

        return "hello";
    }

    @GetMapping("${system.authc-path}/logout")
    public String logout() {
        CustomPrincipal principal = ShiroUtil.getPrincipal();
        // 如果已经登录，则跳转到管理首页
        if (principal != null) {
            ShiroUtil.getSubject().logout();
        }
        return REDIRECT + systemConfig.getAuthcPath() + "/login";
    }

}

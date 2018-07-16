package com.jee.learn.controller.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jee.learn.common.config.Global;
import com.jee.learn.common.mapper.JsonMapper;
import com.jee.learn.common.util.Constant;
import com.jee.learn.common.util.IdGen;
import com.jee.learn.component.security.LoginFormAuthenticationFilter;
import com.jee.learn.component.security.Principal;
import com.jee.learn.component.security.realm.SystemAuthorizingRealm;
import com.jee.learn.component.session.SessionDAO;
import com.jee.learn.component.util.UserUtils;
import com.jee.learn.component.util.ValidateCodeUtils;
import com.jee.learn.component.web.ValidateCodeServlet;
import com.jee.learn.config.SysConfigBean;
import com.jee.learn.dto.MenuDto;
import com.jee.learn.model.sys.SysMenu;
import com.jee.learn.model.sys.SysUser;
import com.jee.learn.service.sys.SysMenuService;
import com.jee.learn.util.CookieUtils;
import com.jee.learn.util.TreeUtils;
import com.jee.learn.util.json.AjaxJson;

/**
 * 登录Controller
 * 
 * @author chnskin
 * @version 2013-5-31
 */
@Controller
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysConfigBean configBean;
    @Autowired
    private SessionDAO sessionDAO;
    @Autowired
    private SysMenuService menuService;
    @Autowired
    private IdGen idGen;
    @Autowired
    private ValidateCodeUtils validateCodeUtils;
    @Autowired
    private UserUtils userUtils;

    /**
     * 打开登陆页面
     */
    @RequestMapping(value = "${sys.adminPath}/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

        Principal principal = userUtils.getPrincipal();
        logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());

        // 刷新管理端页面是否清除登陆状态，由sys.notAllowRefreshIndex配置
        if (Constant.TRUE.equals(Global.getConfig("sys.notAllowRefreshIndex"))) {
            CookieUtils.setCookie(response, "LOGINED", "false");
        }

        // 如果已经登录，则跳转到管理首页
        if (principal != null && !principal.isMobileLogin()) {
            return "redirect:" + configBean.getAdminPath();
        }

        // 获取跳转到login之前的URL
        SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        
        // 如果是手机没有登录跳转到到login，则返回JSON字符串
        if (savedRequest != null) {
            String queryStr = savedRequest.getQueryString();
            if (queryStr != null && (queryStr.contains("__ajax") || queryStr.contains("mobileLogin"))) {
                AjaxJson j = new AjaxJson();
                j.setSuccess(false);
                j.setErrorCode("0");
                j.setMsg("没有登录!");
                return renderString(response, JsonMapper.toJsonString(j));
            }
        }

        model.addAttribute("title", configBean.getName());
        return "main/login";
    }

    /**
     * 登录失败，真正登录的POST请求由Filter完成<br/>
     * {@link SystemAuthorizingRealm}
     */
    @RequestMapping(value = "${sys.adminPath}/login", method = RequestMethod.POST)
    public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {

        Principal principal = userUtils.getPrincipal();

        // 如果已经登录，则跳转到管理首页
        if (principal != null) {
            return "redirect:" + configBean.getAdminPath();
        }

        String username = WebUtils.getCleanParam(request, LoginFormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        boolean rememberMe = WebUtils.isTrue(request, LoginFormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
        boolean mobile = WebUtils.isTrue(request, LoginFormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
        String exception = (String) request
                .getAttribute(LoginFormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String message = (String) request.getAttribute(LoginFormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

        if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")) {
            message = "登录失败, 请联系管理员.";
        }

        model.addAttribute(LoginFormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
        model.addAttribute(LoginFormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
        model.addAttribute(LoginFormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
        model.addAttribute(LoginFormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
        model.addAttribute(LoginFormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

        logger.debug("login fail, active session size: {}, message: {}, exception: {}",
                sessionDAO.getActiveSessions(false).size(), message, exception);

        // 非授权异常，登录失败，验证码加1。
        if (!UnauthorizedException.class.getName().equals(exception)) {
            boolean b = validateCodeUtils.isValidateCodeLogin(username, true, false);
            model.addAttribute("isValidateCodeLogin", b);
            logger.debug("下次是否需要验证码登陆 isValidateCodeLogin = {}", b);
        }

        // 验证失败清空验证码
        request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, idGen.uuid());

        // 如果是手机登录，则返回JSON字符串
        if (mobile) {
            AjaxJson j = new AjaxJson();
            j.setSuccess(false);
            j.setMsg(message);
            j.put("username", username);
            j.put("name", "");
            j.put("mobileLogin", mobile);
            j.put("JSESSIONID", "");
            return renderString(response, j.getJsonStr());
        }

        model.addAttribute("message", message);
        model.addAttribute("title", configBean.getName());
        return "main/login";
    }

    /** 注销(登出) */
    @RequestMapping(value = "${sys.adminPath}/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        Principal principal = userUtils.getPrincipal();
        // 如果已经登录，则跳转到管理首页
        if (principal != null) {
            userUtils.getSubject().logout();

        }
        // 如果是手机客户端退出跳转到login，则返回JSON字符串
        String ajax = request.getParameter("__ajax");
        if (ajax != null) {
            model.addAttribute("success", "1");
            model.addAttribute("msg", "退出成功");
            return renderString(response, JsonMapper.toJsonString(model));
        }
        logger.debug("退出成功");
        return "redirect:" + configBean.getAdminPath() + "/login";
    }

    /** 登录成功，打开管理首页 */
    @RequiresPermissions("user")
    @RequestMapping(value = "${sys.adminPath}")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        Principal principal = userUtils.getPrincipal();
        // 登录成功后，验证码计算器清零
        validateCodeUtils.isValidateCodeLogin(principal.getLoginName(), false, true);

        logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());

        // 如果已登录，再次访问主页，则退出原账号。
        if (Constant.TRUE.equals(Global.getConfig("sys.notAllowRefreshIndex"))) {
            String logined = CookieUtils.getCookie(request, "LOGINED");
            if (StringUtils.isBlank(logined) || "false".equals(logined)) {
                CookieUtils.setCookie(response, "LOGINED", "true");
            } else if (StringUtils.equals(logined, "true")) {
                userUtils.getSubject().logout();
                return "redirect:" + configBean.getAdminPath() + "/login";
            }
        }

        // 如果是手机登录，则返回JSON字符串
        if (principal.isMobileLogin()) {
            if (request.getParameter("login") != null) {
                return renderString(response, JsonMapper.toJsonString(principal));
            }
            if (request.getParameter("index") != null) {
                return "sys/mobile/index";
            }
            return "redirect:" + configBean.getAdminPath() + "/login";
        }

        // 中英文名称
        model.addAttribute("title", configBean.getName());
        SysUser u = userUtils.getUser();
        model.addAttribute("navi", u==null?"guest":u.getName());

        // 加载所属菜单
        model.addAttribute("leftMenu", buildMenuList());
        return configBean.getLogginSuccessPage();
    }

    /**
     * 客户端返回字符串
     * 
     * @param response
     * @param string
     * @return
     */
    private String renderString(HttpServletResponse response, String string) {
        try {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /** 左侧菜单加载 */
    private List<MenuDto> buildMenuList() {
        List<SysMenu> orignMenuList = new ArrayList<>();
        SysUser user = userUtils.getUser();

        if (user != null) {
            orignMenuList = menuService.findByUser(user.getId());
        }
        List<MenuDto> mdl = TreeUtils.buildNaviMenu(orignMenuList, 2);
        return mdl;
    }

}

package com.jee.learn.manager.security;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import com.jee.learn.manager.config.SystemConfig;
import com.jee.learn.manager.domain.sys.SysLog;
import com.jee.learn.manager.domain.sys.SysMenu;
import com.jee.learn.manager.domain.sys.SysUser;
import com.jee.learn.manager.service.sys.SysLogService;
import com.jee.learn.manager.service.sys.SysMenuService;
import com.jee.learn.manager.support.cache.CacheConstants;
import com.jee.learn.manager.support.cache.EhcacheService;
import com.jee.learn.manager.util.Constants;
import com.jee.learn.manager.util.base.ExceptionUtil;
import com.jee.learn.manager.util.idgen.IdGenerate;
import com.jee.learn.manager.util.net.IPUtil;
import com.jee.learn.manager.util.time.ClockUtil;

/**
 * 日志拦截保存工具<br/>
 * 当无法获取当前用户信息时, 不予保存日志
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年11月15日 上午10:49:40 ccp 新建
 */
@Component
public class LogUtil {

    private static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";

    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private EhcacheService ehcacheService;
    @Autowired
    private UserUtil userUtil;

    @Autowired
    private SysMenuService menuService;
    @Autowired
    private SysLogService logService;

    /** 保存日志 */
    public void saveLog(HttpServletRequest request, String title) {
        saveLog(request, null, null, title);
    }

    /** 保存日志 */
    public void saveLog(HttpServletRequest request, Object handler, Exception ex, String title) {
        SysUser user = userUtil.getUser();
        if (user != null && user.getId() != null) {
            saveLog(request, handler, ex, title, user.getId());
        }
    }

    /** 保存日志 */
    public void saveLog(HttpServletRequest request, Object handler, Exception ex, String title, String createrId) {
        SysLog log = new SysLog();
        log.setTitle(title);
        log.setRemoteAddr(IPUtil.getIp(request));
        log.setUserAgent(request.getHeader("user-agent"));
        log.setRequestUri(request.getRequestURI());
        log.setParams(getParams(request.getParameterMap()));
        log.setMethod(request.getMethod());
        log.setCreateBy(createrId);
        log.setType(ex == null ? Constants.LOG_TYPE_ACCESS : Constants.LOG_TYPE_EXCEPTION);
        log.setCreateDate(ClockUtil.currentDate());
        log.setId(IdGenerate.fastUUID());
        // 异步保存日志
        new SaveLogThread(log, handler, ex).start();
    }

    /** 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑） */
    private String getMenuNamePath(String requestUri, String permission) {

        String href = StringUtils.substringAfter(requestUri, systemConfig.getAuthcPath());
        // 获取字典
        Map<String, String> menuMap = getMenuNamePathMap();

        String menuNamePath = menuMap.get(href);
        if (menuNamePath == null) {
            for (String p : StringUtils.split(permission)) {
                menuNamePath = menuMap.get(p);
                if (StringUtils.isNotBlank(menuNamePath)) {
                    break;
                }
            }
            if (menuNamePath == null) {
                return StringUtils.EMPTY;
            }
        }
        return menuNamePath;
    }

    /** 获取菜单字典 */
    @SuppressWarnings("unchecked")
    private Map<String, String> getMenuNamePathMap() {
        Map<String, String> menuMap = (Map<String, String>) ehcacheService.get(CacheConstants.EHCACHE_DEFAULT,
                CACHE_MENU_NAME_PATH_MAP);
        if (menuMap == null) {
            menuMap = new HashMap<>();
            List<SysMenu> menuList = menuService.findList(new SysMenu());
            for (SysMenu menu : menuList) {
                // 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
                String namePath = "";
                if (menu.getParentIds() != null) {
                    List<String> namePathList = new ArrayList<>();
                    for (String id : StringUtils.split(menu.getParentIds(), Constants.COMMA)) {
                        if ("0".equals(id)) {
                            continue; // 过滤跟节点
                        }
                        for (SysMenu m : menuList) {
                            if (m.getId().equals(id)) {
                                namePathList.add(m.getName());
                                break;
                            }
                        }
                    }
                    namePathList.add(menu.getName());
                    namePath = StringUtils.join(namePathList, "-");
                }

                // 设置菜单名称路径
                if (StringUtils.isNotBlank(menu.getHref())) {
                    menuMap.put(menu.getHref(), namePath);// 如果有href, 则使用<href,
                                                          // namePath>
                } else if (StringUtils.isNotBlank(menu.getPermission())) {
                    for (String p : StringUtils.split(menu.getPermission())) {
                        menuMap.put(p, namePath);// 如果没有href, 则使用<permission,
                                                 // namePath>
                    }
                }

            }
            ehcacheService.put(CacheConstants.EHCACHE_DEFAULT, CACHE_MENU_NAME_PATH_MAP, menuMap);
        }
        return menuMap;
    }

    /** 设置请求参数转换 */
    private String getParams(Map<String, String[]> paramMap) {
        if (paramMap == null) {
            return null;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>) paramMap).entrySet()) {
            params.append("".equals(params.toString()) ? "" : "&").append(param.getKey()).append("=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            paramValue = StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue;
            if (paramValue.length() > 200) {
                params.append("...");
                break;
            }
            params.append(paramValue);
        }
        return params.toString();
    }

    /**
     * 保存日志线程
     */
    private class SaveLogThread extends Thread {

        private SysLog log;
        private Object handler;
        private Exception ex;

        public SaveLogThread(SysLog log, Object handler, Exception ex) {
            super(SaveLogThread.class.getSimpleName());
            this.log = log;
            this.handler = handler;
            this.ex = ex;
        }

        @Override
        public void run() {
            if (log == null) {
                return;
            }
            // 获取日志标题
            if (StringUtils.isBlank(log.getTitle())) {
                String permission = StringUtils.EMPTY;
                if (handler instanceof HandlerMethod) {
                    Method m = ((HandlerMethod) handler).getMethod();
                    RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
                    permission = (rp != null ? StringUtils.join(rp.value(), Constants.COMMA) : StringUtils.EMPTY);
                }
                log.setTitle(getMenuNamePath(log.getRequestUri(), permission));
            }
            // 如果有异常，设置异常信息
            log.setException(ex == null ? null : ExceptionUtil.stackTraceText(ex));
            // 如果无标题并无异常日志，则不保存信息
            if (StringUtils.isBlank(log.getTitle()) && StringUtils.isBlank(log.getException())) {
                return;
            }
            if (log.getUserAgent().length() > 200) {
                log.setUserAgent(log.getUserAgent().substring(0, 200));
            }

            // 保存日志信息
            logService.saveOrUpdate(log);
        }
    }
}

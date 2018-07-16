/**
 * Copyright &copy; 2015-2020 <a href="http://www.chinaskin.net/">chnskin</a> All rights reserved.
 */
package com.jee.learn.component.util;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jee.learn.component.security.Principal;
import com.jee.learn.model.sys.SysMenu;
import com.jee.learn.model.sys.SysUser;
import com.jee.learn.service.sys.SysMenuService;
import com.jee.learn.service.sys.SysUserService;

/**
 * 用户工具类
 * 
 * @author chnskin
 * @version 2013-12-05
 */
@Component
public class UserUtils {

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysMenuService menuService;
    // private SysOfficeService officeService ;

    public static final String USER_CACHE = "userCache";
    public static final String USER_CACHE_ID_ = "id_";
    public static final String USER_CACHE_LOGIN_NAME_ = "ln";
    public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

    // public static final String CACHE_ROLE_LIST = "roleList";
    // public static final String CACHE_MENU_LIST = "menuList";
    // public static final String CACHE_AREA_LIST = "areaList";
    // public static final String CACHE_OFFICE_LIST = "officeList";
    // public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";

    /**
     * 根据ID获取用户
     * 
     * @param id
     * @return 取不到返回null
     */
    public SysUser get(String id) {
        SysUser user = (SysUser) cacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
        if (user == null) {
            user = userService.findOne(id);
            if (user == null) {
                return null;
            }
            // 可添加user对应的角色...

            cacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
            cacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
        }
        return user;
    }

    /**
     * 根据登录名获取用户
     * 
     * @param loginName
     * @return 取不到返回null
     */
    public SysUser getByLoginName(String loginName) {
        SysUser user = (SysUser) cacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
        if (user == null) {
            user = userService.findByLoginName(loginName);
            if (user == null) {
                return null;
            }
            // 可添加user对应的角色...

            cacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
            cacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
        }
        return user;

    }

    /**
     * 清除当前用户缓存
     */
    public void clearCache() {
        // removeCache(CACHE_ROLE_LIST);
        // removeCache(CACHE_MENU_LIST);
        // removeCache(CACHE_AREA_LIST);
        // removeCache(CACHE_OFFICE_LIST);
        // removeCache(CACHE_OFFICE_ALL_LIST);
        clearCache(getUser());
    }

    /**
     * 清除指定用户缓存
     * 
     * @param user
     */
    public void clearCache(SysUser user) {
        cacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
        cacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
        cacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
        if (user.getOffice() != null && user.getOffice().getId() != null) {
            cacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + user.getOffice().getId());
        }
    }

    /**
     * 获取当前用户
     * 
     * @return 取不到返回 new User()
     */
    public SysUser getUser() {
        Principal principal = getPrincipal();
        if (principal != null) {
            SysUser user = get(principal.getId());
            if (user != null) {
                return user;
            }
            return new SysUser();
        }
        // 如果没有登录，则返回实例化空的User对象。
        return new SysUser();
    }

    /**
     * 获取当前用户角色列表
     * 
     * @return
     */
    // public List<SysRole> getRoleList() {
    // @SuppressWarnings("unchecked")
    // List<SysRole> roleList = (List<SysRole>) getCache(CACHE_ROLE_LIST);
    // if (roleList == null){
    // SysUser user = getUser();
    // if (user.isAdmin()){
    // roleList = roleDao.findAllList(new Role());
    // }else{
    // Role role = new Role();
    // //role.getSqlMap().put("dsf",
    // BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
    //
    // //机构、角色、用户，隔离企业账号数据，加入权限过滤
    // role.getSqlMap().put("dsf", " AND
    // a.account_id='"+UserUtils.getUser().getAccountId()+"' AND a.id <> 0");
    // roleList = roleDao.findList(role);
    // }
    // putCache(CACHE_ROLE_LIST, roleList);
    // }
    // return roleList;
    // return null;
    // }

    /**
     * 获取当前用户授权菜单
     * 
     * @return
     */
    public List<SysMenu> getMenuList() {
        // @SuppressWarnings("unchecked")
        // List<SysMenu> menuList = (List<SysMenu>) getCache(CACHE_MENU_LIST);
        List<SysMenu> menuList = null;
        if (menuList == null || menuList.size() == 0) {
            SysUser user = getUser();
            if (user.isAdmin()) {
                menuList = menuService.findAll();
            } else {
                menuList = menuService.findByUser(user.getId());
            }
            // putCache(CACHE_MENU_LIST, menuList);
        }
        return menuList;
    }

    /**
     * 获取当前用户授权菜单
     * 
     * @return
     */
    // public SysMenu getTopMenu() {
    // SysMenu topMenu = getMenuList().get(0);
    // return topMenu;
    // }

    /**
     * 获取当前用户授权的区域
     * 
     * @return
     */
    // public List<SysArea> getAreaList() {
    // @SuppressWarnings("unchecked")
    // List<SysArea> areaList = (List<SysArea>) getCache(CACHE_AREA_LIST);
    // List<SysArea> areaList = null;
    // if (areaList == null) {
    // areaList = areaService.findList(null);
    // putCache(CACHE_AREA_LIST, areaList);
    // }
    // return areaList;
    // }

    /**
     * 获取当前用户有权限访问的部门
     * 
     * @return
     */
    // public List<SysOffice> getOfficeList() {
    // @SuppressWarnings("unchecked")
    // List<SysOffice> officeList = (List<SysOffice>)
    // getCache(CACHE_OFFICE_LIST);
    // if (officeList == null){
    // SysUser user = getUser();
    // if (user.isAdmin()){
    //
    // Office office = new Office();
    // //机构、角色、用户，隔离企业账号数据，加入权限过滤
    // office.getSqlMap().put("dsf", " AND
    // a.account_id='"+UserUtils.getUser().getAccountId()+"' ");
    // officeList = officeDao.findAllList(office);
    // }else{
    // Office office = new Office();
    // //office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user,"a",
    // ""));
    //
    // //机构、角色、用户，隔离企业账号数据，加入权限过滤
    // office.getSqlMap().put("dsf", " AND
    // a.account_id='"+UserUtils.getUser().getAccountId()+"' ");
    // officeList = officeDao.findList(office);
    // }
    // putCache(CACHE_OFFICE_LIST, officeList);
    // }
    // return officeList;
    // return officeService.findAll();
    // }

    /**
     * 获取当前用户有权限访问的部门
     * 
     * @return
     */
    // public List<SysOffice> getOfficeAllList() {
    // @SuppressWarnings("unchecked")
    // List<SysOffice> officeList = (List<SysOffice>)
    // getCache(CACHE_OFFICE_ALL_LIST);
    // if (officeList == null) {
    // officeList = officeService.findList(new SysOffice());
    // }
    // return officeList;
    // return officeService.findAll();
    // }

    /**
     * 获取授权主要对象<br/>
     * 与 {@link SysUser } 解耦,可供其他用户表使用
     */
    public Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前登录者对象<br/>
     * 与 {@link SysUser } 解耦,可供其他用户表使用
     */
    public Principal getPrincipal() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return principal;
            }
            // 不需要退出
            // subject.logout();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 获取当前session会话<br/>
     * 与 {@link SysUser } 解耦,可供其他用户表使用
     */
    public Session getSession() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null) {
                session = subject.getSession();
            }
            if (session != null) {
                return session;
            }
            // subject.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ============== User Cache ==============

    public Object getCache(String key) {
        return getCache(key, null);
    }

    public Object getCache(String key, Object defaultValue) {
        // Object obj = getCacheMap().get(key);
        Object obj = getSession().getAttribute(key);
        return obj == null ? defaultValue : obj;
    }

    public void putCache(String key, Object value) {
        // getCacheMap().put(key, value);
        getSession().setAttribute(key, value);
    }

    public void removeCache(String key) {
        // getCacheMap().remove(key);
        getSession().removeAttribute(key);
    }

    public String getTime(Date date) {
        StringBuffer time = new StringBuffer();
        Date date2 = new Date();
        long temp = date2.getTime() - date.getTime();
        long days = temp / 1000 / 3600 / 24; // 相差小时数
        if (days > 0) {
            time.append(days + "天");
        }
        long temp1 = temp % (1000 * 3600 * 24);
        long hours = temp1 / 1000 / 3600; // 相差小时数
        if (days > 0 || hours > 0) {
            time.append(hours + "小时");
        }
        long temp2 = temp1 % (1000 * 3600);
        long mins = temp2 / 1000 / 60; // 相差分钟数
        time.append(mins + "分钟");
        return time.toString();
    }

    /**
     * 导出Excel调用,根据姓名转换为ID
     */
    // public SysUser getByUserName(String name) {
    // SysUser user = userService.findUniqueByProperty("name", name);
    // if (user != null) {
    // return user;
    // }
    // return new SysUser();
    // }

    /**
     * 导出Excel使用，根据名字转换为id
     */
    // public SysOffice getByOfficeName(String name) {
    // SysOffice o = officeService.findUniqueByProperty("name", name);
    // if (o != null) {
    // return o;
    // } else {
    // return new SysOffice();
    // }
    // }

    /**
     * 导出Excel使用，根据名字转换为id
     */
    // public SysArea getByAreaName(String name) {
    // SysArea a = areaService.findUniqueByProperty("name", name);
    // if (a != null) {
    // return a;
    // } else {
    // return new SysArea();
    // }
    // }

}

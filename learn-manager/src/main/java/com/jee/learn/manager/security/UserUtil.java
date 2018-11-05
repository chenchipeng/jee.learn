package com.jee.learn.manager.security;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.jee.learn.manager.config.shiro.security.CustomPrincipal;
import com.jee.learn.manager.domain.sys.SysMenu;
import com.jee.learn.manager.domain.sys.SysRole;
import com.jee.learn.manager.domain.sys.SysUser;
import com.jee.learn.manager.repository.sys.SysMenuRepository;
import com.jee.learn.manager.repository.sys.SysRoleRepository;
import com.jee.learn.manager.repository.sys.SysUserRepository;
import com.jee.learn.manager.security.shiro.ShiroUtil;
import com.jee.learn.manager.support.cache.CacheConstants;
import com.jee.learn.manager.support.cache.EhcacheService;
import com.jee.learn.manager.util.Constants;

/**
 * 跳过service层查找用户相关数据
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月29日 下午6:06:03 ccp 新建
 */
@Component
public class UserUtil {

    @Autowired
    private SysUserRepository userRepository;
    @Autowired
    private SysMenuRepository menuRepository;
    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private EhcacheService ehcacheService;

    /**
     * 根据登录名查找用户
     * 
     * @param loginName
     * @return
     */
    public SysUser findOneByLoginName(String loginName) {
        return StringUtils.isBlank(loginName) ? null : userRepository.findOneByLoginName(loginName);
    }

    /**
     * 获取当前用户
     * 
     * @return
     */
    public SysUser getUser() {
        CustomPrincipal principal = ShiroUtil.getPrincipal();
        if (principal == null) {
            return new SysUser();
        }
        SysUser user = userRepository.getOne(principal.getId());
        return user == null ? new SysUser() : user;
    }

    /**
     * 判断当前用户是不是超管
     * 
     * @return true->是
     */
    public boolean isAdmin() {
        SysUser user = getUser();
        return Constants.ADMIN_ID.equals(user.getId());
    }

    /**
     * 获取当前用户拥有的菜单
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SysMenu> getMenuList() {
        List<SysMenu> menuList = null;
        SysUser user = getUser();
        if (user == null) {
            return menuList;
        }

        menuList = (List<SysMenu>) ehcacheService.get(CacheConstants.EHCACHE_USER,
                CacheConstants.CACHE_KEY_USER_MENU + user.getId());

        if (CollectionUtils.isEmpty(menuList)) {
            if (isAdmin()) {
                menuList = menuRepository.findAll(Sort.by(Constants.SORT_NAME));
            } else {
                menuList = menuRepository.findListByUserId(user.getId());
            }
            ehcacheService.put(CacheConstants.EHCACHE_USER, CacheConstants.CACHE_KEY_USER_MENU + user.getId(),
                    menuList);
        }

        return menuList;
    }

    /**
     * 获取当前用户拥有的角色
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SysRole> getRoleList() {
        List<SysRole> roleList = null;
        SysUser user = getUser();
        if (user == null) {
            return roleList;
        }

        roleList = (List<SysRole>) ehcacheService.get(CacheConstants.EHCACHE_USER,
                CacheConstants.CACHE_KEY_USER_ROLE + user.getId());

        if (CollectionUtils.isEmpty(roleList)) {
            roleList = roleRepository.findListByUserId(user.getId());
            ehcacheService.put(CacheConstants.EHCACHE_USER, CacheConstants.CACHE_KEY_USER_ROLE + user.getId(),
                    roleList);
        }

        return roleList;
    }

}

package com.jee.learn.manager.security;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jee.learn.manager.config.shiro.security.CustomPrincipal;
import com.jee.learn.manager.domain.sys.SysMenu;
import com.jee.learn.manager.domain.sys.SysRole;
import com.jee.learn.manager.domain.sys.SysUser;
import com.jee.learn.manager.repository.sys.SysMenuRepository;
import com.jee.learn.manager.repository.sys.SysRoleRepository;
import com.jee.learn.manager.repository.sys.SysUserRepository;
import com.jee.learn.manager.security.shiro.ShiroUtil;
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
    public List<SysMenu> getMenuList() {
        List<SysMenu> menuList = null;
        if (CollectionUtils.isEmpty(menuList)) {

            SysUser user = getUser();
            if (isAdmin()) {
                menuList = menuRepository.findAll();
            } else {
                menuList = menuRepository.findListByUserId(user.getId());
            }
        }
        return menuList;
    }

    /**
     * 获取当前用户拥有的角色
     * 
     * @return
     */
    public List<SysRole> getRoleList() {
        List<SysRole> roleList = null;
        if (CollectionUtils.isEmpty(roleList)) {
            SysUser user = getUser();
            roleList = roleRepository.findListByUserId(user.getId());
        }
        return roleList;
    }

}

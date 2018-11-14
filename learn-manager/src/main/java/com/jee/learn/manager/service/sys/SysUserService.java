package com.jee.learn.manager.service.sys;

import com.jee.learn.manager.domain.sys.SysUser;
import com.jee.learn.manager.support.dao.service.EntityService;

public interface SysUserService extends EntityService<SysUser, String> {

    /**
     * 更新用户登录信息
     * 
     * @param id
     */
    void updateUserLoginInfo(String id);

    /**
     * 更新用户个人信息
     * 
     * @param user
     */
    void updateUserProfileInfo(SysUser user);

    /**
     * 更新用户头像信息
     * 
     * @param id
     * @param photo
     */
    void updatePhoto(String id, String photo);

}

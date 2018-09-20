package com.jee.learn.mybatis.repository.mapper;

import java.util.List;

import com.jee.learn.mybatis.domain.ApiUser;

public interface ApiUserDao {

    ApiUser get(String id);
    
    List<ApiUser> findList(ApiUser apiUser);
}

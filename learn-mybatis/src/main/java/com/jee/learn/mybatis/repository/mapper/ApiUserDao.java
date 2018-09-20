package com.jee.learn.mybatis.repository.mapper;

import com.jee.learn.mybatis.domain.ApiUser;

public interface ApiUserDao {

    ApiUser get(String id);
}

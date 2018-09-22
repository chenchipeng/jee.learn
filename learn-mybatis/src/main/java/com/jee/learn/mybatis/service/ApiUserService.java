package com.jee.learn.mybatis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.mybatis.domain.api.ApiUser;
import com.jee.learn.mybatis.repository.mapper.ApiUserMapper;

@Service
@Transactional(readOnly=true)
public class ApiUserService extends BaseService<ApiUser , String, ApiUserMapper>{

}

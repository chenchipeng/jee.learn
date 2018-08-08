package com.jee.learn.interfaces.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.domain.ApiUser;
import com.jee.learn.interfaces.support.BaseServiceImpl;

@Service
@Transactional(readOnly = true)
public class TestServiceImpl extends BaseServiceImpl<ApiUser> implements TestService {

}

package com.jee.learn.interfaces.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jee.learn.interfaces.service.BaseService;

public abstract class BaseServiceImpl implements BaseService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

}

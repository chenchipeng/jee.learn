package com.jee.learn.manager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jee.learn.manager.config.SystemConfig;
import com.jee.learn.manager.security.LogUtil;

@Component
public class BaseController {

    protected static final String REDIRECT = "redirect:";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected SystemConfig systemConfig;
    @Autowired
    protected LogUtil logUtil;

}

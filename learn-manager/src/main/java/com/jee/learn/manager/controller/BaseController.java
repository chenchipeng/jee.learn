package com.jee.learn.manager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseController {

    protected static final String REDIRECT = "redirect:";
    
    protected Logger logger = LoggerFactory.getLogger(getClass());

}

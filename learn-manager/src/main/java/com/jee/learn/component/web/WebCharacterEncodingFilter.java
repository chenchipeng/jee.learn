package com.jee.learn.component.web;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import org.springframework.web.filter.CharacterEncodingFilter;

@WebFilter(filterName = "encodingFilter", urlPatterns = "/*", initParams = {
        @WebInitParam(name = "encoding", value = "UTF-8"), @WebInitParam(name = "forceEncoding", value = "true") })
public class WebCharacterEncodingFilter extends CharacterEncodingFilter {

}

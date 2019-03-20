package com.jee.learn.interfaces.support.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.jee.learn.interfaces.support.filter.wrapper.ParameterRequestWrapper;

/**
 * request 参数过滤器<br/>
 * 1.校验请求参数, 可进行转码以及关键字(敏感词)过滤, 防止SQL注入<br/>
 * 2.对请求参数进行统一转码<br/>
 * 参考:https://blog.csdn.net/xieyuooo/article/details/8447301
 * 
 * @see ParameterRequestWrapper
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年2月12日 上午11:56:44 ccp 新建
 */
public class ParameterFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        return;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper((HttpServletRequest) request);
        chain.doFilter(requestWrapper, response);
    }

    @Override
    public void destroy() {
        return;
    }

}

package com.jee.learn.controller.main;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 异常页面controller<br/>
 * 该controller仅用于测试
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年2月5日 上午9:01:14 1002360 新建
 */
// @org.springframework.stereotype.Controller
@RequestMapping("error")
public class ExceptionController {

    @RequestMapping(value = "403")
    public String error403() {
        return "/error/403";
    }

    @RequestMapping(value = "404")
    public String error404() {
        return "/error/404";
    }

    @RequestMapping(value = "500")
    public String error500() {
        return "/error/500";
    }

}

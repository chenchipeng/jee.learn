package com.jee.learn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试用
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年4月25日 下午6:04:38 1002360 新建
 */
@Controller
@RequestMapping(value = "${sys.frontPath:/f}")
public class ThymeleafController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /** http://localhost:8080/f/thymeleaf/hello */
    @RequestMapping("/thymeleaf/hello")
    public String index(Model model) {
        logger.info("收到请求 /thymeleaf/hello");
        model.addAttribute("title", "thymeleaf");
        return "thymeleaf";
    }

}

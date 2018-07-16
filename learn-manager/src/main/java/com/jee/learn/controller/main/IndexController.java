package com.jee.learn.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页 controller
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年2月27日 下午5:06:53 1002360 新建
 */
@Controller
@RequestMapping(value = "${sys.adminPath}/index")
public class IndexController {

    /**
     * 首页内容iframe<br/>
     * 测试模式显示index_demo
     */
    @RequestMapping(value = "main")
    public String indexMain() {
        return "main/index_main_demo";
    }

}

package com.jee.learn.controller.sys;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jee.learn.dto.BootstrapTable;
import com.jee.learn.model.sys.SysRole;
import com.jee.learn.service.sys.SysRoleService;

@Controller
@RequestMapping(value = { "${sys.adminPath}/sys/role" })
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;
    
    @RequestMapping(value = { "list" })
    public String list(Model model) {
        model.addAttribute("title", "list");
        return "sys/sysRoleList";
    }

    @RequestMapping(value = { "page" })
    @ResponseBody
    public BootstrapTable<SysRole> page(Model model) {
        Page<SysRole> page = sysRoleService.findPage(null, 1, 1, StringUtils.EMPTY);
        BootstrapTable<SysRole> bt = new BootstrapTable<SysRole>(page.getTotalElements(), page.getContent());
        return bt;
    }

}

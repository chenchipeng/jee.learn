package com.jee.learn.controller.sys;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jee.learn.common.util.Constant;
import com.jee.learn.dto.BootstrapTable;
import com.jee.learn.model.sys.SysLog;
import com.jee.learn.service.sys.SysLogService;

@Controller
@RequestMapping(value = { "${sys.adminPath}/sys/log" })
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @RequestMapping(value = { "list" })
    public String list(Model model) {
        model.addAttribute("title", "list");
        model.addAttribute("pageSize", Constant.DEFAULT_PAGE_SIZE);
        return "sys/log/sysLogList";
    }

    @RequestMapping(value = { "page" })
    @ResponseBody
    public BootstrapTable<SysLog> page(HttpServletRequest request, HttpServletResponse response, Model model) {

        int pageSize = Constant.DEFAULT_PAGE_SIZE;
        int pageNo = 1;
        String orderBy = Constant.CREATE_DATE_NAME + StringUtils.SPACE + Constant.SORT_DESC;
        String sort = request.getParameter("sort");
        String sortOrder = request.getParameter("sortOrder");

        String size = request.getParameter("pageSize");
        if (StringUtils.isNotBlank(size)) {
            pageSize = Integer.parseInt(size);
        }
        String no = request.getParameter("pageNo");
        if (StringUtils.isNotBlank(no)) {
            pageNo = Integer.parseInt(no);
        }
        if (StringUtils.isNoneBlank(sort, sortOrder)) {
            orderBy = sort + StringUtils.SPACE + sortOrder;
        }

        Map<String, String> params = new HashMap<>();

        Page<SysLog> page = sysLogService.load(params, pageNo, pageSize, orderBy);
        BootstrapTable<SysLog> bt = new BootstrapTable<SysLog>(page.getTotalElements(), page.getContent());
        return bt;
    }

}

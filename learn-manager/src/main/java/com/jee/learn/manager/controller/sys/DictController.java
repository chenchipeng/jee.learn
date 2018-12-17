package com.jee.learn.manager.controller.sys;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jee.learn.manager.controller.BaseController;
import com.jee.learn.manager.domain.sys.SysDict;
import com.jee.learn.manager.dto.base.DParam;
import com.jee.learn.manager.dto.base.RequestParam;
import com.jee.learn.manager.dto.base.ResponseDto;
import com.jee.learn.manager.dto.sys.DictDto;
import com.jee.learn.manager.service.sys.SysDictService;
import com.jee.learn.manager.util.WebConstants;

@Controller
@RequestMapping("${system.authc-path}/sys/dict")
public class DictController extends BaseController {

    private static final String TOKEN = "oQXWv0mE9Q0vTDNqKl2uFXMMuspI";

    @Autowired
    private SysDictService dictService;

    /** 根据类型返回字典列表 */
    @RequiresPermissions("sys:dict:list")
    @Async
    @ResponseBody
    @PostMapping(path = "type", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<ResponseDto<DictDto>> getDictList(@RequestBody RequestParam<DParam> param) {

        if (param.getH() == null || !TOKEN.equals(param.getH().getT()) || param.getD() == null
                || 1 != param.getD().getA()) {
            return CompletableFuture.completedFuture(
                    new ResponseDto<>(WebConstants.PARAMETER_ERROR_CODE, WebConstants.PARAMETER_ERROR_MESSAGE));
        }
        if (StringUtils.isBlank(param.getD().getT())) {
            return CompletableFuture.completedFuture(
                    new ResponseDto<>(WebConstants.RECORD_NOT_FOUND_CODE, WebConstants.RECORD_NOT_FOUND_MESSAGE));
        }

        List<SysDict> list = dictService.findList("type", param.getD().getT(), "sort asc");
        DictDto d = new DictDto();
        d.setL(list);

        ResponseDto<DictDto> r = new ResponseDto<>(WebConstants.SUCCESS_CODE);
        r.setD(d);
        return CompletableFuture.completedFuture(r);
    }

}

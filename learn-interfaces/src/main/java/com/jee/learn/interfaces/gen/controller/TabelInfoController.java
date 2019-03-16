package com.jee.learn.interfaces.gen.controller;

import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jee.learn.interfaces.gen.dto.GenTableDto;
import com.jee.learn.interfaces.gen.dto.GenTableParam;
import com.jee.learn.interfaces.gen.service.GeneratorService;
import com.jee.learn.interfaces.support.web.WebConstants;
import com.jee.learn.interfaces.support.web.base.AbstractInnerController;
import com.jee.learn.interfaces.support.web.dto.RequestParams;
import com.jee.learn.interfaces.support.web.dto.ResponseDto;

/**
 * gen.new.tableInfo 获取数据表列表
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月16日 下午4:12:58 ccp 新建
 */
@RestController
public class TabelInfoController extends AbstractInnerController<GenTableDto, GenTableParam> {

    @Autowired
    private GeneratorService generatorService;

    @CrossOrigin
    @PostMapping(path = "/rest/gen.new.tableInfo", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Override
    public CompletableFuture<ResponseDto<GenTableDto>> execute(@RequestBody RequestParams<GenTableParam> params) {
        return super.execute(params);
    }

    @Override
    protected ResponseDto<GenTableDto> checkRequestParams(RequestParams<GenTableParam> params) {
        ResponseDto<GenTableDto> r = super.checkRequestParams(params);
        if (r.getC().equals(WebConstants.SUCCESS_CODE)) {
            if (params.getD().getA() != 1 || StringUtils.isBlank(params.getD().getName())) {
                return new ResponseDto<>(WebConstants.PARAMETER_ERROR_CODE, WebConstants.PARAMETER_ERROR_MESSAGE);
            }
        }
        return r;
    }

    @Override
    protected ResponseDto<GenTableDto> checkToken(RequestParams<GenTableParam> params) {
        // TODO 校验用户token
        return super.checkToken(params);
    }

    @Override
    protected ResponseDto<GenTableDto> handler(RequestParams<GenTableParam> params) {
        ResponseDto<GenTableDto> r = new ResponseDto<GenTableDto>(WebConstants.SUCCESS_CODE);
        GenTableDto d = generatorService.getTebleInfo(params.getD().getName());
        r.setD(d);
        return r;
    }

}

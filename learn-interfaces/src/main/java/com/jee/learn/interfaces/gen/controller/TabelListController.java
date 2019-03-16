package com.jee.learn.interfaces.gen.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jee.learn.interfaces.gen.dto.GenTableDto;
import com.jee.learn.interfaces.gen.service.GeneratorService;
import com.jee.learn.interfaces.support.web.WebConstants;
import com.jee.learn.interfaces.support.web.base.AbstractInnerController;
import com.jee.learn.interfaces.support.web.dto.DParam;
import com.jee.learn.interfaces.support.web.dto.RequestParams;
import com.jee.learn.interfaces.support.web.dto.ResponseDto;

/**
 * gen.new.tableList 获取数据表列表
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月16日 下午4:12:58 ccp 新建
 */
@RestController
public class TabelListController extends AbstractInnerController<List<GenTableDto>, DParam> {

    @Autowired
    private GeneratorService generatorService;

    @CrossOrigin
    @PostMapping(path = "/rest/gen.new.tableList", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Override
    public CompletableFuture<ResponseDto<List<GenTableDto>>> execute(@RequestBody RequestParams<DParam> params) {
        return super.execute(params);
    }

    @Override
    protected ResponseDto<List<GenTableDto>> checkRequestParams(RequestParams<DParam> params) {
        ResponseDto<List<GenTableDto>> r = super.checkRequestParams(params);
        if (r.getC().equals(WebConstants.SUCCESS_CODE)) {
            if (params.getD().getA() != 1) {
                return new ResponseDto<>(WebConstants.PARAMETER_ERROR_CODE, WebConstants.PARAMETER_ERROR_MESSAGE);
            }
        }
        return r;
    }

    @Override
    protected ResponseDto<List<GenTableDto>> checkToken(RequestParams<DParam> params) {
        // TODO 校验用户token
        return super.checkToken(params);
    }

    @Override
    protected ResponseDto<List<GenTableDto>> handler(RequestParams<DParam> params) {
        ResponseDto<List<GenTableDto>> r = new ResponseDto<List<GenTableDto>>(WebConstants.SUCCESS_CODE);
        List<GenTableDto> d = generatorService.getTabelList();
        r.setD(d);
        return r;
    }

}

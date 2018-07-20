package com.jee.learn.interfaces.service.api;

import com.jee.learn.interfaces.dto.RequestDto;
import com.jee.learn.interfaces.dto.ResponseDto;
import com.jee.learn.interfaces.dto.api.ApiUserDto;
import com.jee.learn.interfaces.service.BaseService;
import com.jee.learn.interfaces.util.exception.IntfcException;

public interface ApiUserService extends BaseService {

    /**
     * 根据id查询一条记录
     * 
     * @param id
     * @return
     * @throws IntfcException
     */
    ResponseDto<ApiUserDto> get(RequestDto<ApiUserDto> requestDto);

    ResponseDto<ApiUserDto> save(RequestDto<ApiUserDto> requestDto);
    
    ResponseDto<ApiUserDto> findList(RequestDto<ApiUserDto> requestDto);

}

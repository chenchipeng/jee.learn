package com.jee.learn.interfaces.service.api;

import com.jee.learn.interfaces.domain.ApiUser;
import com.jee.learn.interfaces.dto.RequestDto;
import com.jee.learn.interfaces.dto.ResponseDto;
import com.jee.learn.interfaces.dto.api.ApiUserDto;
import com.jee.learn.interfaces.service.BaseService;

public interface ApiUserService extends BaseService<ApiUser> {

    ResponseDto<ApiUserDto> get(RequestDto<ApiUserDto> requestDto);

    ResponseDto<ApiUserDto> save(RequestDto<ApiUserDto> requestDto);

    ResponseDto<ApiUserDto> findList(RequestDto<ApiUserDto> requestDto);

    ApiUser get(String id);

    ApiUser saveOrUpdate(ApiUser apiUser);

}

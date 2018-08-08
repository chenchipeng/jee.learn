package com.jee.learn.interfaces.service.api;

import com.jee.learn.interfaces.domain.ApiUser;
import com.jee.learn.interfaces.dto.RequestDto;
import com.jee.learn.interfaces.dto.ResponseDto;
import com.jee.learn.interfaces.dto.api.ApiUserDto;

public interface ApiUserService {

    ApiUser get(String id);

    ResponseDto<ApiUserDto> get(RequestDto<ApiUserDto> requestDto);

    ResponseDto<ApiUserDto> save(RequestDto<ApiUserDto> requestDto);

    ResponseDto<ApiUserDto> findList(RequestDto<ApiUserDto> requestDto);

}

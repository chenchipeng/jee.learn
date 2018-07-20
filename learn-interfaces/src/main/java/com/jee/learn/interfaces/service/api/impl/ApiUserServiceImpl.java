package com.jee.learn.interfaces.service.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.domain.api.ApiUser;
import com.jee.learn.interfaces.dto.RequestDto;
import com.jee.learn.interfaces.dto.ResponseDto;
import com.jee.learn.interfaces.dto.api.UserDto;
import com.jee.learn.interfaces.repository.api.ApiUserRepository;
import com.jee.learn.interfaces.service.BaseServiceImpl;
import com.jee.learn.interfaces.service.api.ApiUserService;
import com.jee.learn.interfaces.util.WebConstants;
import com.jee.learn.interfaces.util.cache.EhcacheService;
import com.jee.learn.interfaces.util.exception.IntfcException;

@Service
@Transactional(readOnly = true)
public class ApiUserServiceImpl extends BaseServiceImpl implements ApiUserService {

    @Autowired
    private ApiUserRepository apiUserRepository;
    @SuppressWarnings("unused")
    @Autowired
    private EhcacheService ehcacheService;

    @Override
    public ResponseDto<UserDto> get(RequestDto<UserDto> requestDto) {
        ResponseDto<UserDto> rd = new ResponseDto<UserDto>();
        UserDto d = new UserDto();

        ApiUser apiUser = apiUserRepository.findOneById(requestDto.getD().getId());
        if (apiUser == null) {
            throw new IntfcException(WebConstants.RECORD_NOT_FOUND_CODE, WebConstants.RECORD_NOT_FOUND_MESSAGE);
        }

        d.setId(apiUser.getId());
        d.setLoginName(apiUser.getLoginName());
        rd.setD(d);
        rd.setC(WebConstants.SUCCESS_CODE);
        return rd;
    }

    @Transactional(readOnly = false)
    @Override
    public ResponseDto<UserDto> save(RequestDto<UserDto> requestDto) {
        ResponseDto<UserDto> rd = new ResponseDto<UserDto>();

        String id = requestDto.getD().getId();
        ApiUser apiUser = apiUserRepository.findOneById(id);
        if (apiUser == null) {
            throw new IntfcException(WebConstants.RECORD_NOT_FOUND_CODE, WebConstants.RECORD_NOT_FOUND_MESSAGE);
        }
        apiUser.setRemarks(String.valueOf(System.currentTimeMillis()));
        apiUser = apiUserRepository.save(apiUser);

        rd.setC(WebConstants.SUCCESS_CODE);
        return rd;
    }

}

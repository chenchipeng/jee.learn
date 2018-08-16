package com.jee.learn.interfaces.service.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.config.datasource.dynamic.TargetDataSource;
import com.jee.learn.interfaces.domain.ApiUser;
import com.jee.learn.interfaces.dto.RequestDto;
import com.jee.learn.interfaces.dto.ResponseDto;
import com.jee.learn.interfaces.dto.api.ApiUserDto;
import com.jee.learn.interfaces.repository.ApiUserRepository;
import com.jee.learn.interfaces.service.api.ApiUserService;
import com.jee.learn.interfaces.util.WebConstants;
import com.jee.learn.interfaces.util.exception.IntfcException;
import com.jee.learn.interfaces.util.support.Criteria;
import com.jee.learn.interfaces.util.support.Restrictions;

@Service
@Transactional(readOnly = true)
public class ApiUserServiceImpl implements ApiUserService {

    @Autowired
    private ApiUserRepository apiUserRepository;

    @Override
    public ResponseDto<ApiUserDto> get(RequestDto<ApiUserDto> requestDto) {
        ResponseDto<ApiUserDto> rd = new ResponseDto<ApiUserDto>();
        ApiUserDto d = new ApiUserDto();

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
    public ResponseDto<ApiUserDto> save(RequestDto<ApiUserDto> requestDto) {
        ResponseDto<ApiUserDto> rd = new ResponseDto<ApiUserDto>();

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

    @Override
    public ResponseDto<ApiUserDto> findList(RequestDto<ApiUserDto> requestDto) {
        ResponseDto<ApiUserDto> rd = new ResponseDto<ApiUserDto>();
        ApiUserDto d = new ApiUserDto();

        Criteria<ApiUser> criteria = new Criteria<>();
        criteria.add(Restrictions.eq("loginName", requestDto.getD().getLoginName()));
        List<ApiUser> list = apiUserRepository.findAll(criteria);

        List<ApiUserDto> dtos = new ArrayList<>(list.size());
        ApiUserDto dto = null;
        for (ApiUser u : list) {
            dto = new ApiUserDto();
            BeanUtils.copyProperties(u, dto);
            dtos.add(dto);
        }

        d.setL(dtos);
        rd.setD(d);
        rd.setC(WebConstants.SUCCESS_CODE);
        return rd;
    }

    @Override
    public ApiUser get(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        ApiUser u = apiUserRepository.findOneById(id);
        return u;
    }
    
    @TargetDataSource
    @Override
    public ApiUser getById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        ApiUser u = apiUserRepository.findOneById(id);
        return u;
    }

}

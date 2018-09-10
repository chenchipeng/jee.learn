package com.jee.learn.jpa.service.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.jpa.domain.ApiUser;
import com.jee.learn.jpa.support.spec.Filter;
import com.jee.learn.jpa.support.spec.QueryParams;
import com.jee.learn.jpa.support.spec.service.SpecSerciceImpl;

@Service
@Transactional(readOnly = true)
public class ApiUserServiceImpl extends SpecSerciceImpl<ApiUser, String> implements ApiUserService {

    @Override
    protected QueryParams<ApiUser> parseQueryParams(ApiUser entity) {
        QueryParams<ApiUser> spec = super.parseQueryParams(entity);

        if (StringUtils.isNotBlank(entity.getDelFlag())) {
            spec.and(Filter.eq("delFlag", entity.getDelFlag()));
        }

        return spec;
    }

}

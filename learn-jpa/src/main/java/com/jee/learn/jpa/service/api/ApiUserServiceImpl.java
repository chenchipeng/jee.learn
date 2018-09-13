package com.jee.learn.jpa.service.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.jpa.domain.ApiUser;
import com.jee.learn.jpa.support.dao.Condition;
import com.jee.learn.jpa.support.dao.Sort;
import com.jee.learn.jpa.support.dao.Condition.Operator;
import com.jee.learn.jpa.support.dao.service.EntityServiceImpl;

@Service
@Transactional(readOnly = true)
public class ApiUserServiceImpl extends EntityServiceImpl<ApiUser, String> implements ApiUserService {

    @Override
    protected Condition parseQueryParams(ApiUser entity) {
      Condition con = super.parseQueryParams(entity);
      if(StringUtils.isNotBlank(entity.getLoginName())) {
          con.add("loginName",Operator.EQ,entity.getLoginName());
      }
      return con;
    }

    @Override
    protected Sort parseSort(String orderBy) {
        return super.parseSort(orderBy);
    }

}

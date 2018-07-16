package com.jee.learn.service.sys.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.common.persist.Condition;
import com.jee.learn.common.persist.Condition.Operator;
import com.jee.learn.common.service.impl.BaseServiceImpl;
import com.jee.learn.model.sys.SysLog;
import com.jee.learn.service.sys.SysLogService;

@Service
@Transactional(readOnly = true)
public class SysLogServiceImpl extends BaseServiceImpl<SysLog, String> implements SysLogService {

    @Override
    protected Condition parseQueryParams(Map<String, String> params) {
        // 该条件默认用在findAll上
        Condition con = new Condition();
        if (StringUtils.isNotBlank(params.get("title"))) {
            con.add("title", Operator.LIKE, params.get("title"));
        }
        return con;
    }

}
package com.jee.learn.interfaces.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.domain.ApiUser;
import com.jee.learn.interfaces.support.BaseServiceImpl;

@Service
@Transactional(readOnly = true)
public class TestServiceImpl extends BaseServiceImpl<ApiUser> implements TestService {

    @Transactional(readOnly = false)
    @Override
    public void test1() {
        try {
            ApiUser u = findOne("1");
            logger.debug("findOne = {}", u.getLoginName());

            logger.debug("save 之前 = {}", u.getRemarks());
            u.setRemarks(String.valueOf(System.currentTimeMillis()));
            save(u);
            u = findOne("1");
            logger.debug("save 之后 = {}", u.getRemarks());
        } catch (Exception e) {
            logger.debug("", e);
        }
    }

}

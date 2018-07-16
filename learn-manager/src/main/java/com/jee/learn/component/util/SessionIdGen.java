package com.jee.learn.component.util;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.jee.learn.common.util.IdGen;

@Component
@Lazy(false)
public class SessionIdGen implements SessionIdGenerator {

    @Autowired
    private IdGen idGen;

    @Override
    public Serializable generateId(Session session) {
        return idGen.uuid();
    }

}

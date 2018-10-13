package com.jee.learn.manager.config.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.jee.learn.manager.util.idgen.IdGenerate;

@Component
@Lazy(false)
public class CustomSessionIdGenerator implements SessionIdGenerator {

    @Override
    public Serializable generateId(Session session) {
        return IdGenerate.fastUUID();
    }

}

package com.jee.learn.manager.config.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.jee.learn.manager.util.idgen.IdGenerate;

/**
 * session id 生产器
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月16日 上午10:27:10 ccp 新建
 */
@Component
@Lazy(false)
public class CustomSessionIdGenerator implements SessionIdGenerator {

    @Override
    public Serializable generateId(Session session) {
        return IdGenerate.fastUUID();
    }

}

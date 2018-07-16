package com.jee.learn.component.security.realm;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import com.jee.learn.component.security.CustomUsernamePasswordToken;

/**
 * <p>
 * 自定义 ModularRealmAuthenticator 认证器
 * </p>
 * <p>
 * 使用securityManager.setAuthenticator(authenticator)
 * 可以实现多Realm认证，并且可以指定特定Realm处理特定类型的验证
 * </p>
 * 注意，当需要分别定义处理普通用户和管理员验证的Realm时，对应Realm的全类名应该包含字符串 (字符串在 {@link RealmType }
 * 中定义) "User", 或者"Admin". 并且, 他们不能相互包含, 例如, 处理普通用户验证的Realm的全类名中不应该包含字符串"Admin".
 * <br/>
 * 参考:https://blog.csdn.net/xiangwanpeng/article/details/54802509
 * 
 * @author admin
 * @version 1.0 <br/>
 *          修改记录: <br/>
 *          1.2018年5月9日 下午10:57:16 admin 新建
 */
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {

        // 判断getRealms()是否返回为空
        assertRealmsConfigured();

        // 强制转换回自定义的CustomizedToken
        CustomUsernamePasswordToken customizedToken = (CustomUsernamePasswordToken) authenticationToken;
        // realm类型
        String loginType = customizedToken.getRealmType();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 登录类型对应的所有Realm
        Collection<Realm> typeRealms = new ArrayList<>();
        for (Realm realm : realms) {
            if (realm.getName().contains(loginType))
                typeRealms.add(realm);
        }

        // 判断是单Realm还是多Realm
        if (typeRealms.size() == 1) {
            return doSingleRealmAuthentication(typeRealms.iterator().next(), customizedToken);
        } else {
            return doMultiRealmAuthentication(typeRealms, customizedToken);
        }
    }

}

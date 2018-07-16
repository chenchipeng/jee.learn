package com.jee.learn.component.security.realm;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.jee.learn.component.security.CustomCredentialsMatcher;

/**
 * 子项目的安全认证实现类(父类)<br/>
 * 提供钩子方法, 由子类继承实现
 * 
 * @author admin
 * @version 1.0 <br/>
 *          修改记录: <br/>
 *          1.2018年5月9日 下午11:26:12 admin 新建
 */
@Component
@DependsOn({ "sessionDAO" })
public class CustomAuthorizingRealm extends AuthorizingRealm {

    /**
     * 认证回调函数, 登录时调用<br/>
     * 主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 设定密码校验的Hash算法与迭代次数
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        setCredentialsMatcher(CustomCredentialsMatcher.initHashedCredentialsMatcher());
    }

}

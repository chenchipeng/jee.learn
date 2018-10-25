package com.jee.learn.manager.support.servlet.captcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jee.learn.manager.support.cache.CacheConstants;
import com.jee.learn.manager.support.cache.EhcacheService;

@Component
public class CaptchaUtil {

    public static final int LIGIN_FAIL_NUM = 3;// 校验码错误次数
    public static final String LOGIN_FAIL_TIMES = "_loginFailTimes";

    @Autowired
    private EhcacheService ehcacheService;

    /**
     * 是否是验证码登录
     * 
     * @param useruame 用户名
     * @param isFail true->计数加1
     * @param clean true->计数清零
     * @return
     */
    public boolean isCaptchaLogin(String useruame, boolean isFail, boolean clean) {
        String key = useruame + LOGIN_FAIL_TIMES;

        Integer loginFailTimes = (Integer) ehcacheService.get(CacheConstants.EHCACHE_SHIRO, key);
        if (loginFailTimes == null) {
            loginFailTimes = 0;
        }
        if (isFail) {
            loginFailTimes++;
            ehcacheService.put(CacheConstants.EHCACHE_SHIRO, key, loginFailTimes);
        }
        if (clean) {
            ehcacheService.remove(CacheConstants.EHCACHE_SHIRO, key);
        }

        return loginFailTimes >= LIGIN_FAIL_NUM;
    }

}

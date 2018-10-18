package com.jee.learn.manager.support.servlet.captcha;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jee.learn.manager.support.cache.CacheConstants;
import com.jee.learn.manager.support.cache.EhcacheService;

@Component
public class CaptchaUtil {

    public static final int LIGIN_FAIL_NUM = 3;// 校验码错误次数
    public static final String LOGIN_FAIL_MAP = "loginFailMap";

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
    @SuppressWarnings("unchecked")
    public boolean isCaptchaLogin(String useruame, boolean isFail, boolean clean) {
        Map<String, Integer> loginFailMap = (Map<String, Integer>) ehcacheService.get(CacheConstants.EHCACHE_SHIRO,
                LOGIN_FAIL_MAP);
        if (loginFailMap == null) {
            loginFailMap = new HashMap<>(1);
            ehcacheService.put(CacheConstants.EHCACHE_SHIRO, LOGIN_FAIL_MAP, loginFailMap);
        }

        Integer loginFailNum = loginFailMap.get(useruame);
        if (loginFailNum == null) {
            loginFailNum = 0;
        }
        if (isFail) {
            loginFailNum++;
            loginFailMap.put(useruame, loginFailNum);
        }
        if (clean) {
            loginFailMap.remove(useruame);
        }
        return loginFailNum >= LIGIN_FAIL_NUM;
    }

}

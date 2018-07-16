package com.jee.learn.component.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
public class ValidateCodeUtils {
    
    @Autowired
    private CacheUtils cacheUtils;
    
    /**
     * 是否是验证码登录
     * 
     * @param useruame
     *            用户名
     * @param isFail
     *            计数加1
     * @param clean
     *            计数清零
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean) {
        Map<String, Integer> loginFailMap = (Map<String, Integer>) cacheUtils.get("loginFailMap");
        if (loginFailMap == null) {
            loginFailMap = Maps.newHashMap();
            cacheUtils.put("loginFailMap", loginFailMap);
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
        // 失败三次后需要验证码
        return loginFailNum >= 3;
    }

}

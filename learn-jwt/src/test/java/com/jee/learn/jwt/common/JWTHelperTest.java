package com.jee.learn.jwt.common;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jee.learn.jwt.common.helper.JWTHelper;
import com.jee.learn.jwt.common.helper.RsaKeyHelper;

/**
 * JWT 体验类
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月20日 下午2:57:16 ccp 新建
 */
public class JWTHelperTest {

    private static final Logger log = LoggerFactory.getLogger(JWTHelperTest.class);

    // token 生成与解析
    @Test
    public void tokenTest() {

        IJWTInfo jwtInfo = new JWTInfo("9527", "1", "ccp");
        Map<String, byte[]> keyMap = null;
        int expire = 3600;

        try {
            // 生成
            keyMap = RsaKeyHelper.generateKey("dhcp");
            String token = JWTHelper.generateToken(jwtInfo, keyMap.get(JWTConstants.PRI_KEY), expire);
            tokenLog(token);
            // 解析
            IJWTInfo obj = JWTHelper.getInfoFromToken(token, keyMap.get(JWTConstants.PUB_KEY));
            log.info("{} {} {}", obj.getId(), obj.getName(), obj.getUniqueName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // log 打印
    private void tokenLog(String token) {
        log.info("token.length = {}", token.length());
        log.info("token = {}", token);
        String[] tokenAry = token.split("\\.");
        log.info("长度 = {} 头 = {}", tokenAry[0].length(), tokenAry[0]);
        log.info("长度 = {} 荷载 = {}", tokenAry[1].length(), tokenAry[1]);
        log.info("长度 = {} 签名 = {}", tokenAry[2].length(), tokenAry[2]);
    }

}

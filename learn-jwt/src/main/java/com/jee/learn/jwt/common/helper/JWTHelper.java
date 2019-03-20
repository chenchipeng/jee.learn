package com.jee.learn.jwt.common.helper;

import org.joda.time.DateTime;

import com.jee.learn.jwt.common.IJWTInfo;
import com.jee.learn.jwt.common.JWTConstants;
import com.jee.learn.jwt.common.JWTInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * jwt工具类<br/>
 * Update by ccp Created by ace on 2017/9/10.<br/>
 * 参考: https://gitee.com/minull/ace-security/tree/master
 *
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年12月26日 下午8:12:42 ccp 更新
 */
public class JWTHelper {
    private static RsaKeyHelper rsaKeyHelper = new RsaKeyHelper();

    /**
     * 密钥加密token
     *
     * @param jwtInfo
     * @param priKeyPath
     * @param expire     seconds
     * @return
     * @throws Exception
     */
    public static String generateToken(IJWTInfo jwtInfo, String priKeyPath, int expire) throws Exception {
        String compactJws = Jwts.builder().setSubject(jwtInfo.getUniqueName())
                .claim(JWTConstants.JWT_KEY_USER_ID, jwtInfo.getId())
                .claim(JWTConstants.JWT_KEY_USER_NAME, jwtInfo.getName())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())// 在当前时间上增加指定秒数后再转为Date
                .signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKeyPath)).compact();
        return compactJws;
    }

    /**
     * 密钥加密token
     *
     * @param jwtInfo
     * @param priKey
     * @param expire  seconds
     * @return
     * @throws Exception
     */
    public static String generateToken(IJWTInfo jwtInfo, byte priKey[], int expire) throws Exception {
        String compactJws = Jwts.builder().setSubject(jwtInfo.getUniqueName())
                .claim(JWTConstants.JWT_KEY_USER_ID, jwtInfo.getId())
                .claim(JWTConstants.JWT_KEY_USER_NAME, jwtInfo.getName())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())// 在当前时间上增加指定秒数后再转为Date
                .signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKey)).compact();
        return compactJws;
    }

    /**
     * 公钥解析token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Jws<Claims> parserToken(String token, String pubKeyPath) throws Exception {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKeyPath))
                .parseClaimsJws(token);
        return claimsJws;
    }

    /**
     * 公钥解析token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Jws<Claims> parserToken(String token, byte[] pubKey) throws Exception {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKey)).parseClaimsJws(token);
        return claimsJws;
    }

    /**
     * 获取token中的用户信息
     *
     * @param token
     * @param pubKeyPath
     * @return
     * @throws Exception
     */
    public static IJWTInfo getInfoFromToken(String token, String pubKeyPath) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKeyPath);
        Claims body = claimsJws.getBody();
        return new JWTInfo(body.getSubject(), StringHelper.getObjectValue(body.get(JWTConstants.JWT_KEY_USER_ID)),
                StringHelper.getObjectValue(body.get(JWTConstants.JWT_KEY_USER_NAME)));
    }

    /**
     * 获取token中的用户信息
     *
     * @param token
     * @param pubKey
     * @return
     * @throws Exception
     */
    public static IJWTInfo getInfoFromToken(String token, byte[] pubKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKey);
        Claims body = claimsJws.getBody();
        return new JWTInfo(body.getSubject(), StringHelper.getObjectValue(body.get(JWTConstants.JWT_KEY_USER_ID)),
                StringHelper.getObjectValue(body.get(JWTConstants.JWT_KEY_USER_NAME)));
    }
}

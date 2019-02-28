package com.jee.learn.interfaces.support.web.base;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;

import com.jee.learn.interfaces.support.cache.CacheConstants;
import com.jee.learn.interfaces.support.web.WebConstant;
import com.jee.learn.interfaces.util.mapper.BeanMapper;
import com.jee.learn.interfaces.util.security.MD5Util;

/**
 * 第三方接口处理模板方法类
 * 
 * @author yjf
 * @version 1.0
 *
 *          2018年11月19日 上午10:51:31
 */
public abstract class AbstractFacadeController<R, D extends DParam> extends BaseController {

    private static final char AMP = '&';
    private static final char EQ = '=';
    private static final long INTERVEL_TIME = 30L * 60 * 1000;// 有效期，30分钟

    protected static final String APPKEY = "564898754124534";// 使用此KEY时, 客户端需要使用下方SECRET生成签名
    protected static final String APPSECRET = "46dd92ee240779ae8a34678593390422423423f";

    /**
     * 勾子方法，由子类实现，接口业务处理
     * 
     * @param rd
     * @param params
     */
    protected abstract ResponseDto<R> handler(RequestParams<D> params);

    /**
     * 接口处理入口
     * 
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public CompletableFuture<ResponseDto<R>> execute(RequestParams<D> params) {
        if (logger.isDebugEnabled()) {
            logger.debug("进入第三方接口处理 -> 请求参数：{}", params);
        }

        // 参数校验
        ResponseDto<R> response = checkRequestParams(params);
        if (!WebConstant.SUCCESS_CODE.equals(response.getC())) {
            return CompletableFuture.completedFuture(response);
        }

        // 校验签名
        response = checkSignature(params);
        if (!WebConstant.SUCCESS_CODE.equals(response.getC())) {
            return CompletableFuture.completedFuture(response);
        }

        String redisKey = getRedisKey(params);
        if (StringUtils.isNotBlank(redisKey)) {
            response = (ResponseDto<R>) redisService.getKeyValue(redisKey);
            if (response != null) {
                return CompletableFuture.completedFuture(response);
            }
        }

        // 业务处理
        response = handler(params);

        if (StringUtils.isNotBlank(redisKey)) {
            redisService.setKeyValue(redisKey, response, CacheConstants.SIXTY);
        }

        return CompletableFuture.completedFuture(response);
    }

    /**
     * 参数校验 默认校验 H D T
     * 
     * @param params
     * @return
     */
    protected ResponseDto<R> checkRequestParams(RequestParams<D> params) {
        if (params.getH() == null || params.getD() == null || StringUtils.isBlank(params.getH().getT())) {
            return new ResponseDto<>(WebConstant.PARAMETER_ERROR_CODE, WebConstant.PARAMETER_ERROR_MESSAGE);
        }
        return new ResponseDto<>(WebConstant.SUCCESS_CODE);
    }

    /**
     * 校验调用是否合法
     * 
     * @param params
     * @return
     */
    protected ResponseDto<R> checkSignature(RequestParams<D> params) {
        // 检验appkey合法性
        if (!params.getH().getK().equals(APPKEY)) {
            return new ResponseDto<>(WebConstant.APP_IDENTITY_ERROR_CODE, WebConstant.APP_IDENTITY_ERROR_MESSAGE);
        }

        // 检验时戳
        if (System.currentTimeMillis() - Long.valueOf(params.getH().getI()) > INTERVEL_TIME) {
            return new ResponseDto<>(WebConstant.APP_SIGN_TIMEOUT_CODE, WebConstant.APP_SIGN_TIMEOUT_MESSAGE);
        }

        // 检验签名
        if (!params.getH().getS().equals(generateSignature(params))) {
            return new ResponseDto<>(WebConstant.APP_SIGN_INVALID_CODE, WebConstant.APP_SIGN_INVALID_MESSAGE);
        }

        return new ResponseDto<>(WebConstant.SUCCESS_CODE);
    }

    /**
     * 获取redis缓存key，返回null表示不用缓存
     * 
     * @param params
     * @return
     */
    protected String getRedisKey(RequestParams<D> params) {
        return null;
    }

    /** 生成签名 */
    @SuppressWarnings("unchecked")
    private String generateSignature(RequestParams<D> params) {

        // 1.参数块d转map对象
        Map<String, Object> map = BeanMapper.map(params.getD(), HashMap.class);
        // 2.map对象根据key排序, 升序
        map = sortMapByKey(map);
        // 3.组装类似URL参数格式的字符串, 使用&连接
        StringBuffer sb = new StringBuffer();
        sb.append("?t=").append(params.getH().getT());
        if (map != null) {
            for (Entry<String, Object> entity : map.entrySet()) {
                sb.append(AMP).append(entity.getKey()).append(EQ)
                        .append(entity.getValue() == null ? StringUtils.EMPTY : entity.getValue());
            }
        }
        // 4.对参数串进行32位MD5, 然后拼接上APPSECRET再次MD5
        String md5 = MD5Util.md5Hex(MD5Util.md5Hex(sb.toString()) + APPSECRET);

        // x.打印结果
        if (logger.isDebugEnabled()) {
            logger.debug("明文={}", sb.toString());
            logger.debug("密文={}", md5);
            for (Entry<String, Object> entity : map.entrySet()) {
                logger.debug("key={} value={}", entity.getKey(), entity.getValue());
            }
        }

        return md5;
    }

    /**
     * 使用 Map按key进行排序
     * 
     * @param map
     * @return
     */
    private Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }

        });
        sortMap.putAll(map);
        return sortMap;
    }

}

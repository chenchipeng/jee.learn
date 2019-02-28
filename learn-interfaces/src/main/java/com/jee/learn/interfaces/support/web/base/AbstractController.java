package com.jee.learn.interfaces.support.web.base;

import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.jee.learn.interfaces.support.cache.CacheConstants;
import com.jee.learn.interfaces.support.web.WebConstant;

/**
 * 接口处理模板方法类
 * 
 * @author yjf
 * @version 1.0
 *
 *          2018年9月15日 下午5:37:09
 */
public abstract class AbstractController<R, D extends DParam> extends BaseController {

    protected static final String WECHAT_ACCESS_TOKEN = "4d092ee240779ae8a34678593390422f"; // 请求token校验

    /**
     * 接口处理入口
     * 
     * @param params
     * @return
     */
    public CompletableFuture<ResponseDto<R>> execute(RequestParams<D> params) {
        return execute(params, null, null);
    }

    /**
     * 接口处理入口<br/>
     * 扩展获取客户端IP, 使用{@link HParam#getIp()}获取
     * 
     * @param params
     * @param request
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
    public CompletableFuture<ResponseDto<R>> execute(RequestParams<D> params, HttpServletRequest request,
            HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug("API请求参数：{}", params);
        }

        // 参数校验
        ResponseDto<R> dto = checkRequestParams(params);
        if (!WebConstant.SUCCESS_CODE.equals(dto.getC())) {
            return CompletableFuture.completedFuture(dto);
        }

        // 校验用户
        dto = checkToken(params);
        if (!WebConstant.SUCCESS_CODE.equals(dto.getC())) {
            return CompletableFuture.completedFuture(dto);
        }

        String redisKey = getRedisKey(params);
        if (StringUtils.isNotBlank(redisKey)) {
            dto = (ResponseDto<R>) redisService.getKeyValue(redisKey);
            if (dto != null) {
                return CompletableFuture.completedFuture(dto);
            }
        }

        // 获取客户端IP
        if (request != null && params != null && params.getH() != null) {
            params.getH().setIp(getIp(request));
        }

        // 业务处理
        dto = handler(params);

        if (StringUtils.isNotBlank(redisKey)) {
            redisService.setKeyValue(redisKey, dto, CacheConstants.SIXTY);
        }

        return CompletableFuture.completedFuture(dto);
    }

    /**
     * 第一步参数校验<br/>
     * 默认校验 H, D, T 是否为空
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
     * 第二步校验token是否合法
     * 
     * @param params
     * @return
     */
    protected ResponseDto<R> checkToken(RequestParams<D> params) {
        if (StringUtils.isBlank(params.getH().getT())) {
            return new ResponseDto<>(WebConstant.PARAMETER_ERROR_CODE, WebConstant.PARAMETER_ERROR_MESSAGE);
        }

        if (getCurrentUser(params) == null) {
            return new ResponseDto<>(WebConstant.RELOGIN_CODE, WebConstant.RELOGIN_MESSAGE);
        }
        return new ResponseDto<>(WebConstant.SUCCESS_CODE);
    }

    /**
     * 获取当前用户
     * 
     * @param params
     * @return
     */
    protected Object getCurrentUser(RequestParams<D> params) {
        return new Object();
    }

    /**
     * 第三步具体操作. 勾子方法，由子类实现，接口业务处理
     * 
     * @param rd
     * @param params
     */
    protected abstract ResponseDto<R> handler(RequestParams<D> params);

    /**
     * 第四步获取redis缓存key，返回null表示不用缓存
     * 
     * @param params
     * @return
     */
    protected String getRedisKey(RequestParams<D> params) {
        return null;
    }

    protected void getClientIP() {

    }

}

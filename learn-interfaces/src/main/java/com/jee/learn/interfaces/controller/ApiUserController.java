package com.jee.learn.interfaces.controller;

import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jee.learn.interfaces.dto.RequestDto;
import com.jee.learn.interfaces.dto.ResponseDto;
import com.jee.learn.interfaces.dto.user.UserDto;
import com.jee.learn.interfaces.service.ApiUserService;
import com.jee.learn.interfaces.util.CacheConstants;
import com.jee.learn.interfaces.util.cache.EhcacheService;
import com.jee.learn.interfaces.util.codec.Md5Utils;
import com.jee.learn.interfaces.util.mapper.JsonMapper;

@RestController
public class ApiUserController extends BaseController {

    @Autowired
    private EhcacheService ehcacheService;
    @Autowired
    private ApiUserService apiUserService;

    @Async
    @PostMapping(path = "/rest/learn.user.info", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<ResponseDto<UserDto>> get(@RequestBody RequestDto<UserDto> params) {
        ResponseDto<UserDto> rd = new ResponseDto<UserDto>();

        // 尝试从缓存中读取数据
        String cacheKey = Md5Utils.getMD5(JsonMapper.toJson(params));
        String data = String.valueOf(ehcacheService.get(CacheConstants.EHCACHE_DEFAULT, cacheKey));
        if (StringUtils.isNotBlank(data) && !CacheConstants.NULL_STRING.equals(data)) {
            logger.debug("======读取ehcache缓存======");
            rd = JsonMapper.fromJson(data, ResponseDto.class, UserDto.class);
            return CompletableFuture.completedFuture(rd);
        }
        
        // 没有缓存时从数据库中获取数据
        rd = apiUserService.get(params);
        
        // 写入缓存
        ehcacheService.put(CacheConstants.EHCACHE_DEFAULT, cacheKey, JsonMapper.toJson(rd));
        return CompletableFuture.completedFuture(rd);
    }

    @Async
    @PostMapping(path = "/rest/learn.user.update", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<ResponseDto<UserDto>> update(@RequestBody RequestDto<UserDto> params) {
        ResponseDto<UserDto> rd = apiUserService.save(params);
        return CompletableFuture.completedFuture(rd);
    }

}

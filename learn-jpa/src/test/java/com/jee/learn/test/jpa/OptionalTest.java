package com.jee.learn.test.jpa;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jee.learn.jpa.domain.ApiUser;
import com.jee.learn.jpa.util.mapper.JsonMapper;

/**
 * JDK 1.8 {@link Optional} 体验<br/>
 * https://www.cnblogs.com/zhangboyu/p/7580262.html
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月7日 上午11:37:44 1002360 新建
 */
public class OptionalTest {

    private static Logger logger = LoggerFactory.getLogger(OptionalTest.class);

    @Test
    public void nullTest() {
        ApiUser u = new ApiUser();
        u.setId("1");

        /* Optional对象创建 */

        // 对象即可能是 null 也可能是非 null，使用 ofNullable() 方法
        Optional<ApiUser> opt = Optional.ofNullable(u);
        // 对象不为 null, isPresent()返回 true
        logger.debug("{}", opt.isPresent());

        /* Optional判空 */

        // 判断入如果不为空
        opt.ifPresent(obj -> {
            obj.setLoginName("alice");
            logger.debug("{}", JsonMapper.toJson(opt.get()));
        });

        // Optional 不能判断 EMPTY 的情况
        String str = StringUtils.EMPTY;
        Optional<String> strOpt = Optional.ofNullable(str);
        logger.debug("{}", strOpt.isPresent());// 依然是true

        /* 返回默认值 */

        u = null;
        // orElseGet()比orElse()好一点，因为在ofNullable()有值的时候orElseGet()不会执行而orElse()会
        Optional.ofNullable(u).orElseGet(() -> {
            // 当ofNullable() 方法发现对象为 null 时执行操作
            logger.debug("create new ApiUser");
            return new ApiUser();
        });

        /* 返回异常 */
        u = null;
        try {
            Optional.ofNullable(u).orElseThrow(() -> new RuntimeException("ofNullable()没有值，抛出自选异常"));
        } catch (Exception e) {
            logger.info("{}", e.getMessage());
        }

    }

}

package com.jee.learn.interfaces.support.web.base;

import java.util.HashMap;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * d 响应参数基类
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年12月27日 下午8:41:43 ccp 新建
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class DDto extends HashMap<String, Object>{

    private static final long serialVersionUID = 1L;

}

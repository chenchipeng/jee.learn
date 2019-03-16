package com.jee.learn.interfaces.gen.dto;

import com.jee.learn.interfaces.support.web.dto.DParam;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * GenTableParam
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月16日 下午4:36:56 ccp 新建
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GenTableParam extends DParam {

    private String name;

}

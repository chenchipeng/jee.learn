package com.jee.learn.manager.support.filter.wrapper;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 动态修改request的parameter<br/>
 * 参考:https://blog.csdn.net/xieyuooo/article/details/8447301
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年2月12日 上午10:49:34 ccp 新建
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {

    protected Logger log = LoggerFactory.getLogger(getClass());
    private Map<String, String[]> params;

    public ParameterRequestWrapper(HttpServletRequest request) {
        // 将request交给父类，以便于调用对应方法的时候，将其输出，其实父亲类的实现方式和第一种new的方式类似
        super(request);

        // 将参数表，赋予给当前的Map以便于持有request中的参数
        params = new HashMap<String, String[]>(request.getParameterMap().size());
        this.params.putAll(request.getParameterMap());

        // 参数转换
        this.modifyParameterValues();
    }

    // 重载一个构造方法
    public ParameterRequestWrapper(HttpServletRequest request, Map<String, Object> extendParams) {
        this(request);

        // 这里将扩展参数写入参数表
        addAllParameters(extendParams);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new Vector<String>(params.keySet()).elements();
    }

    @Override
    public String getParameter(String name) {

        // 重写getParameter，代表参数从当前类中的map获取
        String[] values = params.get(name);

        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    /** 获取参数 */
    public String[] getParameterValues(String name) {
        // 同上
        return params.get(name);
    }

    /** 增加多个参数 */
    public void addAllParameters(Map<String, Object> otherParams) {
        // 增加多个参数
        for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
            addParameter(entry.getKey(), entry.getValue());
        }
    }

    /** 增加单个参数 */
    public void addParameter(String name, Object value) {
        // 增加参数
        if (value != null) {
            if (value instanceof String[]) {
                params.put(name, (String[]) value);
            } else if (value instanceof String) {
                params.put(name, new String[] { (String) value });
            } else {
                params.put(name, new String[] { String.valueOf(value) });
            }
        }
    }

    /** 修改参数值 */
    protected void modifyParameterValues() {
        Set<String> set = params.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            String[] values = params.get(key);
            for (String v : values) {
                log.trace("参数检测: {}", v);
            }
            params.put(key, values);
        }
    }
}

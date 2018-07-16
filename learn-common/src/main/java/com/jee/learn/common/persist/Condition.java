package com.jee.learn.common.persist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.jee.learn.common.persist.dao.IEntityDao;

/**
 * 
 * <p>
 * Title: Condition
 * </p>
 * <p>
 * Description: 条件封装类，用于封装查询条件 {@link IEntityDao}
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013 ITDCL All right reserved.
 * </p>
 * <p>
 * Company: ITDCL
 * </p>
 * 
 * @author yjf
 * @version 1.0
 *
 *          修改记录: 下面填写修改的内容以及修改的日期 1.2013-9-6 下午5:11:49 yjf new
 */
public class Condition implements Iterable<Condition.Paramerter>, Serializable {

    private static final long serialVersionUID = -729360116543324193L;

    private String groupby;
    private List<Paramerter> params = new ArrayList<Condition.Paramerter>();

    public String getGroupby() {
        return groupby;
    }

    public void setGroupby(String groupby) {
        this.groupby = groupby;
    }

    public int size() {
        return params.size();
    }

    public boolean isEmpty() {
        return params.isEmpty();
    }

    /**
     * 创建条件类实例
     */
    public Condition() {

    }

    /**
     * 创建条件类实例
     * 
     * @param params
     */
    public Condition(Paramerter... params) {
        this(Arrays.asList(params));
    }

    /**
     * 创建条件类实例
     * 
     * @param params
     */
    public Condition(List<Paramerter> params) {
        if (params == null || params.isEmpty()) {
            throw new IllegalArgumentException("至少得提供一个查询条件对象");
        }
        this.params.addAll(params);
    }

    /**
     * 创建条件类实例
     * 
     * @param fieldName
     * @param operator
     * @param value
     */
    public Condition(String fieldName, Operator operator, Object value) {
        this(new Paramerter(fieldName, operator, value));
    }

    /**
     * 添加条件参数
     * 
     * @param param
     * @return
     */
    public Condition add(Paramerter param) {
        this.params.add(param);

        return this;
    }

    /**
     * 添加条件参数
     * 
     * @param fieldName
     * @param operator
     * @param value
     * @return
     */
    public Condition add(String fieldName, Operator operator, Object value) {
        return this.add(new Paramerter(fieldName, operator, value));
    }

    /**
     * 返回与当前对象的条件合并后产生新的条件对象
     * 
     * @param con
     * @return
     */
    public Condition and(Condition con) {
        if (con == null) {
            return this;
        }
        List<Paramerter> list = new ArrayList<Condition.Paramerter>(this.params);
        for (Paramerter param : con) {
            list.add(param);
        }
        return new Condition(list);
    }

    @Override
    public Iterator<Paramerter> iterator() {
        return this.params.iterator();
    }

    /**
     * EQ,等于 NE,不等于 LIKE,模糊查询 GT,大于 LT,小于 GE,大于或者等于 LE,小于或者等于 ISNULL,为null
     * ISNOTNULL,不为null ISEMPTY,集合属性为empty ISNOTEMPTY,集合属性不为empty IN,属性在集合中
     * NOTIN,属性不在集合中 OR,或条件操作
     */
    public enum Operator {
        EQ, NE, LIKE, GT, LT, GE, LE, ISNULL, ISNOTNULL, ISEMPTY, ISNOTEMPTY, IN, NOTIN, OR
    }

    /**
     * 
     * <p>
     * Title: Paramerter
     * </p>
     * <p>
     * Description: 内部参数类，包含键名、值及操作三元素信息 {@link Condition}
     * </p>
     * <p>
     * Copyright: Copyright (c) 2013 ITDCL All right reserved.
     * </p>
     * <p>
     * Company: ITDCL
     * </p>
     * 
     * @author yjf
     * @version 1.0
     *
     *          修改记录: 下面填写修改的内容以及修改的日期 1.2013-9-6 下午5:13:04 yjf new
     */
    public static class Paramerter implements Serializable {
        private static final long serialVersionUID = 2561923223819705L;
        private String fieldName;
        private Object value;
        private Operator operator;

        public Paramerter(String fieldName, Operator operator, Object value) {
            this.fieldName = fieldName;
            this.value = value;
            this.operator = operator;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Operator getOperator() {
            return operator;
        }

        public void setOperator(Operator operator) {
            this.operator = operator;
        }

    }

}

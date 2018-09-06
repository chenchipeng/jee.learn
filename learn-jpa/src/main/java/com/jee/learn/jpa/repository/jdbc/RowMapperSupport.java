package com.jee.learn.jpa.repository.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * <p>
 * Title: RowMapperSupport
 * </p>
 * <p>
 * Description: Pojo对象转换器，用于将jdbc查询结果转换为pojo对象
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
 *          修改记录: 下面填写修改的内容以及修改的日期<br/>
 *          1.2013-9-10 上午11:13:41 yjf new
 */
public class RowMapperSupport {

    private Class<?> clazz;

    public RowMapperSupport(Class<?> clazz) {
        super();
        this.clazz = clazz;
    }

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Object pojo = null;
        try {

            pojo = clazz.newInstance();

            Field fields[] = clazz.getDeclaredFields();
            String pojoTypeName = null;
            String columnLabel = null;
            String setterName = null;
            Method setMthd = null;
            for (Field fd : fields) {
                pojoTypeName = fd.getType().getName();
                columnLabel = parseDbColumn(fd.getName());
                if (isExistColumn(rs, columnLabel)) {
                    setterName = "set" + fd.getName().substring(0, 1).toUpperCase() + fd.getName().substring(1);
                    setMthd = clazz.getDeclaredMethod(setterName, new Class<?>[] { fd.getType() });

                    if (pojoTypeName.equals("java.lang.Integer") || pojoTypeName.equals("int")) {
                        setMthd.invoke(pojo, rs.getInt(columnLabel));
                    } else if (pojoTypeName.equals("java.lang.Long") || pojoTypeName.equals("long")) {
                        setMthd.invoke(pojo, rs.getLong(columnLabel));
                    } else if (pojoTypeName.equals("java.lang.Double") || pojoTypeName.equals("double")) {
                        setMthd.invoke(pojo, rs.getDouble(columnLabel));
                    } else if (pojoTypeName.equals("java.lang.Float") || pojoTypeName.equals("float")) {
                        setMthd.invoke(pojo, rs.getFloat(columnLabel));
                    } else if (pojoTypeName.equals("java.lang.Short") || pojoTypeName.equals("short")) {
                        setMthd.invoke(pojo, rs.getShort(columnLabel));
                    } else if (pojoTypeName.equals("java.lang.String")) {
                        setMthd.invoke(pojo, rs.getString(columnLabel));
                    } else if (pojoTypeName.equals("java.math.BigDecimal")) {
                        setMthd.invoke(pojo, rs.getBigDecimal(columnLabel));
                    } else if (pojoTypeName.equals("java.util.Date")) {
                        setMthd.invoke(pojo, rs.getDate(columnLabel));
                    } else if (pojoTypeName.equals("java.sql.Timestamp")) {
                        setMthd.invoke(pojo, rs.getTimestamp(columnLabel));
                    } else {
                        setMthd.invoke(pojo, rs.getObject(columnLabel));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return pojo;

    }

    /** 判断查询结果集中是否存在某列 */
    private boolean isExistColumn(ResultSet rs, String columnName) {
        try {
            if (rs.findColumn(columnName) > 0) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    /** 数据表字段名解析 */
    private String parseDbColumn(String str) {
        char[] chars = str.toCharArray();
        String dbColumnName = "";
        for (int i = 0, len = chars.length; i < len; i++) {
            char ch = chars[i];
            if (Character.isLowerCase(ch)) {
                dbColumnName += ch;
            } else if (Character.isUpperCase(ch)) {
                dbColumnName += ("_" + Character.toLowerCase(ch));
            } else {
                dbColumnName += ch;
            }
        }
        return dbColumnName;
    }

}

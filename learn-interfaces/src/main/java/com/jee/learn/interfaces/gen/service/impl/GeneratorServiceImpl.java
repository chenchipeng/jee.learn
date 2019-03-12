package com.jee.learn.interfaces.gen.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jee.learn.interfaces.config.datasource.dynamic.DynamicDataSource;
import com.jee.learn.interfaces.gen.GenConstants;
import com.jee.learn.interfaces.gen.dto.GenTableColumnDto;
import com.jee.learn.interfaces.gen.dto.GenTableDto;
import com.jee.learn.interfaces.gen.service.GeneratorService;
import com.jee.learn.interfaces.util.text.CamelUtil;

/**
 * 数据库源数据service
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月6日 上午11:30:04 ccp 新建
 */
@Component
public class GeneratorServiceImpl implements GeneratorService {

    /* 数据库类型 */
    private static final String ORACLE = "oracle";
    private static final String MSSQL = "mssql";
    private static final String MYSQL = "mysql";

    /* 获取表名和注释的sql */
    private static final String ORACLE_TABLE_SELECT = "SELECT t.TABLE_NAME AS name, c.COMMENTS AS comments FROM user_tables t, user_tab_comments c WHERE t.table_name = c.table_name AND 1=1 ORDER BY t.TABLE_NAME";
    private static final String MSSQL_TABLE_SELECT = "ELECT t.name AS name,b.value AS comments FROM sys.objects t LEFT JOIN sys.extended_properties b ON b.major_id=t.object_id and b.minor_id=0 and b.class=1 AND b.name='MS_Description' WHERE t.type='U' AND 1=1 ORDER BY t.name";
    private static final String MYSQL_TABLE_SELECT = "SELECT t.TABLE_NAME AS name, t.TABLE_COMMENT AS comments FROM information_schema.`TABLES` t WHERE t.TABLE_SCHEMA = (select database()) AND 1=1 ORDER BY t.TABLE_NAME";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Override
    public List<GenTableDto> selectDataTables() {
        return selectDataTables(StringUtils.EMPTY);
    }

    @Override
    public List<GenTableDto> selectDataTables(String tableName) {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = null;
        List<GenTableDto> tList = new ArrayList<GenTableDto>();
        try {
            Connection conn = dynamicDataSource.getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
            logger.debug("数据库产品名->{} 所连接的数据库->{}", dbmd.getDatabaseProductName(), conn.getCatalog());

            // 创建一个Statement语句对象
            stmt = conn.createStatement();
            // 执行sql语句
            sql = tableSelecter(dbmd.getDatabaseProductName(), tableName);
            rs = stmt.executeQuery(sql);
            // 获取结果
            while (rs.next()) {
                logger.debug("表名->{} 注释->{}", rs.getString(1), rs.getString(2));
                tList.add(new GenTableDto(rs.getString(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            logger.info("获取当前所连接数据库中的所有表异常 sql={}", sql, e);
            return tList;
        } finally {
            close(stmt, rs);
        }
        return tList;
    }

    @Override
    public List<GenTableColumnDto> selectTableColumn(String tableName) {
        // 检查表
        List<GenTableDto> tables = selectDataTables(tableName);
        if (tables == null) {
            throw new IllegalArgumentException(tableName + "不存在");
        }
        // 获取主键信息
        List<String> pkList = selecePrivateKey(tableName);
        // 定义结果集
        List<GenTableColumnDto> cList = new ArrayList<GenTableColumnDto>();
        GenTableColumnDto c = null;

        try {
            Connection conn = dynamicDataSource.getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getColumns(null, null, tableName, "%");

            while (rs.next()) {
                c = new GenTableColumnDto();
                c.setName(rs.getString("COLUMN_NAME"));
                c.setComments(rs.getString("REMARKS"));
                c.setJdbcType(buildJdbcType(rs.getString("TYPE_NAME"), rs.getInt("COLUMN_SIZE"),
                        rs.getInt("DECIMAL_DIGITS")));
                c.setJavaType(buildJavaType(c.getJdbcType()));
                c.setJavaField(CamelUtil.toFieldName(rs.getString("COLUMN_NAME")));
                c.setIsPk(isPK(pkList, rs.getString("COLUMN_NAME")));
                c.setIsNull(GenConstants.YES.equals(rs.getString("IS_NULLABLE")) ? GenConstants.Y : GenConstants.N);
                c.setIsInc(GenConstants.YES.equals(rs.getString("IS_AUTOINCREMENT")) ? GenConstants.Y : GenConstants.N);
                // 加入list
                cList.add(c);

                logger.debug("列名->{} 类型名称->{} 列大小->{} 小数点->{} 注释->{} 自增->{} 为空->{}", rs.getString("COLUMN_NAME"),
                        rs.getString("TYPE_NAME"), rs.getInt("COLUMN_SIZE"), rs.getInt("DECIMAL_DIGITS"),
                        rs.getString("REMARKS"), rs.getString("IS_AUTOINCREMENT"), rs.getString("IS_NULLABLE"));
            }
        } catch (SQLException | IllegalArgumentException e) {
            logger.info("获取指定表的所有列异常", e);
            return cList;
        }
        return cList;
    }

    @Override
    public List<String> selecePrivateKey(String tableName) {
        Statement stmt = null;
        ResultSet rs = null;
        List<String> list = new ArrayList<String>();
        try {
            Connection conn = dynamicDataSource.getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
            rs = dbmd.getPrimaryKeys(null, null, tableName);
            while (rs.next()) {
                list.add(rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            logger.info("获取指定表 {} 的主键信息异常", tableName, e);
            return list;
        } finally {
            close(stmt, rs);
        }
        return list;
    }

    /**
     * 跟据数据库类型选择数据表元数据sql
     * 
     * @param productName
     * @param tableName
     * @return
     */
    private String tableSelecter(String productName, String tableName) {
        boolean b = StringUtils.isBlank(tableName);
        String repKey = "1=1";
        if (ORACLE.equals(productName.toLowerCase())) {
            return b ? ORACLE_TABLE_SELECT : ORACLE_TABLE_SELECT.replace(repKey, "t.TABLE_NAME = '" + tableName + "'");
        }
        if (MSSQL.equals(productName.toLowerCase())) {
            return b ? MSSQL_TABLE_SELECT : MSSQL_TABLE_SELECT.replace(repKey, "t.name = '" + tableName + "'");
        }
        if (MYSQL.equals(productName.toLowerCase())) {
            return b ? MYSQL_TABLE_SELECT : MYSQL_TABLE_SELECT.replace(repKey, "t.TABLE_NAME = '" + tableName + "'");
        }
        throw new IllegalArgumentException("unknown database product");
    }

    /**
     * 关闭数据连接
     * 
     * @param stmt
     * @param rs
     */
    private void close(Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception e) {
            logger.info("关闭数据连接异常", e);
        }
    }

    /**
     * 构造 jdbcType
     * 
     * @param typeName
     * @param columnSize
     * @param decimalDigits
     * @return
     */
    private String buildJdbcType(String typeName, int columnSize, int decimalDigits) {
        StringBuilder sb = new StringBuilder();
        sb.append(typeName).append("(").append(columnSize);
        if (decimalDigits != 0) {
            sb.append(",").append(decimalDigits);
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * 根据 jdbcType 构造 javaType
     * 
     * @param jdbcType
     * @return
     */
    private String buildJavaType(String jdbcType) {

        if ((StringUtils.startsWithIgnoreCase(jdbcType, "CHAR"))
                || (StringUtils.startsWithIgnoreCase(jdbcType, "VARCHAR"))
                || (StringUtils.startsWithIgnoreCase(jdbcType, "TEXT"))
                || (StringUtils.startsWithIgnoreCase(jdbcType, "LONGTEXT"))
                || (StringUtils.startsWithIgnoreCase(jdbcType, "NARCHAR"))) {
            return "String";
        } else if ((StringUtils.startsWithIgnoreCase(jdbcType, "DATE"))
                || (StringUtils.startsWithIgnoreCase(jdbcType, "DATETIME"))
                || (StringUtils.startsWithIgnoreCase(jdbcType, "TIMESTAMP"))) {
            return "java.util.Date";
        } else if ((StringUtils.startsWithIgnoreCase(jdbcType, "BIGINT"))
                || (StringUtils.startsWithIgnoreCase(jdbcType, "NUMBER"))) {
            String[] ss = StringUtils.split(StringUtils.substringBetween(jdbcType, "(", ")"), ",");
            if ((ss != null) && (ss.length == 2) && (Integer.parseInt(ss[1]) > 0)) {
                return "Double";
            } else if ((ss != null) && (ss.length == 1) && (Integer.parseInt(ss[0]) <= 10)) {
                return "Integer";
            } else {
                return "Long";
            }
        } else if (StringUtils.startsWithIgnoreCase(jdbcType, "DECIMAL")) {
            return "java.math.BigDecimal";
        } else if (StringUtils.startsWithIgnoreCase(jdbcType, "TINYINT")
                || (StringUtils.startsWithIgnoreCase(jdbcType, "INT"))) {
            return "Integer";
        } else if (StringUtils.startsWithIgnoreCase(jdbcType, "DOUBLE")
                || (StringUtils.startsWithIgnoreCase(jdbcType, "FLOAT"))) {
            return "Double";
        }

        return "Object";
    }

    /**
     * 检查指定列是否为主键
     * 
     * @param pkList
     * @param columnName
     * @return 0:否, 1:是
     */
    private int isPK(List<String> pkList, String columnName) {
        if (CollectionUtils.isEmpty(pkList) || StringUtils.isBlank(columnName)) {
            return GenConstants.N;
        }
        int r = GenConstants.N;
        for (String pk : pkList) {
            if (pk.equals(columnName)) {
                r = GenConstants.Y;
                break;
            }
        }
        return r;
    }

}

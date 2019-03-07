package com.jee.learn.interfaces.gen.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jee.learn.interfaces.config.datasource.dynamic.DynamicDataSource;
import com.jee.learn.interfaces.gen.domain.GenTable;
import com.jee.learn.interfaces.gen.service.GeneratorService;

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

    private static final String SEMICOLON = ":";
    private static final String TABLE_NAME = "name";
    private static final String TABLE_COMMENT = "comment";

    /* 数据库类型 */
    private static final String ORACLE = "oracle";
    private static final String MSSQL = "mssql";
    private static final String MYSQL = "mysql";

    /* 获取表名和注释的sql */
    private static final String ORACLE_TABLE_SELECT = "SELECT t.TABLE_NAME AS name, c.COMMENTS AS comments FROM user_tables t, user_tab_comments c WHERE t.table_name = c.table_name ORDER BY t.TABLE_NAME";
    private static final String MSSQL_TABLE_SELECT = "ELECT t.name AS name,b.value AS comments FROM sys.objects t LEFT JOIN sys.extended_properties b ON b.major_id=t.object_id and b.minor_id=0 and b.class=1 AND b.name='MS_Description' WHERE t.type='U' ORDER BY t.name";
    private static final String MYSQL_TABLE_SELECT = "SELECT t.TABLE_NAME AS name, t.TABLE_COMMENT AS comments FROM information_schema.`TABLES` t WHERE t.TABLE_SCHEMA = (select database()) ORDER BY t.TABLE_NAME";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Override
    public List<String> selectDataTables() {
        Statement stmt = null;
        ResultSet rs = null;
        List<String> tList = new ArrayList<>();
        try {
            Connection conn = dynamicDataSource.getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
            logger.debug("数据库产品名={} 所连接的数据库={}", dbmd.getDatabaseProductName(), conn.getCatalog());

            // 创建一个Statement语句对象
            stmt = conn.createStatement();
            // 执行sql语句
            rs = stmt.executeQuery(tableSelecter(dbmd.getDatabaseProductName()));
            // 获取结果
            while (rs.next()) {
                logger.debug("表名={} 注释={}", rs.getString(1), rs.getString(2));
                tList.add(rs.getString(1) + SEMICOLON + rs.getString(2));
            }

        } catch (SQLException e) {
            logger.info("获取当前所连接数据库中的所有表异常", e);
            return tList;
        } finally {
            close(stmt, rs);
        }
        return tList;
    }
    
    
    
    public List<GenTable> selectDataTables2() {
        Statement stmt = null;
        ResultSet rs = null;
        List<GenTable> tList = new ArrayList<>();
        try {
            Connection conn = dynamicDataSource.getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
            logger.debug("数据库产品名={} 所连接的数据库={}", dbmd.getDatabaseProductName(), conn.getCatalog());

            // 创建一个Statement语句对象
            stmt = conn.createStatement();
            // 执行sql语句
            rs = stmt.executeQuery(tableSelecter(dbmd.getDatabaseProductName()));
            // 获取结果
            while (rs.next()) {
                logger.debug("表名={} 注释={}", rs.getString(1), rs.getString(2));
                tList.add(rs.getString(1) + SEMICOLON + rs.getString(2));
            }

        } catch (SQLException e) {
            logger.info("获取当前所连接数据库中的所有表异常", e);
            return tList;
        } finally {
            close(stmt, rs);
        }
        return tList;
    }
    
    
    

    @Override
    public void selectTableColumn(String tableKey) {

        try {
            Map<String, String> map = analizeTableKey(tableKey);
            if (map == null) {
                throw new IllegalArgumentException("表名备注解析异常");
            }

            Connection conn = dynamicDataSource.getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();

            ResultSet rs = dbmd.getColumns(null, null, map.get(TABLE_NAME), "%");
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");// 列名
                String dataTypeName = rs.getString("TYPE_NAME");// java.sql.Types类型名称
                int columnSize = rs.getInt("COLUMN_SIZE");// 列大小
                String comment = rs.getString("REMARKS");// 注释

                logger.info("{} {} {} {}", columnName, dataTypeName, columnSize, comment);
            }
        } catch (SQLException | IllegalArgumentException e) {
            logger.info("获取指定表的所有列异常", e);
        }
    }

    @Override
    public Map<String, String> analizeTableKey(String tableKey) {
        if (StringUtils.isBlank(tableKey)) {
            return null;
        }
        String[] ary = tableKey.split(SEMICOLON);
        if (StringUtils.isBlank(ary[0])) {
            return null;
        }
        Map<String, String> map = new HashMap<>(2);
        map.put(TABLE_NAME, ary[0]);
        map.put(TABLE_COMMENT, tableKey.substring(ary[0].length() + 1));
        return map;
    }

    /**
     * 跟据数据库类型选择数据表元数据sql
     * 
     * @param productName
     * @return
     */
    private String tableSelecter(String productName) {
        if (ORACLE.equals(productName.toLowerCase())) {
            return ORACLE_TABLE_SELECT;
        }
        if (MSSQL.equals(productName.toLowerCase())) {
            return MSSQL_TABLE_SELECT;
        }
        if (MYSQL.equals(productName.toLowerCase())) {
            return MYSQL_TABLE_SELECT;
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

}

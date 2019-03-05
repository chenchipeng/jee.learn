package com.jee.learn.interfaces.gen.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.jee.learn.interfaces.config.datasource.dynamic.DynamicDataSource;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*
     * <select id="findTableList" resultType="GenTable"> <if test="dbName == 'oracle'"> SELECT t.TABLE_NAME AS name, c.COMMENTS AS comments FROM user_tables t,
     * user_tab_comments c WHERE t.table_name = c.table_name <if test="name != null and name != ''"> AND t.TABLE_NAME = upper(#{name}) </if> ORDER BY
     * t.TABLE_NAME </if> <if test="dbName == 'mssql'"> SELECT t.name AS name,b.value AS comments FROM sys.objects t LEFT JOIN sys.extended_properties b ON
     * b.major_id=t.object_id and b.minor_id=0 and b.class=1 AND b.name='MS_Description' WHERE t.type='U' <if test="name != null and name != ''"> AND t.name =
     * upper(#{name}) </if> ORDER BY t.name </if> <if test="dbName == 'mysql'"> SELECT t.table_name AS name,t.TABLE_COMMENT AS comments FROM
     * information_schema.`TABLES` t WHERE t.TABLE_SCHEMA = (select database()) <if test="name != null and name != ''"> AND t.TABLE_NAME = upper(#{name}) </if>
     * ORDER BY t.TABLE_NAME </if> </select>
     */

    @Override
    public void selectDataTables() {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            Connection conn = dynamicDataSource.getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
            logger.debug("数据库产品名={} 所连接的数据库={}", dbmd.getDatabaseProductName(), conn.getCatalog());

            stmt = conn.createStatement();// 创建一个Statement语句对象
            rs = stmt.executeQuery(sql);// 执行sql语句

            while (rs.next()) {
                System.out.print(rs.getInt(1) + ",");
                System.out.print(rs.getString(2) + ",");// 直接使用参数
                System.out.print(rs.getString(3) + ",");
                System.out.print(rs.getString(4) + ",");
                System.out.println(rs.getString(5));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt, rs);
        }
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
            logger.info("", e);
        }
    }

}

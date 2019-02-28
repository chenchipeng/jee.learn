package com.jee.learn.manager.gen;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.manager.LearnManagerApplication;
import com.jee.learn.manager.config.datasource.dynamic.DynamicDataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnManagerApplication.class)
public class DataSourceTest {

    private static final Logger log = LoggerFactory.getLogger(DataSourceTest.class);

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Test
    public void test1() {
        try {
            Connection conn = dynamicDataSource.getConnection();
            
            
            DatabaseMetaData dbmd = conn.getMetaData();
            log.info("数据库产品名: {}", dbmd.getDatabaseProductName());
            log.info("所连接的数据库: {}", conn.getCatalog());
            

            ResultSet rs = dbmd.getTables(conn.getCatalog(), null, null, new String[] { "TABLE" });
            while (rs.next()) {
                log.info("表名: {} 备注： {}", rs.getString("TABLE_NAME"), rs.getString("TABLE_NAME"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

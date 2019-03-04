package com.jee.learn.interfaces.gen.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jee.learn.interfaces.config.datasource.dynamic.DynamicDataSource;

@Service
public class GeneratorServiceImpl implements GeneratorService{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private DynamicDataSource dynamicDataSource;

	@Override
    public void selectDataTables() {
        try {
            Connection conn = dynamicDataSource.getConnection();
            
            
            DatabaseMetaData dbmd = conn.getMetaData();
            logger.debug("数据库产品名={} 所连接的数据库={}", dbmd.getDatabaseProductName(),conn.getCatalog());
            

            ResultSet rs = dbmd.getTables(conn.getCatalog(), null, null, new String[] { "TABLE" });
            while (rs.next()) {
            	logger.info("表名: {} 备注： {}", rs.getString("TABLE_NAME"), rs.getString("REMARKS"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	
}

package com.jee.learn.interfaces.gen.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.config.datasource.dynamic.DynamicDataSource;
import com.jee.learn.interfaces.gen.GenConstants;
import com.jee.learn.interfaces.gen.GenConstants.QUERY_TYPE;
import com.jee.learn.interfaces.gen.domain.GenScheme;
import com.jee.learn.interfaces.gen.domain.GenTable;
import com.jee.learn.interfaces.gen.domain.GenTableColumn;
import com.jee.learn.interfaces.gen.dto.GenTableColumnDto;
import com.jee.learn.interfaces.gen.dto.GenTableDto;
import com.jee.learn.interfaces.gen.service.GenSchemeService;
import com.jee.learn.interfaces.gen.service.GenTableColumnService;
import com.jee.learn.interfaces.gen.service.GenTableService;
import com.jee.learn.interfaces.gen.service.GeneratorService;
import com.jee.learn.interfaces.gen.thymeleaf.ThymeleafService;
import com.jee.learn.interfaces.util.io.FileUtil;
import com.jee.learn.interfaces.util.mapper.BeanMapper;
import com.jee.learn.interfaces.util.text.CamelUtil;
import com.jee.learn.interfaces.util.time.DateFormatUtil;

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
    @Autowired
    private ThymeleafService thymeleafService;

    @Autowired
    private GenTableService genTableService;
    @Autowired
    private GenTableColumnService genTableColumnService;
    @Autowired
    private GenSchemeService genSchemeService;

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
        } catch (SQLException | IllegalArgumentException e) {
            logger.info("获取当前所连接数据库中的所有表异常 sql={}", sql, e);
            return null;
        } finally {
            close(stmt, rs);
        }
        return tList;
    }

    @Override
    public List<GenTableColumnDto> selectTableColumn(String tableName) {
        // 定义结果集
        List<GenTableColumnDto> cList = new ArrayList<GenTableColumnDto>();
        GenTableColumnDto c = null;

        // 检查表
        List<GenTableDto> tables = selectDataTables(tableName);
        if (tables == null) {
            throw new IllegalArgumentException(tableName + "不存在");
        }
        // 获取主键信息
        List<String> pkList = selecePrivateKey(tableName);
        if (pkList == null) {
            throw new IllegalArgumentException(tableName + "查询主键异常");
        }

        try {
            Connection conn = dynamicDataSource.getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getColumns(conn.getCatalog(), null, tableName, "%");

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
                c.setClassType(CamelUtil.toClassName(rs.getString("COLUMN_NAME")));
                // 加入list
                cList.add(c);

                logger.debug("列名->{} 类型名称->{} 列大小->{} 小数点->{} 注释->{} 自增->{} 为空->{}", rs.getString("COLUMN_NAME"),
                        rs.getString("TYPE_NAME"), rs.getInt("COLUMN_SIZE"), rs.getInt("DECIMAL_DIGITS"),
                        rs.getString("REMARKS"), rs.getString("IS_AUTOINCREMENT"), rs.getString("IS_NULLABLE"));
            }
        } catch (SQLException | IllegalArgumentException e) {
            logger.info("获取指定表的所有列异常", e);
            return null;
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
            rs = dbmd.getPrimaryKeys(conn.getCatalog(), null, tableName);
            while (rs.next()) {
                list.add(rs.getString("COLUMN_NAME"));
                logger.debug("{} 的主键有 {}", tableName, rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            logger.info("获取指定表 {} 的主键信息异常", tableName, e);
            return null;
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
        sb.append(typeName);
        if (StringUtils.startsWithIgnoreCase(typeName, "CHAR") || StringUtils.startsWithIgnoreCase(typeName, "VARCHAR")
                || StringUtils.startsWithIgnoreCase(typeName, "DECIMAL")
                || StringUtils.startsWithIgnoreCase(typeName, "BIGINT")
                || StringUtils.startsWithIgnoreCase(typeName, "NUMBER")) {
            sb.append("(").append(columnSize);
            if (decimalDigits != 0) {
                sb.append(",").append(decimalDigits);
            }
            sb.append(")");
        }

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

    //////// 封装数据库元数据 ///////

    @Override
    public List<GenTableDto> getTabelList() {
        List<GenTableDto> list = selectDataTables();
        for (GenTableDto dto : list) {
            dto.setLabel(dto.getName() + ":" + dto.getComments());
        }
        return list;
    }

    @Override
    public GenTableDto getTebleInfo(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        // 查找表
        List<GenTableDto> tList = selectDataTables(tableName);
        if (CollectionUtils.isEmpty(tList)) {
            return null;
        }
        GenTableDto table = tList.get(0);
        table.setClassName(CamelUtil.toClassName(tableName));
        // 查找列
        List<GenTableColumnDto> cList = selectTableColumn(tableName);
        if (CollectionUtils.isEmpty(cList)) {
            return null;
        }
        int sort = 0;
        for (GenTableColumnDto c : cList) {
            c.setSort(new BigDecimal(sort += 30));
            c.setIsInsert(GenConstants.Y);

            // private String showType; //
            // 字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）
            // private String dictType; // 字典类型

            c.setIsUuid(GenConstants.N);
            if (c.getIsPk() == GenConstants.Y && "String".equals(c.getJavaType())) {
                c.setIsUuid(GenConstants.Y);
            }

            c.setIsEdit(GenConstants.Y);
            if (StringUtils.equalsIgnoreCase(c.getName(), GenConstants.ID)
                    || StringUtils.equalsIgnoreCase(c.getName(), GenConstants.CREATE_BY)
                    || StringUtils.equalsIgnoreCase(c.getName(), GenConstants.CREATE_DATE)
                    || StringUtils.equalsIgnoreCase(c.getName(), GenConstants.DEL_FLAG)) {
                c.setIsEdit(GenConstants.N);
            }

            c.setIsList(GenConstants.N);
            if (StringUtils.equalsIgnoreCase(c.getName(), GenConstants.NAME)
                    || StringUtils.equalsIgnoreCase(c.getName(), GenConstants.TITLE)
                    || StringUtils.equalsIgnoreCase(c.getName(), GenConstants.UPDATE_DATE)) {
                c.setIsList(GenConstants.Y);
            }

            c.setIsQuery(GenConstants.N);
            if (StringUtils.equalsIgnoreCase(c.getName(), GenConstants.NAME)
                    || StringUtils.equalsIgnoreCase(c.getName(), GenConstants.TITLE)) {
                c.setIsQuery(GenConstants.Y);
            }

            c.setQueryType(QUERY_TYPE.EQUAL.getValue());
            if (StringUtils.equalsIgnoreCase(c.getName(), GenConstants.NAME)
                    || StringUtils.equalsIgnoreCase(c.getName(), GenConstants.TITLE)) {
                c.setQueryType(QUERY_TYPE.LIKE.getValue());
            }
            if (StringUtils.equalsIgnoreCase(c.getName(), GenConstants.CREATE_DATE)
                    || StringUtils.equalsIgnoreCase(c.getName(), GenConstants.UPDATE_DATE)) {
                c.setQueryType(QUERY_TYPE.BETWEEN.getValue());
            }
        }
        table.setColumnDtos(cList);
        return table;
    }

    //////// 写入数据库源数据并生成文件 ////////

    @Transactional(readOnly = false)
    @Override
    public void genCodeFromTable(String tableName) {
        GenTableDto tableDto = getTebleInfo(tableName);
        // 表
        GenTable table = genTableService.findOneByName(tableName);
        if (table == null) {
            table = BeanMapper.map(tableDto, GenTable.class);
            genTableService.save(table);
        }
        logger.info("{} id = {}", tableName, table.getId());
        // 列
        List<GenTableColumnDto> tableColumnDtos = tableDto.getColumnDtos();
        for (GenTableColumnDto genTableColumnDto : tableColumnDtos) {
            if (genTableColumnService.findOneByGenTableIdAndName(table.getId(), genTableColumnDto.getName()) != null) {
                continue;
            }
            genTableColumnDto.setGenTableId(table.getId());
            GenTableColumn column = BeanMapper.map(genTableColumnDto, GenTableColumn.class);
            genTableColumnService.save(column);
        }
    }

    @Transactional(readOnly = false)
    @Override
    public boolean schemeSetting(String tableName, String packageName, String moduleName, String functionAuthor) {
        GenTable table = genTableService.findOneByName(tableName);
        if (table == null) {
            logger.info("无法查找指定表");
            return false;
        }
        if (genSchemeService.findOneByGenTableId(table.getId()) != null) {
            return true;
        }

        GenScheme entity = new GenScheme(table.getComments(), GenConstants.CURD, packageName, moduleName,
                table.getComments(), table.getComments(), functionAuthor, table.getId());
        genSchemeService.save(entity);
        return true;
    }

    @Transactional(readOnly = false)
    @Override
    public boolean writeToFile(String tableName) {
        GenTable table = genTableService.findOneByName(tableName);
        if (table == null) {
            logger.info("代码生成失败! GenTable IS NULL");
            return false;
        }
        GenScheme scheme = genSchemeService.findOneByGenTableId(table.getId());
        if (scheme == null) {
            logger.info("代码生成失败! GenScheme IS NULL");
            return false;
        }
        List<GenTableColumn> columns = genTableColumnService.findByGenTableId(table.getId());
        if (CollectionUtils.isEmpty(columns)) {
            logger.info("代码生成失败! List<GenTableColumn> IS EMPTY");
            return false;
        }

        List<GenTableColumn> pks = genTableColumnService.findPrimaryKey(table.getId());
        GenTableColumn pk = null;
        if (CollectionUtils.isNotEmpty(pks)) {
            pk = pks.get(0);
            if (pks.size() > 1) {
                pk.setJavaField("idGroup");
                pk.setJavaType("Object");
                pk.setComments("存在组合主键, repository需要手工修改");
                logger.info("{} {}", tableName, pk.getComments());
            }
        }

        Map<String, Object> map = new HashMap<>(5);
        map.put("version", DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, new Date()));
        map.put("scheme", scheme);
        map.put("table", table);
        map.put("columns", columns);
        map.put("pk", pk);

        // TODO 这里需要走配置
        String[] dirs = { "gen/domain/", "gen/repository/", "gen/service/", "gen/service/impl/" };
        try {
            for (String dir : dirs) {
                FileUtil.makesureDirExists(dir);
            }
        } catch (Exception e) {
            logger.info("目录创建异常", e);
            return false;
        }

        String entityPath = dirs[0] + table.getClassName() + ".java";
        String repositoryPath = dirs[1] + table.getClassName() + "Repository.java";
        String servicePath = dirs[2] + table.getClassName() + "Service.java";
        String serviceImplPath = dirs[3] + table.getClassName() + "ServiceImpl.java";
        try {
            thymeleafService.writeToFile("entity", map, entityPath);
            thymeleafService.writeToFile("repository", map, repositoryPath);
            thymeleafService.writeToFile("service", map, servicePath);
            thymeleafService.writeToFile("serviceImpl", map, serviceImplPath);
        } catch (Exception e) {
            logger.info("代码生成异常! thymeleaf generator reveive a exception", e);
            return false;
        }
        return true;
    }

}

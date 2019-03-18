package com.jee.learn.interfaces.gen.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * GenScheme<br/>
 * The persistent class for the gen_scheme database table.
 * 
 * @author ccp, gen
 * @version 2019-03-18 09:49:35
 */
@Data
@Entity
@Table(name = "gen_scheme")
@NamedQuery(name = "GenScheme.findAll", query = "SELECT t FROM GenScheme t")
@DynamicInsert
@DynamicUpdate
public class GenScheme implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "genSchemeGenerator", strategy = "uuid")
    @GeneratedValue(generator = "genSchemeGenerator")
    private String id; // 编号

    @Column(name = "name")
    private String name; // 名称

    @Column(name = "category")
    private String category; // 分类

    @Column(name = "package_name")
    private String packageName; // 生成包路径

    @Column(name = "module_name")
    private String moduleName; // 生成模块名

    @Column(name = "sub_module_name")
    private String subModuleName; // 生成子模块名

    @Column(name = "function_name")
    private String functionName; // 生成功能名

    @Column(name = "function_name_simple")
    private String functionNameSimple; // 生成功能名(简写)

    @Column(name = "function_author")
    private String functionAuthor; // 生成功能作者

    @Column(name = "gen_table_id")
    private String genTableId; // 生成表编号

    @Column(name = "is_export")
    private String isExport; // 是否导入导出

    @Column(name = "create_by")
    private String createBy; // 创建者

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate; // 创建时间

    @Column(name = "update_by")
    private String updateBy; // 更新者

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate; // 更新时间

    @Column(name = "remarks")
    private String remarks; // 备注

    @Column(name = "del_flag")
    private String delFlag; // 删除标记[0:未删除,1:已删除]

    public GenScheme() {
        super();
    }

    public GenScheme(String name, String category, String packageName, String moduleName, String functionName,
            String functionNameSimple, String functionAuthor, String genTableId) {
        super();
        this.name = name;
        this.category = category;
        this.packageName = packageName;
        this.moduleName = moduleName;
        this.functionName = functionName;
        this.functionNameSimple = functionNameSimple;
        this.functionAuthor = functionAuthor;
        this.genTableId = genTableId;
    }

}
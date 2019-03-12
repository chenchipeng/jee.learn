package com.jee.learn.interfaces.gen.dto;

import java.util.List;

/**
 * GenTableDto
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月7日 下午4:17:46 ccp 新建
 */
public class GenTableDto {

    private String id; // 编号
    private String name; // 名称
    private String comments; // 描述
    private String className; // 实体类名称
    private String parentTable; // 关联父表
    private String parentTableFk; // 关联父表外键
    private List<GenTableColumnDto> columns;// 所包含的列

    private String label;// 前端下拉显示内容

    private List<GenTableColumnDto> columnDtos;

    public GenTableDto() {
        super();
    }

    public GenTableDto(String name, String comments) {
        super();
        this.name = name;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getParentTable() {
        return parentTable;
    }

    public void setParentTable(String parentTable) {
        this.parentTable = parentTable;
    }

    public String getParentTableFk() {
        return parentTableFk;
    }

    public void setParentTableFk(String parentTableFk) {
        this.parentTableFk = parentTableFk;
    }

    public List<GenTableColumnDto> getColumnDtos() {
        return columnDtos;
    }

    public void setColumnDtos(List<GenTableColumnDto> columnDtos) {
        this.columnDtos = columnDtos;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<GenTableColumnDto> getColumns() {
        return columns;
    }

    public void setColumns(List<GenTableColumnDto> columns) {
        this.columns = columns;
    }

}

package com.jee.learn.manager.dto.table.sys;

import java.util.List;
import java.util.Map;

public class MenuVo {

    private Long total;
    private List<Map<String, Object>> rows;

    public MenuVo() {
        super();
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "MenuTabDto [total=" + total + ", rows=" + rows + "]";
    }

}

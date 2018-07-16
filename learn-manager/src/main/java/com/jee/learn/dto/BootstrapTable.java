package com.jee.learn.dto;

import java.util.List;

/**
 * bootstrap-table json格式
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年5月4日 下午7:29:38 1002360 新建
 * @param <T>
 */
public class BootstrapTable<T> {

    private long total;
    private List<T> rows;

    public BootstrapTable() {
        super();
    }

    public BootstrapTable(long total, List<T> rows) {
        super();
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

}

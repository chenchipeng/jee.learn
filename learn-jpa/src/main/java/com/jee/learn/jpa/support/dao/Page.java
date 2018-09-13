package com.jee.learn.jpa.support.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 结合Bootstrap Table简单封装分页对象
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月11日 下午5:03:25 ccp 新建
 */
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int pageSize = 10;
    protected int pageNum = 0;
    protected int total = 0;
    protected int offset = 0;
    protected List<T> rows;

    public Page() {
        super();
    }

    public Page(int pageSize) {
        super();
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

}

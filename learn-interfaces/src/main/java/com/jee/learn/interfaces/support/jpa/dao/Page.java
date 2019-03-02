package com.jee.learn.interfaces.support.jpa.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 简单封装分页对象
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月11日 下午5:03:25 ccp 新建
 */
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int pageSize = 10;
    protected int pageNum = 1;
    protected int totalPages = 1;
    protected long totalElements = 0;
    protected List<T> content;
    protected int offset = 0;

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

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Page [pageSize=").append(pageSize).append(", pageNum=").append(pageNum).append(", totalPages=")
                .append(totalPages).append(", totalElements=").append(totalElements).append(", content=")
                .append(content).append(", offset=").append(offset).append("]");
        return builder.toString();
    }

}

package com.jee.learn.mybatis.domain;

public class BaseEntity {

    protected boolean isNewRecord = false;

    protected boolean isNewRecord() {
        return isNewRecord;
    }

    protected void setNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

}

package com.jee.learn.interfaces.config.datasource;

public enum DsTypeEnum {
    MASTER("master"), SLAVE("slave");

    String value;

    DsTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}

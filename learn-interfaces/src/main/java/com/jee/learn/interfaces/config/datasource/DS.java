package com.jee.learn.interfaces.config.datasource;

public interface DS {

    /** 默认库 */
    String DEFAULT = "dataSource";
    
    /** 主库-可读可写 */
    String MASTER = "masterDS";
    
    /** 从库-只读 */
    String SLAVE = "slaveDS";

}

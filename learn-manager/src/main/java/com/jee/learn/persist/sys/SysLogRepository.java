package com.jee.learn.persist.sys;

import org.springframework.stereotype.Repository;

import com.jee.learn.common.persist.jpa.CustomJpaRepository;
import com.jee.learn.model.sys.SysLog;

@Repository
public interface SysLogRepository extends CustomJpaRepository<SysLog, String> {

}
package com.jee.learn.persist.sys;

import org.springframework.stereotype.Repository;

import com.jee.learn.common.persist.jpa.CustomJpaRepository;
import com.jee.learn.model.sys.SysDict;

@Repository
public interface SysDictRepository extends CustomJpaRepository<SysDict, String> {

}
package com.jee.learn.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jee.learn.jpa.domain.ApiUser;

public interface ApiUserRepository extends JpaSpecificationExecutor<ApiUser>, JpaRepository<ApiUser, String>,
        PagingAndSortingRepository<ApiUser, String> {

    ApiUser findOneById(String id);
    
    Page<ApiUser> findByDelFlag(String delFlag,Pageable pageable);

}

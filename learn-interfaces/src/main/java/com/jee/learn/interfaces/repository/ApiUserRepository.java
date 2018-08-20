package com.jee.learn.interfaces.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jee.learn.interfaces.config.datasource.dynamic.TargetDataSource;
import com.jee.learn.interfaces.domain.ApiUser;

public interface ApiUserRepository extends JpaSpecificationExecutor<ApiUser>, JpaRepository<ApiUser, String>,
        PagingAndSortingRepository<ApiUser, String> {

    @TargetDataSource
    ApiUser findOneById(String id);

    @Override
    <S extends ApiUser> S save(S entity);

}

package com.jee.learn.interfaces.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.jee.learn.interfaces.config.datasource.dynamic.TargetDataSource;
import com.jee.learn.interfaces.domain.ApiUser;

@Repository
public interface ApiUserRepository extends JpaSpecificationExecutor<ApiUser>, JpaRepository<ApiUser, String>,
        PagingAndSortingRepository<ApiUser, String> {

    @TargetDataSource
    ApiUser findOneById(String id);
    
    ApiUser getById(String id);

    @Override
    <S extends ApiUser> S save(S entity);

}

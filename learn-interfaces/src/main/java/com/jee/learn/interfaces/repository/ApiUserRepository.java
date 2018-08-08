package com.jee.learn.interfaces.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jee.learn.interfaces.config.datasource.DataSourceConfig;
import com.jee.learn.interfaces.config.datasource.dynamic.DataSourceTarget;
import com.jee.learn.interfaces.domain.ApiUser;

public interface ApiUserRepository extends JpaSpecificationExecutor<ApiUser>, JpaRepository<ApiUser, String>,
        PagingAndSortingRepository<ApiUser, String> {

    @DataSourceTarget(dataSource = DataSourceConfig.READ_DATASOURCE_KEY)
    ApiUser findOneById(String id);

    @Override
    @DataSourceTarget(dataSource = DataSourceConfig.WRITE_DATASOURCE_KEY)
    <S extends ApiUser> S save(S entity);

}
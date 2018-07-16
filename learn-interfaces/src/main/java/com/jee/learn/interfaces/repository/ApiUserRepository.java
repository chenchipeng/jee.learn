package com.jee.learn.interfaces.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jee.learn.interfaces.config.datasource.DataSourceConfig;
import com.jee.learn.interfaces.config.datasource.dynamic.TargetDataSource;
import com.jee.learn.interfaces.domain.ApiUser;

public interface ApiUserRepository extends JpaRepository<ApiUser, String> {

    @TargetDataSource(dataSource = DataSourceConfig.READ_DATASOURCE_KEY)
    ApiUser findOneById(String id);

    @Override
    @TargetDataSource(dataSource = DataSourceConfig.WRITE_DATASOURCE_KEY)
    <S extends ApiUser> S save(S entity);

}

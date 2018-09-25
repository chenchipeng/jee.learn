package com.jee.learn.thymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jee.learn.thymeleaf.domain.ApiUser;

public interface ApiUserRepository extends JpaRepository<ApiUser, String> {

}

package com.jee.learn.jpa.repository.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jee.learn.jpa.domain.ApiUser;
import com.jee.learn.jpa.repository.BaseRepository;

public interface ApiUserRepository extends BaseRepository<ApiUser, String> {

    Page<ApiUser> findByDelFlag(String delFlag, Pageable pageable);

}

package com.jee.learn.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jee.learn.jpa.domain.ApiUser;
import com.jee.learn.jpa.repository.ApiUserRepository;

@RestController
public class ApiUserController {

    @Autowired
    private ApiUserRepository apiUserRepository;

    @GetMapping("getApiUser")
    public ApiUser getApiUser() {
        return apiUserRepository.findOneById("1");
    }
}

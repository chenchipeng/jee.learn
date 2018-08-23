package com.jee.learn.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jee.learn.mongodb.domain.MgUser;

public interface MgUserRepository extends MongoRepository<MgUser, String> {
    
    MgUser findByName(String name);

}

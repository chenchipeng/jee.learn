package com.jee.learn.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.jee.learn.mongodb.domain.MgUser;

public interface MgUserRepository extends MongoRepository<MgUser, String> {

    MgUser findByName(String name);

    @Query("{ 'name' : ?0 }")
    MgUser queryByName(String name);

}

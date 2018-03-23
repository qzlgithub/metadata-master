package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.OverdueUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OverdueUserRepository extends MongoRepository<OverdueUser, String>
{
    OverdueUser findByPhone(String phone);
}

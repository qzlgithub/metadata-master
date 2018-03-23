package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.OverduePlatform;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OverduePlatformRepository extends MongoRepository<OverduePlatform, String>
{
    List<OverduePlatform> findByPhone(String phone);
}

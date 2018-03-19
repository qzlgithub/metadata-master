package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.OverduePlatForm;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OverduePlatFormRepository extends MongoRepository<OverduePlatForm, String>
{
    List<OverduePlatForm> findByPhone(String phone);
}

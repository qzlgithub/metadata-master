package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.Overdue;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OverdueRepository extends MongoRepository<Overdue, String>
{
    List<Overdue> findByPhone(String phone);
}

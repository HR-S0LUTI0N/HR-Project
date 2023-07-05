package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.Debt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IDebtRepository extends MongoRepository<Debt,String > {

    List<Debt> findAllByUserId(String userId);
}

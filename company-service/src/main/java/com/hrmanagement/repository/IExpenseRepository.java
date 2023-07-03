package com.hrmanagement.repository;


import com.hrmanagement.repository.entity.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IExpenseRepository extends MongoRepository<Expense,String> {
}

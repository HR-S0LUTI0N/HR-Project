package com.hrmanagement.repository;


import com.hrmanagement.repository.entity.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IExpenseRepository extends MongoRepository<Expense,String> {

    List<Expense> findAllByCompanyId(String companyId);
    Optional<Expense> findByCompanyIdAndExpenseId(String companyId, String expenseId);
}

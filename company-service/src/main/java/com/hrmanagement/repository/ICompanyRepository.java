package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICompanyRepository extends MongoRepository<Company,String> {
    Boolean existsByCompanyNameIgnoreCase(String companyName);
    Boolean existsByCompanyId(String companyId);

}

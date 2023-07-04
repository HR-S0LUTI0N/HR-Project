package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.DayOffPermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDayOffPermissionRepository extends MongoRepository<DayOffPermission,String> {
}

package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.DayOffPermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDayOffPermissionRepository extends MongoRepository<DayOffPermission,String> {
    List<DayOffPermission> findAllByStatusAndCompanyId(String status, String companyId);
    List<DayOffPermission> findByUserIdAndStatus(String userId,String status);

}

package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.AdvancePermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAdvancePermissionRepository extends MongoRepository<AdvancePermission, String> {
    Optional<AdvancePermission> findByUserId(String userId);

}

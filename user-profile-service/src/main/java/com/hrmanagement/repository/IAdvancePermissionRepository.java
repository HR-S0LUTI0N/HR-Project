package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.AdvancePermission;
import com.hrmanagement.repository.entity.enums.EAdvanceStatus;
import com.hrmanagement.repository.entity.enums.EStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAdvancePermissionRepository extends MongoRepository<AdvancePermission, String> {
    Optional<AdvancePermission> findByUserId(String userId);

    List<AdvancePermission> findAllByStatusAndCompanyId(EAdvanceStatus status, String companyId);
    Optional<AdvancePermission> findByAdvancedPermissionIdAndCompanyId(String advancePermissionId,String companyId);

}

package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserProfileRepository extends MongoRepository<UserProfile, String> {
    Optional<UserProfile> findByAuthId(Long authId);
    List<UserProfile> findByCompanyId(String companyId);
    Optional<UserProfile> findByEmail(String email);
}

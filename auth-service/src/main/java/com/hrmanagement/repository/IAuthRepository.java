package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IAuthRepository extends JpaRepository<Auth,Long> {
    Optional<Auth> findOptionalByEmail(String email);

    Optional<Auth> findOptionalByAuthId(Long authId);

}

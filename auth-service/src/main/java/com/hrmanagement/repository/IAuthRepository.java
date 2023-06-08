package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAuthRepository extends JpaRepository<Auth,Long> {
    Optional<Auth> findOptionalByEmail(String email);

    Optional<Auth> findOptionalByAuthId(Long authId);

}

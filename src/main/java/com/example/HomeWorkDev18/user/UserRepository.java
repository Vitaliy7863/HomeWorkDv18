package com.example.HomeWorkDev18.user;

import com.example.HomeWorkDev18.security.SecurityConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SecurityConfig.User, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM users u WHERE u.login = :login")
    Optional<SecurityConfig.User> findByLogin(@Param("login") String username);
}

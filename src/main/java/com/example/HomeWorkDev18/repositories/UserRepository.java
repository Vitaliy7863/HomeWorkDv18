package com.example.HomeWorkDev18.repositories;

import com.example.HomeWorkDev18.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM users u WHERE u.login = :login")
    Optional<User> findByLogin(@Param("login") String username);
}

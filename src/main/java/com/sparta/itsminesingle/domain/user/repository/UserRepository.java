package com.sparta.itsminesingle.domain.user.repository;

import com.sparta.itsminesingle.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


public interface UserRepository extends JpaRepository<User, Long> {
    //signup
    Optional<User> findByUsername(String username);
}

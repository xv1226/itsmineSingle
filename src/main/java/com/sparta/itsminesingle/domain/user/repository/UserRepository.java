package com.sparta.itsminesingle.domain.user.repository;

import com.sparta.itsminesingle.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    //signup
    Optional<User> findByUsername(String username);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByEmail(String kakaoEmail);

}

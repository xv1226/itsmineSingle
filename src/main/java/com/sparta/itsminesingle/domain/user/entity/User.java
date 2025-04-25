package com.sparta.itsminesingle.domain.user.entity;

import com.sparta.itsminesingle.domain.user.utils.UserRole;
import com.sparta.itsminesingle.global.TimeStamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user", indexes = {
        @Index(name = "idx_username", columnList = "username")
})
public class User extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(nullable = false)
    private String address;

    private Long kakaoId;


    public User(String username, String password, String name, String nickname, String email,
            UserRole userRole, String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.userRole = userRole;
        this.address = address;
    }

    @Builder
    public User(String username, String password, String name, String nickname, String email,
            UserRole userRole, String address, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.userRole = userRole;
        this.address = address;
        this.kakaoId = kakaoId;
    }

    public void kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
    }
}

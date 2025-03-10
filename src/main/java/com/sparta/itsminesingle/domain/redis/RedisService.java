package com.sparta.itsminesingle.domain.redis;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final Long refreshTokenExpiration = 14 * 24 * 60 * 60 * 1000L; // 14일
    private final Long kakaoTidExpiration = 3 * 60 * 1000L; // 3분

    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set(username, refreshToken, refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }

    public void saveKakaoTid(String username, String tid) {
        redisTemplate.opsForValue().set(username + ":tid", tid, kakaoTidExpiration, TimeUnit.MILLISECONDS);
    }

    public String getValue(String prefix, String key) {
        String redisKey = prefix + key;  // Prefix를 앞에 붙임
        return redisTemplate.opsForValue().get(redisKey);
    }

    public void insertMillionRecords(String username, String refreshToken) {
        IntStream.range(0, 1_000_000).parallel().forEach(i -> {
            String key = username + i;
            String value = refreshToken+i;
            saveRefreshToken(key, value);
        });
    }
}

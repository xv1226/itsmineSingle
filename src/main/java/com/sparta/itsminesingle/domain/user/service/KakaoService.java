package com.sparta.itsminesingle.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.itsminesingle.domain.redis.RedisService;
import com.sparta.itsminesingle.domain.user.dto.KakaoUserInfoDto;
import com.sparta.itsminesingle.domain.user.entity.User;
import com.sparta.itsminesingle.domain.user.repository.UserRepository;
import com.sparta.itsminesingle.domain.user.utils.UserRole;
import com.sparta.itsminesingle.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;

    @Value("${KAKAO_REST_KEY}")
    private String apiKey;

    public String AuthorizationCode(){

        return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+apiKey+"&redirect_uri=http://localhost:8080/api/user/kakao/callback";
    }

    public void kakaoLogin(String code, HttpServletResponse response) throws IOException {

        // 1. "인가 코드"로 "액세스 토큰" 요청
        String tokens = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(tokens);

        // 3. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. JWT 토큰 반환
        String accessToken= jwtUtil.createAccessToken(kakaoUser.getUsername(),kakaoUser.getUserRole());
        String refreshToken=jwtUtil.createRefreshToken(kakaoUser.getUsername(),kakaoUser.getUserRole());

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        // Redis에 RefreshToken 저장
        redisService.saveRefreshToken(kakaoUser.getUsername(),refreshToken);

        // JSON 형식으로 로그인 성공 메시지 작성
        String loginSuccessMessage = String.format("{\"message\": \"로그인 완료\", \"accessToken\": \"%s\"}", accessToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(loginSuccessMessage);

    }

    private String getToken(String code) throws JsonProcessingException {

        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", apiKey);
        body.add("redirect_uri", "http://localhost:8080/api/user/kakao/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {

        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();
        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {

        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);
        if (kakaoUser == null) {

            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;

                // 기존 회원정보에 카카오 Id 추가
                kakaoUser.kakaoIdUpdate(kakaoId);

            }
            else {

                // 신규 회원가입
                // password: random UUID
                // email: kakao email
                String kakaoUsername = kakaoUserInfo.getNickname();
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);
                String email = kakaoUserInfo.getEmail();
                kakaoUser = User.builder()
                        .username(kakaoUsername)
                        .password(encodedPassword)
                        .name(kakaoUsername)
                        .nickname(kakaoUsername)
                        .email(email)
                        .userRole(UserRole.USER)
                        .address("카카오")
                        .kakaoId(kakaoId)
                        .build();

            }
            userRepository.save(kakaoUser);
        }
        return kakaoUser;

    }


}

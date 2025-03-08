package com.sparta.itsminesingle.domain.user.controller;

import com.sparta.itsminesingle.domain.user.dto.LoginRequestDto;
import com.sparta.itsminesingle.domain.user.dto.SignupRequestDto;
import com.sparta.itsminesingle.domain.user.dto.UserResponseDto;
import com.sparta.itsminesingle.domain.user.entity.User;
import com.sparta.itsminesingle.domain.user.service.UserService;
import com.sparta.itsminesingle.global.security.UserDetailsImpl;
import com.sparta.itsminesingle.global.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/user/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @PostMapping("/list/signup")
    public ResponseEntity<String> userListSignUp(@RequestBody @Valid SignupRequestDto requestDto,@RequestParam int n){
        return userService.userListSignUp(requestDto,n);
    }

/*    @PostMapping("/user/login")
    public ResponseEntity<UserResponseDto> login(LoginRequestDto requestDto, HttpServletResponse res) {

        return userService.login(requestDto, res);
    }*/

    @PostMapping("/user/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 사용자의 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 쿠키 전체 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue(null);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        return ResponseEntity.ok("로그아웃 완료");
    }

    @PostMapping("/user/refresh")
    public ResponseEntity<String> refresh(@RequestHeader("RefreshToken") String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.getUserInfoFromToken(refreshToken).getSubject();
            return ResponseEntity.ok(jwtUtil.createAccessToken(username, userService.getUserByUsername(username).getUserRole()));
        } else {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<String> userDelete(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestParam String password) {

        User user = userDetails.getUser();

        return userService.deleteById(user.getId(),password);
    }
}

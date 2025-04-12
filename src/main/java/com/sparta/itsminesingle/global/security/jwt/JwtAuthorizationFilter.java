package com.sparta.itsminesingle.global.security.jwt;

import com.sparta.itsminesingle.domain.user.utils.UserRole;
import com.sparta.itsminesingle.global.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JwtAuthorizationFilter")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getTokenWithoutBearer(request.getHeader("Authorization"));
        String username = jwtUtil.getUsernameFromToken(accessToken);

        try {
            if ((StringUtils.hasText(accessToken))) {
                if (jwtUtil.validateToken(accessToken)) {//토큰 검증
                    setAuthentication(accessToken, request);//인증 처리
                    log.info("정상적인 엑세스 토큰");
                    if(jwtUtil.isTokenExpiringSoon(accessToken)){
                        UpdateAccessToken(request,response,accessToken);// 토큰 확인 후 재발급
                        log.info("만료 임박한 엑세스 토큰 재발급 완료");
                    }
                }
                else if (jwtUtil.hasRefreshToken(username)) {
                    String refreshToken = jwtUtil.substringToken(redisTemplate.opsForValue().get(username));//redis에서 리프래시 토큰 조회 후 "Bearer " 접두어를 제외한 하위 문자열 토큰
                    UpdateAccessToken(request,response,refreshToken);// 토큰 확인 후 재발급
                    log.info("리프래시 완료, 엑세스 토큰 재발급 완료");
                }
                else{
                    log.warn("No valid access or refresh token found for user: {}",username);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Authentication required");
                    return;
                }
            }
        } catch (Exception e) {
            log.error("Token Error: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or expired token");
            return;
        }
        filterChain.doFilter(request, response);
    }

    // 토큰 확인 후 재발급
    private void UpdateAccessToken(HttpServletRequest request, HttpServletResponse response,String Token){
        Claims claims = jwtUtil.getUserInfoFromToken(Token);
        String newAccessToken = jwtUtil.createAccessToken(claims.getSubject(), UserRole.valueOf(claims.get(JwtUtil.AUTHORIZATION_KEY).toString()));
        String newToken=jwtUtil.getTokenWithoutBearer(newAccessToken);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
        setAuthentication(newToken, request);
    }

    // 인증 처리
    private void setAuthentication(String token, HttpServletRequest request) {
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        String username = claims.getSubject();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}

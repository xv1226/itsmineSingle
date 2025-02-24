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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getTokenWithoutBearer(request.getHeader("Authorization"));
        String username = jwtUtil.getUsernameFromToken(accessToken);

        try {
            if ((StringUtils.hasText(accessToken))) {
                if (jwtUtil.validateToken(accessToken)) {
                    setAuthentication(accessToken, request);
                }
                else if (jwtUtil.hasRefreshToken(username)) {
                    String refreshToken = jwtUtil.substringToken(redisTemplate.opsForValue().get(username));
                    Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);
                    String newAccessToken = jwtUtil.getTokenWithoutBearer(jwtUtil.createAccessToken(claims.getSubject(), UserRole.valueOf(claims.get(JwtUtil.AUTHORIZATION_KEY).toString())));
                    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
                    setAuthentication(newAccessToken, request);
                }
            }

        } catch (Exception e) {
            log.error("Token Error: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
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

package com.sparta.itsminesingle.global.security.jwt;

import com.sparta.itsminesingle.domain.user.utils.UserRole;
import com.sparta.itsminesingle.global.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JwtAuthorizationFilter")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getTokenWithoutBearer(request.getHeader("Authorization"));
        String refreshToken = jwtUtil.getTokenWithoutBearer(request.getHeader("RefreshToken"));

        try {
            if (StringUtils.hasText(accessToken) && jwtUtil.validateToken(accessToken)) {
                setAuthentication(accessToken, request);
            } else if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)) {
                Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);
                String newAccessToken = jwtUtil.createAccessToken(claims.getSubject(), UserRole.valueOf(claims.get(JwtUtil.AUTHORIZATION_KEY).toString()));
                response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
                setAuthentication(newAccessToken, request);
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
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}

package com.sparta.itsminesingle.domain.user.service;

import com.sparta.itsminesingle.domain.user.dto.LoginRequestDto;
import com.sparta.itsminesingle.domain.user.dto.SignupRequestDto;
import com.sparta.itsminesingle.domain.user.dto.UserResponseDto;
import com.sparta.itsminesingle.domain.user.entity.User;
import com.sparta.itsminesingle.domain.user.repository.UserBatchRepository;
import com.sparta.itsminesingle.domain.user.repository.UserRepository;
import com.sparta.itsminesingle.domain.user.utils.UserRole;
import com.sparta.itsminesingle.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserBatchRepository userBatchRepository;
    private final RedisTemplate<String,String> redisTemplate;

    public ResponseEntity<UserResponseDto> signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String name = requestDto.getName();
        String nickname = requestDto.getNickname();
        String email = requestDto.getEmail();
        UserRole userRole = UserRole.USER;
        String address = requestDto.getAddress();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 등록
        User user = new User(username, password, name, nickname, email, userRole, address,
                null);
        userRepository.save(user);

        return ResponseEntity.ok(new UserResponseDto(user));
    }

    @Transactional
    public ResponseEntity<String> userListSignUp(SignupRequestDto requestDto,int n) {

        List<User> userList = requestDto.userListRequest(requestDto, n);

        if (userList.isEmpty()) {
            return ResponseEntity.badRequest().body("유저리스트 비어 있음");
        }

        userBatchRepository.insert(userList);

        return ResponseEntity.ok("유저리스트 회원가입 완료");
    }

    public ResponseEntity<String> tokenSelect(String username){
        User user=userRepository.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(user.getRefreshToken());
    }

    public ResponseEntity<String> tokenRedisGet(String username){
        String refreshToken=redisTemplate.opsForValue().get(username);
        return ResponseEntity.ok(refreshToken);
    }

    @Transactional
    public ResponseEntity<String> deleteById(Long id, String password) {
        User user = userRepository.findById(id).orElseThrow();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok("회원탈퇴 완료");
    }

    public ResponseEntity<UserResponseDto> login(LoginRequestDto requestDto,
            HttpServletResponse res) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getUserRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername(), user.getUserRole());

        jwtUtil.addJwtToCookie(accessToken, refreshToken, res);

        return ResponseEntity.ok(new UserResponseDto(user));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));
    }
}
package com.sparta.itsminesingle.domain.user.dto;


import com.sparta.itsminesingle.domain.user.entity.User;
import com.sparta.itsminesingle.domain.user.utils.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @NotBlank(message = "Required Username")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{8,20}$", message = "사용자 ID는 최소 8글자 이상, 최대 20글자 이하여야 합니다.")
    private String username;  // id for log in
    @NotBlank(message = "Required Password")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()]).{10,}$", message = "대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함합니다. \n비밀번호는 최소 10글자 이상이어야 합니다.")
    private String password;
    @NotBlank(message = "Required Name")
    @Pattern(regexp = "^[가-힣]{1,4}$", message = "이름은 한글로만 최대 4글자까지 입력할 수 있습니다.")
    private String name;
    @NotBlank(message = "Required Nickname")
    private String nickname;
    @Email
    private String email;
    private boolean isAdmin;
    private String adminToken;
    private String address;

    public List<User> userListRequest(SignupRequestDto requestDto, int n) {
        List<User> userList = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            String username = requestDto.getUsername() + i;
            String password = requestDto.getPassword();
            String name = requestDto.getName();
            String nickname = requestDto.getNickname() + i;
            String email = requestDto.getEmail() + i;
            UserRole userRole = UserRole.USER;
            String address = requestDto.getAddress();

            User user = new User(username, password, name, nickname, email, userRole, address, null);

            userList.add(user);
        }
        return userList;
    }

}

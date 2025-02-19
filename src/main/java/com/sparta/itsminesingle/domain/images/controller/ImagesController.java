package com.sparta.itsminesingle.domain.images.controller;

import static com.sparta.itsminesingle.global.response.ResponseExceptionEnum.USER_NOT_FOUND;

import com.sparta.itsminesingle.domain.images.dto.ProductImagesResponseDto;
import com.sparta.itsminesingle.domain.images.dto.ProfileImagesResponseDto;
import com.sparta.itsminesingle.domain.images.service.ImagesService;
import com.sparta.itsminesingle.domain.user.repository.UserRepository;
import com.sparta.itsminesingle.global.exception.DataNotFoundException;
import com.sparta.itsminesingle.global.security.UserDetailsImpl;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/s3")
public class ImagesController {

    private final ImagesService imagesService;
    private final UserRepository userRepository;

    // 파일 업로드 엔드포인트
    @PostMapping("/upload")
    public ResponseEntity<ProductImagesResponseDto> uploadFile(
            @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = imagesService.saveFile(file);
            return ResponseEntity.ok(
                    new ProductImagesResponseDto(Collections.singletonList(fileUrl)));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ProductImagesResponseDto(
                    Collections.singletonList("파일 업로드 중 오류가 발생했습니다.")));
        }
    }

    //프로필 업로드 엔드포인트
    @PostMapping("/upload/profile")
    public ResponseEntity<ProfileImagesResponseDto> uploadProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("file") MultipartFile file) {
        try {
            userRepository.findById(userDetails.getUser().getId()).orElseThrow(() -> new DataNotFoundException(USER_NOT_FOUND));
            ProfileImagesResponseDto response = imagesService.uploadProfile(file, userDetails);
            return ResponseEntity.ok(response); // 프로필 업로드 완료 메시지 반환
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ProfileImagesResponseDto("프로필 업로드 중 오류가 발생했습니다."));
        }
    }

    // 파일 삭제 엔드포인트
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        //fileUrl엔 객체Url이 들어가야함
        try {
            imagesService.deleteFile(fileUrl);
            return ResponseEntity.ok("파일 삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("파일 삭제 중 오류가 발생했습니다.");
        }
    }
}

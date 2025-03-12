package com.sparta.itsminesingle.domain.kakaopay.controller;

import static com.sparta.itsminesingle.global.response.ResponseCodeEnum.AUCTION_BID_CANCEL;
import static com.sparta.itsminesingle.global.response.ResponseCodeEnum.KAKAOPAY_APPROVE;
import static com.sparta.itsminesingle.global.response.ResponseCodeEnum.KAKAOPAY_READY;
import static com.sparta.itsminesingle.global.response.ResponseCodeEnum.KAKAOPAY_REFUND;
import static com.sparta.itsminesingle.global.response.ResponseCodeEnum.KAKAOPAY_TID_SUCCESS_GET;

import com.sparta.itsminesingle.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsminesingle.domain.kakaopay.dto.KakaoPayApproveResponseDto;
import com.sparta.itsminesingle.domain.kakaopay.dto.KakaoPayCancelResponseDto;
import com.sparta.itsminesingle.domain.kakaopay.dto.KakaoPayGetTidRequestDto;
import com.sparta.itsminesingle.domain.kakaopay.dto.KakaoPayGetTidResponseDto;
import com.sparta.itsminesingle.domain.kakaopay.dto.KakaoPayReadyResponseDto;
import com.sparta.itsminesingle.domain.kakaopay.service.KakaoPayService;
import com.sparta.itsminesingle.global.response.HttpResponseDto;
import com.sparta.itsminesingle.global.response.ResponseUtils;
import com.sparta.itsminesingle.global.security.UserDetailsImpl;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/kakaopay")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/ready/{productId}")//결제 요청
    public ResponseEntity<HttpResponseDto> ready(@PathVariable("productId") Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody AuctionRequestDto requestDto) {
        KakaoPayReadyResponseDto kakaoPayReadyResponseDto = kakaoPayService.ready(productId,
                userDetails.getUser(),
                requestDto);
        return ResponseUtils.of(KAKAOPAY_READY, kakaoPayReadyResponseDto);
    }

    @GetMapping("/approve/{agent}/{openType}/{productId}/{userId}/{auctionId}")//결제 승인,옥션 결제상태 확인
    public ResponseEntity<HttpResponseDto> approve(@PathVariable("agent") String agent,
            @PathVariable("openType") String openType, @RequestParam("pg_token") String pgToken,
            @PathVariable("productId") Long productId, @PathVariable("userId") Long userId,
            @PathVariable("auctionId") Long auctionId) {
        kakaoPayService.approve(pgToken, productId, userId, auctionId);
        /*String redirectUrl = "https://itsyours.store/itsmine?status=success";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));

        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);*/
        KakaoPayApproveResponseDto kakaoPayApproveResponseDto = kakaoPayService.approve(pgToken,
                productId, userId, auctionId);
        return ResponseUtils.of(KAKAOPAY_APPROVE, kakaoPayApproveResponseDto);
    }

    //결제 도중 취소하면 결제 중단
    @GetMapping("/cancel/{agent}/{openType}")//agent는 매체를 명시, openType은 리다이렉트 방법을 명시
    public ResponseEntity<Void> cancel(@PathVariable("agent") String agent,
            @PathVariable("openType") String openType) {
        // 주문건이 진짜 취소되었는지 확인 후 취소 처리
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is QUIT_PAYMENT before suspending the payment
        String redirectUrl = "https://itsyours.store/itsmine?status=cancel";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));

        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    //결제 실패(도중에 알 수 없는 이유로 연결이 끊겨서 안된다거나)
    @GetMapping("/fail/{agent}/{openType}")//결제 실패
    public ResponseEntity<Void> fail(@PathVariable("agent") String agent,
            @PathVariable("openType") String openType) {
        // 주문건이 진짜 실패되었는지 확인 후 실패 처리
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is FAIL_PAYMENT before suspending the payment
        String redirectUrl = "https://itsyours.store/itsmine?status=fail";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));

        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    //결제 취소 및 환불
    @PostMapping("/refund")//tid를 RequestParam으로 받는다, refund는 걍 지은 이름
    public ResponseEntity<HttpResponseDto> refund(@RequestParam("tid") String tid) {

        KakaoPayCancelResponseDto kakaoPayCancelResponseDto = kakaoPayService.kakaoCancel(tid);

        return ResponseUtils.of(KAKAOPAY_REFUND, kakaoPayCancelResponseDto);
    }

    //환불 없는 입찰 취소
    @PostMapping("/bidCancel")
    public ResponseEntity<HttpResponseDto> bidCancel(@RequestParam("tid") String tid) {

        kakaoPayService.bidCancel(tid);

        return ResponseUtils.of(AUCTION_BID_CANCEL);
    }

    //tid 검색
    @PostMapping("/tid")
    public ResponseEntity<HttpResponseDto> getTid(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody
    KakaoPayGetTidRequestDto kakaoPayGetTidRequestDto) {
        KakaoPayGetTidResponseDto kakaoPayGetTidResponseDto = kakaoPayService.getTid(
                kakaoPayGetTidRequestDto, userDetails.getUser());
        return ResponseUtils.of(KAKAOPAY_TID_SUCCESS_GET, kakaoPayGetTidResponseDto);
    }

    @PostMapping("/additional/{productId}")//추가 결제 요청
    public ResponseEntity<HttpResponseDto> additionalReady(
            @PathVariable("productId") Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (kakaoPayService.FindTidByProductId(productId) != null) {
            kakaoPayService.kakaoCancel(kakaoPayService.FindTidByProductId(productId));
        }
        KakaoPayReadyResponseDto kakaoPayReadyResponseDto = kakaoPayService.additionalReady(
                productId, userDetails.getUser());
        return ResponseUtils.of(KAKAOPAY_READY, kakaoPayReadyResponseDto);
    }
}

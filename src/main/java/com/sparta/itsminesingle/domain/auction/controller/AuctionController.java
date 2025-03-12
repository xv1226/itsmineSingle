package com.sparta.itsminesingle.domain.auction.controller;

import static com.sparta.itsminesingle.global.response.ResponseCodeEnum.AUCTION_SUCCESS_GET;

import com.sparta.itsminesingle.domain.auction.dto.AuctionProductImageResponseDto;
import com.sparta.itsminesingle.domain.auction.service.AuctionService;
import com.sparta.itsminesingle.global.response.HttpResponseDto;
import com.sparta.itsminesingle.global.response.ResponseUtils;
import com.sparta.itsminesingle.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    //유저(구매자(본인)) 입찰 조회(QueryDSL)
    @GetMapping("/auctions")
    public ResponseEntity<HttpResponseDto> getAuctionByUserToPage(
            @AuthenticationPrincipal UserDetailsImpl userDetails, Pageable pageable) {
        Page<AuctionProductImageResponseDto> responseDto = auctionService.getAuctionByUser(
                userDetails.getUser(), pageable);
        return ResponseUtils.of(AUCTION_SUCCESS_GET, responseDto);
    }

}

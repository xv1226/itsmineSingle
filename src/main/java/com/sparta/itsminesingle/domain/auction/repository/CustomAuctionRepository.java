package com.sparta.itsminesingle.domain.auction.repository;

import com.sparta.itsminesingle.domain.auction.dto.AuctionProductImageResponseDto;
import com.sparta.itsminesingle.domain.auction.entity.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomAuctionRepository {

    //자신이 고른 상품 전체 조회
    Page<AuctionProductImageResponseDto> findAuctionAllByUserid(Long userId, Pageable pageable);

    Auction findByProductIdAndMaxBid(Long productId);

    Auction findByBidPriceAndUserAndProduct(Long userId,Long productId,Integer bidPrice);

    Auction findByProductIdForAdditionalPayment(Long productId);
}


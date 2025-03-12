package com.sparta.itsminesingle.domain.auction.repository;

import static com.sparta.itsminesingle.global.response.ResponseExceptionEnum.AUCTION_NOT_FOUND;

import com.sparta.itsminesingle.domain.auction.dto.AuctionProductImageResponseDto;
import com.sparta.itsminesingle.domain.auction.entity.Auction;
import com.sparta.itsminesingle.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionAdapter {

    private final AuctionRepository auctionRepository;

    public Page<AuctionProductImageResponseDto> findAuctionAllByUserid(Long userId, Pageable pageable) {

        return auctionRepository.findAuctionAllByUserid(userId, pageable);
    }

    public Auction getAuction(Long id){
        return auctionRepository.findById(id).orElseThrow(()->new DataNotFoundException(AUCTION_NOT_FOUND));
    }

}

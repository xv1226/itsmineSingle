package com.sparta.itsminesingle.domain.auction.dto;

import com.sparta.itsminesingle.domain.product.entity.Product;
import java.util.List;
import lombok.Getter;

@Getter
public class AuctionProductImageResponseDto extends AuctionProductResponseDto {

    private final List<String> imagesUrl;

    public AuctionProductImageResponseDto(AuctionProductResponseDto auctionProductResponseDto,
            Product product) {
        super(auctionProductResponseDto.getProductId(),auctionProductResponseDto.getUsername(), auctionProductResponseDto.getProductName(),
                auctionProductResponseDto.getBidPrice(), auctionProductResponseDto.getStatus());
        this.imagesUrl = product.getImageUrls();
    }
}

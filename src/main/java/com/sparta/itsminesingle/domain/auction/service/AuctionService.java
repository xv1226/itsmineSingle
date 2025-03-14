package com.sparta.itsminesingle.domain.auction.service;

import static com.sparta.itsminesingle.domain.product.utils.ProductStatus.NEED_PAYMENT;

import com.sparta.itsminesingle.domain.auction.dto.AuctionProductImageResponseDto;
import com.sparta.itsminesingle.domain.auction.dto.AuctionRequestDto;
import com.sparta.itsminesingle.domain.auction.dto.AuctionRequestForSuccessBidDto;
import com.sparta.itsminesingle.domain.auction.dto.AuctionResponseDto;
import com.sparta.itsminesingle.domain.auction.entity.Auction;
import com.sparta.itsminesingle.domain.auction.repository.AuctionAdapter;
import com.sparta.itsminesingle.domain.auction.repository.AuctionRepository;
import com.sparta.itsminesingle.domain.product.entity.Product;
import com.sparta.itsminesingle.domain.product.repository.ProductAdapter;
import com.sparta.itsminesingle.domain.product.repository.ProductRepository;
import com.sparta.itsminesingle.domain.product.utils.ProductStatus;
import com.sparta.itsminesingle.domain.user.entity.User;
import com.sparta.itsminesingle.global.Redisson.Lock.DistributedLock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final AuctionAdapter adapter;
    private final ProductAdapter productAdapter;

    private final String LOCK_KEY_PREFIX = "auctionLock";

    @DistributedLock(prefix = LOCK_KEY_PREFIX, key = "productId")
    public AuctionResponseDto createAuction(User user, Long productId,
            AuctionRequestDto requestDto, Integer totalAmount) {
        Product product = productAdapter.getProduct(productId);
        Integer bidPrice = requestDto.getBidPrice();
        ProductStatus status = NEED_PAYMENT;

        Auction auction = createAuctionEntity(user, product, bidPrice, status, totalAmount);

        checkAuctionValidity(auction, product, bidPrice, user);
        auctionRepository.save(auction);
        return new AuctionResponseDto(auction);
    }

    @DistributedLock(prefix = LOCK_KEY_PREFIX, key = "productId")
    public AuctionResponseDto createAuctionForSuccessBid(User user, Long productId,
            Integer totalAmount) {
        Product product = productAdapter.getProduct(productId);

        AuctionRequestForSuccessBidDto auctionRequestDto = AuctionRequestForSuccessBidDto.builder()
                .bidPrice(totalAmount)
                .build();

        ProductStatus status = ProductStatus.NEED_PAYMENT_FOR_SUCCESS_BID;

        Auction auction = createAuctionEntity(user, product, auctionRequestDto.getBidPrice(), status, totalAmount);

        auction.checkUser(user, product);

        auctionRepository.save(auction);
        return new AuctionResponseDto(auction);
    }

    private Auction createAuctionEntity(User user, Product product, Integer bidPrice,
            ProductStatus status, Integer totalAmount) {
        return new Auction(user, product, bidPrice, status, totalAmount);
    }

    private void checkAuctionValidity(Auction auction, Product product, Integer bidPrice,
            User user) {
        auction.checkUser(user, product);
        auction.checkStatus(product.getStatus());
        auction.checkBidPrice(bidPrice, product);
        auction.checkCurrentPrice(bidPrice, product.getCurrentPrice());
    }

    private long calculateDelay(LocalDateTime dueDate) {
        return dueDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                - System.currentTimeMillis();
    }

    public void currentPriceUpdate(Integer bidPrice, Product product) {
        product.currentPriceUpdate(bidPrice);
        productRepository.save(product);
    }

    public Page<AuctionProductImageResponseDto> getAuctionByUser(User user, Pageable pageable) {
        return adapter.findAuctionAllByUserid(user.getId(), pageable);
    }

    public void deleteAllNeedPay(List<Auction> auctions) {
        //뭐가 됐든 DueDate가 다 됐으면 NEED_PAYMENT 상태의 입찰은 다 제거해줘야함
        for (Auction auction : auctions) {//얘부터 옮기고
            if (auction.getStatus().equals(NEED_PAYMENT)) {
                auctionRepository.delete(auction);
            }
        }
    }

}

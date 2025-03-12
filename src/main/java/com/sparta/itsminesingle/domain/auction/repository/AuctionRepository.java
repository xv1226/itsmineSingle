package com.sparta.itsminesingle.domain.auction.repository;

import com.sparta.itsminesingle.domain.auction.entity.Auction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long>, CustomAuctionRepository {

    void deleteAllByProductId(Long productId);

    List<Auction> findAllByProductId(Long productId);

}


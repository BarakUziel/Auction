package com.auction.server.converters;

import com.auction.server.dtos.BidDto;
import com.auction.server.entities.Bid;

import java.time.LocalDateTime;

public class BidConverter {

    public static Bid toEntity(BidDto dto) {
        Bid bid = new Bid();
        bid.setItemId(dto.getItemId());
        bid.setBidderId(dto.getBidderId());
        bid.setAmount(dto.getAmount());
        bid.setTimestamp(LocalDateTime.now());
        return bid;
    }
}

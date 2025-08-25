package com.auction.server.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BidDto {
    private Integer itemId;
    private Integer bidderId;
    private BigDecimal amount;
}

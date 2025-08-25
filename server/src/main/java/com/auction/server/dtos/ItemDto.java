package com.auction.server.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ItemDto {
    private Integer sellerId;
    private Integer categoryId;
    private String name;
    private String description;
    private BigDecimal startPrice;
    private Integer auctionDays;
    private List<ImageDto> images;
    private Integer winnerId;
    private BigDecimal finalPrice;
}

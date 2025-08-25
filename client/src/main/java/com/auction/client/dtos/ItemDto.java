package com.auction.client.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private double currentPrice;
    private String endDate;
    private Integer sellerId;
    private Integer categoryId;
    private Integer startPrice;
    private String startDate;
    private Integer winnerId;
    private Integer finalPrice;
    private Integer bidCount;
    private List<ImageDto> images;
}

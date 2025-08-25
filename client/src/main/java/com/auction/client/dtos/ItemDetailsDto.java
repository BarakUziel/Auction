package com.auction.client.dtos;

import lombok.Data;

@Data
public class ItemDetailsDto {
    private String name;
    private String description;
    private String startPrice;
    private String currentPrice;
    private String startDate;
    private String endDate;
    private String imagePath;
}

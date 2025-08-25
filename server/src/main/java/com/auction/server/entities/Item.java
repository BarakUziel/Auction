package com.auction.server.entities;

import com.auction.server.dtos.ImageDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Item {
    private Integer id;
    private Integer sellerId;
    private Integer categoryId;
    private String name;
    private String description;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer winnerId;
    private BigDecimal finalPrice;
    private Integer bidCount;
    private List<ImageDto> images;
}

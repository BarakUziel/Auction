package com.auction.server.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Bid {
    private Integer id;
    private Integer itemId;
    private Integer bidderId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}

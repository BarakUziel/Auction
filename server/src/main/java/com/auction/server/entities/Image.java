package com.auction.server.entities;

import lombok.Data;

@Data
public class Image {
    private Integer id;
    private Integer itemId;
    private String path;
}

package com.auction.server.dtos;

import lombok.Data;

@Data
public class ImageDto {
    private String path;

    public ImageDto(String path) {
        this.path = path;
    }
}

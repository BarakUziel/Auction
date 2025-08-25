package com.auction.client.dtos;

import lombok.Data;

@Data
public class ImageDto {
    private String path;

    public ImageDto() {
    }

    public ImageDto(String path) {
        this.path = path;
    }
}

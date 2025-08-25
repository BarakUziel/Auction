package com.auction.server.converters;

import com.auction.server.dtos.ItemDto;
import com.auction.server.entities.Item;

import java.time.LocalDateTime;

public class ItemConverter {

    public static Item toEntity(ItemDto dto) {
        Item item = new Item();
        item.setSellerId(dto.getSellerId());
        item.setCategoryId(dto.getCategoryId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setStartPrice(dto.getStartPrice());
        item.setCurrentPrice(dto.getStartPrice());
        item.setStartDate(LocalDateTime.now());
        item.setEndDate(LocalDateTime.now().plusDays(dto.getAuctionDays()));
        item.setFinalPrice(dto.getFinalPrice());
        item.setWinnerId(dto.getWinnerId());
        return item;
    }

    public static ItemDto toDto(Item entity) {
        ItemDto dto = new ItemDto();
        dto.setSellerId(entity.getSellerId());
        dto.setCategoryId(entity.getCategoryId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStartPrice(entity.getStartPrice());
        dto.setAuctionDays((int) java.time.Duration.between(entity.getStartDate(), entity.getEndDate()).toDays());
        dto.setFinalPrice(entity.getFinalPrice());
        dto.setWinnerId(entity.getWinnerId());
        return dto;
    }
}

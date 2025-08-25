package com.auction.server.controllers;

import com.auction.server.dtos.ImageDto;
import com.auction.server.dtos.ItemDto;
import com.auction.server.entities.Item;
import com.auction.server.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public ResponseEntity<List<Item>> getByCategory(@RequestParam Integer categoryId) {
        return ResponseEntity.ok(itemService.getItemsByCategory(categoryId));
    }

    @GetMapping("/sold")
    public ResponseEntity<List<Item>> getSoldItems(@RequestParam Integer sellerId) {
        return ResponseEntity.ok(itemService.getSoldItems(sellerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getById(@PathVariable Integer id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ItemDto dto) {
        itemService.createItem(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-with-images")
    public ResponseEntity<Void> createWithImages(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam double startPrice,
            @RequestParam int auctionDays,
            @RequestParam int categoryId,
            @RequestParam int sellerId,
            @RequestPart(required = false) List<MultipartFile> images
    ) {
        itemService.createItemWithImages(name, description, startPrice, auctionDays, categoryId, sellerId, images);
        return ResponseEntity.ok().build();
    }
}

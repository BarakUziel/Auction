package com.auction.server.controllers;

import com.auction.server.dtos.BidDto;
import com.auction.server.entities.Bid;
import com.auction.server.services.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    @GetMapping
    public ResponseEntity<List<Bid>> getBidsForItem(@RequestParam Integer itemId) {
        return ResponseEntity.ok(bidService.getBidsForItem(itemId));
    }

    @PostMapping
    public ResponseEntity<String> placeBid(@RequestBody BidDto dto) {
        try {
            bidService.placeBid(dto);
            return ResponseEntity.ok("Bid placed successfully.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}

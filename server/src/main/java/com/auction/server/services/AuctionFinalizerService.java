package com.auction.server.services;

import com.auction.server.entities.Bid;
import com.auction.server.entities.Item;
import com.auction.server.repositories.BidRepository;
import com.auction.server.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionFinalizerService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BidRepository bidRepository;

    @Scheduled(fixedRate = 60000)
    public void finalizeExpiredAuctions() {
        List<Item> expiredItems = itemRepository.findExpiredItemsWithoutWinner(LocalDateTime.now());

        for (Item item : expiredItems) {
            Optional<Bid> highestBid = bidRepository.findHighestBid(item.getId());

            if (highestBid.isPresent()) {
                Bid bid = highestBid.get();
                itemRepository.markItemAsSold(item.getId(), bid.getBidderId(), bid.getAmount());
                System.out.printf("Item %d sold to user %d for %.2f%n",
                        item.getId(), bid.getBidderId(), bid.getAmount());
            } else {
                System.out.printf("Item %d expired with no bids%n", item.getId());
            }
        }
    }
}



package com.auction.server.services;

import com.auction.server.converters.BidConverter;
import com.auction.server.dtos.BidDto;
import com.auction.server.entities.Bid;
import com.auction.server.entities.Item;
import com.auction.server.repositories.BidRepository;
import com.auction.server.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ItemRepository itemRepository;

    public List<Bid> getBidsForItem(Integer itemId) {
        return bidRepository.findByItem(itemId);
    }

    @Transactional
    public void placeBid(BidDto dto) {
        Bid bid = BidConverter.toEntity(dto);

        if (bid.getBidderId() == null) {
            throw new IllegalArgumentException("Bidder ID must not be null");
        }

        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (item.getEndDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Bidding is closed for this item.");
        }

        BigDecimal currentPrice = item.getCurrentPrice();
        BigDecimal minIncrement = getMinIncrement(currentPrice);
        Optional<BigDecimal> maybeHighestBid = bidRepository.findHighestBidForItem(dto.getItemId());

        if (maybeHighestBid.isPresent()) {
            BigDecimal highestBid = maybeHighestBid.get();
            if (bid.getAmount().compareTo(highestBid.add(minIncrement)) < 0) {
                throw new IllegalArgumentException("Bid must be at least " + minIncrement + " higher than current highest bid (" + highestBid + ")");
            }
        } else {
            if (bid.getAmount().compareTo(currentPrice) < 0) {
                throw new IllegalArgumentException("First bid must be at least equal to the starting price (" + currentPrice + ")");
            }
        }

        itemRepository.updateCurrentPrice(item.getId(), bid.getAmount());
        bidRepository.save(bid);
    }


    private BigDecimal getMinIncrement(BigDecimal price) {
        if (price.compareTo(BigDecimal.valueOf(100)) <= 0) {
            return BigDecimal.valueOf(5);
        } else if (price.compareTo(BigDecimal.valueOf(1000)) <= 0) {
            return BigDecimal.valueOf(10);
        } else {
            return BigDecimal.valueOf(50);
        }
    }
}

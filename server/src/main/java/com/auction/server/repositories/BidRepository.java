package com.auction.server.repositories;

import com.auction.server.entities.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class BidRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Bid> findByItem(Integer itemId) {
        String sql = "SELECT * FROM bids WHERE item_id = ? ORDER BY amount DESC";
        return jdbcTemplate.query(sql, this::mapRowToBid, itemId);
    }

    public Optional<BigDecimal> findHighestBidForItem(Integer itemId) {
        String sql = "SELECT MAX(amount) FROM bids WHERE item_id = ?";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class, itemId);
        return Optional.ofNullable(result);
    }

    public void save(Bid bid) {
        String sql = "INSERT INTO bids (item_id, bidder_id, amount, timestamp) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, bid.getItemId(), bid.getBidderId(), bid.getAmount(), bid.getTimestamp());
    }

    private Bid mapRowToBid(ResultSet rs, int rowNum) throws SQLException {
        Bid bid = new Bid();
        bid.setId(rs.getInt("id"));
        bid.setItemId(rs.getInt("item_id"));
        bid.setBidderId(rs.getInt("bidder_id"));
        bid.setAmount(rs.getBigDecimal("amount"));
        bid.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        return bid;
    }

    public Optional<Bid> findHighestBid(Integer itemId) {
        String sql = """
        SELECT * FROM bids 
        WHERE item_id = ? 
        ORDER BY amount DESC 
        LIMIT 1
    """;
        List<Bid> bids = jdbcTemplate.query(sql, this::mapRowToBid, itemId);
        return bids.isEmpty() ? Optional.empty() : Optional.of(bids.get(0));
    }
}

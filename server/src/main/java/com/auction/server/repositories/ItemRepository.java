package com.auction.server.repositories;

import com.auction.server.dtos.ImageDto;
import com.auction.server.entities.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Item> findByCategory(Integer categoryId) {
        String itemSql = """
        SELECT i.*, COUNT(b.id) AS bid_count
        FROM items i
        LEFT JOIN bids b ON i.id = b.item_id
        WHERE i.category_id = ? AND i.winner_id IS NULL AND i.end_date > NOW()
        GROUP BY i.id
    """;

        List<Item> items = jdbcTemplate.query(itemSql, (rs, rowNum) -> {
            Item item = mapRowToItem(rs, rowNum);
            item.setBidCount(rs.getInt("bid_count"));
            return item;
        }, categoryId);

        if (!items.isEmpty()) {
            List<Integer> itemIds = items.stream().map(Item::getId).toList();

            String placeholders = String.join(",", Collections.nCopies(itemIds.size(), "?"));
            String imageSql = "SELECT item_id, path FROM images WHERE item_id IN (" + placeholders + ")";

            Map<Integer, List<String>> imagesMap = new HashMap<>();

            jdbcTemplate.query(imageSql, itemIds.toArray(), rs -> {
                int itemId = rs.getInt("item_id");
                String path = rs.getString("path");
                imagesMap.computeIfAbsent(itemId, k -> new ArrayList<>()).add(path);
            });

            for (Item item : items) {
                List<String> paths = imagesMap.get(item.getId());
                if (paths != null) {
                    List<ImageDto> imageDtos = paths.stream()
                            .map(ImageDto::new)
                            .collect(Collectors.toList());
                    item.setImages(imageDtos);
                }
            }


        }

        return items;
    }


    public List<Item> findSoldItems(Integer sellerId) {
        String sql = "SELECT * FROM items WHERE winner_id IS NOT NULL AND seller_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToItem, sellerId);
    }

    public Optional<Item> findById(Integer id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        List<Item> items = jdbcTemplate.query(sql, this::mapRowToItem, id);
        return items.stream().findFirst();
    }

    public Integer saveAndReturnId(Item item) {
        String sql = "INSERT INTO items (seller_id, category_id, name, description, start_price, current_price, start_date, end_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, item.getSellerId());
            ps.setInt(2, item.getCategoryId());
            ps.setString(3, item.getName());
            ps.setString(4, item.getDescription());
            ps.setBigDecimal(5, item.getStartPrice());
            ps.setBigDecimal(6, item.getCurrentPrice());
            ps.setTimestamp(7, Timestamp.valueOf(item.getStartDate()));
            ps.setTimestamp(8, Timestamp.valueOf(item.getEndDate()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }


    private Item mapRowToItem(ResultSet rs, int rowNum) throws SQLException {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setSellerId(rs.getInt("seller_id"));
        item.setCategoryId(rs.getInt("category_id"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setStartPrice(rs.getBigDecimal("start_price"));
        item.setCurrentPrice(rs.getBigDecimal("current_price"));
        item.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
        item.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());
        int winnerId = rs.getInt("winner_id");
        if (!rs.wasNull()) item.setWinnerId(winnerId);
        BigDecimal finalPrice = rs.getBigDecimal("final_price");
        if (finalPrice != null) item.setFinalPrice(finalPrice);
        return item;
    }

    public void updateCurrentPrice(Integer itemId, BigDecimal amount) {
        String sql = "UPDATE items SET current_price = ? WHERE id = ?";
        jdbcTemplate.update(sql, amount, itemId);
    }

    public List<Item> findExpiredItemsWithoutWinner(LocalDateTime now) {
        String sql = """
        SELECT * FROM items 
        WHERE end_date < ? 
          AND winner_id IS NULL
    """;
        return jdbcTemplate.query(sql, this::mapRowToItem, Timestamp.valueOf(now));
    }

    public void markItemAsSold(int itemId, int winnerId, BigDecimal finalPrice) {
        String sql = "UPDATE items SET winner_id = ?, final_price = ? WHERE id = ?";
        jdbcTemplate.update(sql, winnerId, finalPrice, itemId);
    }
}

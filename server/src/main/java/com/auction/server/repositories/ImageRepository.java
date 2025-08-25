package com.auction.server.repositories;

import com.auction.server.entities.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ImageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Integer itemId, String path) {
        String sql = "INSERT INTO images (item_id, path) VALUES (?, ?)";
        jdbcTemplate.update(sql, itemId, path);
    }

    public void deleteByItemId(Integer itemId) {
        String sql = "DELETE FROM images WHERE item_id = ?";
        jdbcTemplate.update(sql, itemId);
    }
}


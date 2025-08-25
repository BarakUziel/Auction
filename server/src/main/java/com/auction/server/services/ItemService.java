package com.auction.server.services;

import com.auction.server.converters.ItemConverter;
import com.auction.server.dtos.ItemDto;
import com.auction.server.entities.Item;
import com.auction.server.repositories.ImageRepository;
import com.auction.server.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ImageRepository imageRepository;

    public List<Item> getItemsByCategory(Integer categoryId) {
        return itemRepository.findByCategory(categoryId);
    }

    public List<Item> getSoldItems(Integer sellerId) {
        return itemRepository.findSoldItems(sellerId);
    }

    public Optional<Item> getItemById(Integer id) {
        return itemRepository.findById(id);
    }

    public void createItem(ItemDto dto) {
        Item item = ItemConverter.toEntity(dto);
        Integer itemId = itemRepository.saveAndReturnId(item);

        if (dto.getImages() != null) {
            dto.getImages().forEach(imageDto ->
                    imageRepository.save(itemId, imageDto.getPath())
            );
        }
    }

    public void createItemWithImages(String name, String description, double startPrice, int auctionDays,
                                     int categoryId, int sellerId, List<MultipartFile> images) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setStartPrice(BigDecimal.valueOf(startPrice));
        item.setCurrentPrice(BigDecimal.valueOf(startPrice));
        item.setCategoryId(categoryId);
        item.setSellerId(sellerId);
        item.setStartDate(LocalDateTime.now());
        item.setEndDate(LocalDateTime.now().plusDays(auctionDays));

        Integer itemId = itemRepository.saveAndReturnId(item);

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                try {
                    String filename = saveImageToDisk(image);
                    imageRepository.save(itemId, filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String saveImageToDisk(MultipartFile file) throws IOException {
        String uploadDir = "uploads";
        Files.createDirectories(Paths.get(uploadDir));

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir, filename);
        Files.write(path, file.getBytes());
        return filename;
    }
}


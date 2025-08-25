package com.auction.client.controllers;

import com.auction.client.dtos.ItemDto;
import com.auction.client.services.ApiClient;
import com.auction.client.utils.SceneManager;
import com.auction.client.utils.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;

public class CategoryItemsController implements Initializable {

    @FXML
    private Label categoryLabel, errorLabel;

    @FXML
    private TableView<ItemDto> itemsTable;

    @FXML
    private TableColumn<ItemDto, String> nameColumn, descriptionColumn, priceColumn, endDateColumn, startPriceColumn, startDateColumn, sellerIdColumn, bidsColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Integer categoryId = SessionManager.getSelectedCategoryId();
        String categoryName = SessionManager.getSelectedCategoryName();
        if (categoryId == null) {
            errorLabel.setText("No category selected.");
            return;
        }

        categoryLabel.setText("Items in Category " + categoryName);

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        startPriceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStartPrice())));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCurrentPrice())));
        startDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDate()));
        endDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndDate()));
        sellerIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSellerId())));
        bidsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getBidCount())));

        loadItems(categoryId);

        itemsTable.setRowFactory(tv -> {
            TableRow<ItemDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    ItemDto selectedItem = row.getItem();
                    SessionManager.setSelectedItem(selectedItem);
                    SceneManager.loadScene("item.fxml", "Item Details");
                }
            });
            return row;
        });

    }

    private void loadItems(Integer categoryId) {
        try {
            HttpResponse<String> response = ApiClient.get("/items?categoryId=" + categoryId);
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                List<ItemDto> items = mapper.readValue(response.body(), new TypeReference<>() {});
                itemsTable.getItems().setAll(items);
            } else {
                errorLabel.setText("Failed to load items: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
        }
    }
}

package com.auction.client.controllers;

import com.auction.client.dtos.ItemDto;
import com.auction.client.services.ApiClient;
import com.auction.client.utils.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;

public class SoldItemsController implements Initializable {

    @FXML
    private TableView<ItemDto> soldItemsTable;

    @FXML
    private TableColumn<ItemDto, String> nameColumn, finalPriceColumn, buyerIdColumn;

    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        finalPriceColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getFinalPrice() != null ? cell.getValue().getFinalPrice().toString() : "N/A"));
        buyerIdColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getWinnerId() != null ? String.valueOf(cell.getValue().getWinnerId()) : "N/A"));

        loadSoldItems();
    }

    private void loadSoldItems() {
        try {
            int sellerId = SessionManager.getLoggedInUser().getUserId();
            HttpResponse<String> response = ApiClient.get("/items/sold?sellerId=" + sellerId);
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                List<ItemDto> items = mapper.readValue(response.body(), new TypeReference<>() {});
                soldItemsTable.getItems().setAll(items);
            } else {
                errorLabel.setText("Failed to fetch sold items: " + response.body());
            }
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


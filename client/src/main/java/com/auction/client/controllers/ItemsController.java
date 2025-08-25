package com.auction.client.controllers;

import com.auction.client.services.ApiClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ItemsController implements Initializable {

    @FXML
    private Label categoryLabel;

    @FXML
    private ListView<String> itemsList;

    @FXML
    private Label errorLabel;

    private String categoryName;

    public void setCategoryName(String category) {
        this.categoryName = category;
        categoryLabel.setText("Items in " + category);
        loadItems();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private void loadItems() {
        try {
            HttpResponse<String> response = ApiClient.get("/items?categoryId=" + categoryName);
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                List<String> items = mapper.readValue(response.body(), new TypeReference<>() {});
                itemsList.getItems().setAll(items);
            } else {
                errorLabel.setText("Failed to load items: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
        }
    }
}

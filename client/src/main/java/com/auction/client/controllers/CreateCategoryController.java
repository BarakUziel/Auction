package com.auction.client.controllers;

import com.auction.client.services.ApiClient;
import com.auction.client.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.http.HttpResponse;

public class CreateCategoryController {

    @FXML
    private TextField nameField;

    @FXML
    private Label errorLabel;

    @FXML
    public void createCategory() {
        String name = nameField.getText();

        if (name.isBlank()) {
            errorLabel.setText("Category name is required.");
            return;
        }

        String json = String.format("{\"name\": \"%s\"}", name);

        try {
            HttpResponse<String> response = ApiClient.post("/categories", json);
            if (response.statusCode() == 200) {
                SceneManager.loadScene("categories.fxml", "Categories");
            } else {
                errorLabel.setText("Failed: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
        }
    }
}

package com.auction.client.controllers;

import com.auction.client.services.ApiClient;
import com.auction.client.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.http.HttpResponse;

public class PlaceBidController {

    @FXML
    private TextField amountField;

    @FXML
    private Label errorLabel;

    private int itemId;

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @FXML
    public void placeBid() {
        String amount = amountField.getText();

        if (amount.isBlank()) {
            errorLabel.setText("Amount is required.");
            return;
        }

        String json = String.format("""
            {
                "itemId": %d,
                "amount": %s
            }
            """, itemId, amount);

        try {
            HttpResponse<String> response = ApiClient.post("/bids", json);
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

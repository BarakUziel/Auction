package com.auction.client.controllers;

import com.auction.client.dtos.ItemDto;
import com.auction.client.services.ApiClient;
import com.auction.client.utils.SceneManager;
import com.auction.client.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class BidController implements Initializable {

    @FXML
    private Label itemNameLabel, currentPriceLabel, errorLabel;
    @FXML private TextField bidAmountField;

    private ItemDto item;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        item = SessionManager.getSelectedItem();
        if (item == null) {
            errorLabel.setText("No item selected.");
            return;
        }

        itemNameLabel.setText("Bidding on: " + item.getName());
        currentPriceLabel.setText("Current Price: $" + item.getCurrentPrice());
    }

    @FXML
    public void submitBid() {
        try {
            double bidAmount = Double.parseDouble(bidAmountField.getText());

            if (bidAmount <= item.getCurrentPrice()) {
                errorLabel.setText("Bid must be higher than current price.");
                return;
            }

            String json = String.format("""
                {
                    "itemId": %d,
                    "bidderId": %d,
                    "amount": %.2f
                }
                """, item.getId(), SessionManager.getLoggedInUser().getUserId(), bidAmount);

            HttpResponse<String> response = ApiClient.post("/bids", json);
            if (response.statusCode() == 200) {
                SceneManager.loadScene("categories.fxml", "Categories");
            } else {
                errorLabel.setText("Failed to submit bid: " + response.body());
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid bid amount.");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
        }
    }
}


package com.auction.client.controllers;

import com.auction.client.dtos.ItemDto;
import com.auction.client.utils.SceneManager;
import com.auction.client.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemController implements Initializable {

    @FXML
    private Label itemNameLabel, descriptionLabel, startPriceLabel, currentPriceLabel,
            startDateLabel, endDateLabel, bidCountLabel, sellerLabel;

    private ItemDto item;

    @FXML
    private FlowPane imagePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        item = SessionManager.getSelectedItem();
        if (item == null) return;

        itemNameLabel.setText(item.getName());
        descriptionLabel.setText("Description: " + item.getDescription());
        startPriceLabel.setText("Start Price: $" + item.getStartPrice());
        currentPriceLabel.setText("Current Price: $" + item.getCurrentPrice());
        startDateLabel.setText("Start Date: " + item.getStartDate());
        endDateLabel.setText("End Date: " + item.getEndDate());
        bidCountLabel.setText("Total Bids: " + item.getBidCount());
        sellerLabel.setText("Seller ID: " + item.getSellerId());

        if (item.getImages() != null) {
            for (var imageDto : item.getImages()) {
                Image image = new Image("http://localhost:8080/uploads/" + imageDto.getPath(), 300, 300, true, true);
                ImageView imageView = new ImageView(image);
                imagePane.getChildren().add(imageView);
            }
        }
    }

    @FXML
    public void goToBid() {
        SceneManager.loadScene("bid.fxml", "Place Bid");
    }
}

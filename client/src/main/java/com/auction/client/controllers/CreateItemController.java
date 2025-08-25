package com.auction.client.controllers;

import com.auction.client.dtos.CategoryDto;
import com.auction.client.services.ApiClient;
import com.auction.client.utils.SceneManager;
import com.auction.client.utils.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.*;

public class CreateItemController implements Initializable {

    @FXML
    private TextField nameField, startPriceField, daysField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Label errorLabel;

    @FXML
    private ComboBox<CategoryDto> categoryComboBox;

    private List<File> selectedImages = new ArrayList<>();

    @FXML private Label imageCountLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureCategoryComboBox();
        loadCategories();
    }

    private void configureCategoryComboBox() {
        categoryComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(CategoryDto category, boolean empty) {
                super.updateItem(category, empty);
                setText(empty || category == null ? "" : category.getName());
            }
        });
        categoryComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(CategoryDto category, boolean empty) {
                super.updateItem(category, empty);
                setText(empty || category == null ? "" : category.getName());
            }
        });
    }

    private void loadCategories() {
        try {
            HttpResponse<String> response = ApiClient.get("/categories");
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                List<CategoryDto> categories = mapper.readValue(response.body(), new TypeReference<>() {});
                categoryComboBox.getItems().setAll(categories);

                Integer preselectedId = SessionManager.getSelectedCategoryId();
                if (preselectedId != null) {
                    categories.stream()
                            .filter(cat -> cat.getId().equals(preselectedId))
                            .findFirst()
                            .ifPresent(categoryComboBox::setValue);
                }

            } else {
                errorLabel.setText("Failed to load categories: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void createItem() {
        CategoryDto selectedCategory = categoryComboBox.getValue();
        if (selectedCategory == null) {
            errorLabel.setText("Please select a category.");
            return;
        }

        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();
        String startPriceStr = startPriceField.getText().trim();
        String daysStr = daysField.getText().trim();

        if (name.isEmpty() || startPriceStr.isEmpty() || daysStr.isEmpty()) {
            errorLabel.setText("Please fill all required fields.");
            return;
        }

        double startPrice;
        int auctionDays;

        try {
            startPrice = Double.parseDouble(startPriceStr);
            auctionDays = Integer.parseInt(daysStr);
        } catch (NumberFormatException e) {
            errorLabel.setText("Start Price must be a number and Auction Days must be an integer.");
            return;
        }

        try {
            Map<Object, Object> formFields = new LinkedHashMap<>();
            formFields.put("name", name);
            formFields.put("description", description);
            formFields.put("startPrice", String.valueOf(startPrice));
            formFields.put("auctionDays", String.valueOf(auctionDays));
            formFields.put("categoryId", String.valueOf(selectedCategory.getId()));
            formFields.put("sellerId", String.valueOf(SessionManager.getLoggedInUser().getUserId()));

            HttpResponse<String> response = ApiClient.postMultipart("/items/create-with-images", formFields, selectedImages);

            if (response.statusCode() == 200) {
                SceneManager.loadScene("categories.fxml", "Categories");
            } else {
                errorLabel.setText("Failed to create item: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
        }
    }


    @FXML
    public void handleSelectImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files != null) {
            selectedImages = files;
            imageCountLabel.setText(files.size() + " images selected");
        }
    }
}

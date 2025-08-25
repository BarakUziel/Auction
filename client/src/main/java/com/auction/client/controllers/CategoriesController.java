package com.auction.client.controllers;

import com.auction.client.dtos.CategoryDto;
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
import javafx.scene.control.TableView;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;

public class CategoriesController implements Initializable {

    @FXML
    private TableView<CategoryDto> categoriesTable;

    @FXML
    private TableColumn<CategoryDto, String> categoryColumn;

    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        loadCategories();

        categoriesTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && categoriesTable.getSelectionModel().getSelectedItem() != null) {
                CategoryDto selected = categoriesTable.getSelectionModel().getSelectedItem();
                SessionManager.setSelectedCategoryId(selected.getId());
                SessionManager.setSelectedCategoryName(selected.getName());
                SceneManager.loadScene("category_items.fxml", "Items in Category");
            }
        });
    }

    private void loadCategories() {
        try {
            HttpResponse<String> response = ApiClient.get("/categories");
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                List<CategoryDto> categories = mapper.readValue(response.body(), new TypeReference<>() {});
                categoriesTable.getItems().setAll(categories);
            } else {
                errorLabel.setText("Failed to load categories: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void goToAddCategory() {
        SceneManager.loadScene("create_category.fxml", "Add Category");
    }

    @FXML
    public void goToAddItem() {
        SceneManager.loadScene("create_item.fxml", "Add Item");
    }
}

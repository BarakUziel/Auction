package com.auction.client.controllers;

import com.auction.client.utils.SceneManager;
import com.auction.client.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private Button categoriesButton;

    @FXML
    private Button addItemButton;

    @FXML
    private Button soldButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (SessionManager.isLoggedIn()) {
            categoriesButton.setVisible(true);
            addItemButton.setVisible(true);
            soldButton.setVisible(true);
            loginButton.setVisible(false);
            signupButton.setVisible(false);
        } else {
            categoriesButton.setVisible(false);
            addItemButton.setVisible(false);
            soldButton.setVisible(false);
            loginButton.setVisible(true);
            signupButton.setVisible(true);
        }
    }

    @FXML
    public void goToLogin(ActionEvent event) {
        SceneManager.loadScene("login.fxml", "Login");
    }

    @FXML
    public void goToSignUp(ActionEvent event) {
        SceneManager.loadScene("signup.fxml", "Sign Up");
    }

    @FXML
    public void goToCategories(ActionEvent event) {
        SceneManager.loadScene("categories.fxml", "Categories");
    }

    @FXML
    public void goToAddItem(ActionEvent event) {
        SceneManager.loadScene("create_item.fxml", "Add item");
    }

    @FXML
    public void goToSoldItems(ActionEvent event) {
        SceneManager.loadScene("sold_items.fxml", "Sold items");
    }
}

package com.auction.client.controllers;

import com.auction.client.utils.SceneManager;
import com.auction.client.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable{

    @FXML
    private Button logoutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolean loggedIn = SessionManager.isLoggedIn();
        logoutButton.setVisible(loggedIn);
    }

    @FXML
    private void logout() {
        SessionManager.logout();
        SceneManager.loadScene("home.fxml", "Home");
    }

    @FXML
    public void goHome(ActionEvent event) {
        SceneManager.loadScene("home.fxml", "Home");
    }
}

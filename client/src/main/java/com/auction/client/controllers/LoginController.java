package com.auction.client.controllers;

import com.auction.client.dtos.UserDto;
import com.auction.client.services.ApiClient;
import com.auction.client.utils.SceneManager;
import com.auction.client.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.http.HttpResponse;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isBlank() || password.isBlank()) {
            errorLabel.setText("Please fill all required fields.");
            return;
        }

        String json = String.format("""
            {
                "email": "%s",
                "password": "%s"
            }
            """, email, password);

        try {
            HttpResponse<String> response = ApiClient.post("/users/login", json);

            if (response.statusCode() == 200) {
                System.out.println("Login successful: " + response.body());

                int userId = Integer.parseInt(response.body().trim());

                UserDto user = new UserDto();
                user.setEmail(email);
                user.setUserId(userId);

                SessionManager.login(user);

                SceneManager.loadScene("categories.fxml", "Categories");
            } else {
                errorLabel.setText("Login failed: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
        }
    }
}

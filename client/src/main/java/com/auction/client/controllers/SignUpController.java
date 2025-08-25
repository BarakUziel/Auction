package com.auction.client.controllers;

import com.auction.client.dtos.UserDto;
import com.auction.client.services.ApiClient;
import com.auction.client.utils.SceneManager;
import com.auction.client.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.http.HttpResponse;

public class SignUpController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void handleSignUp(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            errorLabel.setText("Please fill all fields.");
            return;
        }

        String json = String.format("""
            {
                "name": "%s",
                "email": "%s",
                "password": "%s"
            }
            """, name, email, password);

        try {
            HttpResponse<String> response = ApiClient.post("/users", json);

            if (response.statusCode() == 200) {
                System.out.println("Sign Up successful: " + response.body());

                int userId = Integer.parseInt(response.body().trim());

                UserDto user = new UserDto();
                user.setUserId(userId);
                user.setEmail(email);
                user.setName(name);

                SessionManager.login(user);

                SceneManager.loadScene("categories.fxml", "Categories");
            } else {
                errorLabel.setText("Sign Up failed: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
        }
    }
}

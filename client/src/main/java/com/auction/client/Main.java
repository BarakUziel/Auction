package com.auction.client;

import com.auction.client.utils.SceneManager;
import javafx.application.Application;

import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setStage(stage);
        SceneManager.loadScene("home.fxml", "Home");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

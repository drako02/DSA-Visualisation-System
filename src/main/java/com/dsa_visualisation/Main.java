package com.dsa_visualisation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public Scene homeScene;


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Home.fxml" ));
        homeScene = new Scene(fxmlLoader.load());
        stage.setTitle("DSA Visualisation");
        
        stage.setScene(homeScene);
        stage.show();

    }



    public static void main(String[] args) {
        launch();
    }
}
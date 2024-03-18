package com.dsa_visualisation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomepageController {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;

    @FXML
    private BorderPane borderPane;


    @FXML
    private void onDatastructuresButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HomepageApplication.class.getResource("DataStructures.fxml"));
        borderPane.setCenter(fxmlLoader.load());

    }

    @FXML
    private void onHomeButtonClick(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage =(Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HomepageApplication.class.getResource("Home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

}
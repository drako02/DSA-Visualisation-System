package com.dsa_visualisation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AlgorithmsController {
    @FXML
    private BorderPane borderPane;

    public void setBorderPane(BorderPane borderPane){
        this.borderPane = borderPane;
    }


    public void onBubbleSortClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("individualDSA/BubbleSort.fxml"));
        Parent parent = fxmlLoader.load();
        borderPane.setCenter(parent);

    }

}

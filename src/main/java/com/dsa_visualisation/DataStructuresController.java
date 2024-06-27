package com.dsa_visualisation;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class DataStructuresController {
    private  BorderPane borderPane;

    public void setBorderPane1(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void onStackButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("individualDSA/Stack.fxml"));
        Parent parent = fxmlLoader.load();
        borderPane.setCenter(parent);
    }

    public void onQueueButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("individualDSA/Queue.fxml"));
        Parent parent = fxmlLoader.load();
        borderPane.setCenter(parent);
    }

    public void onArrayButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("individualDSA/Array.fxml"));
        Parent parent = fxmlLoader.load();
        borderPane.setCenter(parent);
    }

    public void onLinkedListButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("individualDSA/LinkedList.fxml"));
        Parent parent = fxmlLoader.load();
        borderPane.setCenter(parent);
    }
}

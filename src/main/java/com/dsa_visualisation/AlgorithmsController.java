package com.dsa_visualisation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class AlgorithmsController {
//    @FXML
    private BorderPane borderPane;

    public void setBorderPane(BorderPane borderPane){
        this.borderPane = borderPane;
    }


    public void onBubbleSortClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("individualDSA/BubbleSort.fxml"));
        Parent parent = fxmlLoader.load();
        borderPane.setCenter(parent);

    }

    public void onSelectionSortClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("individualDSA/SelectionSort.fxml"));
        Parent parent = fxmlLoader.load();
        borderPane.setCenter(parent);

    }

    public void onBSTClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("individualDSA/BST.fxml"));
        Parent parent = fxmlLoader.load();
        borderPane.setCenter(parent);

    }

    public void onBinarySearchClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("individualDSA/BinarySearch.fxml"));
        Parent parent = fxmlLoader.load();
        borderPane.setCenter(parent);

    }

    public void onLinearSearchClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("individualDSA/LinearSearch.fxml"));
        Parent parent = fxmlLoader.load();
        borderPane.setCenter(parent);

    }

}

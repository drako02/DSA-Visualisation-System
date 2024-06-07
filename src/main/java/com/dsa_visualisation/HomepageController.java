package com.dsa_visualisation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HomepageController {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;

    @FXML
    private BorderPane borderPane;

    private Scene homeScene;


    private boolean dataStructuresLoaded = false; // Flag to track if DataStructures.fxml is loaded
    private Parent dataStructuresRoot; // Reference to the loaded DataStructures.fxml root

    @FXML
    private void onDatastructuresButtonClick(ActionEvent event) throws IOException {
        if (!dataStructuresLoaded) {
            // Load DataStructures.fxml and set its root to dataStructuresRoot
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("DataStructures.fxml"));
            dataStructuresRoot = fxmlLoader.load();
            dataStructuresLoaded = true;

            DataStructuresController dataStructuresController = fxmlLoader.getController();
            dataStructuresController.setBorderPane1(borderPane);
        }

        // Set the root of DataStructures.fxml to the center of the BorderPane
        borderPane.setCenter(dataStructuresRoot);
    }


    @FXML
    private void onHomeButtonClick(ActionEvent event) throws IOException {
        // If homeScene is null, load the Home.fxml and create the scene
        if (homeScene == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Home.fxml"));
            homeScene = new Scene(fxmlLoader.load());
        }

        // Get the source node that triggered the event
        Node source = (Node) event.getSource();
        // Get the stage from the source node
        Stage stage = (Stage) source.getScene().getWindow();
        // Set the scene to the stage
        stage.setScene(homeScene);
    }

    @FXML
    private boolean algorithmsLoaded = false; // Flag to track if DataStructures.fxml is loaded
    private Parent algorithmsRoot; // Reference to the loaded DataStructures.fxml root

    @FXML
    private void onAlgorithmsButtonClick(ActionEvent event) throws IOException {
        if (!algorithmsLoaded) {
            // Load DataStructures.fxml and set its root to dataStructuresRoot
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Algorithms.fxml"));
            algorithmsRoot = fxmlLoader.load();
            algorithmsLoaded = true;

            AlgorithmsController algorithmsController = fxmlLoader.getController();
            algorithmsController.setBorderPane(borderPane);
        }


        // Set the root of DataStructures.fxml to the center of the BorderPane
        borderPane.setCenter(algorithmsRoot);
    }


    @FXML
    private void onNotesButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Notes.fxml"));
        borderPane.setCenter(fxmlLoader.load());
    }

    @FXML
    private void onChataiButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ChatAI.fxml"));
        borderPane.setCenter(fxmlLoader.load());
    }

    @FXML
    private void onAboutButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("About.fxml"));
        borderPane.setCenter(fxmlLoader.load());
    }

    @FXML
    private VBox sidePanel;

    @FXML
    private Button sideButton1;
    @FXML
    private Button sideButton2;
    @FXML
    private Button sideButton3;
    @FXML
    private Button sideButton4;
    @FXML
    private Button sideButton5;
    @FXML
    private Button sideButton6;


    private double defaultWidth;

    @FXML
    private void initialize(){
        defaultWidth = sidePanel.getPrefWidth();
    }

    @FXML
    private void resizeSidePanel(ActionEvent event) {

        if(sidePanel.getPrefWidth() == defaultWidth){
            sidePanel.setPrefWidth(45);
            sideButton1.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            sideButton2.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            sideButton3.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            sideButton4.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            sideButton5.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            sideButton6.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);


        } else {
            sidePanel.setPrefWidth(defaultWidth);
            sideButton1.setContentDisplay(ContentDisplay.LEFT);
            sideButton2.setContentDisplay(ContentDisplay.LEFT);
            sideButton3.setContentDisplay(ContentDisplay.LEFT);
            sideButton4.setContentDisplay(ContentDisplay.LEFT);
            sideButton5.setContentDisplay(ContentDisplay.LEFT);
            sideButton6.setContentDisplay(ContentDisplay.LEFT);


        }
    }
    @FXML
    private Button bubbleSort;
    private void onBubbleSortClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("individualDSA/BubbleSort.fxml"));
        borderPane.setCenter(fxmlLoader.load());
    }
}


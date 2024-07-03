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

public class HomepageController{
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;

    @FXML
    private BorderPane borderPane1;

    private Node initialCenter;


    private boolean dataStructuresLoaded = false; // Flag to track if DataStructures.fxml is loaded
    private Parent dataStructuresRoot; // Reference to the loaded DataStructures.fxml root

    private boolean chatAILoaded = false; // Flag to track if DataStructures.fxml is loaded
    private Parent chatAIRoot;

    private boolean notesLoaded = false; // Flag to track if DataStructures.fxml is loaded
    private Parent notesRoot;


    @FXML
    private void onDatastructuresButtonClick(ActionEvent event) throws IOException {
        if (!dataStructuresLoaded) {
            // Load DataStructures.fxml and set its root to dataStructuresRoot
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("DataStructures.fxml"));
            dataStructuresRoot = fxmlLoader.load();
            dataStructuresLoaded = true;

            DataStructuresController dataStructuresController = fxmlLoader.getController();
            dataStructuresController.setBorderPane1(borderPane1);
        }

        // Set the root of DataStructures.fxml to the center of the BorderPane
        borderPane1.setCenter(dataStructuresRoot);
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
            algorithmsController.setBorderPane(borderPane1);
        }


        // Set the root of DataStructures.fxml to the center of the BorderPane
        borderPane1.setCenter(algorithmsRoot);
    }


    @FXML
    private void onNotesButtonClick(ActionEvent event) throws IOException {
        if (!notesLoaded) {
            // Load DataStructures.fxml and set its root to dataStructuresRoot
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Notes.fxml"));
            notesRoot = fxmlLoader.load();
            notesLoaded = true;

            NotesController notesController = fxmlLoader.getController();
            notesController.setBorderPane2(borderPane1);
        }

        // Set the root of DataStructures.fxml to the center of the BorderPane
        borderPane1.setCenter(notesRoot);
    }


//    @FXML
//    private void onNotesButtonClick(ActionEvent event) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Notes.fxml"));
//        borderPane.setCenter(fxmlLoader.load());
//    }


    @FXML
    private void onChataiButtonClick(ActionEvent event) throws IOException {
        if (!chatAILoaded) {
            // Load DataStructures.fxml and set its root to dataStructuresRoot
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ChatAI.fxml"));
            chatAIRoot = fxmlLoader.load();
            chatAILoaded = true;

//            DataStructuresController dataStructuresController = fxmlLoader.getController();
//            dataStructuresController.setBorderPane1(borderPane);
        }

        // Set the root of DataStructures.fxml to the center of the BorderPane
        borderPane1.setCenter(chatAIRoot);
    }


    @FXML
    private void onAboutButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("About.fxml"));
        borderPane1.setCenter(fxmlLoader.load());
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
        initialCenter = borderPane1.getCenter(); // Save the initial center node
    }

    @FXML
    private void onHomeButtonClick(ActionEvent event) {
        borderPane1.setCenter(initialCenter);
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
        borderPane1.setCenter(fxmlLoader.load());
    }


}


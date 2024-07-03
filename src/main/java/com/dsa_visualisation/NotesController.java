package com.dsa_visualisation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NotesController implements Initializable {

    @FXML
    private Button arrayButton;
    @FXML
    private Button linkedlistButton;
    @FXML
    private Button stackButton;
    @FXML
    private Button queueButton;
    @FXML
    private Button binarysearchButton;
    @FXML
    private Button linearsearchButton;
    @FXML
    private Button bubblesortButton;
    @FXML
    private Button selectionsortButton;

    private BorderPane borderPane;


    public void setBorderPane2(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    private boolean arrayLoaded = false; // Flag to track if DataStructures.fxml is loaded
    private Parent arrayRoot;

    private boolean linkedlistLoaded = false; // Flag to track if DataStructures.fxml is loaded
    private Parent linkedlistRoot;

    private boolean stackLoaded = false; // Flag to track if DataStructures.fxml is loaded
    private Parent stackRoot;

    private boolean queueLoaded = false; // Flag to track if DataStructures.fxml is loaded
    private Parent queueRoot;

    private boolean binarysearchLoaded = false; // Flag to track if DataStructures.fxml is loaded
    private Parent binarysearchRoot;

    private boolean linearsearchLoaded = false; // Flag to track if DataStructures.fxml is loaded
    private Parent linearsearchRoot;

    private boolean bubblesortLoaded = false; // Flag to track if DataStructures.fxml is loaded
    private Parent bubblesortRoot;

    private boolean selectionsortLoaded = false; // Flag to track if DataStructures.fxml is loaded
    private Parent selectionsortRoot;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        arrayButton.setOnAction(event -> {
            if (!arrayLoaded) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/dsa_visualisation/Notes/ArrayNotes.fxml"));
                try {
                    arrayRoot = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                arrayLoaded = true;

            }

            borderPane.setCenter(arrayRoot);
        });

        linkedlistButton.setOnAction(event -> {
            if (!linkedlistLoaded) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/dsa_visualisation/Notes/DSANotesSkeleton.fxml"));
                try {
                    linkedlistRoot = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                linkedlistLoaded = true;

            }

            borderPane.setCenter(linkedlistRoot);
        });

        stackButton.setOnAction(event -> {
            if (!stackLoaded) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/dsa_visualisation/Notes/DSANotesSkeleton.fxml"));
                try {
                    stackRoot = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stackLoaded = true;

            }

            borderPane.setCenter(stackRoot);
        });

        queueButton.setOnAction(event -> {
            if (!queueLoaded) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/dsa_visualisation/Notes/DSANotesSkeleton.fxml"));
                try {
                    queueRoot = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                queueLoaded = true;

            }

            borderPane.setCenter(queueRoot);
        });

        binarysearchButton.setOnAction(event -> {
            if (!binarysearchLoaded) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/dsa_visualisation/Notes/BinarysearchNotes.fxml"));
                try {
                    binarysearchRoot = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                binarysearchLoaded = true;

            }

            borderPane.setCenter(binarysearchRoot);
        });

        linearsearchButton.setOnAction(event -> {
            if (!linearsearchLoaded) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/dsa_visualisation/Notes/DSANotesSkeleton.fxml"));
                try {
                    linearsearchRoot = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                linearsearchLoaded = true;

            }

            borderPane.setCenter(linearsearchRoot);
        });

        bubblesortButton.setOnAction(event -> {
            if (!bubblesortLoaded) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/dsa_visualisation/Notes/DSANotesSkeleton.fxml"));
                try {
                    bubblesortRoot = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                bubblesortLoaded = true;

            }

            borderPane.setCenter(bubblesortRoot);
        });

        selectionsortButton.setOnAction(event -> {
            if (!selectionsortLoaded) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/dsa_visualisation/Notes/DSANotesSkeleton.fxml"));
                try {
                    selectionsortRoot = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                selectionsortLoaded = true;

            }

            borderPane.setCenter(selectionsortRoot);
        });

    }




}

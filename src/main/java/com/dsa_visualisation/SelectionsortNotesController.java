package com.dsa_visualisation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;


import java.net.URL;
import java.util.ResourceBundle;

public class SelectionsortNotesController implements Initializable {

    public Label introHeaading;
    public TextArea introContent;
    public TextArea operationContent;
    public Label operationsHeading;
    public Label applicationHeading;
    public TextArea applicationContent;

    public ScrollPane scrollPane1;

    public ScrollPane scrollPane2;



    private NotesLoader notesLoader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notesLoader = new NotesLoader("src/main/resources/com/dsa_visualisation/Notes/DSANotes.json");
        String arrayIntroHeading= notesLoader.getAlgorithmNote("selectionsort").getIntroHeading();
        String arrayIntro = notesLoader.getAlgorithmNote("selectionsort").getIntro();
        String arrayOperationsHeadding = notesLoader.getAlgorithmNote("selectionsort").getOperationsHeading();
        String arrayOperations = notesLoader.getAlgorithmNote("selectionsort").getOperations();
        String arrayApplicationsHeading = notesLoader.getAlgorithmNote("selectionsort").getApplicationHeading();
        String arrayApplications = notesLoader.getAlgorithmNote("selectionsort").getApplications();

        introHeaading.setText(arrayIntroHeading);
        introContent.setText(arrayIntro);
        operationsHeading.setText(arrayOperationsHeadding);
        operationContent.setText(arrayOperations);
        applicationHeading.setText(arrayApplicationsHeading);
        applicationContent.setText(arrayApplications);

        introContent.setWrapText(true);
        operationContent.setWrapText(true);
        applicationContent.setWrapText(true);

        scrollPane1.setFitToWidth(true);
        scrollPane1.setFitToHeight(true);
        scrollPane2 .setFitToWidth(true);
        scrollPane2.setFitToHeight(true);





    }
}

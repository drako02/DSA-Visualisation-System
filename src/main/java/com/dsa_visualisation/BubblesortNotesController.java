package com.dsa_visualisation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;


import java.net.URL;
import java.util.ResourceBundle;

public class BubblesortNotesController implements Initializable {

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
        String arrayIntroHeading= notesLoader.getDataStructureNote("array").getIntroHeading();
        String arrayIntro = notesLoader.getDataStructureNote("array").getIntro();
        String arrayOperationsHeadding = notesLoader.getDataStructureNote("array").getOperationsHeading();
        String arrayOperations = notesLoader.getDataStructureNote("array").getOperations();
        String arrayApplicationsHeading = notesLoader.getDataStructureNote("array").getApplicationHeading();
        String arrayApplications = notesLoader.getDataStructureNote("array").getApplications();

        introHeaading.setText(arrayIntroHeading);
        introContent.setText(arrayIntro);
        operationsHeading.setText(arrayOperationsHeadding);
        operationContent.setText(arrayOperations);
        applicationHeading.setText(arrayApplicationsHeading);
        applicationContent.setText(arrayApplications);

        scrollPane1.setFitToWidth(true);
        scrollPane1.setFitToHeight(true);
        scrollPane2 .setFitToWidth(true);
        scrollPane2.setFitToHeight(true);





    }
}

package com.dsa_visualisation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;


import java.net.URL;
import java.util.ResourceBundle;

public class LinearsearchNotesController implements Initializable {

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
        String arrayIntroHeading= notesLoader.getAlgorithmNote("linearsearch").getIntroHeading();
        String arrayIntro = notesLoader.getAlgorithmNote("linearsearch").getIntro();
        String arrayOperationsHeadding = notesLoader.getAlgorithmNote("linearsearch").getOperationsHeading();
        String arrayOperations = notesLoader.getAlgorithmNote("linearsearch").getOperations();
        String arrayApplicationsHeading = notesLoader.getAlgorithmNote("linearsearch").getApplicationHeading();
        String arrayApplications = notesLoader.getAlgorithmNote("linearsearch").getApplications();

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

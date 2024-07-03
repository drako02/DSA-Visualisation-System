package com.dsa_visualisation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.fxmisc.richtext.InlineCssTextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class SkeletonController implements Initializable {

    @FXML
    private ScrollPane scrollPane1;
    @FXML
    private ScrollPane scrollPane2;
    @FXML
    private ScrollPane scrollPane3;

    private NotesLoader notesLoader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        WebView noteArea1 = new WebView();
        WebEngine webEngine = noteArea1.getEngine() ;

        notesLoader = new NotesLoader("src/main/resources/com/dsa_visualisation/Notes/DSANotes.json");
        String arrayNote_1 = notesLoader.getDataStructureNote("array", "part1");

        String htmlCOntent1 = """
            <!DOCTYPE html>
            <html>
            <head>
                
            </head>
            <body>
                <p> %s </p>
            </body>
            </html>
            """.formatted(arrayNote_1);

        webEngine.loadContent(htmlCOntent1);
        scrollPane1.setContent(noteArea1);


    }
}

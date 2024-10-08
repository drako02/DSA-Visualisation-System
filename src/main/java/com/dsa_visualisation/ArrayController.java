package com.dsa_visualisation;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.json.JSONObject;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArrayController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ToggleButton javaButton;
    @FXML
    private ToggleButton cppButton;
    @FXML
    private ToggleButton jsButton;

    private WebView webView;
    private JSONObject codeJson;
    private WebEngine webEngine;

    private Random rng = new Random();

    private ExecutorService exec = Executors.newCachedThreadPool(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });

    private List<StackPane> arrayElements = new ArrayList<>();
    private int arraySize = 0;

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HBox arrayHBox = new HBox();
        arrayHBox.setAlignment(Pos.CENTER);
        arrayHBox.setPadding(new Insets(10));
        arrayHBox.setSpacing(0); // Ensures the boxes are joined together

        TextField sizeField = new TextField();
        sizeField.setPromptText("Array Size");
        sizeField.setPrefWidth(100);

        TextField elementsField = new TextField();
        elementsField.setPromptText("Enter elements (comma separated)");
        elementsField.setPrefWidth(200);

        TextField inputField = new TextField();
        inputField.setPromptText("Element to Update");
        inputField.setPrefWidth(100);

        TextField indexField = new TextField();
        indexField.setPromptText("Index");
        indexField.setPrefWidth(50);

        TextField removeIndexField = new TextField();
        removeIndexField.setPromptText("Index to remove");
        removeIndexField.setPrefWidth(100);

        Button createButton = new Button("Create Array");
        Button updateButton = new Button("Update");
        Button removeButton = new Button("Remove");
        Button resetButton = new Button("Reset");

        HBox inputBox = new HBox(5, sizeField, elementsField, createButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(5));

        HBox arrayControlBox = new HBox(5, inputField, indexField, updateButton, removeIndexField, removeButton);
        arrayControlBox.setAlignment(Pos.CENTER);
        arrayControlBox.setPadding(new Insets(5));

        VBox vbox = new VBox(10, inputBox, arrayControlBox, resetButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        createButton.setOnAction(e -> {
            String sizeText = sizeField.getText();
            String elementsText = elementsField.getText();
            try {
                arraySize = Integer.parseInt(sizeText);
                List<Integer> elements = new ArrayList<>();
                if (!elementsText.isEmpty()) {
                    String[] elementStrings = elementsText.split(",");
                    for (String elementString : elementStrings) {
                        elements.add(Integer.parseInt(elementString.trim()));
                    }
                }
                createArray(arraySize, elements, arrayHBox);
                sizeField.clear();
                elementsField.clear();
            } catch (NumberFormatException ex) {
                // Handle invalid input
            }
        });

        updateButton.setOnAction(e -> {
            String inputText = inputField.getText();
            String indexText = indexField.getText();
            try {
                int number = Integer.parseInt(inputText);
                int index = Integer.parseInt(indexText);
                if (isValidUpdateIndex(index)) {
                    Task<Void> updateTask = createUpdateTask(number, index, arrayHBox);
                    arrayControlBox.setDisable(true);
                    updateTask.setOnSucceeded(event -> arrayControlBox.setDisable(false));
                    exec.submit(updateTask);
                } else {
                    showAlert("Invalid Index", "You can only update filled boxes or the first unfilled box.");
                }
                inputField.clear();
                indexField.clear();
            } catch (NumberFormatException ex) {
                // Handle invalid input
            }
        });

        removeButton.setOnAction(e -> {
            String removeIndexText = removeIndexField.getText();
            try {
                int index = Integer.parseInt(removeIndexText);
                Task<Void> removeTask = createRemoveTask(index, arrayHBox);
                arrayControlBox.setDisable(true);
                removeTask.setOnSucceeded(event -> arrayControlBox.setDisable(false));
                exec.submit(removeTask);
                removeIndexField.clear();
            } catch (NumberFormatException ex) {
                // Handle invalid input
            }
        });

        resetButton.setOnAction(e -> {
            sizeField.clear();
            elementsField.clear();
            inputField.clear();
            indexField.clear();
            removeIndexField.clear();
            arrayHBox.getChildren().clear();
            arrayElements.clear();
            arraySize = 0;
        });

        borderPane.setCenter(arrayHBox);
        borderPane.setBottom(vbox);
        createArray(8, createRandomSeries(), arrayHBox);

        loadJson();

        webView = new WebView();
        webEngine = webView.getEngine();
        scrollPane.setContent(webView);

        webView.prefWidthProperty().bind(scrollPane.widthProperty());
        webView.prefHeightProperty().bind(scrollPane.heightProperty());

        javaButton.setOnAction(event -> loadCode("java", "language-java"));
        cppButton.setOnAction(event -> loadCode("cpp", "language-cpp"));
        jsButton.setOnAction(event -> loadCode("javascript", "language-js"));

        javaButton.fire();
    }

    private void loadJson() {
        try {
            String jsonContent = new String(
                    Files.readAllBytes(Paths.get("src/main/resources/com/dsa_visualisation/Codes/Array.json")));
            codeJson = new JSONObject(jsonContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCode(String language, String languageClass) {
        if (codeJson != null) {
            // Retrieve the code snippet for the selected language from the JSONObject
            String prismCssPath = getClass().getResource("/com/dsa_visualisation/individualDSA/prism.css")
                    .toExternalForm();
            String prismJsPath = getClass().getResource("/com/dsa_visualisation/individualDSA/prism.js")
                    .toExternalForm();
            String code = codeJson.getString(language);
            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <link href="%s" rel="stylesheet" />
                        <script src="%s"></script>
                    </head>
                    <body>
                        <pre style="font-size: 13px; line-height: 1;"><code class="%s" id="codeBlock" style = 'font-size :13px; '  ></code></pre>
                        <script>
                            // JavaScript to set the code content
                            function setCodeContent(code) {
                                document.getElementById('codeBlock').textContent = code;
                                Prism.highlightAll();
                            }
                            setCodeContent(`%s`);
                        </script>
                    </body>
                    </html>
                    """
                    .formatted(prismCssPath, prismJsPath, languageClass, code);

            // Load the HTML content into the WebView
            webEngine.loadContent(htmlContent);
        }
    }

    private void createArray(int size, List<Integer> elements, HBox arrayHBox) {
        arrayElements.clear();
        arrayHBox.getChildren().clear();

        for (int i = 0; i < size; i++) {
            /**
             * Rectangle rect = new Rectangle(50, 50);
             * rect.setFill(Color.TRANSPARENT);
             * rect.setStroke(Color.BLACK);
             **/
            HBox hboxRec = new HBox();
            hboxRec.setPrefWidth(50); // Set the preferred width to 200 pixels
            hboxRec.setPrefHeight(50); // Set the preferred height to 100 pixels
            hboxRec.setStyle("-fx-border-color: black; -fx-border-width: 1;");

            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(hboxRec);
            stackPane.setAlignment(Pos.CENTER);

            if (i < elements.size()) {
                Text text = new Text(String.valueOf(elements.get(i)));
                text.setFill(Color.BLACK);
                stackPane.getChildren().add(text);
            }

            Text indexText = new Text(String.valueOf(i));
            indexText.setFill(Color.BLACK);

            VBox elementBox = new VBox(stackPane, indexText);
            elementBox.setAlignment(Pos.CENTER);

            arrayHBox.getChildren().add(elementBox);
            arrayElements.add(stackPane);
        }
    }

    private boolean isValidUpdateIndex(int index) {
        if (index < 0 || index >= arrayElements.size()) {
            return false;
        }

        // Check if the index is for a filled box
        if (arrayElements.get(index).getChildren().size() == 2) {
            return true;
        }

        // Check if the index is the first unfilled box
        for (int i = 0; i < arrayElements.size(); i++) {
            if (arrayElements.get(i).getChildren().size() == 1) {
                return i == index;
            }
        }

        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Task<Void> createUpdateTask(int number, int index, HBox arrayHBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (index >= 0 && index < arrayElements.size()) {
                    Platform.runLater(() -> {
                        StackPane stackPane = arrayElements.get(index);
                        if (stackPane.getChildren().size() == 2) {
                            stackPane.getChildren().remove(1);
                        }
                        Text text = new Text(String.valueOf(number));
                        text.setFill(Color.BLACK);
                        stackPane.getChildren().add(text);
                    });
                }
                return null;
            }
        };
    }

    private Task<Void> createRemoveTask(int index, HBox arrayHBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (index >= 0 && index < arrayElements.size() && arrayElements.get(index).getChildren().size() == 2) {
                    Platform.runLater(() -> {
                        arrayElements.get(index).getChildren().remove(1);

                        // Shift elements to fill the gap
                        for (int i = index; i < arrayElements.size() - 1; i++) {
                            StackPane current = arrayElements.get(i);
                            StackPane next = arrayElements.get(i + 1);

                            if (next.getChildren().size() == 2) {
                                Text movingText = (Text) next.getChildren().get(1);
                                next.getChildren().remove(1);
                                current.getChildren().add(movingText);
                            }
                        }
                    });
                }
                return null;
            }
        };
    }

    private List<Integer> createRandomSeries() {
        List<Integer> elements = new ArrayList<>() {
        };
        for (int i = 0; i < 5; i++) {
            int randomValue = rng.nextInt(50) + 1; // Random value between 1 and 100
            elements.add(randomValue);
        }
        return elements;
    }
}

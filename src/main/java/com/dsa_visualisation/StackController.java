package com.dsa_visualisation;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.shape.Ellipse;
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
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StackController implements Initializable {

    private ExecutorService exec = Executors.newCachedThreadPool(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });

    private List<StackPane> stackElements = new ArrayList<>();

    @FXML
    private BorderPane borderPane;

    @FXML
    private ToggleButton javaButton;

    @FXML
    private ToggleButton cppButton;

    @FXML
    private ToggleButton jsButton;

    @FXML
    private ScrollPane scrollPane;

    private NotesLoader codeLoader;

    private WebView webView;
    private JSONObject codeJson;
    private WebEngine webEngine;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        codeLoader = new NotesLoader("src/main/resources/com/dsa_visualisation/Codes/Stack.json");

        VBox stackVBox = new VBox();
        stackVBox.setAlignment(Pos.BOTTOM_CENTER);
        stackVBox.setPadding(new Insets(10));

        TextField inputField = new TextField();
        inputField.setPromptText("Enter a number");
        inputField.setPrefWidth(100);

        Button pushButton = new Button("Push");
        Button popButton = new Button("Pop");
        Button peekButton = new Button("Peek");
        Button resetButton = new Button("Reset");
        Button sizeButton = new Button("Size"); // Create a size button

        HBox inputBox = new HBox(5, inputField, pushButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(5));

        HBox buttons = new HBox(5, popButton, peekButton, resetButton, sizeButton); // Add the size button to the HBox
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(5));

        HBox hbox = new HBox(20, inputBox, buttons);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10));

        sizeButton.setOnAction(e -> {
        int stackSize = stackElements.size();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Stack Size");
        alert.setHeaderText(null);
        alert.setContentText("The size of the stack is: " + stackSize);
        alert.showAndWait();
        });

        pushButton.setOnAction(e -> {
            String inputText = inputField.getText();
            try {
                int number = Integer.parseInt(inputText);
                Task<Void> pushTask = createPushTask(number, stackVBox);
                buttons.setDisable(true);
                pushTask.setOnSucceeded(event -> buttons.setDisable(false));
                exec.submit(pushTask);
                inputField.clear();
            } catch (NumberFormatException ex) {
                // Handle invalid input
            }
        });

        popButton.setOnAction(e -> {
            Task<Void> popTask = createPopTask(stackVBox);
            buttons.setDisable(true);
            popTask.setOnSucceeded(event -> buttons.setDisable(false));
            exec.submit(popTask);
        });

        peekButton.setOnAction(e -> {
            Task<Void> peekTask = createPeekTask();
            buttons.setDisable(true);
            peekTask.setOnSucceeded(event -> buttons.setDisable(false));
            exec.submit(peekTask);
        });

        resetButton.setOnAction(e -> {
            inputField.clear();
            stackVBox.getChildren().clear();
            stackElements.clear();
        });

        stackVBox.setPadding(new Insets(0 ,0 ,40,0));
        borderPane.setCenter(stackVBox);
        borderPane.setBottom(hbox);


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
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/resources/com/dsa_visualisation/Codes/Stack.json")));
            codeJson = new JSONObject(jsonContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCode(String language, String languageClass) {
        if (codeJson != null) {
            // Retrieve the code snippet for the selected language from the JSONObject
            String prismCssPath = getClass().getResource("/com/dsa_visualisation/individualDSA/prism.css").toExternalForm();
            String prismJsPath = getClass().getResource("/com/dsa_visualisation/individualDSA/prism.js").toExternalForm();
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
            """.formatted(prismCssPath, prismJsPath, languageClass, code);

            // Load the HTML content into the WebView
            webEngine.loadContent(htmlContent);
        }


//    @FXML
//    private void onToggleButtonClick(ActionEvent event){
//        ToggleButton sourceButton = (ToggleButton) event.getSource();
//        String code = "";
//        if(sourceButton == javaButton) {
//            code = codeLoader.getCode("java");
//        } else if(sourceButton == cppButton) {
//            code = codeLoader.getCode("cpp");
//        } else if(sourceButton == jsButton) {
//            code = codeLoader.getCode("javascript");
//        }
//
//        TextArea textArea = new TextArea(code);
//        textArea.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px;");
//        textArea.setEditable(false);
//        textArea.setWrapText(true);
//
//        scrollPane.setContent(textArea);
//        scrollPane.setFitToWidth(true);
//        scrollPane.setFitToHeight(true);
    }

    private Task<Void> createPushTask(int number, VBox stackVBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    Ellipse ellipse = new Ellipse(30, 20);
                    ellipse.setFill(Color.TRANSPARENT);
                    ellipse.setStroke(Color.BLACK);
                    ellipse.setStrokeWidth(2.5);
                    Text text = new Text(String.valueOf(number));
                    text.setFill(Color.BLACK);

                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().addAll(ellipse, text);
                    stackPane.setAlignment(Pos.CENTER);

                    stackVBox.getChildren().add(0, stackPane); // Add new element to the bottom
                    stackElements.add(stackPane);

                    // Animation to move the stackPane from the bottom to the correct position
                    TranslateTransition transition = new TranslateTransition(Duration.millis(500), stackPane);
                    transition.setFromY(-(stackVBox.getBoundsInParent().getMaxY()-100));
                    transition.setToY(0);
                    transition.play();
                });
                return null;
            }
        };
    }

    private Task<Void> createPopTask(VBox stackVBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (!stackElements.isEmpty()) {
                    StackPane topElement = stackElements.remove(stackElements.size() - 1);
                    Platform.runLater(() -> {
                        TranslateTransition transition = new TranslateTransition(Duration.millis(500), topElement);
                        transition.setByY(-stackVBox.getHeight());
                        transition.setOnFinished(event -> stackVBox.getChildren().remove(topElement));
                        transition.play();
                    });
                }
                return null;
            }
        };
    }

    private Task<Void> createPeekTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (!stackElements.isEmpty()) {
                    StackPane topElement = stackElements.get(stackElements.size() - 1);
                    Platform.runLater(() -> {
                        // Highlight the top element in green
                        Ellipse ellipse = (Ellipse) topElement.getChildren().get(0);

                        FillTransition fillTransition = new FillTransition(Duration.millis(500), ellipse);
                        fillTransition.setFromValue(Color.TRANSPARENT);
                        fillTransition.setToValue(Color.valueOf("#00CC00"));
//                        fillTransition.setCycleCount(1);
//                        fillTransition.setAutoReverse(true);

                        StrokeTransition strokeTransition = new StrokeTransition(Duration.millis(500), ellipse, Color.BLACK, Color.GREEN);
//                        strokeTransition.setAutoReverse(true);

                        ParallelTransition parallelTransition = new ParallelTransition(fillTransition, strokeTransition);
                        parallelTransition.play();

                        ellipse.setFill(Color.valueOf("#00CC00"));

                        // Reset the color back to blue after a delay
                        exec.submit(() -> {
                            try {
                                Thread.sleep(1500); // 1.5 seconds delay
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Platform.runLater(() -> {
                                    ellipse.setFill(Color.TRANSPARENT);
                                    ellipse.setStroke(Color.BLACK);
                            });
                        });
                    });
                }
                return null;
            }
        };
    }


//    @FXML
//    private void onJavaButtonCLick (ActionEvent event) {
//        TextArea textArea = new TextArea("kfkfkfkfkfkkfkfkfkfffffffffffffffffffffffffffff\n" +
//                "kjfbjvkbkjbvkjvbjvbkablkavbv\n" +
//                "kjfbjvkbkjbvkjvbjvbkablkavbv\n" +
//                "kjfbjvkbkjbvkjvbjvbkablkavbv\n" +
//                "kjfbjvkbkjbvkjvbjvbkablkavbv\n" +
//                "kjfbjvkbkjbvkjvbjvbkablkavbv\n" +
//                "kjfbjvkbkjbvkjvbjvbkablkavbv\n" +
//                "kjfbjvkbkjbvkjvbjvbkablkavbv\n" +
//                "kjfbjvkbkjbvkjvbjvbkablkavbv\n");
//
//        textArea.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 18px;");
//        textArea.setWrapText(true);
//
//        scrollPane.setContent(textArea);
//        scrollPane.setFitToWidth(true);
//        scrollPane.setFitToHeight(true);
//    }


}

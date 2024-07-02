package com.dsa_visualisation;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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

public class QueueController implements Initializable {

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

    private ExecutorService exec = Executors.newCachedThreadPool(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });

    private List<StackPane> queueElements = new ArrayList<>();

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HBox queueHBox = new HBox();
        queueHBox.setAlignment(Pos.CENTER_LEFT);
        queueHBox.setPadding(new Insets(10));

        TextField inputField = new TextField();
        inputField.setPromptText("Enter a number");
        inputField.setPrefWidth(100);

        Button enqueueButton = new Button("Enqueue");
        Button dequeueButton = new Button("Dequeue");
        Button resetButton = new Button("Reset");

        HBox inputBox = new HBox(5, inputField, enqueueButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(5));

        HBox buttons = new HBox(5, dequeueButton, resetButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(5));

        HBox hbox = new HBox(20, inputBox, buttons);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10));

        enqueueButton.setOnAction(e -> {
            String inputText = inputField.getText();
            try {
                int number = Integer.parseInt(inputText);
                Task<Void> enqueueTask = createEnqueueTask(number, queueHBox);
                buttons.setDisable(true);
                enqueueTask.setOnSucceeded(event -> buttons.setDisable(false));
                exec.submit(enqueueTask);
                inputField.clear();
            } catch (NumberFormatException ex) {
                // Handle invalid input
            }
        });

        dequeueButton.setOnAction(e -> {
            Task<Void> dequeueTask = createDequeueTask(queueHBox);
            buttons.setDisable(true);
            dequeueTask.setOnSucceeded(event -> buttons.setDisable(false));
            exec.submit(dequeueTask);
        });

        resetButton.setOnAction(e -> {
            inputField.clear();
            queueHBox.getChildren().clear();
            queueElements.clear();
        });

        borderPane.setCenter(queueHBox);
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
    }

    private void loadJson() {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/resources/com/dsa_visualisation/Codes/Queue.json")));
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
    }

    private Task<Void> createEnqueueTask(int number, HBox queueHBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    Circle circle = new Circle(20);
                    circle.setFill(Color.TRANSPARENT);
                    circle.setStroke(Color.BLACK);
                    Text text = new Text(String.valueOf(number));
                    text.setFill(Color.BLACK);

                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().addAll(circle, text);
                    stackPane.setAlignment(Pos.CENTER);

                    if (!queueElements.isEmpty()) {
                        Line arrow = new Line(0, 0, 50, 0); // Arrow line from one circle to the next
                        arrow.setStroke(Color.BLACK);
                        StackPane arrowPane = new StackPane(arrow);
                        queueHBox.getChildren().add(arrowPane);
                    }

                    queueHBox.getChildren().add(stackPane); // Add new element to the end
                    queueElements.add(stackPane);

                    // Animation to move the stackPane to the correct position
                    TranslateTransition transition = new TranslateTransition(Duration.millis(500), stackPane);
                    transition.setFromX(queueHBox.getWidth());
                    transition.setToX(0);
                    transition.play();
                });
                return null;
            }
        };
    }

    private Task<Void> createDequeueTask(HBox queueHBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (!queueElements.isEmpty()) {
                    StackPane frontElement = queueElements.remove(0);
                    Platform.runLater(() -> {
                        // Animate the front element out of the view
                        TranslateTransition transition = new TranslateTransition(Duration.millis(500), frontElement);
                        transition.setByX(-queueHBox.getWidth());
                        transition.setOnFinished(event -> queueHBox.getChildren().remove(frontElement));

                        // If there's an arrow after the front element, remove it
                        if (queueHBox.getChildren().size() > 1) {
                            queueHBox.getChildren().remove(1); // Remove the arrow after the front element
                        }

                        transition.play();
                    });
                }
                return null;
            }
        };
    }
}

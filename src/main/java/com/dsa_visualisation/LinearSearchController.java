package com.dsa_visualisation;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.json.JSONObject;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class LinearSearchController implements Initializable {

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

    private Random rng = new Random(0);

    private ExecutorService exec = Executors.newCachedThreadPool(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });

    @FXML
    private BorderPane borderPane;

    private DoubleProperty animationSpeed = new SimpleDoubleProperty(1.0);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BarChart<String, Number> chart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        chart.setAnimated(false);

        TextField inputField = new TextField();
        inputField.setPromptText("Enter numbers separated by commas");
        inputField.setPrefWidth(225);

        TextField searchField = new TextField();
        searchField.setPromptText("Enter element to search for");
        searchField.setPrefWidth(50);

        Button submit = new Button("Submit");
        Button search = new Button("Search");
        Button reset = new Button("Reset");

        Slider speedSlider = new Slider(0.1, 4.0, 1.0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setMinorTickCount(5);
        speedSlider.setBlockIncrement(0.1);
        animationSpeed.bind(speedSlider.valueProperty());

        submit.setOnAction(e -> {
            String inputText = inputField.getText();
            XYChart.Series<String, Number> series = createSeriesFromInput(inputText);
            chart.getData().setAll(series);
            search.setDisable(false);

            // Create an iterator bar as an empty space at index -1
            XYChart.Data<String, Number> iterator = new XYChart.Data<>("-1", 0);
            series.getData().add(0, iterator);
            Platform.runLater(() -> {
                iterator.getNode().setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-background-color: transparent;");
            });
        });

        reset.setOnAction(e -> {
            inputField.clear();
            searchField.clear();
            chart.getData().clear();
//            search.setDisable(true);
            XYChart.Series<String, Number> series = createRandomSeries();
            chart.getData().setAll(series);
            search.setDisable(false);

            XYChart.Data<String, Number> iterator = new XYChart.Data<>("-1", 0);
            series.getData().add(0, iterator);
            Platform.runLater(() -> {
                iterator.getNode().setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-background-color: transparent;");
            });
        });

        HBox inputBox = new HBox(5, inputField, submit);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(5));

        HBox searchBox = new HBox(5, searchField, search);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPadding(new Insets(5));

        HBox buttons = new HBox(5, reset);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(5));

        HBox hbox = new HBox(5, buttons, inputBox, searchBox, speedSlider);
        HBox.setHgrow(speedSlider, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5));

        search.setOnAction(e -> {
            String searchText = searchField.getText();
            if (!searchText.isEmpty()) {
                Task<Void> animateSearchTask = createSearchingTask(chart.getData().get(0), Integer.parseInt(searchText));
                buttons.setDisable(true);
                animateSearchTask.setOnSucceeded(event -> buttons.setDisable(false));
                exec.submit(animateSearchTask);
            }
        });

        borderPane.setCenter(chart);
        borderPane.setBottom(hbox);

        XYChart.Series<String, Number> series = createRandomSeries();
        chart.getData().setAll(series);

        XYChart.Data<String, Number> iterator = new XYChart.Data<>("-1", 0);
        series.getData().add(0, iterator);
        Platform.runLater(() -> {
            iterator.getNode().setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-background-color: transparent;");
        });

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
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/resources/com/dsa_visualisation/Codes/LinearSearch.json")));
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

    private Task<Void> createSearchingTask(XYChart.Series<String, Number> series, int target) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<XYChart.Data<String, Number>> data = series.getData();

                XYChart.Data<String, Number> iterator = data.get(0);

                // Update iterator bar height to target value
                Platform.runLater(() -> iterator.setYValue(target));

                Thread.sleep((long) (1000 / animationSpeed.get()));;

                for (int i = 1; i < data.size(); i++) {
                    Data<String, Number> current = data.get(i);

                    Platform.runLater(() -> {
                        double targetX = current.getNode().getParent().localToScene(current.getNode().getBoundsInParent()).getMinX();
                        double iteratorX = iterator.getNode().getParent().localToScene(iterator.getNode().getBoundsInParent()).getMinX();
                        double distance = targetX - iteratorX;

                        TranslateTransition move = new TranslateTransition(Duration.millis(500), iterator.getNode());
                        move.setByX(distance);
                        move.play();
                    });

                    Thread.sleep((long) (750 / animationSpeed.get()));; // Wait for the animation to complete

                    if (current.getYValue().doubleValue() == target) {
                        Thread.sleep((long) (500 / animationSpeed.get()));;
                        Platform.runLater(() -> iterator.getNode().setStyle("-fx-border-color: green; -fx-border-width: 4px; -fx-background-color: transparent;"));
                        Thread.sleep((long) (500 / animationSpeed.get()));;
                        Platform.runLater(() -> current.getNode().setStyle("-fx-background-color: blue;"));
                        break;
                    }
                }

                Platform.runLater(() -> {
                    iterator.setXValue("-1");
                    iterator.setYValue(0);
                    iterator.getNode().setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-background-color: transparent;");
                });
                return null;
            }
        };
    }

    private XYChart.Series<String, Number> createSeriesFromInput(String input) {
        List<Integer> numbers = Arrays.stream(input.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < numbers.size(); i++) {
            series.getData().add(new XYChart.Data<>(Integer.toString(i + 1), numbers.get(i)));
        }
        return series;
    }

    private XYChart.Series<String, Number> createRandomSeries() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < 8; i++) {
            int randomValue = rng.nextInt(100) + 1; // Random value between 1 and 100
            series.getData().add(new Data<>(Integer.toString(i + 1), randomValue));
        }
        return series;
    }
}

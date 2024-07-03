package com.dsa_visualisation;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.json.JSONObject;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BinarySearchController implements Initializable {

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

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BarChart<String, Number> chart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        chart.setAnimated(false);

        TextField inputField = new TextField();
        inputField.setPromptText("Enter numbers separated by commas");
        inputField.setPrefWidth(225);

        TextField searchField = new TextField();
        searchField.setPromptText("Enter element to search for");
        searchField.setPrefWidth(150);

        Button submit = new Button("Submit");
        Button search = new Button("Search");
        Button reset = new Button("Reset");

        submit.setOnAction(e -> {
            String inputText = inputField.getText();
            List<Integer> numbers = Arrays.stream(inputText.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            if (!isSorted(numbers)) {
                showAlert("Binary search only works on sorted lists");
                return;
            }

            XYChart.Series<String, Number> series = createSeriesFromInput(numbers);
            chart.getData().setAll(series);
            search.setDisable(false);

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
            search.setDisable(true);
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

        HBox hbox = new HBox(5, buttons, inputBox, searchBox);
        hbox.setAlignment(Pos.CENTER);
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
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/resources/com/dsa_visualisation/Codes/BinarySearch.json")));
            codeJson = new JSONObject(jsonContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCode(String language, String languageClass) {
        if (codeJson != null) {
            // Retrieve the code snippet for the selected language from the JSONObject
            String code = codeJson.getString(language);
            String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <link href="https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism.min.css" rel="stylesheet" />
                <script src="https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/prism.min.js"></script>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-%s.min.js"></script>
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
            """.formatted(language, languageClass, code);

            // Load the HTML content into the WebView
            webEngine.loadContent(htmlContent);
        }
    }

    private boolean isSorted(List<Integer> numbers) {
        for (int i = 1; i < numbers.size(); i++) {
            if (numbers.get(i) < numbers.get(i - 1)) {
                return false;
            }
        }
        return true;
    }

    private Task<Void> createSearchingTask(XYChart.Series<String, Number> series, int target) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<XYChart.Data<String, Number>> data = series.getData();

                XYChart.Data<String, Number> iterator = data.get(0);

                Platform.runLater(() -> iterator.setYValue(target));

                Thread.sleep(1000);

                int left = 1;
                int right = data.size() - 1;
                boolean found = false;

                while (left <= right) {
                    int mid = left + (right - left) / 2;
                    Data<String, Number> midData = data.get(mid);

                    double midX = midData.getNode().getParent().localToScene(midData.getNode().getBoundsInParent()).getMinX();
                    double iteratorX = iterator.getNode().getParent().localToScene(iterator.getNode().getBoundsInParent()).getMinX();
                    double distance = midX - iteratorX;

                    Platform.runLater(() -> {
                        TranslateTransition move = new TranslateTransition(Duration.millis(500), iterator.getNode());
                        move.setByX(distance);
                        move.play();
                    });

                    Thread.sleep(750);

                    if (midData.getYValue().doubleValue() == target) {
                        found = true;
                        Platform.runLater(() -> {
                            iterator.getNode().setStyle("-fx-border-color: green; -fx-border-width: 4px; -fx-background-color: transparent;");
                            midData.getNode().setStyle("-fx-background-color: blue;");
                        });
                        break;
                    } else if (midData.getYValue().doubleValue() < target) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }

                    Thread.sleep(500);
                }

                if (!found) {
                    Platform.runLater(() -> iterator.getNode().setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-background-color: transparent;"));
                }

                return null;
            }
        };
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private XYChart.Series<String, Number> createSeriesFromInput(List<Integer> numbers) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < numbers.size(); i++) {
            series.getData().add(new XYChart.Data<>(Integer.toString(i + 1), numbers.get(i)));
        }
        return series;
    }
}

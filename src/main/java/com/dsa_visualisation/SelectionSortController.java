package com.dsa_visualisation;

import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
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
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.nio.file.Paths;
import java.nio.file.Files;

public class SelectionSortController implements Initializable {

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

        Button submit = new Button("Submit");
        Button sort = new Button("Sort");
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
            sort.setDisable(false);
        });

        reset.setOnAction(e -> {
            inputField.clear();
            chart.getData().clear();
//            sort.setDisable(true);
            chart.getData().setAll(createRandomSeries());
        });

        HBox inputBox = new HBox(5, inputField, submit);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(5));

        HBox buttons = new HBox(5, sort, reset);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(5));


        HBox hbox = new HBox(5, buttons, inputBox, speedSlider);
        HBox.setHgrow(speedSlider, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5));

        sort.setOnAction(e -> {
            Task<Void> animateSortTask = createSortingTask(chart.getData().get(0));
            buttons.setDisable(true);
            animateSortTask.setOnSucceeded(event -> buttons.setDisable(false));
            exec.submit(animateSortTask);
        });

        VBox vbox = new VBox(5, chart);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        borderPane.setCenter(chart);
        borderPane.setBottom(hbox);

        chart.getData().add(createRandomSeries());

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

//        Platform.runLater(() -> javaButton.fire());

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
    }

    private Task<Void> createSortingTask(XYChart.Series<String, Number> series) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<XYChart.Data<String, Number>> data = series.getData();
                int n = data.size();
                for (int i = 0; i < n - 1; i++) {
                    int minIndex = i;
                    for (int j = i + 1; j < n; j++) {
                        XYChart.Data<String, Number> minValueData = data.get(minIndex);
                        XYChart.Data<String, Number> currentValueData = data.get(j);

                        Platform.runLater(() -> {
                            minValueData.getNode().setStyle("-fx-background-color: red;");
                        });

                        Thread.sleep((long) (500 / animationSpeed.get()));

                        Platform.runLater(() -> {
                            currentValueData.getNode().setStyle("-fx-background-color: green;");
                        });

                        Thread.sleep((long) (500 / animationSpeed.get()));

                        if (currentValueData.getYValue().doubleValue() < minValueData.getYValue().doubleValue()) {
                            minIndex = j;
                        }

                        Thread.sleep((long) (500 / animationSpeed.get()));

                        Platform.runLater(() -> {
                            minValueData.getNode().setStyle("");
                            currentValueData.getNode().setStyle("");
                        });
                    }

                    if (minIndex != i) {
                        XYChart.Data<String, Number> first = data.get(i);
                        XYChart.Data<String, Number> second = data.get(minIndex);

                        Platform.runLater(() -> {
                            first.getNode().setStyle("-fx-background-color: red;");
                            second.getNode().setStyle("-fx-background-color: red;");
                        });

                        Thread.sleep((long) (500 / animationSpeed.get()));

                        CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(() -> {
                            Animation swap = createSwapAnimation(first, second);
                            swap.setOnFinished(e -> latch.countDown());
                            swap.play();
                        });
                        latch.await();

                        Thread.sleep((long) (500 / animationSpeed.get()));

                        Platform.runLater(() -> {
                            first.getNode().setStyle("");
                            second.getNode().setStyle("");
                        });
                    }
                    int sortedIndex = i;
                    Thread.sleep((long) (500 / animationSpeed.get()));
                    Platform.runLater(() -> data.get(sortedIndex).getNode().setStyle("-fx-background-color: turquoise;"));
                }
                Platform.runLater(() -> data.get(n - 1).getNode().setStyle("-fx-background-color: turquoise;"));
                return null;
            }
        };
    }

    private <T> Animation createSwapAnimation(Data<?, T> first, Data<?, T> second) {
        double firstX = first.getNode().getParent().localToScene(first.getNode().getBoundsInParent()).getMinX();
        double secondX = second.getNode().getParent().localToScene(second.getNode().getBoundsInParent()).getMinX();

        double firstStartTranslate = first.getNode().getTranslateX();
        double secondStartTranslate = second.getNode().getTranslateX();

        TranslateTransition firstTranslate = new TranslateTransition(Duration.millis(500 / animationSpeed.get()), first.getNode());
        firstTranslate.setByX(secondX - firstX);
        TranslateTransition secondTranslate = new TranslateTransition(Duration.millis(500 / animationSpeed.get()), second.getNode());
        secondTranslate.setByX(firstX - secondX);
        ParallelTransition translate = new ParallelTransition(firstTranslate, secondTranslate);

        translate.statusProperty().addListener((obs, oldStatus, newStatus) -> {
            if (oldStatus == Animation.Status.RUNNING) {
                T temp = first.getYValue();
                first.setYValue(second.getYValue());
                second.setYValue(temp);
                first.getNode().setTranslateX(firstStartTranslate);
                second.getNode().setTranslateX(secondStartTranslate);
            }
        });

        return translate;
    }

    private XYChart.Series<String, Number> createSeriesFromInput(String input) {
        List<Integer> numbers = Arrays.stream(input.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < numbers.size(); i++) {
            series.getData().add(new Data<>(Integer.toString(i + 1), numbers.get(i)));
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

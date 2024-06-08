package com.dsa_visualisation;

import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
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
import javafx.scene.chart.XYChart.*;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class SelectionSortController implements Initializable {

    private Random rng = new Random(0);

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

        Button submit = new Button("Submit");
        Button sort = new Button("Sort");
        Button reset = new Button("Reset");

        submit.setOnAction(e ->{
            String inputText = inputField.getText();
            Series <String, Number> series = createSeriesFromInput(inputText);
            chart.getData().setAll(series);
            sort.setDisable(false);
        });

        reset.setOnAction(e -> {
            inputField.clear();
            chart.getData().clear();
            sort.setDisable(true);
        });

        HBox inputBox = new HBox(5, inputField, submit);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(5));

        HBox buttons = new HBox(5, sort, reset);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(5));

        HBox hbox = new HBox(5, buttons, inputBox);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(5));

        sort.setOnAction(e -> {
            Task<Void> animateSortTask = createSortingTask(chart.getData().get(0));
            buttons.setDisable(true);
            animateSortTask.setOnSucceeded(event -> buttons.setDisable(false));
            exec.submit(animateSortTask);
        });

        borderPane.setCenter(chart);
        borderPane.setBottom(hbox);
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

                        Thread.sleep(500);

                        Platform.runLater(() -> {
                            currentValueData.getNode().setStyle("-fx-background-color: green;");

                        });

                        Thread.sleep(500);

                        if (currentValueData.getYValue().doubleValue() < minValueData.getYValue().doubleValue()) {
                            minIndex = j;
                        }

                        Thread.sleep(500);

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

                        Thread.sleep(500);

                        CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(() -> {
                            Animation swap = createSwapAnimation(first, second);
                            swap.setOnFinished(e -> latch.countDown());
                            swap.play();
                        });
                        latch.await();

                        Thread.sleep(500);

                        Platform.runLater(() -> {
                            first.getNode().setStyle("");
                            second.getNode().setStyle("");
                        });

                    }
                    int sortedIndex = i;
                    Thread.sleep(500);
                    Platform.runLater(() -> data.get(sortedIndex).getNode().setStyle("-fx-background-color: turquoise;"));

                }
                Platform.runLater(() ->data.get(n-1).getNode().setStyle("-fx-background-color: turquoise"));
                return null;
            }
        };
    }

    private <T> Animation createSwapAnimation(Data<?, T> first, Data<?, T> second) {
        double firstX = first.getNode().getParent().localToScene(first.getNode().getBoundsInParent()).getMinX();
        double secondX = second.getNode().getParent().localToScene(second.getNode().getBoundsInParent()).getMinX();

        double firstStartTranslate = first.getNode().getTranslateX();
        double secondStartTranslate = second.getNode().getTranslateX();

        TranslateTransition firstTranslate = new TranslateTransition(Duration.millis(500), first.getNode());
        firstTranslate.setByX(secondX - firstX);
        TranslateTransition secondTranslate = new TranslateTransition(Duration.millis(500), second.getNode());
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
        List <Integer> numbers = Arrays.stream(input.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        Series <String, Number> series = new Series<>();
        for (int i = 0; i < numbers.size(); i++) {
            series.getData().add(new Data<>(Integer.toString(i + 1), numbers.get(i)));
        }
        return series;
    }
}
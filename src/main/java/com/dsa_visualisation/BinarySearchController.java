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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BinarySearchController implements Initializable {

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

        Button search = new Button("Search");

        Button reset = new Button("Reset");

        submit.setOnAction(e -> {
            String inputText = inputField.getText();
            XYChart.Series<String, Number> series = createSeriesFromInput(inputText);
            chart.getData().setAll(series);
            search.setDisable(false);
        });

        reset.setOnAction(e -> {
            inputField.clear();
            chart.getData().clear();
            search.setDisable(true);
        });

        HBox inputBox = new HBox(5, inputField, submit);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(5));

        HBox buttons = new HBox(5, search, reset);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(5));

        HBox hbox = new HBox(5, buttons, inputBox) ;
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(5));

        search.setOnAction(e -> {
            Task<Void> animateSearchTask = createSearchTask(chart.getData().get(0), Integer.parseInt(inputField.getText().split(",")[0].trim()));
            buttons.setDisable(true);
            animateSearchTask.setOnSucceeded(event -> buttons.setDisable(false));
            exec.submit(animateSearchTask);
        });

        borderPane.setCenter(chart);
        borderPane.setBottom(hbox);
    }

    private Task<Void> createSearchTask(XYChart.Series<String, Number> series, int target) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                ObservableList<XYChart.Data<String, Number>> data = series.getData();
                int low = 0;
                int high = data.size() - 1;

                while (low <= high) {
                    int mid = (low + high) / 2;
                    Data<String, Number> midData = data.get(mid);

                    Platform.runLater(() -> midData.getNode().setStyle("-fx-border-color: red; -fx-background-color: transparent;"));

                    Thread.sleep(500);

                    if (midData.getYValue().intValue() == target) {
                        Platform.runLater(() -> midData.getNode().setStyle("-fx-background-color: green;"));
                        break;
                    } else if (midData.getYValue().intValue() < target) {
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }

                    Platform.runLater(() -> midData.getNode().setStyle(""));

                    Thread.sleep(500);
                }

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
}

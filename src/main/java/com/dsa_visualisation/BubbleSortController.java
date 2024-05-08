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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BubbleSortController implements Initializable{

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
        XYChart.Series<String, Number> series = generateRanomIntegerSeries(10);
        chart.getData().add(series);

        Button sort = new Button("Sort");

        Button reset = new Button("Reset");

        reset.setOnAction(e -> chart.getData().set(0, generateRanomIntegerSeries(10)));

        HBox buttons = new HBox(5, sort, reset);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(5));

       sort.setOnAction(e -> {
            Task<Void> animateSortTask = createSortingTask(chart.getData().get(0));
            buttons.setDisable(true);
            animateSortTask.setOnSucceeded(event -> buttons.setDisable(false));
            exec.submit(animateSortTask);
        });

       borderPane.setCenter(chart);
       borderPane.setBottom(buttons);
    }

    private Task<Void> createSortingTask(XYChart.Series<String, Number> series){
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                ObservableList<XYChart.Data<String, Number>> data = series.getData();
                for (int i = data.size() - 1; i >= 0; i--) {
                    for (int j = 0; j < i; j++) {
                        XYChart.Data<String, Number> first = data.get(j);
                        Data<String, Number> second = data.get(j+1);

                        Platform.runLater(() -> {
                            first.getNode().setStyle("-fx-background-color: green;");
                            second.getNode().setStyle("-fx-background-color: green;");
                        });

                        Thread.sleep(500);

                        if(first.getYValue().doubleValue() > second.getYValue().doubleValue()) {
                            CountDownLatch latch = new CountDownLatch(1);
                            Platform.runLater(() -> {
                                Animation swap = createSwapAnimation(first, second);
                                swap.setOnFinished(e -> latch.countDown());
                                swap.play();
                            });
                            latch.await();
                        }
                        Thread.sleep(500);

                        Platform.runLater(() -> {
                            first.getNode().setStyle("");
                            second.getNode().setStyle("");
                        });
                    }

                }
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

        translate.statusProperty().addListener((obs, oldStatus, newStatus) ->{
            if(oldStatus == Animation.Status.RUNNING) {
                T temp = first.getYValue();
                first.setYValue(second.getYValue());
                second.setYValue(temp);
                first.getNode().setTranslateX(firstStartTranslate);
                second.getNode().setTranslateX(secondStartTranslate);
            }
        });

        return translate;

    }

    private Series<String, Number> generateRanomIntegerSeries(int n) {
        Series<String, Number> series = new Series<>();
        for (int i = 1; i <= n; i++) {
            series.getData().add(new Data<>(Integer.toString(i), rng.nextInt(100) + 1 ));
        }
        return series;
    }
}
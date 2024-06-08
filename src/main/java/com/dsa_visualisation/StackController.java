package com.dsa_visualisation;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        HBox inputBox = new HBox(5, inputField, pushButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(5));

        HBox buttons = new HBox(5, popButton, peekButton, resetButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(5));

        HBox hbox = new HBox(20, inputBox, buttons);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10));

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

        borderPane.setCenter(stackVBox);
        borderPane.setBottom(hbox);
    }

    private Task<Void> createPushTask(int number, VBox stackVBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    Ellipse ellipse = new Ellipse(30, 20);
                    ellipse.setFill(Color.TRANSPARENT);
                    ellipse.setStroke(Color.BLACK);
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
                        ellipse.setFill(Color.GREEN);

                        // Reset the color back to blue after a delay
                        new Thread(() -> {
                            try {
                                Thread.sleep(1500); // 1.5 seconds delay
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Platform.runLater(() -> ellipse.setFill(Color.TRANSPARENT));
                        }).start();
                    });
                }
                return null;
            }
        };
    }
}

package com.dsa_visualisation;

import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LinkedListController implements Initializable {

    private ExecutorService exec = Executors.newCachedThreadPool(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });

    private List<StackPane> listElements = new ArrayList<>();

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HBox listHBox = new HBox();
        listHBox.setAlignment(Pos.CENTER_LEFT);
        listHBox.setPadding(new Insets(10));

        // Insert operations
        TextField insertFrontField = new TextField();
        insertFrontField.setPromptText("Value");

        Button insertFrontButton = new Button("Insert Front");
        insertFrontField.setPrefWidth(insertFrontButton.getWidth());

        TextField insertEndField = new TextField();
        insertEndField.setPromptText("Value");
        Button insertEndButton = new Button("Insert End");
        insertEndField.setPrefWidth(insertEndButton.getWidth());

        TextField insertAtValueField = new TextField();
        insertAtValueField.setPromptText("Value");
        TextField insertAtPositionField = new TextField();
        insertAtPositionField.setPromptText("Position");
        Button insertAtButton = new Button("Insert At");
        insertAtValueField.setPrefWidth(insertAtButton.getWidth());
        insertAtPositionField.setPrefWidth(insertAtButton.getWidth());

        // Delete operations
        Button deleteFrontButton = new Button("Delete Front");
        Button deleteEndButton = new Button("Delete End");

        TextField deleteAtField = new TextField();
        deleteAtField.setPromptText("Position");
        Button deleteAtButton = new Button("Delete At");
        deleteAtField.setPrefWidth(deleteAtButton.getWidth());

        // Search operation
        TextField searchField = new TextField();
        searchField.setPromptText("Value");
        Button searchButton = new Button("Search");
        searchField.setPrefWidth(searchButton.getWidth());

        // Reset button
        Button resetButton = new Button("Reset");

        // Layout for insert operations
        VBox insertFrontBox = new VBox(5, insertFrontField, insertFrontButton);
        VBox insertEndBox = new VBox(5, insertEndField, insertEndButton);
        VBox insertAtBox = new VBox(5, insertAtValueField, insertAtPositionField, insertAtButton);
        HBox insertBox = new HBox(10, insertFrontBox, insertEndBox, insertAtBox);

        // Layout for delete operations
        VBox deleteAtBox = new VBox(5, deleteAtField, deleteAtButton);
        HBox deleteBox = new HBox(10, deleteFrontButton, deleteEndButton, deleteAtBox);

        // Layout for search operation
        VBox searchBox = new VBox(5, searchField, searchButton);

        // Combine all controls
        HBox controlsBox = new HBox(20, insertBox, deleteBox, searchBox, resetButton);
        controlsBox.setAlignment(Pos.CENTER);
        controlsBox.setPadding(new Insets(10));

        String styleSheet = Main.class.getResource("individualDSA/controlBox.css").toExternalForm();
        controlsBox.getStylesheets().add(styleSheet);
        borderPane.getStylesheets().add(styleSheet);

        insertFrontButton.setOnAction(e -> handleInsertFront(insertFrontField, listHBox, controlsBox));
        insertEndButton.setOnAction(e -> handleInsertEnd(insertEndField, listHBox, controlsBox));
        insertAtButton.setOnAction(e -> handleInsertAt(insertAtValueField, insertAtPositionField, listHBox, controlsBox));
        deleteFrontButton.setOnAction(e -> handleDeleteFront(listHBox, controlsBox));
        deleteEndButton.setOnAction(e -> handleDeleteEnd(listHBox, controlsBox));
        deleteAtButton.setOnAction(e -> handleDeleteAt(deleteAtField, listHBox, controlsBox));
        searchButton.setOnAction(e -> handleSearch(searchField, listHBox, controlsBox));
        resetButton.setOnAction(e -> handleReset(listHBox, controlsBox, insertFrontField, insertEndField, insertAtValueField, insertAtPositionField, deleteAtField, searchField));

        borderPane.setCenter(listHBox);
        borderPane.setBottom(controlsBox);
    }

    private void handleInsertFront(TextField field, HBox listHBox, HBox controlsBox) {
        int number = getInputNumber(field);
        if (number != -1) {
            Task<Void> task = createInsertAtBeginningTask(number, listHBox);
            executeTask(task, controlsBox);
        } else {
            showError("Invalid input for Insert Front.");
        }
    }

    private void handleInsertEnd(TextField field, HBox listHBox, HBox controlsBox) {
        int number = getInputNumber(field);
        if (number != -1) {
            Task<Void> task = createInsertAtEndTask(number, listHBox);
            executeTask(task, controlsBox);
        } else {
            showError("Invalid input for Insert End.");
        }
    }

    private void handleInsertAt(TextField valueField, TextField positionField, HBox listHBox, HBox controlsBox) {
        int number = getInputNumber(valueField);
        int position = getInputNumber(positionField);
        if (number != -1 && position != -1) {
            Task<Void> task = createInsertAtPositionTask(number, position, listHBox);
            executeTask(task, controlsBox);
        } else {
            showError("Invalid input for Insert At.");
        }
    }

    private void handleDeleteFront(HBox listHBox, HBox controlsBox) {
        Task<Void> task = createDeleteFromBeginningTask(listHBox);
        executeTask(task, controlsBox);
    }

    private void handleDeleteEnd(HBox listHBox, HBox controlsBox) {
        Task<Void> task = createDeleteFromEndTask(listHBox);
        executeTask(task, controlsBox);
    }

    private void handleDeleteAt(TextField field, HBox listHBox, HBox controlsBox) {
        int position = getInputNumber(field);
        if (position != -1) {
            Task<Void> task = createDeleteFromPositionTask(position, listHBox);
            executeTask(task, controlsBox);
        } else {
            showError("Invalid input for Delete At.");
        }
    }

    private void handleSearch(TextField field, HBox listHBox, HBox controlsBox) {
        int number = getInputNumber(field);
        if (number != -1) {
            Task<Void> task = createSearchTask(number, listHBox);
            executeTask(task, controlsBox);
        } else {
            showError("Invalid input for Search.");
        }
    }

    private void handleReset(HBox listHBox, HBox controlsBox, TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
        listHBox.getChildren().clear();
        listElements.clear();
    }

    private int getInputNumber(TextField field) {
        try {
            return Integer.parseInt(field.getText());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private void executeTask(Task<Void> task, HBox buttons) {
        buttons.setDisable(true);
        task.setOnSucceeded(event -> buttons.setDisable(false));
        task.setOnFailed(event -> {
            buttons.setDisable(false);
            showError("An error occurred while executing the task.");
        });
        exec.submit(task);
    }

    private Task<Void> createInsertAtBeginningTask(int number, HBox listHBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    StackPane newNode = createNode(number);
                    if (!listElements.isEmpty()) {
                        StackPane arrowPane = createArrow();
                        listHBox.getChildren().add(0, arrowPane);
                    }
                    listHBox.getChildren().add(0, newNode);
                    listElements.add(0, newNode);
                    animateNodeInsertion(newNode, -listHBox.getWidth(), 0);
                });
                return null;
            }
        };
    }

    private Task<Void> createInsertAtEndTask(int number, HBox listHBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    StackPane newNode = createNode(number);
                    if (!listElements.isEmpty()) {
                        StackPane arrowPane = createArrow();
                        listHBox.getChildren().add(arrowPane);
                    }
                    listHBox.getChildren().add(newNode);
                    listElements.add(newNode);
                    animateNodeInsertion(newNode, listHBox.getWidth(), 0);
                });
                return null;
            }
        };
    }

    private Task<Void> createInsertAtPositionTask(int number, int position, HBox listHBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    if (position < 0 || position > listElements.size()) {
                        showError("Invalid position for Insert At.");
                        return;
                    }
                    StackPane newNode = createNode(number);
                    if (!listElements.isEmpty()) {
                        StackPane arrowPane = createArrow();
                        listHBox.getChildren().add(position * 2, arrowPane);
                    }
                    listHBox.getChildren().add(position * 2, newNode);
                    listElements.add(position, newNode);
                    animateNodeInsertion(newNode, position * newNode.getWidth(), 0);
                });
                return null;
            }
        };
    }

    private Task<Void> createDeleteFromBeginningTask(HBox listHBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    if (!listElements.isEmpty()) {
                        listHBox.getChildren().remove(0);
                        listHBox.getChildren().remove(0);
                        listElements.remove(0);
                    }
                });
                return null;
            }
        };
    }

    private Task<Void> createDeleteFromEndTask(HBox listHBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    if (!listElements.isEmpty()) {
                        listHBox.getChildren().remove(listHBox.getChildren().size() - 1);
                        listHBox.getChildren().remove(listHBox.getChildren().size() - 1);
                        listElements.remove(listElements.size() - 1);
                    }
                });
                return null;
            }
        };
    }

    private Task<Void> createDeleteFromPositionTask(int position, HBox listHBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    if (position < 0 || position >= listElements.size()) {
                        showError("Invalid position for Delete At.");
                        return;
                    }
                    listHBox.getChildren().remove(position * 2);
                    if (position != listElements.size() - 1) {
                        listHBox.getChildren().remove(position * 2);
                    } else {
                        listHBox.getChildren().remove(position * 2 - 1);
                    }
                    listElements.remove(position);
                });
                return null;
            }
        };
    }

    private Task<Void> createSearchTask(int number, HBox listHBox) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    for (StackPane node : listElements) {
                        Text nodeText = (Text) node.getChildren().get(1);
                        int nodeValue = Integer.parseInt(nodeText.getText());
                        if (nodeValue == number) {
                            highlightNode(node);
                            break;
                        }
                    }
                });
                return null;
            }
        };
    }

    private void highlightNode(StackPane node) {
        FillTransition ft = new FillTransition(Duration.millis(500), (Circle) node.getChildren().get(0));
        ft.setFromValue(Color.WHITE);
        ft.setToValue(Color.YELLOW);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.play();
    }

    private void animateNodeInsertion(StackPane node, double fromX, double toX) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), node);
        tt.setFromX(fromX);
        tt.setToX(toX);
        tt.play();
    }

    private StackPane createNode(int value) {
        StackPane stackPane = new StackPane();
        Circle circle = new Circle(20, Color.WHITE);
        circle.setStroke(Color.BLACK);
        Text text = new Text(Integer.toString(value));
        stackPane.getChildren().addAll(circle, text);
        return stackPane;
    }

    private StackPane createArrow() {
        StackPane stackPane = new StackPane();
        Line line = new Line(0, 0, 50, 0);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(2);
        stackPane.getChildren().add(line);
        return stackPane;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

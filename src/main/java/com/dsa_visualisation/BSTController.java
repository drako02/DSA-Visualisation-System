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

public class BSTController implements Initializable {

    private ExecutorService exec = Executors.newCachedThreadPool(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });

    private TreeNode root;

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VBox treeVBox = new VBox();
        treeVBox.setAlignment(Pos.TOP_CENTER);
        treeVBox.setPadding(new Insets(10));

        TextField inputField = new TextField();
        inputField.setPromptText("Enter a number");
        inputField.setPrefWidth(100);

        Button insertButton = new Button("Insert");
        Button searchButton = new Button("Search");
        Button deleteButton = new Button("Delete");
        Button resetButton = new Button("Reset");

        HBox inputBox = new HBox(5, inputField, insertButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(5));

        HBox buttons = new HBox(5, searchButton, deleteButton, resetButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(5));

        HBox hbox = new HBox(20, inputBox, buttons);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10));

        insertButton.setOnAction(e -> {
            String inputText = inputField.getText();
            try {
                int number = Integer.parseInt(inputText);
                Task<Void> insertTask = createInsertTask(number, treeVBox);
                buttons.setDisable(true);
                insertTask.setOnSucceeded(event -> buttons.setDisable(false));
                exec.submit(insertTask);
                inputField.clear();
            } catch (NumberFormatException ex) {
                // Handle invalid input
            }
        });

        searchButton.setOnAction(e -> {
            String inputText = inputField.getText();
            try {
                int number = Integer.parseInt(inputText);
                Task<Void> searchTask = createSearchTask(number);
                buttons.setDisable(true);
                searchTask.setOnSucceeded(event -> buttons.setDisable(false));
                exec.submit(searchTask);
            } catch (NumberFormatException ex) {
                // Handle invalid input
            }
        });

        deleteButton.setOnAction(e -> {
            String inputText = inputField.getText();
            try {
                int number = Integer.parseInt(inputText);
                Task<Void> deleteTask = createDeleteTask(number, treeVBox);
                buttons.setDisable(true);
                deleteTask.setOnSucceeded(event -> buttons.setDisable(false));
                exec.submit(deleteTask);
                inputField.clear();
            } catch (NumberFormatException ex) {
                // Handle invalid input
            }
        });

        resetButton.setOnAction(e -> {
            inputField.clear();
            treeVBox.getChildren().clear();
            root = null;
        });

        borderPane.setCenter(treeVBox);
        borderPane.setBottom(hbox);
    }

    private Task<Void> createInsertTask(int number, VBox treeVBox) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                TreeNode newNode = new TreeNode(number);
                if (root == null) {
                    root = newNode;
                    Platform.runLater(() -> {
                        treeVBox.getChildren().add(newNode.getNode());
                    });
                } else {
                    insertNode(root, newNode, treeVBox, 0, 0, 100);
                }
                return null;
            }
        };
    }

    private void insertNode(TreeNode current, TreeNode newNode, VBox treeVBox, double parentX, double parentY, double offsetX) {
        if (newNode.value < current.value) {
            if (current.left == null) {
                current.left = newNode;
                Platform.runLater(() -> {
                    StackPane newNodePane = newNode.getNode();
                    newNodePane.setTranslateX(parentX - offsetX);
                    newNodePane.setTranslateY(parentY + 60);
                    treeVBox.getChildren().add(newNodePane);
                    Line line = new Line(parentX, parentY, parentX - offsetX, parentY + 60);
                    line.setStroke(Color.BLACK);
                    treeVBox.getChildren().add(line);
                });
            } else {
                insertNode(current.left, newNode, treeVBox, parentX - offsetX, parentY + 60, offsetX / 2);
            }
        } else {
            if (current.right == null) {
                current.right = newNode;
                Platform.runLater(() -> {
                    StackPane newNodePane = newNode.getNode();
                    newNodePane.setTranslateX(parentX + offsetX);
                    newNodePane.setTranslateY(parentY + 60);
                    treeVBox.getChildren().add(newNodePane);
                    Line line = new Line(parentX, parentY, parentX + offsetX, parentY + 60);
                    line.setStroke(Color.BLACK);
                    treeVBox.getChildren().add(line);
                });
            } else {
                insertNode(current.right, newNode, treeVBox, parentX + offsetX, parentY + 60, offsetX / 2);
            }
        }
    }

    private Task<Void> createSearchTask(int number) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                searchNode(root, number);
                return null;
            }
        };
    }

    private void searchNode(TreeNode current, int number) {
        if (current == null) {
            // Node not found
            return;
        }
        Platform.runLater(() -> current.circle.setFill(Color.YELLOW));
        if (number == current.value) {
            Platform.runLater(() -> current.circle.setFill(Color.GREEN));
        } else if (number < current.value) {
            searchNode(current.left, number);
        } else {
            searchNode(current.right, number);
        }
    }

    private Task<Void> createDeleteTask(int number, VBox treeVBox) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                root = deleteNode(root, number, treeVBox);
                return null;
            }
        };
    }

    private TreeNode deleteNode(TreeNode root, int number, VBox treeVBox) {
        if (root == null) {
            return null;
        }
        if (number < root.value) {
            root.left = deleteNode(root.left, number, treeVBox);
        } else if (number > root.value) {
            root.right = deleteNode(root.right, number, treeVBox);
        } else {
            if (root.left == null && root.right == null) {
                Platform.runLater(() -> treeVBox.getChildren().remove(root.getNode()));
                return null;
            } else if (root.left == null) {
                Platform.runLater(() -> {
                    treeVBox.getChildren().remove(root.getNode());
                    Line line = new Line(root.circle.getCenterX(), root.circle.getCenterY(),
                            root.right.circle.getCenterX(), root.right.circle.getCenterY());
                    treeVBox.getChildren().remove(line);
                });
                return root.right;
            } else if (root.right == null) {
                Platform.runLater(() -> {
                    treeVBox.getChildren().remove(root.getNode());
                    Line line = new Line(root.circle.getCenterX(), root.circle.getCenterY(),
                            root.left.circle.getCenterX(), root.left.circle.getCenterY());
                    treeVBox.getChildren().remove(line);
                });
                return root.left;
            } else {
                TreeNode minNode = findMin(root.right);
                root.value = minNode.value;
                root.right = deleteNode(root.right, minNode.value, treeVBox);
            }
        }
        return root;
    }

    private TreeNode findMin(TreeNode root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }
}

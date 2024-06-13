package com.dsa_visualisation;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class TreeNode {
    int value;
    TreeNode left, right;
    Circle circle;
    Text text;

    TreeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.circle = new Circle(20, Color.TRANSPARENT);
        this.circle.setStroke(Color.BLACK);
        this.text = new Text(String.valueOf(value));
    }

    StackPane getNode() {
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(circle, text);
        stackPane.setAlignment(Pos.CENTER);
        return stackPane;
    }
}

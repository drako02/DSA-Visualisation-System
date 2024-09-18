package com.dsa_visualisation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatAIController implements Initializable {
    @FXML
    private TextField userInput;

    @FXML
    private VBox chatArea;

    @FXML
    private Button sendButton;

    @FXML
    private ScrollPane scrollPane;

    // Replace with your actual FastAPI backend URL
    private final String API_URL = "http://ec2-13-60-196-157.eu-north-1.compute.amazonaws.com/chat";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setFitToWidth(true); // Optional: Allow ScrollPane to resize horizontally
        chatArea.setSpacing(15);
        userInput.setPromptText("Ask about Data Structures and Algorithms...");

        // Initial message from the system
        HBox systemTextArea = new HBox(10);
        systemTextArea.setAlignment(Pos.CENTER_LEFT);

        VBox system_Icon = new VBox();
        system_Icon.setAlignment(Pos.CENTER_LEFT);

        Image image_1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/dsa_visualisation/icons/systemIcon.png")));
        ImageView sys_Icon = new ImageView(image_1);

        sys_Icon.setFitWidth(30);
        sys_Icon.setFitHeight(30);

        system_Icon.getChildren().add(sys_Icon);

        Label systemText = new Label("What do you want to know about data structures and algorithms?\n");
        systemText.setStyle("-fx-background-color: rgba(255, 255, 255);");
        systemText.setWrapText(true);
        systemTextArea.getChildren().addAll(system_Icon, systemText);
        chatArea.getChildren().add(systemTextArea);

        // Set up the send button action
        sendButton.setOnAction(event -> {
            String question = userInput.getText();
            if (question.trim().isEmpty()) {
                return; // Don't send empty messages
            }

            Label userText = new Label( question + "\n");
            HBox userTextArea = new HBox(10);

            VBox userIcon = new VBox();
            userIcon.setAlignment(Pos.CENTER_RIGHT);


            Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/dsa_visualisation/icons/userIcon.png")));
            ImageView usIcon = new ImageView(image1);

            usIcon.setFitWidth(30);
            usIcon.setFitHeight(30);

            userIcon.getChildren().add(usIcon);

            userTextArea.setAlignment(Pos.CENTER_RIGHT);
            userText.setStyle("-fx-background-color: rgba(155, 142, 111, 0.5);");
            userText.setWrapText(true);
            userTextArea.getChildren().addAll(userText, userIcon);
            chatArea.getChildren().add(userTextArea);

            userTextArea.heightProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.doubleValue() <= 50) {
                    userIcon.setAlignment(Pos.CENTER_RIGHT);
                } else {
                    userIcon.setAlignment(Pos.TOP_RIGHT);
                }
            });

            // Task for handling the backend request
            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    return sendPromptToBackend(question);
                }
            };

            task.setOnSucceeded(e -> {
                String response = task.getValue();
                HBox responseTextArea = new HBox(10);
                responseTextArea.setAlignment(Pos.CENTER_LEFT);

                VBox systemIcon = new VBox();
                systemIcon.setAlignment(Pos.CENTER_LEFT);
//                systemIcon.setAlignment(Pos.TOP_LEFT);

                Image image2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/dsa_visualisation/icons/systemIcon.png")));
                ImageView sysIcon = new ImageView(image2);

                sysIcon.setFitWidth(30);
                sysIcon.setFitHeight(30);

                systemIcon.getChildren().add(sysIcon);

                Label responseText = new Label( response + "\n");
                responseText.setStyle("-fx-background-color: rgba(255, 255, 255);");
                responseText.setWrapText(true);
                responseTextArea.getChildren().addAll(systemIcon, responseText);
                chatArea.getChildren().add(responseTextArea);
                userInput.clear();

                responseTextArea.heightProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal.doubleValue() <= 50) {
                        systemIcon.setAlignment(Pos.CENTER_LEFT);
                    } else {
                        systemIcon.setAlignment(Pos.TOP_LEFT);
                        systemIcon.setPadding(new Insets(16,0 ,0 ,0));
                    }
                });
            });

            task.setOnFailed(e -> {
                HBox errorTextArea = new HBox();
                errorTextArea.setAlignment(Pos.CENTER_LEFT);
                Label errorText = new Label("Error: " + task.getException().getMessage() + "\n");
                errorText.setStyle("-fx-background-color: rgba(155, 142, 111, 0.5);");
                errorText.setWrapText(true);
                errorTextArea.getChildren().add(errorText);
                chatArea.getChildren().add(errorTextArea);
            });

            new Thread(task).start();
        });
    }

    private String sendPromptToBackend(String question) throws Exception {
        ObjectMapper objectMapper;
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            // Create an instance of ObjectMapper
            objectMapper = new ObjectMapper();

            // Create JSON payload
            String requestBody = "{\"query\": \"" + question + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        if (response.statusCode() == 200) {
            // Parse JSON response using Jackson
            JsonNode jsonResponse = objectMapper.readTree(response.body());
            return jsonResponse.get("response").asText();
        } else {
            throw new Exception("Error from backend: " + response.statusCode() + " " + response.body());
        }
    }

}
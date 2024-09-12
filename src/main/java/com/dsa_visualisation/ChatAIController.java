package com.dsa_visualisation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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
        chatArea.setSpacing(10);
        userInput.setPromptText("Ask about Data Structures and Algorithms...");

        sendButton.setOnAction(event -> {
            String question = userInput.getText();
            Label userText = new Label("You: " + question + "\n");
            HBox userTextArea = new HBox();
            userTextArea.setAlignment(Pos.CENTER_RIGHT);
            userText.setStyle("-fx-background-color: rgba(232, 213, 167, 0.5);");
            userText.setWrapText(true);
            userTextArea.getChildren().add(userText);
            chatArea.getChildren().add(userTextArea);

            // Task for handling the backend request
            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    return sendPromptToBackend(question);
                }
            };

            task.setOnSucceeded(e -> {
                String response = task.getValue();
                HBox responseTextArea = new HBox();
                responseTextArea.setAlignment(Pos.CENTER_LEFT);
                Label responseText = new Label("OpenAI (RAG): " + response + "\n");
                responseText.setStyle("-fx-background-color: rgba(155, 142, 111, 0.5);");
                responseText.setWrapText(true);
                responseTextArea.getChildren().add(responseText);
                chatArea.getChildren().add(responseTextArea);
                userInput.clear();
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
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper(); // Create an instance of ObjectMapper

        // Create JSON payload
        String requestBody = "{\"query\": \"" + question + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Parse JSON response using Jackson
            JsonNode jsonResponse = objectMapper.readTree(response.body());
            return jsonResponse.get("response").asText();
        } else {
            throw new Exception("Error from backend: " + response.statusCode() + " " + response.body());
        }
    }


}
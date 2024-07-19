package com.dsa_visualisation;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Arrays;
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

    OpenAiService openAiService = new OpenAiService("OPENAI_API_KEY"); //Put the actual api key here


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setFitToWidth(true); // Optional: Allow ScrollPane to resize horizontally


        chatArea.setSpacing(10);


        userInput.setPromptText("Ask about Data Structures and Algorithms...");

//        chatArea.setEditable(false);

        sendButton.setOnAction(event -> {
            String question = userInput.getText();
            Label userText = new Label("You: " + question + "\n");
            HBox userTextArea = new HBox();
            userTextArea.setAlignment(Pos.CENTER_LEFT);
//            userText.setStyle("-fx-fill: blue; -fx-font-weight: bold;");
            userText.setStyle("-fx-background-color: rgba(232, 213, 167, 0.5);");
            userText.setWrapText(true);
            userTextArea.getChildren().add(userText);
            chatArea.getChildren().add(userTextArea);

            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    return getOpenAiResponse(question);
                }
            };

            task.setOnSucceeded(e -> {
                String response = task.getValue();
                HBox responseTextArea = new HBox();
                responseTextArea.setAlignment(Pos.CENTER_RIGHT);
                Label responseText = new Label("OpenAI: " + response + "\n");
                responseText.setStyle("-fx-background-color: rgba(155, 142, 111, 0.5);");
                responseText.setWrapText(true);
                responseTextArea.getChildren().add(responseText);
                chatArea.getChildren().add(responseTextArea);
                userInput.clear();
            });

            task.setOnFailed(e -> {
                HBox errorTextArea = new HBox();
                errorTextArea.setAlignment(Pos.CENTER_RIGHT);
                Label errorText = new Label("Error: " + task.getException().getMessage() + "\n");

                errorText.setStyle("-fx-background-color: rgba(155, 142, 111, 0.5);");
                errorText.setWrapText(true);
                errorTextArea.getChildren().add(errorText);
                chatArea.getChildren().add(errorTextArea);
            });

            new Thread(task).start();
        });


    }

    private String getOpenAiResponse(String question) {
        try {
            ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), question);
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model("gpt-3.5-turbo")
                    .messages(Arrays.asList(userMessage))
                    .maxTokens(150)
                    .build();
            ChatMessage responseMessage = openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
            return responseMessage.getContent();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}


package com.dsa_visualisation;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ChatAIController implements Initializable {
    @FXML
    private TextField userInput;


    @FXML
    private Button sendButton;

    @FXML
    private ScrollPane scrollPane;

    OpenAiService openAiService = new OpenAiService("OPENAI_API_KEY"); //Put the actual api key here


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox chatArea = new VBox();
        scrollPane.setContent(chatArea);
        scrollPane.setOpacity(0.7);

        VBox userTextFlow = new VBox();
        VBox responseTextFlow = new VBox();
        userTextFlow.setAlignment(Pos.CENTER_LEFT);
        responseTextFlow.setAlignment(Pos.CENTER_LEFT);



        chatArea.prefWidthProperty().bind(scrollPane.widthProperty());
        chatArea.prefHeightProperty().bind(scrollPane.heightProperty());


        userInput.setPromptText("Ask about Data Structures and Algorithms...");

//        chatArea.setEditable(false);

        sendButton.setOnAction(event -> {
            String question = userInput.getText();
            Text userText = new Text("You: " + question + "\n");
            userText.setStyle("-fx-fill: black; -fx-font-size: 13px; -fx-font-family: Poppins; -fx-line-spacing: 4px;");

            userTextFlow.setAlignment(Pos.CENTER_LEFT);
            userTextFlow.setStyle("-fx-background-color: rgba(232, 213, 167, 0.2); -fx-alignment: center-left;");
            userTextFlow.getChildren().add(userText);
            chatArea.getChildren().add(userTextFlow);

            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    return getOpenAiResponse(question);
                }
            };

            task.setOnSucceeded(e -> {
                String response = task.getValue();
                Text responseText = new Text("OpenAI: " + response + "\n");
                responseText.setStyle("-fx-fill: black; -fx-font-size: 13px; -fx-font-family: Poppins; -fx-line-spacing: 4px; -fx-font-weight: 300");

                responseTextFlow.setStyle("-fx-background-color: rgba(52, 53, 206, 0.2); -fx-alignment: center-left;");
                responseTextFlow.getChildren().add(responseText);

                chatArea.getChildren().add(responseTextFlow);
                userInput.clear();
            });

            task.setOnFailed(e -> {
                Text errorText = new Text("Error: " + task.getException().getMessage() + "\n");
                errorText.setStyle("-fx-fill: red;");
                VBox errorTextFlow = new VBox(errorText);
                chatArea.getChildren().add(errorTextFlow);
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


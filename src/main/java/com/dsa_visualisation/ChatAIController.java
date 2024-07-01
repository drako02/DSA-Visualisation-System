package com.dsa_visualisation;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ChatAIController implements Initializable {
    @FXML
    private TextField userInput;

    @FXML
    private TextFlow chatArea;

    @FXML
    private Button sendButton;

    OpenAiService openAiService = new OpenAiService("OPENAI_API_KEY"); //Put the actual api key here


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        userInput.setPromptText("Ask about Data Structures and Algorithms...");

//        chatArea.setEditable(false);

        sendButton.setOnAction(event -> {
            String question = userInput.getText();
            Text userText = new Text("You: " + question + "\n");
            userText.setStyle("-fx-fill: blue; -fx-font-weight: bold;");
            chatArea.getChildren().add(userText);

            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    return getOpenAiResponse(question);
                }
            };

            task.setOnSucceeded(e -> {
                String response = task.getValue();
                Text responseText = new Text("OpenAI: " + response + "\n");
                responseText.setStyle("-fx-fill: green;");
                chatArea.getChildren().add(responseText);
                userInput.clear();
            });

            task.setOnFailed(e -> {
                Text errorText = new Text("Error: " + task.getException().getMessage() + "\n");
                errorText.setStyle("-fx-fill: red;");
                chatArea.getChildren().add(errorText);
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


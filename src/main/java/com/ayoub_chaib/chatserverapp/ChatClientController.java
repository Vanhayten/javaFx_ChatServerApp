package com.ayoub_chaib.chatserverapp;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatClientController {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField inputField;

    private ChatClient chatClient;

    // Called by ChatClient to provide a reference to itself.
    public void setChatClient(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @FXML
    private void handleSend() {
        String message = inputField.getText();
        if (!message.trim().isEmpty()) {
            // Send the message via the ChatClient instance.
            chatClient.sendMessage(message);
            inputField.clear();
        }
    }

    // Append a new message to the chat area.
    public void appendMessage(String message) {
        chatArea.appendText(message + "\n");
    }
}

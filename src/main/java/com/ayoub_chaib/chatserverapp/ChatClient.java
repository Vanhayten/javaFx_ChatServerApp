package com.ayoub_chaib.chatserverapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient extends Application {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String userName;
    private ChatClientController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML layout.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatClient.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.setChatClient(this);

        Scene scene = new Scene(root, 400, 350);
        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Prompt the user for a username.
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Username");
        dialog.setHeaderText("Enter your username:");
        dialog.setContentText("Username:");
        dialog.showAndWait().ifPresent(name -> userName = name);

        // Connect to the chat server in a new thread.
        new Thread(() -> {
            try {
                // Connect to server at localhost on port 12345.
                socket = new Socket("localhost", 12345);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                // Send the username as the first message.
                writer.println(userName);
                // Continuously read messages from the server.
                String message;
                while ((message = reader.readLine()) != null) {
                    String finalMessage = message;
                    Platform.runLater(() -> controller.appendMessage(finalMessage));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    // Called by the controller when the user sends a message.
    public void sendMessage(String message) {
        if (writer != null) {
            writer.println(message);
            // Show the self message immediately in the chat area.
            Platform.runLater(() -> controller.appendMessage("You: " + message));
        }
    }

    @Override
    public void stop() throws Exception {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.ayoub_chaib.chatserverapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String userName;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            // Initialize reader and writer for the socket
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            // The first message from the client is assumed to be the username.
            userName = reader.readLine();
            ChatServer.broadcast(userName + " has joined the chat", this);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        try {
            // Continuously listen for messages from this client.
            while ((message = reader.readLine()) != null) {
                System.out.println("Received from " + userName + ": " + message);
                ChatServer.broadcast(userName + ": " + message, this);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // On disconnect, close socket and remove client from the server.
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ChatServer.removeClient(this);
            ChatServer.broadcast(userName + " has left the chat", this);
        }
    }

    // Send a message to this client.
    public void sendMessage(String message) {
        writer.println(message);
    }
}

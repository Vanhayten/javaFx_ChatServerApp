package com.ayoub_chaib.chatserverapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private static final int PORT = 12345;
    // A thread-safe set to hold all connected client handlers.
    private static final Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        System.out.println("Chat server started on port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Accept new client connection
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);
                // Create a new ClientHandler for each connection
                ClientHandler handler = new ClientHandler(socket);
                clientHandlers.add(handler);
                // Run the handler in a new thread
                new Thread(handler).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Broadcast message to all clients except the sender.
    public static void broadcast(String message, ClientHandler excludeUser) {
        synchronized (clientHandlers) {
            for (ClientHandler client : clientHandlers) {
                if (client != excludeUser) {
                    client.sendMessage(message);
                }
            }
        }
    }

    // Remove a client when they disconnect.
    public static void removeClient(ClientHandler client) {
        clientHandlers.remove(client);
    }
}

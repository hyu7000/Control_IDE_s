package Control_IDEs;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

public class NetworkManager {
    private ServerSocket serverSocket;
    private boolean running = false;
    
    private CommandParser commandParser;
    
    public NetworkManager() {
    	this.commandParser = new CommandParser();
    }

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Server is running on port " + port + ".");

            new Thread(() -> {
                try {
                    while (running) {
                        System.out.println("Waiting for a client connection...");
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());
                        handleClient(clientSocket);
                    }
                } catch (IOException e) {
                    if (!serverSocket.isClosed()) {
                        System.out.println("Server error: " + e.getMessage());
                        e.printStackTrace();
                    }
                } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }).start();
        } catch (IOException e) {
            System.out.println("Could not listen on port " + port + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Server has been stopped.");
            } catch (IOException e) {
                System.out.println("Error closing server: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void handleClient(Socket clientSocket) throws Exception {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);
                out.println(inputLine);  // Echo back the received message
                System.out.println("Sent to client: " + inputLine);
                
                this.commandParser.parseCommand(inputLine, new ActionCallback() {
                    @Override
                    public void onSuccess() {
                        out.println("Command executed successfully");
                    }

                    @Override
                    public void onError(Exception e) {
                        String errorMessage = "Error processing command: " + e.getMessage();
                        out.println(errorMessage);
                        System.out.println("Sent to client: " + errorMessage);
                    }
                });

            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port or listening for a connection");
            System.out.println(e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client connection closed.");
            } catch (IOException e) {
                System.out.println("Could not close a socket: " + e.getMessage());
            }
        }
    }
}

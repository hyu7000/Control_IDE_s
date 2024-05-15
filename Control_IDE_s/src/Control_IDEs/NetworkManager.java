package Control_IDEs;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

public class NetworkManager {
    private ServerSocket serverSocket;
    private boolean running = false;
    
    private CommandParser commandParser;
    
    public NetworkManager() {
        this.commandParser = new CommandParser();
    }

    /*
     * Public Function
     */
    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Server is running on port " + port + ".");

            new Thread(this::acceptClients).start();
        } catch (IOException e) {
            handleError("Could not listen on port " + port + ": ", e);
        }
    }
    
    /*
     * Private Function
     */
    private void acceptClients() {
        try {
            while (running) {
                System.out.println("Waiting for a client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            if (!serverSocket.isClosed()) {
                handleError("Server error: ", e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
     * Public Function
     */
    public void stopServer() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Server has been stopped.");
            } catch (IOException e) {
                handleError("Error closing server: ", e);
            }
        }
    }
    
    /*
     * Private Function
     */
    private void handleClient(Socket clientSocket) throws Exception {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                processClientInput(inputLine, out);
            }
        } catch (IOException e) {
            handleClientError(clientSocket, e);
        } finally {
            closeClientSocket(clientSocket);
        }
    }

    private void processClientInput(String inputLine, PrintWriter out) {
        System.out.println("Received from client: " + inputLine);
        out.println(inputLine);  // Echo back the received message
        System.out.println("Sent to client: " + inputLine);

        commandParser.parseCommand(inputLine, new ActionCallback() {
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

            @Override
            public void DebugPoint(String msg) {
                try {
                    byte[] bytes = msg.getBytes("UTF-8");
                    String utf8_msg = new String(bytes, "UTF-8");
                    out.println("[Debug] " + utf8_msg);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace(); // 예외 정보를 출력하거나 적절한 예외 처리 로직을 구현
                }
            }
        });

        out.println("end");
    }

    private void handleClientError(Socket clientSocket, IOException e) {
        System.out.println("Exception caught when trying to listen on port or listening for a connection");
        System.out.println(e.getMessage());
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println(e);
        } catch (IOException ioException) {
            System.out.println("Failed to send the last message to the client: " + ioException.getMessage());
        }
    }

    private void closeClientSocket(Socket clientSocket) {
        try {
            clientSocket.close();
            System.out.println("Client connection closed.");
        } catch (IOException e) {
            System.out.println("Could not close a socket: " + e.getMessage());
        }
    }

    private void handleError(String message, Exception e) {
        System.out.println(message + e.getMessage());
        e.printStackTrace();
    }
}

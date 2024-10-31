package no.ntnu.idata2304.group2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * TCP server for the SmartTv.
 */
public class TvServer {
    public int portNumber;
    boolean isTcpServerRunning;
    private TvLogic logic;
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> connectedClients;

    /**
     * Create a new TvServer.
     * Uses default tcp port 1238 to listen for incoming connections.
     * @param logic TvLogic to use.
     */
    public TvServer(TvLogic logic) {
        this.logic = logic;
        isTcpServerRunning = false;
        portNumber = 1238;
        connectedClients = new ArrayList<>();
    }

    /**
     * Create a new TvServer with custom tcp port.
     * @param logic TvLogic to use.
     * @param port A TCP port to listen for incoming connections.
     */
    public TvServer(TvLogic logic, int port) {
        this.logic = logic;
        isTcpServerRunning = false;
        portNumber = port;
        connectedClients = new ArrayList<>();
    }

    /**
     * Starts the server.
     * Listens for incoming connections while running.
     * After a client connects it is passed to a new client handler.
     */
    public void startServer() {
        this.serverSocket = openListeningSocket();
        System.out.println("Server listening on port " + portNumber);

        while (isTcpServerRunning) {
            Socket clientSocket = acceptNextClientConnection();
            if (clientSocket != null) {
                System.out.println("New client connected from " + clientSocket.getRemoteSocketAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                connectedClients.add(clientHandler);
                clientHandler.start();
            }
        }
    }

    /**
     * Opens the listening socket.
     * If successful the server state is changed to running. isTcpServerRunning = true;
     * @return ServerSocket that listens for connections.
     */
    private ServerSocket openListeningSocket() {
        ServerSocket listeningSocket = null;
        try {
            listeningSocket = new ServerSocket(portNumber);
            isTcpServerRunning = true;
        } catch (IOException e) {
            System.err.println("Could not open server socket: " + e.getMessage());
        }
        return listeningSocket;
    }

    /**
     * Tries to accept incoming client connections.
     * @return An accepted socket connection.
     */
    private Socket acceptNextClientConnection() {
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();

        } catch (IOException e) {
            System.err.println("Could not accept client connection: " + e.getMessage());
        }
        return clientSocket;
    }

    /**
     * Get the TvLogic for the SmartTv.
     * @return TvLogic for the SmartTv.
     */
    public TvLogic getLogic() {
        return this.logic;
    }

    /**
     * Send a reponse to all clients.
     * @param response Response to send.
     */
    public void sendResponseToAllClients(String response) {
        for (ClientHandler c : connectedClients) {
            c.sendResponseToClient(response);
        }
    }

    /**
     * Remove a client from the list of connected clients.
     * @param client a client to remove.
     */
    public void disconnectClient(ClientHandler client) {
        this.connectedClients.remove(client);
    }
}



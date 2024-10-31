package no.ntnu.idata2304.group2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {
    private final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    public static final String CHANNEL_COUNT_COMMAND = "count";
    public static final String TURN_ON_COMMAND = "on";
    public static final String IS_ON = "?";
    public static final String TURN_OFF_COMMAND = "off";
    public static final String CURRENT_CHANNEL = "current";
    public static final String ONE_CHANNEL_UP = "up";
    public static final String ONE_CHANNEL_DOWN = "down";
    public static final String TV_OFF_RESPONSE = "The TV is off";

    private TvServer server;
    private Socket clientSocket;
    private BufferedReader socketReader;
    private PrintWriter socketWriter;

    /**
     * Constructor for a new client handler.
     * @param clientSocket An accepted socket connection from the server socket.
     * @param server The TvServer that accepted the socket.
     */
    public ClientHandler(Socket clientSocket, TvServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    /**
     * Starts running the client handler.
     */
    @Override
    public void run() {
        if (!initializeStreams()) {
            return;
        }
        System.out.println("Processing client on " + Thread.currentThread().getName());
        handleClient();
    }

    /**
     * Initializes the input and output streams from the client socket.
     * @return true if successful, false if failed.
     */
    private boolean initializeStreams() {
        boolean success = false;
        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            success = true;
        } catch (Exception e) {
            logger.log(Level.FINE, e.getMessage(), e);
        }
        return success;
    }

    /**
     * Handles client requests and server responses while the client is connected.
     */
    private void handleClient() {
        String response;
        do {
            String clientRequest = readClientRequest();
            System.out.println("Received from client: " + clientRequest);
            response = handleClientRequest(clientRequest);
            if (response != null) {
                sendResponseToClient(response);
            }
        } while (response != null);
        server.disconnectClient(this);
    }

    /**
     * Read one message from the TCP socket - from the client.
     *
     * @return The received client message, or null on error
     */
    private String readClientRequest() {
        String clientRequest = null;
        try {
            clientRequest = socketReader.readLine();
        } catch (IOException e) {
            System.err.println("Could not receive client request: " + e.getMessage());
        }
        return clientRequest;
    }

    /**
     * Handles a single client request.
     *
     * @param clientRequest a client request
     * @return a response
     */
    public String handleClientRequest(String clientRequest) {
        String response = null;

        switch (clientRequest) {
            case (CHANNEL_COUNT_COMMAND): {
                if (this.server.getLogic().isTvOn()) {
                    response = "Number of channels: " + this.server.getLogic().getNumberOfChannels();
                } else {
                    response = TV_OFF_RESPONSE;
                }
                break;
            }
            case (TURN_ON_COMMAND): {
                response = this.server.getLogic().turnOn() ? "Tv is turned on" : "";
                break;
            }
            case (TURN_OFF_COMMAND): {
                response = this.server.getLogic().turnOff() ? "" : "TV is turned off";
                break;
            }
            case (IS_ON): {
                response = this.server.getLogic().isTvOn() ? "The TV is on" : "The TV is off";
                break;
            }
            case (CURRENT_CHANNEL): {
                if (this.server.getLogic().isTvOn()) {
                    response = "Current channel: " + this.server.getLogic().getCurrentChannel();
                } else {
                    response = TV_OFF_RESPONSE;
                }
                break;
            }
            case (ONE_CHANNEL_UP): {
                if (this.server.getLogic().isTvOn()) {
                    if (this.server.getLogic().getCurrentChannel() != this.server.getLogic().getNumberOfChannels()) {
                        this.server.getLogic().setChannel(this.server.getLogic().getCurrentChannel() + 1);
                        response = "Channel changed.";
                        this.server.sendResponseToAllClients("Channel is set to: " + this.server.getLogic().getCurrentChannel());
                    } else {
                        response = "Not an available channel.";
                    }
                } else {
                    response = TV_OFF_RESPONSE;
                }
                break;
            }
            case (ONE_CHANNEL_DOWN): {
                if (this.server.getLogic().isTvOn()) {
                    if (this.server.getLogic().getCurrentChannel() != 1) {
                        this.server.getLogic().setChannel(this.server.getLogic().getCurrentChannel() - 1);
                        response = "Channel changed.";
                        this.server.sendResponseToAllClients("Channel is set to: " + this.server.getLogic().getCurrentChannel());
                    } else {
                        response = "Not an available channel.";
                    }
                } else {
                    response = TV_OFF_RESPONSE;
                }
                break;
            }
            default: {
                response = "Not a valid command. See protocol.md.";
            }
        }
        return response;
    }

    /**
     * Sends a response to the client
     * @param response Response from server.
     */
    public void sendResponseToClient(String response) {
        socketWriter.println(response);
    }
}

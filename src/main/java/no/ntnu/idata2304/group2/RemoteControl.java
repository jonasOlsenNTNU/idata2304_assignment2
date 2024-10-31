package no.ntnu.idata2304.group2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Remote control for a SmartTV - a TCP client.
 */
public class RemoteControl {
  private BufferedReader socketReader;
  private PrintWriter socketWriter;

  /**
   * Create and run a remote control client.
   * Default parameters: "localhost", 1238
   *
   * @param args (IP-address, TCP-port). Leave empty to use default
   */
  public static void main(String[] args) {
    if (args != null) {
      try {
        String address = Arrays.stream(args).iterator().next();
        int port = Integer.parseInt(Arrays.stream(args).iterator().next());
        RemoteControl remoteControl = new RemoteControl();
        remoteControl.run(address, port);
      } catch (Exception e) {
        throw new IllegalArgumentException("Illegal arguments");
      }
    } else {
      RemoteControl remoteControl = new RemoteControl();
      remoteControl.run("localhost", 1238);
    }
  }

  /**
   * Run the RemoteControl client
   * @param address ip address for a running SmartTv server
   * @param port Tcp port for a running SmartTV server
   */
  public void run(String address, int port) {
    try {
      printFunctionInfoToUser();
      Socket socket = new Socket(address, port);
      socketWriter = new PrintWriter(socket.getOutputStream(), true);
      socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      Scanner scanner = new Scanner(System.in);

      // Start a separate thread for receiving server responses.
      Thread responseThread = new Thread(this::receiveServerResponses);
      responseThread.start();

      // Read user input and send it to the server.

      boolean isRunning = true;
      while (isRunning) {
        String userInput = scanner.nextLine();
        if (userInput.toLowerCase() == "exit") {
          isRunning = false;
          System.out.println("Remote is disconnected.");
        } else {
          sendCommandToServer(userInput);
        }
      }

    } catch (IOException e) {
      System.err.println("Could not establish connection to the server: " + e.getMessage());
    }
  }

  /**
   * Sends a command to the connected SmartTv server
   * @param command See protocol.md for available commands
   */
  private void sendCommandToServer(String command){
      socketWriter.println(command);
  }

  /**
   * Start receiving server responses as long as the server is running.
   */
  private void receiveServerResponses() {
    try {
      boolean serverRunning = true;
      while (serverRunning) {
        String serverResponse = socketReader.readLine();
        if (serverResponse == null) {
          serverRunning = false;
        }
        System.out.println("Server's response: " + serverResponse);
      }
    } catch (IOException e) {
      System.err.println("Error while receiving server responses: " + e.getMessage());
    }
  }

  /**
   * Print available commands to the user in the CLI.
   */
  private void printFunctionInfoToUser(){
    System.out.println(
            """
            off     - to turn off tv
            on      - to turn on
            ?       - to check if tv is turned on
            count   - to get amount of channels
            current - to get current channel
            up      - to increase channel by 1
            down    - to decrease channel by 1
            exit    - to disconnect from the TV
            """);
  }
}

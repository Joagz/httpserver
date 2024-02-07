package github.joago.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import github.joago.config.EnvironmentConfig;
import github.joago.exceptions.BadHTTPObjectException;

public class Server {

  public static void main(String[] args) {
    listen(8000);
  }

  private static void listen(int port) {

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      while (!serverSocket.isClosed())
        acceptClientRequest(serverSocket.accept());
    } catch (IOException | BadHTTPObjectException e) {
      if (EnvironmentConfig.DEBUG)
        e.printStackTrace();

      listen(8000);
    }
  }

  private static void acceptClientRequest(Socket client) throws IOException, BadHTTPObjectException {

    byte[] buffer = new byte[32]; // Get input in buffers (in parts)
    StringBuilder sb = new StringBuilder();
    DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));

    while (dis.read(buffer) != -1 && dis.available() > 0) {
      sb.append(new String(buffer, StandardCharsets.US_ASCII));
    }

    if (EnvironmentConfig.DEBUG)
      System.out.println("Received HTTP Request: " + sb);

    Worker worker = new Worker(client, sb.toString());
    worker.start();

  }

}

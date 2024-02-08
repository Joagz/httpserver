package github.joago.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
    Worker worker = new Worker(client);
    new Thread(worker).start();
  }

}

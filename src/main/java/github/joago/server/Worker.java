package github.joago.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import github.joago.config.EnvironmentConfig;
import github.joago.exceptions.BadHTTPObjectException;
import github.joago.handlers.DocumentHandler;
import github.joago.http.HTTPRequest;

public class Worker extends Thread {

  private Socket client;
  private String request;

  public Worker(Socket client, String request) {
    this.client = client;
    this.request = request;
  }

  @Override
  public void run() {

    try {
      String message = DocumentHandler.generateResponse(HTTPRequest.toHttpRequest(request));
      send(message);
      client.close();
    } catch (BadHTTPObjectException e) {
      if (EnvironmentConfig.DEBUG)
        System.err.println(e);
    } catch (IOException e) {
      if (EnvironmentConfig.DEBUG)
        System.err.println(e);
    }
  }

  private void send(String message) {
    try (OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8)) {
      writer.write(message);
      writer.flush();
    } catch (IOException e) {
      if (EnvironmentConfig.DEBUG)
        System.err.println(e);
      return;
    }
  }

}

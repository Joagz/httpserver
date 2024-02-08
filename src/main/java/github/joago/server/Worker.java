package github.joago.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import github.joago.config.EnvironmentConfig;
import github.joago.exceptions.BadHTTPObjectException;
import github.joago.handlers.DocumentHandler;
import github.joago.http.HTTPRequest;
import github.joago.http.HTTPStatus;

public class Worker implements Runnable {

  private Socket client;

  public Worker(Socket client) {
    this.client = client;
  }

  @Override
  public void run() {

    try {
      byte[] buffer = new byte[64]; // Get input in buffers (in parts)
      StringBuilder sb = new StringBuilder();
      DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));

      while (dis.read(buffer) != -1 && dis.available() > 0) {
        sb.append(new String(buffer, StandardCharsets.UTF_8));
      }

      String request = sb.toString();

      if (EnvironmentConfig.DEBUG)
        System.out.println("Received HTTP Request: " + sb);

      String message = DocumentHandler.generateResponse(HTTPRequest.toHttpRequest(request));
      send(message);
      client.close();
    } catch (BadHTTPObjectException e) {
      if (EnvironmentConfig.DEBUG)
        System.err.println(e);
    } catch (IOException e) {
      if (EnvironmentConfig.DEBUG)
        System.err.println(e);
      DocumentHandler.createResponse(HTTPStatus.INTERNAL_ERROR, EnvironmentConfig.INTERNAL_SERVER_ERROR_URL);
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

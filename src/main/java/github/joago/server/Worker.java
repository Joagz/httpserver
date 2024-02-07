package github.joago.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

import github.joago.config.EnvironmentConfig;
import github.joago.exceptions.BadHTTPObjectException;
import github.joago.http.HTTPRequest;
import github.joago.http.HTTPResponse;
import github.joago.http.HTTPResponseHeaders;

public class Worker extends Thread {

  private Socket client;
  private String request;

  public Worker(Socket client, String request) {
    this.client = client;
    this.request = request;
  }

  @Override
  public void run() throws BadHTTPObjectException {
    try {
      try {
        boolean inBody = false;
        String[] lines = request.split("\n");
        String method = null, url = null;
        HashMap<String, Object> headers = new HashMap<>();
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {

          if (i == 0) {

            String string = lines[i];

            if (string.indexOf(":") != -1) {
              throw new BadHTTPObjectException("HTTP Request should contain start-line");
            }

            String[] parts = string.split(" ");
            method = parts[0];
            url = parts[1];
            continue;
          }

          else if (lines[i].isEmpty() || lines[i].isBlank()) {
            inBody = true;
            continue;
          }

          else if (inBody) {
            body.append(lines[i]);
            continue;
          }

          String string = lines[i].trim();
          String header = string.substring(0, string.indexOf(":"));
          String value = string.substring(string.indexOf(":") + 1, string.length());
          if (value.startsWith(" ")) {
            value = value.substring(1);
          }
          headers.put(header, value);

        }

        if (!inBody || method == null || url == null || headers.isEmpty())
          throw new BadHTTPObjectException("HTTP Request is malformed!");

        HTTPRequest httpRequest = new HTTPRequest(method, url, headers, body.toString());
        if (EnvironmentConfig.DEBUG)
          System.out.println(httpRequest);

        /* SEND MESSAGE TO CLIENT */
        HashMap<String, Object> resHeaders = new HashMap<>();

        resHeaders.put(HTTPResponseHeaders.CONTENT_TYPE.getHeader(), "text/html");
        resHeaders.put(HTTPResponseHeaders.CONTENT_LENGTH.getHeader(), "This is the body".length());
        resHeaders.put(HTTPResponseHeaders.CONTENT_LANGUAGE.getHeader(), "en-US");
        resHeaders.put(HTTPResponseHeaders.CONNECTION.getHeader(), "keep-alive");
        HTTPResponse httpResponse = new HTTPResponse("200 OK", resHeaders, "This is the body");
        System.out.println(httpResponse.toHttpMessage());
        send(httpResponse.toHttpMessage());

      } catch (BadHTTPObjectException e) {
        send(e.getMessage());
        if (EnvironmentConfig.DEBUG)
          System.out.println("Client closed due to " + e.getClass().getSimpleName() + "\nMessage: " + e.getMessage());
        client.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void send(String message) {
    try (OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream())) {
      writer.write(message);
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

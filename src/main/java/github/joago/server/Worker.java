package github.joago.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import github.joago.config.EnvironmentConfig;
import github.joago.document.DocumentHandler;
import github.joago.header.HeaderUtils;
import github.joago.http.HTTPRequest;
import github.joago.http.HTTPResponse;
import github.joago.http.HTTPStatus;
import github.joago.util.Commons;

public class Worker implements Runnable {

  private Socket client;

  public Worker(Socket client) {
    this.client = client;
  }

  @Override
  public void run() {

    try {

      String request = readRequest();

      if (request == null) {
        client.close();
      }

      HTTPResponse res = DocumentHandler.main(HTTPRequest.toHttpRequest(request));
      System.out.println(res);

      sendBytes("HTTP/1.1 " + res.statusCode(), HeaderUtils.hashMapToString(res.headers()), res.body());
      client.close();

    } catch (IOException e) {
      if (EnvironmentConfig.DEBUG)
        System.err.println(e);
      DocumentHandler.createResponse(HTTPStatus.INTERNAL_ERROR, EnvironmentConfig.INTERNAL_SERVER_ERROR_URL);
    }
  }

  private String readRequest() {
    byte[] buffer;
    try {
      buffer = new byte[client.getSendBufferSize() / (16 * 16)];

      StringBuilder sb = new StringBuilder();
      DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));

      while (dis.read(buffer) != -1 && dis.available() > 0) {
        sb.append(new String(buffer, StandardCharsets.UTF_8));
      }

      String request = sb.toString();

      if (EnvironmentConfig.DEBUG)
        System.out.println("Received HTTP Request: " + sb);

      return request;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Deprecated
  private void sendUTF(String message) {
    char[] buffer = new char[calculateBufferSize(message.length(), Math.round((float) Math.pow(64, 2)))];
    try (OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8)) {
      BufferedReader reader = new BufferedReader(new StringReader(message));
      while (reader.read(buffer) != -1) {
        writer.write(buffer);
      }
      writer.flush();
    } catch (IOException e) {
      if (EnvironmentConfig.DEBUG)
        System.err.println(e);
      return;
    }
  }

  /*
   * Handle file and image upload
   */
  private void sendBytes(String firstLine, String headers, Object obj) {
    try (OutputStream outputStream = client.getOutputStream()) {

      String response = firstLine + System.lineSeparator() + headers + System.lineSeparator();
      outputStream.write(response.getBytes(StandardCharsets.UTF_8));

      try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        out.write((byte[]) obj);
        outputStream.write(out.toByteArray());
      }

      outputStream.flush();
    } catch (IOException e) {
      if (EnvironmentConfig.DEBUG) {
        System.err.println(e);
      }
    }
  }

  int calculateBufferSize(int dataSize, int defaultBufferSize) {
    double scaleFactor = 1.5;
    // Scale until 4096.
    int bufferSize = (int) Math.min(defaultBufferSize, dataSize * scaleFactor);
    return bufferSize;
  }

}

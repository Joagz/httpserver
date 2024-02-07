package github.joago.http;

import java.util.HashMap;

public record HTTPResponse(String statusCode, HashMap<String, Object> headers, String body) {
  public String toHttpMessage() {

    StringBuilder sb = new StringBuilder();
    sb.append(statusCode + " HTTP/1.1" + System.lineSeparator());
    headers.forEach((k, v) -> {
      sb.append(k + ": " + v + System.lineSeparator());
    });
    sb.append(System.lineSeparator());
    sb.append(body);
    return sb.toString();
  }
}

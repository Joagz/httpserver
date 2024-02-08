package github.joago.http;

import java.util.HashMap;

public record HTTPResponse(String statusCode, HashMap<String, String> headers, String body) {

  /*
   * After creating an http response object, transform it into a valid http response string.
   */

  public String toHttpResponse() {
    StringBuilder sb = new StringBuilder();
    sb.append("HTTP/1.1 " + statusCode + System.lineSeparator());
    headers.forEach((k, v) -> {
      sb.append(k + ": " + v + System.lineSeparator());
    });
    sb.append(System.lineSeparator());
    sb.append(body);
    sb.trimToSize();
    return sb.toString().trim();
  }
}

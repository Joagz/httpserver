package github.joago.http;

import java.util.HashMap;

import github.joago.config.EnvironmentConfig;
import github.joago.exceptions.BadHTTPObjectException;

public record HTTPRequest(String method, String url, HashMap<String, Object> headers, String body) {

  /*
   * Transform incoming buffer to an http request.
   */

  public static HTTPRequest toHttpRequest(String request) throws BadHTTPObjectException {
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

      try {
        String string = lines[i].trim();
        String header = string.substring(0, string.indexOf(":"));
        String value = string.substring(string.indexOf(":") + 1, string.length());
        if (value.startsWith(" ")) {
          value = value.substring(1);
        }
        headers.put(header, value);
      } catch (java.lang.StringIndexOutOfBoundsException e) {
        continue;
      }

    }

    if (method == null || url == null || headers.isEmpty())
      throw new BadHTTPObjectException("HTTP Request is malformed!");

    HTTPRequest httpRequest = new HTTPRequest(method, url, headers, body.toString());

    if (EnvironmentConfig.DEBUG)
      System.out.println(httpRequest);

    return httpRequest;
  }

}

package github.joago.handlers;

import github.joago.config.EnvironmentConfig;
import github.joago.config.FileConfiguration;
import github.joago.header.HeadersFactory;
import github.joago.http.HTTPRequest;
import github.joago.http.HTTPRequestHeaders;
import github.joago.http.HTTPResponse;
import github.joago.http.HTTPStatus;
import github.joago.mapping.Mapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DocumentHandler {

  private static final File mainDirectory = new File(
      new FileConfiguration().documents);

  public DocumentHandler() {
  }

  /*
   * HOST("Host"),
   * ACCEPT("Accept"),
   * ACCEPT_CHARSET("Accept-Charset"),
   * ACCEPT_ENCODING("Accept-Encoding"),
   * ACCEPT_LANGUAGE("Accept-Language"),
   * AUTHORIZATION("Authorization"),
   * CONNECTION("Connection"),
   * DATE("Date"),
   * COOKIE("Cookie"),
   * ORIGIN("Origin"),
   * X_CSRF_TOKEN("X-Csrf-Token"),
   * USER_AGENT("");
   */

  public synchronized static String generateResponse(HTTPRequest request) throws NullPointerException, IOException {

    String url = (String) request.url();
    String method = request.method();

    return respond(url, method);
  }

  private static String makeDefaultHeaders(String body) {
    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    String formattedDate = sdf.format(new Date());

    try {
      return "Date:" + formattedDate.toString() + "//Server:Joaquin/1.0//Content-Length:"
          + (body.getBytes("UTF-8").length - 1)
          + "//Content-Type:text/html; charset=UTF-8//Connection:close";
    } catch (UnsupportedEncodingException e) {
      if (EnvironmentConfig.DEBUG)
        e.printStackTrace();
    }
    return "";
  }

  /* Make response */
  private static String respond(String url, String method) throws NullPointerException, IOException {

    if (url == null || method == null) {
      String body = "Bad Request!";
      HTTPResponse response = new HTTPResponse(HTTPStatus.BAD_REQUEST.getStatus(),
          HeadersFactory.barSeparatedResponseHeaderBuilder(
              makeDefaultHeaders(body)),
          body);
      return response.toHttpResponse();
    }

    try {
      return createResponse(HTTPStatus.OK, url);
    } catch (NullPointerException e) {
      return createResponse(HTTPStatus.NOT_FOUND, EnvironmentConfig.NOT_FOUND_URL);
    }
  }

  private static String makeBody(String url) throws IOException, NullPointerException {

    if (url.endsWith("/")) {
      url = url.substring(0, url.length() - 1);
    }
    File file;
    file = new File(mainDirectory.getPath() + url);
    if (file.isDirectory()) {
      file = new File(mainDirectory.getPath() + url + "/index.html");
    } else {
      file = new File(mainDirectory.getPath() + url + ".html");
    }
    return writeIntoResponse(file);

  }

  /* Generate request body */
  private static String writeIntoResponse(File file) {
    try (BufferedReader reader = new BufferedReader(new FileReader(file, Charset.forName("UTF-8")))) {
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(new String(line)).append("\n");
      }

      return sb.toString();
    } catch (IOException e) {
      if (EnvironmentConfig.DEBUG)
        e.printStackTrace();
    }
    return null;
  }

  public static String createResponse(HTTPStatus status, String bodyUrl) {
    String body;
    try {
      body = makeBody(bodyUrl);
      HTTPResponse response = new HTTPResponse(status.getStatus(),
          HeadersFactory.barSeparatedResponseHeaderBuilder(
              makeDefaultHeaders(body)),
          body);
      return response.toHttpResponse();

    } catch (NullPointerException | IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}

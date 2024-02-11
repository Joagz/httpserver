package github.joago.document;

import github.joago.config.EnvironmentConfig;
import github.joago.config.FileConfiguration;
import github.joago.header.HeaderUtils;
import github.joago.http.ContentType;
import github.joago.http.FileExtension;
import github.joago.http.HTTPRequest;
import github.joago.http.HTTPResponse;
import github.joago.http.HTTPStatus;
import github.joago.util.Commons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.nio.charset.StandardCharsets;

public class DocumentHandler {

  private static final File mainDirectory = new File(new FileConfiguration().documents);

  public DocumentHandler() {
    if (mainDirectory == null) {
      System.exit(-1);
    }
  }

  private static String insertHeaders(long bodySize, String fileName) {
    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    String formattedDate = sdf.format(new Date());
    String contentType = ContentType.TEXT_PLAIN.getValue();

    StringBuilder sb = new StringBuilder();

    sb.append("Date:" + formattedDate.toString() + "//Server:" + EnvironmentConfig.SERVER_HEADER + "//Content-Length:"
        + (bodySize - 1));

    if (fileName == null) {
      sb.append("//Content-Type:" + contentType);
      sb.append("//Connection:close");
      return sb.toString();
    }

    contentType = HeaderUtils.determineHeaderForFile(fileName);
    sb.append("//Content-Type:" + contentType);
    sb.append("//Connection:keep-alive");

    return sb.toString();
  }

  private static String insertHeaders(int bodySize, String fileName, String attachment) {
    StringBuilder headers = new StringBuilder(insertHeaders(bodySize, fileName));
    headers.append("//Content-Disposition: attachment; filename=\"" + attachment + "\"");
    return headers.toString();
  }

  public synchronized static HTTPResponse main(HTTPRequest request) {

    String url = new String(request.url()).replace("%20", " ");
    String method = request.method();

    return respond(url, method);
  }

  /* Make response */
  private static HTTPResponse respond(String url, String method) {

    try {
      if (url == null || method == null) {
        createResponse(HTTPStatus.BAD_REQUEST, EnvironmentConfig.INTERNAL_SERVER_ERROR_URL);
      }

      return createResponse(HTTPStatus.OK, url);
    } catch (NullPointerException e) {
      return createResponse(HTTPStatus.NOT_FOUND, EnvironmentConfig.NOT_FOUND_URL);
    }
  }

  private static Document makeBody(String url) throws IOException, NullPointerException {

    if (url.endsWith("/")) {
      url = url.substring(0, url.length());
    }

    Document document;

    File file = getFile(url);
    Path path = Paths.get(file.getPath());

    document = new Document(file.getName(), Files.readAllBytes(path),
        Files.size(path));
    return document;
  }

  private static File getFile(String url) {
    File file = new File(mainDirectory.getPath() + url);
    String name = file.getName();

    if (!name.contains(FileExtension.html.toString()) && name.matches("^[A-Za-z0-9]+\\.[A-Za-z0-9]+$")
        || name.matches("^([A-Za-z0-9]+( [A-Za-z0-9]+)+)\\.[A-Za-z0-9]+$")) {
      file = new File(mainDirectory.getPath() + url);
    } else {
      if (file.isDirectory()) {
        file = new File(mainDirectory.getPath() + url + "/index.html");
      } else {
        file = new File(mainDirectory.getPath() + url + ".html");
      }
    }

    return file;
  }

  /* Generate request body */
  @Deprecated
  private static String writeBody(File file) {

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

  public static HTTPResponse createResponse(HTTPStatus status, String bodyUrl) throws NullPointerException {
    Document body;

    try {
      body = makeBody(bodyUrl);

      HTTPResponse response = new HTTPResponse(status.getStatus(),
          HeaderUtils.barSeparatedResponseHeaderBuilder(
              insertHeaders(body.length() + 1, body.name())),
          body.content());
      return response;

    } catch (

    IOException e) {
      e.printStackTrace();
    }

    return null;
  }

}

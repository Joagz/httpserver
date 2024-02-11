package github.joago.header;

import java.util.HashMap;

import github.joago.http.ContentType;
import github.joago.http.FileExtension;
import github.joago.util.Commons;

public class HeaderUtils {

  public static String determineHeaderForFile(String fileName) {

    if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
      return ContentType.TEXT_HTML_UTF_8.getValue();
    } else if (fileName.endsWith(".js")) {
      return ContentType.TEXT_JS.getValue();
    } else if (fileName.endsWith(".png")) {
      return ContentType.IMAGE_PNG.getValue();
    } else if (fileName.endsWith(".css")) {
      return ContentType.TEXT_CSS.getValue();
    } else if (fileName.endsWith(".ico")) {
      return ContentType.ICON.getValue();
    } else if (fileName.matches("/(?i:^.*\\.(jpg|jpeg|gif|bmp|tiff|psd|raw|cr2|nef|orf|sr2)$)/gm\r\n")) {
      return ContentType.IMAGE.getValue();
    } else if (fileName.matches("/(?i:^.*\\.(mp4|avi|wmv|mov|flv|mkv|webm|mpeg|mpg)$)/gm\r\n")) {
      return ContentType.VIDEO.getValue();
    } else if (fileName.endsWith(".pdf")) {
      return ContentType.APPLICATION_PDF.getValue();
    } else if (fileName.endsWith(".json")) {
      return ContentType.APPLICATION_JSON.getValue();
    }

    return ContentType.TEXT_PLAIN.getValue();
  }

  public static String hashMapToString(HashMap<String, String> headers) {
    StringBuilder sb = new StringBuilder();

    headers.forEach((k, v) -> {
      sb.append(k + ": " + v + System.lineSeparator());
    });
    return sb.toString();
  }

  /*
   * transform a bar separated string into a hashmap with headers: key,
   * header-values: values
   *
   * i.e: Transforms the following string to a hashmap
   *
   * "Content-Length:200//Content-Type:application/json//Server:MyServer/1.0"
   */

  public static HashMap<String, String> barSeparatedResponseHeaderBuilder(String list) {
    String[] parts = list.split("//");
    HashMap<String, String> headers = new HashMap<>();
    for (String string : parts) {
      String p1 = string.substring(0, string.indexOf(":"));
      String p2 = string.substring(string.indexOf(":") + 1, string.length());
      headers.put(p1, p2);
    }
    return headers;
  }

}

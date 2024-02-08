package github.joago.header;

import java.util.HashMap;

public class HeadersFactory {

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

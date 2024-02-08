package github.joago.header;

import java.util.HashMap;

public class HeadersFactory {

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

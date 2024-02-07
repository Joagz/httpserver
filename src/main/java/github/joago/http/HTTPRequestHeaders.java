package github.joago.http;

public enum HTTPRequestHeaders {

  HOST("Host"),
  ACCEPT("Accept"),
  ACCEPT_CHARSET("Accept-Charset"),
  ACCEPT_ENCODING("Accept-Encoding"),
  ACCEPT_LANGUAGE("Accept-Language"),
  AUTHORIZATION("Authorization"),
  CONNECTION("Connection"),
  DATE("Date"),
  COOKIE("Cookie"),
  ORIGIN("Origin"),
  X_CSRF_TOKEN("X-Csrf-Token"),
  USER_AGENT("");

  String header = null;

  HTTPRequestHeaders(String header) {
    this.header = header;
  }

  String getValue() {
    return this.header;
  }

}

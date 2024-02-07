package github.joago.http;

public enum HTTPResponseHeaders {

  CONTENT_LENGTH("Content-Length"),
  CONTENT_TYPE("Content-Type"),
  CONTENT_ENCODING("Content-Encoding"),
  CONTENT_LANGUAGE("Content-Language"),
  CONTENT_DISPOSITION("Content-Disposition"),
  CONTENT_LOCATION("Content-Location"),
  EXPIRES("Expires"),
  CONNECTION("Connection"),
  SET_COOKIE("Set-Cookie"),
  WWW_AUTHENTICATE("WWW-Authenticate"),
  CACHE_CONTROL("Cache-Control"),
  ALLOW("Allow");

  String header = null;

  HTTPResponseHeaders(String value) {
    this.header = (value);
  }

  public String getHeader() {
    return this.header;
  }

}

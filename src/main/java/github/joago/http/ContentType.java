package github.joago.http;

public enum ContentType {

  IMAGE("image/*"),
  APPLICATION_JSON("application/json"),
  APPLICATION_PDF("application/pdf"),
  ICON("image/x-icon"),
  IMAGE_JPEG("image/jpeg"),
  IMAGE_GIF("image/gif"),
  IMAGE_PNG("image/png"),
  MULTIPART_FORM_DATA("multipart/form-data"),
  TEXT_HTML_UTF_8("text/html; charset=UTF-8"),
  TEXT_PLAIN("text/plain"),
  TEXT_JS("text/javascript"),
  TEXT_HTML("text/html"),
  TEXT_CSS("text/css"),
  VIDEO("video/*");

  private String value = null;

  ContentType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}

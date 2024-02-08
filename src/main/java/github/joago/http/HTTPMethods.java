package github.joago.http;

enum HTTPMethods {
  GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE"), HEAD("HEAD");

  private String value = null;

  String getValue() {
    return this.value;
  }

  HTTPMethods(String value) {
    this.value = value;
  }
}

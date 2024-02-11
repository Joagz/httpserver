package github.joago.http;

public enum HTTPStatus {
  OK("200 OK"), NOT_FOUND("404 NOT FOUND"), BAD_REQUEST("400 BAD REQUEST"), INTERNAL_ERROR("500 INTERNAL SERVER ERROR");

  private String status = null;

  public String getStatus() {
    return status;
  }

  HTTPStatus(String value) {
    this.status = value;
  }

}

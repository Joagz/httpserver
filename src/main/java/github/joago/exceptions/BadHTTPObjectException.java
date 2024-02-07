package github.joago.exceptions;

public class BadHTTPObjectException extends RuntimeException {

  private String message;

  public BadHTTPObjectException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return (this.message);
  }

}

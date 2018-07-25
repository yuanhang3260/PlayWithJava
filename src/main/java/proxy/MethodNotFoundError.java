package proxy;

public class MethodNotFoundError extends RuntimeException {
  public MethodNotFoundError(String msg) {
    super(msg);
  }
}

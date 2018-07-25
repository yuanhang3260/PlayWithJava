package proxy;

public class ClassNotFoundError extends RuntimeException {
  public ClassNotFoundError(String msg) {
    super(msg);
  }
}

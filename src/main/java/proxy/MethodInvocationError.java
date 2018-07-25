package proxy;

public class MethodInvocationError extends RuntimeException {
  public MethodInvocationError(String msg) {
    super(msg);
  }
}

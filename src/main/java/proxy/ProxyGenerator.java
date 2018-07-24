package proxy;

public abstract class ProxyGenerator {
  protected static final String PACKAGE_NAME = "hyproxygen";

  public abstract Class<?> generateProxyClass(Class<?>[] interfaces);

  protected abstract String proxyClassNamePrefix();
}

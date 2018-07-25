package proxy;

public class ExampleProxy implements proxy.Test.If1, proxy.Test.If2 {
  private static java.util.HashMap<String, java.lang.reflect.Method> proxyMethods;

  private proxy.InvocationHandler handler;

  public ExampleProxy(proxy.InvocationHandler handler) {
    this.handler = handler;
  }
  
  static {
    proxyMethods = new java.util.HashMap<String, java.lang.reflect.Method>();
    try {
      Class<?> clazz;
      clazz = Class.forName("proxy.Test$If1");
      for (java.lang.reflect.Method method : clazz.getMethods()) {
        proxyMethods.put(method.toString(), method);
      }
    } catch (ClassNotFoundException e) {
      throw new proxy.ClassNotFoundError(e.getMessage());
    }
    try {
      Class<?> clazz;
      clazz = Class.forName("proxy.Test$If2");
      for (java.lang.reflect.Method method : clazz.getMethods()) {
        proxyMethods.put(method.toString(), method);
      }
    } catch (ClassNotFoundException e) {
      throw new proxy.ClassNotFoundError(e.getMessage());
    }

  }

  public void sayHello() {
    java.lang.reflect.Method m = proxyMethods.get("public abstract void proxy.Test$If1.sayHello()");
    try {
      Object re = handler.invoke(this, m, new Object[] {});
      return;
    } catch (Exception e) {
      throw new proxy.MethodInvocationError(e.getMessage());
    }
  }
  public long foo(java.lang.String arg0) {
    java.lang.reflect.Method m = proxyMethods.get("public abstract long proxy.Test$If1.foo(java.lang.String)");
    try {
      Object re = handler.invoke(this, m, new Object[] {arg0});
      return (Long)re;
    } catch (Exception e) {
      throw new proxy.MethodInvocationError(e.getMessage());
    }
  }
  public java.lang.String bar(java.lang.String arg0, int arg1) {
    java.lang.reflect.Method m = proxyMethods.get("public abstract java.lang.String proxy.Test$If2.bar(java.lang.String,int)");
    try {
      Object re = handler.invoke(this, m, new Object[] {arg0, arg1});
      return (java.lang.String)re;
    } catch (Exception e) {
      throw new proxy.MethodInvocationError(e.getMessage());
    }
  }

}

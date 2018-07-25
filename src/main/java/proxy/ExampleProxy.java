package proxy;

public class ExampleProxy implements proxy.Test.If1, proxy.Test.If2 {
  private static java.util.HashMap<String, java.lang.reflect.Method> proxyMethods;

  private proxy.InvocationHandler handler;

  static {
    proxyMethods = new java.util.HashMap<String, java.lang.reflect.Method>();
    Class<?> clazz;
    try {
      clazz = Class.forName("proxy.Test$If1");
      for (java.lang.reflect.Method method : clazz.getMethods()) {
        proxyMethods.put(method.toString(), method);
      }

      clazz = Class.forName("proxy.Test$If2");
      for (java.lang.reflect.Method method : clazz.getMethods()) {
        proxyMethods.put(method.toString(), method);
      }
    } catch (ClassNotFoundException e) {
      throw new proxy.ClassNotFoundError(e.getMessage());
    }
  }

  public ExampleProxy(proxy.InvocationHandler handler) {
    this.handler = handler;
  }

  public void sayHello() {
    java.lang.reflect.Method m = proxyMethods.get("public abstract void proxy.Test$If1.sayHello()");
    try {
      Object re = handler.invoke(this, m, new Object[] {});
      return;
    } catch (Exception e) {
      e.printStackTrace();
      
    }
  }
  public void foo(java.lang.String arg0) {
    java.lang.reflect.Method m = proxyMethods.get("public abstract void proxy.Test$If1.foo(java.lang.String)");
    try {
      Object re = handler.invoke(this, m, new Object[] {arg0});
      return;
    } catch (Exception e) {
      e.printStackTrace();
      
    }
  }
  public java.lang.String bar(java.lang.String arg0, java.lang.Integer arg1) {
    java.lang.reflect.Method m = proxyMethods.get("public abstract java.lang.String proxy.Test$If2.bar(java.lang.String,java.lang.Integer)");
    try {
      Object re = handler.invoke(this, m, new Object[] {arg0, arg1});
      return (java.lang.String)re;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}

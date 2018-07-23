package asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import asm.InvocationHandler;
import asm.ProxyGenerator;

public class Test {
  public static interface If1 {
    void sayHello();
    void foo(String str);
  };

  public static interface If2 {
    String bar(String str, Integer num);
  }

  public static class Original implements If1, If2 {
    @Override
    public void sayHello() {
      System.out.println("hello");
    }

    @Override
    public void foo(String str) {
      System.out.println(str);
    }

    @Override
    public String bar(String str, Integer num) {
      return str + "-" + String.valueOf(num);
    }
  }

  public static class Handler implements InvocationHandler {
    private final Object obj;

    public Handler(Object obj) {
      this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      System.out.println("=== BEFORE ===");
      method.invoke(obj, args);
      System.out.println("=== AFTER ===");
      return null;
    }
  }

  public static void main(String[] args) throws Exception {
    // If1 instance = (If1)(new Original());
    // instance.sayHello();

    Class<?> proxyClass = ProxyGenerator.generateProxyClass(new Class<?>[] {If1.class, If2.class});
    Constructor constructor = proxyClass.getConstructor(InvocationHandler.class);

    If1 instance = (If1)constructor.newInstance(new Handler(new Original()));
    instance.sayHello();
  }
}

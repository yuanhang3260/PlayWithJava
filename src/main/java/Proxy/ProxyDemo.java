package Proxy;

import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyDemo {
  interface If {
    void originalMethod(String s);
  }

  interface If2 {
    void originalMethod2(String s);
  }

  static class Original implements If, If2 {
    public void originalMethod(String s) {
      System.out.println("originalMethod: " + s);
      System.out.println(s);
    }

    public void originalMethod2(String s) {
      System.out.println("originalMethod2: " + s);
    }
  }

  static class Wrapper implements InvocationHandler {
    private final If original;

    public Wrapper(If original) {
      this.original = original;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws IllegalAccessException, IllegalArgumentException,
               InvocationTargetException {
      System.out.println("BEFORE");
      method.invoke(original, args);
      System.out.println("AFTER");
      return null;
    }
  }

  public static void main(String[] args){
    System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

    Original original = new Original();
    Wrapper wrapper = new Wrapper(original);
    If2 f = (If2)Proxy.newProxyInstance(If.class.getClassLoader(),
                                        new Class[] {If.class, If2.class},
                                        wrapper);
    f.originalMethod2("Hello");
  }
}

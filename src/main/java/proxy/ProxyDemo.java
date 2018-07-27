package proxy;

import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// This is a demo for standard Java proxy usage.
public class ProxyDemo {
  interface If1 {
    Integer originalMethod(String s);
  }

  interface If2 {
    void originalMethod2(String s);
  }

  static class Original implements If1, If2 {
    public Integer originalMethod(String s) {
      System.out.println("originalMethod: " + s);
      System.out.println(s);
      return 1;
    }

    public void originalMethod2(String s) {
      System.out.println("originalMethod2: " + s);
    }
  }

  static class Wrapper implements InvocationHandler {
    private final Original original;

    public Wrapper(Original original) {
      this.original = original;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws IllegalAccessException, IllegalArgumentException,
               InvocationTargetException {
      System.out.println("BEFORE");
      System.out.println(method.toString());
      Object re = method.invoke(original, args);
      System.out.println("AFTER");
      return re;
    }
  }

  public static void main(String[] args){
    System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

    Original original = new Original();
    Wrapper wrapper = new Wrapper(original);
    If1 f = (If1)Proxy.newProxyInstance(If1.class.getClassLoader(),
                                        new Class[] {If1.class, If2.class},
                                        wrapper);
    f.originalMethod("Hello");
  }
}

package proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import proxy.InvocationHandler;
import proxy.AsmProxyGenerator;
import proxy.JooqProxyGenerator;

public class Test {
  public static interface If1 {
    void sayHello();
    long foo(String str);
  };

  public static interface If2 {
    String bar(String str, int num);
  }

  public static class Original implements If1, If2 {
    @Override
    public void sayHello() {
      System.out.println("hello proxy");
    }

    @Override
    public long foo(String str) {
      System.out.println(str);
      return (long)str.length();
    }

    @Override
    public String bar(String str, int num) {
      return "bar: " + str + "-" + String.valueOf(num);
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
      Object re = method.invoke(obj, args);
      System.out.println("=== AFTER ===");
      return re;
    }
  }

  public static void testProxyGenerator(String type) throws Exception {
    ProxyGenerator pg;
    if (type.equals("asm")) {
      pg = new AsmProxyGenerator();
    } else if (type.equals("jooq")) {
      pg = new JooqProxyGenerator(new String[] {
        "/home/hy/Desktop/test/Java/PlayWithJava/target/classes"
      });
    } else {
      return;
    }

    Class<?> proxyClass = pg.generateProxyClass(new Class<?>[] {If1.class, If2.class});
    Constructor constructor = proxyClass.getConstructor(InvocationHandler.class);

    Object obj = constructor.newInstance(new Handler(new Original()));
    ((If1)obj).sayHello();
    System.out.println();

    System.out.println(((If1)obj).foo("foo"));
    System.out.println();

    System.out.println(((If2)obj).bar("testBar", 5));
    System.out.println();
  }

  public static void main(String[] args) throws Exception {
    testProxyGenerator("asm");
  }
}

package test;

import org.objectweb.asm.Type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import proxy.ProxyDemo;

public class Test<V> {
  private V v;
  private int value = 5;
  private String str;

  public Test(V v) {
    this.v = v;
    this.str = new String("hello");
  }

  public int hello(int v) {
    this.value += 6;
    return v + this.value;
  }

  public void foo(int x, int y) {
    this.fooImpl(x, y, null, null);
  }

  private void fooImpl(int x, int y, proxy.ProxyDemo d, proxy.ProxyDemo d2) {
    for (int i = 0; i < x; i++) {
      System.out.println(i);
    }

    for (int i = 0; i < y; i++) {
      System.out.println(i);
    }
  }

  public int bar() {
    int result = this.barImpl(3);
    return result + this.barImpl();
  }

  public int barImpl() {
    int a = 2;
    int b = 3;
    int c = a + b;
    return a + b;
  }

  public int barImpl(int m) {
    return m;
  }

  public void hehe() {
    String s = v.toString();
  }

  private interface If1 {
    void foo(String s);
    int bar();
  }

  public void initMethods() {
    Class<?> clazz;
    try {
      clazz = Class.forName("test.Test$If1");
    } catch (Exception e) {
      return;
    }

    for (Method method : clazz.getMethods()) {
      Method m = method;
      System.out.println(m.getName());
    }
  }

  public static void main(String[] dummy) {
    Test<Integer> t = new Test<Integer>(111117);
  }
}

package test;

import org.objectweb.asm.Type;

import java.util.ArrayList;
import Proxy.ProxyDemo;

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
    this.fooImpl(x, null, null);
  }

  private void fooImpl(int x, Proxy.ProxyDemo d, Proxy.ProxyDemo d2) {
    for (int i = 0; i < x; i++) {
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

  public static void main(String[] args) {
    Test<Integer> t = new Test<Integer>(111117);
    t.foo(3, 5);
    t.bar();

    ProxyDemo p = new ProxyDemo();
    System.out.println(Type.getDescriptor(ProxyDemo.class));
    System.out.println(Type.getDescriptor(Integer.class));
  }
}

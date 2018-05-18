package generic;

import generic.Pair;

public class Test extends Pair<String> {
  private int name;

  public Test() {
    super();
  }

  @Override
  public void setV1(String v1) {
    System.out.println("Override method called with string " + v1.length());
  }

  public static void main(String[] args) {
    Test test = new Test();
    test.foo(123);
  }
}
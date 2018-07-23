package generic;

import java.util.ArrayList;

import generic.InterfaceA;
import generic.InterfaceB;

public class Test {
  public static <F extends InterfaceA<?>> void bar(F f) {
    
  }

  public static void main(String[] args) {
    Test.bar(new InterfaceB<Void>() {
      @Override
      public void foo() {}
    });
  }
}
package aop.example;

import aop.annotation.*;

@Component
public class Calculator {

  public int add(int x, int y) {
    return x + y;
  }

  public long sub(long x, long y) {
    return x - y;
  }
}

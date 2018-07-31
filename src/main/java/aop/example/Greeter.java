package aop.example;

import aop.annotation.*;

@Component
public class Greeter {

  public void sayHello(String name) {
    System.out.println("Hello, " + name);
  }
}

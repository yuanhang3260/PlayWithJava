package aop.example;

import aop.annotation.*;

@Component
@Aspect
public class LoggingAspect {

  @PointCut(type=PointCutType.BEFORE,
            cut="public int aop.example.Calculator.add(int, int);" +
                "public void aop.example.Greeter.sayHello(java.lang.String);")
  public static void logBefore() {
    System.out.println("beforeMethod logging");
  }

  @PointCut(type=PointCutType.AFTER,
            cut="public int aop.example.Calculator.sub(long, long);"+
                "public void aop.example.Greeter.sayHello(java.lang.String)")
  public static void logAfter() {
    System.out.println("afterMethod logging");
  }

}

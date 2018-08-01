package aop.example;

import java.util.concurrent.atomic.AtomicInteger;

import aop.annotation.*;

@Component
@Aspect
public class LoggingAspect {
  private AtomicInteger count = new AtomicInteger();

  // Log before.
  @PointCut(type=PointCutType.BEFORE,
            cut="public int aop.example.Calculator.add(int, int);" +
                "public void aop.example.Greeter.sayHello(java.lang.String);")
  public static void logBefore() {
    System.out.println("=== Before ===");
  }

  // Log after.
  @PointCut(type=PointCutType.AFTER,
            cut="public long aop.example.Calculator.sub(long, long);" +
                "public void aop.example.Greeter.sayHello(java.lang.String)")
  public static void logAfter() {
    System.out.println("=== After ===");
  }

  // Increment counter.
  @PointCut(type=PointCutType.AFTER,
            cut="public int aop.example.Calculator.add(int, int);" +
                "public long aop.example.Calculator.sub(long, long);" +
                "public void aop.example.Greeter.sayHello(java.lang.String);")
  public void incCount() {
    System.out.println("count: " + count.incrementAndGet());
  }

}

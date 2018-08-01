package aop;

import java.util.*;
import java.lang.reflect.*;

import static org.junit.Assert.*;
import org.junit.Test;

import aop.BeanAspects;
import aop.DefaultBeanFactory;
import aop.example.*;

public class AOPTest {

  @Test
  public void testBeanFactory() throws Exception {
    DefaultBeanFactory factory = new DefaultBeanFactory(new String[] {
      "aop.example"
    });

    for (Map.Entry<String, BeanAspects> entry1 : factory.aops.entrySet()) {
      System.out.println("PointCut class: " + entry1.getKey());
      for (Map.Entry<Method, Map<String, BeanAspects.AspectMethods>> entry2 :
              entry1.getValue().pointCutAspects.entrySet()) {
        System.out.println("  PointCut method: " + entry2.getKey().getName());
        for (Map.Entry<String, BeanAspects.AspectMethods> entry3 : entry2.getValue().entrySet()) {
          System.out.println("    Aspect class: " + entry3.getKey());
          for (Method method : entry3.getValue().beforeMethods) {
            System.out.println("      Aspect before method: " + method.getName());
          }
          for (Method method : entry3.getValue().afterMethods) {
            System.out.println("      Aspect after method: " + method.getName());
          }
        }
      }
    }
    System.out.println();

    Greeter greeter = (Greeter)factory.getBean("aop.example.Greeter");
    greeter.sayHello("snoopy");
    System.out.println();

    Calculator calculator = (Calculator)factory.getBean("aop.example.Calculator");
    System.out.println(calculator.add(1, 2));
    System.out.println();

    System.out.println(calculator.sub(300, 500));
    System.out.println();
  }

}
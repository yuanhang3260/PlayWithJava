package aop;

import java.util.*;
import java.lang.reflect.*;

import aop.BeanAspects;
import aop.DefaultBeanFactory;

import static org.junit.Assert.*;
import org.junit.Test;

public class AOPTest {

  @Test
  public void testBeanFactory() {
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
  }

}
package aop;

import aop.DefaultBeanFactory;

import static org.junit.Assert.*;
import org.junit.Test;

public class Test {

  @Test
  public void testBeanFactory() {
    DefaultBeanFactory factory = new DefaultBeanFactory(new String[] {
      "aop.example"
    });
  }

}
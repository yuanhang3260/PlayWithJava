package aop;

import java.lang.reflect.Method;
import java.util.*;

import aop.annotation.*;

public class BeanAspects {

  public static class AspectMethods {
    public List<Method> beforeMethods = new ArrayList<Method>();
    public List<Method> afterMethods = new ArrayList<Method>();
  }

  // Map from pointcut method to aspects. Each aspect is a also a map, from aspect bean to aspect
  // methods in this bean.
  protected Map<Method, Map<String, AspectMethods>> pointCutAspects =
      new HashMap<Method, Map<String, AspectMethods>>();

  public void addAspectMethod(Method pointcutMethod, PointCut pointCut,
                              String aspectBeanId, Method aspectMethod)
  {
    // Find pointcut method.
    Map<String, AspectMethods> aspects = pointCutAspects.get(pointcutMethod);
    if (aspects == null) {
      aspects = new HashMap<String, AspectMethods>();
      pointCutAspects.put(pointcutMethod, aspects);
    }

    // Find aspect bean and add aspect method.
    AspectMethods aspectMethods = aspects.get(aspectBeanId);
    if (aspectMethods == null) {
      aspectMethods = new AspectMethods();
      aspects.put(aspectBeanId, aspectMethods);
    }

    if (pointCut.type() == PointCutType.BEFORE) {
      aspectMethods.beforeMethods.add(aspectMethod);
    } else if (pointCut.type() == PointCutType.AFTER) {
      aspectMethods.afterMethods.add(aspectMethod);
    }
  }
}

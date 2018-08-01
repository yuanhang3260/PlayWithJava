package aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import aop.*;
import aop.annotation.*;

public class DefaultBeanFactory {
  private Map<String, Bean> beans = new HashMap<String, Bean>();
  protected Map<String, BeanAspects> aops = new HashMap<String, BeanAspects>();

  public DefaultBeanFactory(String[] packages) {
    scanPackages(packages);
  }

  public Object getBean(String beanId) throws Exception {
    Bean bean = beans.get(beanId);
    if (bean != null) {
      return bean.getInstance(this);
    } else {
      throw new RuntimeException("No such bean: " + beanId);
    }
  }

  private void scanPackages(String[] packages) {
    for (String pkg : packages) {
      scanBeansInPackage(pkg);
    }
  }

  private void scanBeansInPackage(String pkg) {
    ClassPath cp;
    try {
      cp = ClassPath.from(getClass().getClassLoader());
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    for (ClassPath.ClassInfo ci : cp.getTopLevelClasses(pkg)) {
      Class<?> clazz = ci.load();
      String className = clazz.getName();

      // Check this class is a bean.
      Annotation componentAnnotation = clazz.getAnnotation(Component.class);
      if (componentAnnotation == null) {
        continue;
      }

      String beanId = ((Component)componentAnnotation).id();
      if (beanId == null || beanId.length() == 0) {
        beanId = className;
      }

      // Add new Bean.
      beans.put(beanId, new Bean(beanId, clazz));

      // Add aspects if it is an aspect bean.
      if (clazz.getAnnotation(Aspect.class) != null) {
        for (Method aspectMethod : clazz.getMethods()) {
          addBeanAspects(beanId, aspectMethod);
        }
      }
    }
  }

  private void addBeanAspects(String aspectBeanId, Method aspectMethod) {
    // Get aspect annotation and parse the pointCut expression.
    for (Annotation annotation : aspectMethod.getDeclaredAnnotations()) {
      if (!(annotation instanceof PointCut)) {
        continue;
      }

      PointCut pointCut = (PointCut)annotation;

      for (Method pointCutMethod : parsePointCutExpr(pointCut.cut())) {
        String pointCutBeanId = pointCutMethod.getDeclaringClass().getName();
        BeanAspects beanAspects = aops.get(pointCutBeanId);
        if (beanAspects == null) {
          beanAspects = new BeanAspects();
          aops.put(pointCutBeanId, beanAspects);
        }

        beanAspects.addAspectMethod(pointCutMethod, pointCut, aspectBeanId, aspectMethod);
      }
    }
  }

  private List<Method> parsePointCutExpr(String cut) {
    List<Method> result = new ArrayList<Method>();
    Pattern r = Pattern.compile(
        "^public \\s*[\\w\\$]+ \\s*(([\\w\\$]+\\.)*([\\w\\$]+))\\.([\\w\\$]+)\\s*\\((.*)\\)$");
    for (String point : cut.split(";")) {
      Matcher m = r.matcher(point.trim());
      if (!m.find()) {
        System.out.println("Can't parse cut point expression: " + point);
        continue;
      }
      String pointCutClassPath = m.group(1);
      String pointCutMethodName = m.group(4);
      String pointCutMethodArgsList = m.group(5);

      try {
        Class<?> clazz = Class.forName(pointCutClassPath);
        String[] argTypeListStr = pointCutMethodArgsList.split(",");
        Class[] argTypes = new Class[argTypeListStr.length];
        for (int i = 0; i < argTypeListStr.length; i++) {
          argTypes[i] = getArgClass(argTypeListStr[i].trim());
        }
        result.add(clazz.getMethod(pointCutMethodName, argTypes));
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        continue;
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
        continue;
      }
    }
    return result;
  }

  private Class getArgClass(String str) throws ClassNotFoundException {
    if (str.equals("int")) {
      return int.class;
    } else if (str.equals("long")) {
      return long.class;
    } else if (str.equals("short")) {
      return short.class;
    } else if (str.equals("float")) {
      return float.class;
    } else if (str.equals("double")) {
      return double.class;
    } else if (str.equals("byte")) {
      return byte.class;
    } else if (str.equals("char")) {
      return char.class;
    } else if (str.equals("boolean")) {
      return boolean.class;
    } else {
      return Class.forName(str);
    }
  }
}

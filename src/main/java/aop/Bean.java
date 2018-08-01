package aop;

import java.util.*;
import java.lang.reflect.*;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import aop.*;

public class Bean {
  private String id;
  private Class<?> clazz;
  private Object instance;

  public Bean(String id, Class<?> clazz) {
    this.id = id;
    this.clazz = clazz;
  }

  // Method interceptor for bean proxy class.
  private static class BeanProxyInterceptor implements MethodInterceptor {
    DefaultBeanFactory beanFactory;
    BeanAspects beanAspects;

    public BeanProxyInterceptor(DefaultBeanFactory beanFactory, BeanAspects beanAspects) {
      this.beanFactory = beanFactory;
      this.beanAspects = beanAspects;
    }

    private void invokeAspectMethod(String aspectBeanId, Method method) throws Exception {
      if (Modifier.isStatic(method.getModifiers())) {
        method.invoke(null);
      } else {
        method.invoke(beanFactory.getBean(aspectBeanId));
      }
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy proxy) throws Throwable {
      Map<String, BeanAspects.AspectMethods> aspects = beanAspects.pointCutAspects.get(method);
      if (aspects == null) {
        // No aspect for this method.
        return proxy.invokeSuper(obj, args);
      }

      for (Map.Entry<String, BeanAspects.AspectMethods> entry : aspects.entrySet()) {
        String aspectBeanId = entry.getKey();
        BeanAspects.AspectMethods aspectMethods = entry.getValue();
        // Invoke aspect "before" methods.
        for (Method m : aspectMethods.beforeMethods) {
          invokeAspectMethod(aspectBeanId, m);
        }
      }

      // Invoke the original method.
      Object re = proxy.invokeSuper(obj, args);

      for (Map.Entry<String, BeanAspects.AspectMethods> entry : aspects.entrySet()) {
        String aspectBeanId = entry.getKey();
        BeanAspects.AspectMethods aspectMethods = entry.getValue();
        // Invoke aspect "after" methods.
        for (Method m : aspectMethods.afterMethods) {
          invokeAspectMethod(aspectBeanId, m);
        }
      }

      return re;
    }
  }

  public Object getInstance(DefaultBeanFactory beanFactory) throws Exception {
    if (this.instance != null) {
      return this.instance;
    }

    synchronized(this) {
      if (this.instance != null) {
        return this.instance;
      }

      if (!beanFactory.aops.containsKey(id)) {
        this.instance = (Object)clazz.getConstructor(new Class[]{}).newInstance();
      } else {
        BeanAspects beanAspects = beanFactory.aops.get(id);
        // Create proxy class instance.
        Enhancer eh = new Enhancer();
        eh.setSuperclass(clazz);
        eh.setCallback(new BeanProxyInterceptor(beanFactory, beanAspects));
        this.instance = eh.create();
      }

      // TODO: Inject dependencies.
      // ...

      return this.instance;
    }
  }
}

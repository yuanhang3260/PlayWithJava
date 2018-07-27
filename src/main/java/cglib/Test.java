package cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class Test {
  private static class Target{
    public Target() {}

    public void f(){
      System.out.println("Target f()");
    }
    
    public void g(){
      System.out.println("Target g()");
    }
  }

  private static class Interceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj,
                            Method method,
                            Object[] args,
                            MethodProxy proxy) throws Throwable {
      System.out.println("I am intercept begin");
      System.out.println(obj.getClass().getName());
      System.out.println(method.toString());
      proxy.invokeSuper(obj, args);
      System.out.println("I am intercept end");
      return null;
    }
  }

  public static void main(String[] args) {
    Enhancer eh = new Enhancer();
    eh.setSuperclass(Target.class);
    eh.setCallback(new Interceptor());
    Target t = (Target)eh.create();
    t.f();
    t.g();
  }
}

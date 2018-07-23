package asm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface InvocationHandler {
  Object invoke(Object proxy, Method method, Object[] args)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}


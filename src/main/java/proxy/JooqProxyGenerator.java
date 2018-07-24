package proxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import static org.joor.Reflect.*;
import org.jooq.tools.reflect.*;

import proxy.InvocationHandler;
import proxy.ProxyGenerator;

public class JooqProxyGenerator extends ProxyGenerator {
  private static final String JAVA_CLASS_PATH = "java.class.path";

  // Automatically increments on every call of genearateProxyClass();
  protected static int classId = 0;

  public JooqProxyGenerator(String[] classpaths) {
    // This is a bad, bad hack. JOOR compiler does not support custom classpath (at least I don't
    // know how), which means this class actually does NOT work at all if you don't manually add
    // your custom classpaths for runtime compilation.
    for (String cp : classpaths) {
      System.setProperty(JAVA_CLASS_PATH,
                         System.getProperty(JAVA_CLASS_PATH) + File.pathSeparator + cp);
    }
  }

  @Override
  protected String proxyClassNamePrefix() {
    return "JooQProxyClass";
  }

  public Class<?> generateProxyClass(Class<?>[] interfaces) {
    // Class name.
    String proxyClassName = proxyClassNamePrefix() + String.valueOf(classId);

    // Interface names.
    String interfaceList = "";
    for (Class<?> ifc : interfaces) {
      interfaceList += ifc.getCanonicalName();
      interfaceList += ", ";
    }
    interfaceList = interfaceList.substring(0, interfaceList.length() - 2);

    // See definition of generateProxyMethodStr.
    String proxyMethodsStr = "";
    for (Class<?> ifc : interfaces) {
      for (Method method : ifc.getMethods()) {
        proxyMethodsStr += generateProxyMethodStr(method);
      }
    }

    String source = MessageFormat.format(
      "package {4};\n" +
      "\n" +
      "public class {0} implements {1} '{'\n" +
      "  private proxy.InvocationHandler handler;\n" +
      "  private java.util.HashMap<String, java.lang.reflect.Method> proxyMethods;\n" +
      "\n" +
      "  public {0}(proxy.InvocationHandler handler) '{'\n" +
      "    this.handler = handler;\n" +
      "    this.proxyMethods = new java.util.HashMap<String, java.lang.reflect.Method>();\n" +
      "    init();\n" +
      "  '}'\n" +
      "  \n" +
      "{2}\n" +
      "\n" +
      "{3}\n" +
      "'}'",
      new Object[] {
        proxyClassName,
        interfaceList,
        generateInitMethodStr(interfaces),
        proxyMethodsStr,
        ProxyGenerator.PACKAGE_NAME
      }
    );

    System.out.println(source);
    try {
      return org.joor.Reflect.compile(
          ProxyGenerator.PACKAGE_NAME + "." + proxyClassName, source).type();
    } catch (ReflectException e) {
      e.printStackTrace();
    }
    return null;
  }

  private String generateInitMethodStr(Class<?>[] interfaces) {
    String block = "";
    for (Class<?> ifc : interfaces) {
      block += MessageFormat.format(
        "    try '{'\n" +
        "      Class<?> clazz;\n" +
        "      clazz = Class.forName(\"{0}\");\n" +
        "      for (java.lang.reflect.Method method : clazz.getMethods()) '{'\n" +
        "        proxyMethods.put(method.toString(), method);\n" +
        "      '}'\n" +
        "    '}' catch (Exception e) '{'\n" +
        "      e.printStackTrace();\n" +
        "      return;\n" +
        "    '}'\n",
        new Object[] {
          ifc.getName()
        }
      );
    }

    String code = MessageFormat.format(
        "  public void init() '{'\n" +
        "{0}\n" +
        "  '}'",
        new Object[] { block }
    );
    return code;
  }

  private String generateProxyMethodStr(Method method) {
    Class<?>[] parameters = method.getParameterTypes();
    String parameterList = "";
    String parameterIndentifierList = "";
    for (int i = 0; i < parameters.length; i++) {
      Class<?> type = parameters[i];
      parameterList += (type.getCanonicalName() + " " + "arg" + String.valueOf(i));
      parameterIndentifierList += ("arg" + String.valueOf(i));
      if (i < parameters.length - 1) {
        parameterList += ", ";
        parameterIndentifierList += ", ";
      }
    }

    boolean voidReturn = method.getReturnType().equals(Void.TYPE);
    String returnVal = voidReturn ?
        "return;\n" : MessageFormat.format("return ({0})re;\n",
                          new Object[] { method.getReturnType().getCanonicalName() });
    String exceptionReturn = voidReturn ? "" : "return null;";


    String code = MessageFormat.format(
      "  public {0} {1}({2}) '{'\n" +
      "    java.lang.reflect.Method m = proxyMethods.get(\"{3}\");\n" +
      "    try '{'\n" +
      "      Object re = handler.invoke(this, m, new Object[] '{'{4}});\n" +
      "      {5}" +
      "    '}' catch (Exception e) '{'\n" +
      "      e.printStackTrace();\n" +
      "      {6}\n" +
      "    '}'\n" +
      "  '}'\n",
      new Object[] {
        method.getReturnType().getCanonicalName(),
        method.getName(),
        parameterList,
        method.toString(),
        parameterIndentifierList,
        returnVal,
        exceptionReturn,
      }
    );
    return code;
  }
}

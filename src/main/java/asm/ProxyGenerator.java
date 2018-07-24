package asm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import asm.InvocationHandler;

public class ProxyGenerator {
  // Automatically increments on every call of genearateProxyClass();
  private static int classId = 0;
  private static ProxyClassLoader classLoader = new ProxyClassLoader();

  public static Class<?> generateProxyClass(Class<?>[] interfaces) {
    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

    // Generate type names, note that all "." in class path are replaced by "/".
    //
    // Class name.
    String proxyClassName = "asm/proxy/ProxyClass" + String.valueOf(classId++);
    // Interface names.
    String[] interfaceNames = new String[interfaces.length];
    for (int i = 0; i < interfaces.length; i++) {
      interfaceNames[i] = interfaces[i].getName().replace(".", "/");
    }

    // Create a new class.
    cw.visit(Opcodes.V1_7,  // java version: v1.8
             Opcodes.ACC_PUBLIC,  // access flags
             proxyClassName,  // class name
             null,  // signature
             "java/lang/Object",  // super class
             interfaceNames);  // interface names

    addConstructor(cw, proxyClassName);

    // Add a field:
    //   private InvocationHandler handler;
    cw.visitField(Opcodes.ACC_PRIVATE,  // access flags
                  "handler",  // field name
                  Type.getDescriptor(InvocationHandler.class),  // field type
                  null,  // signature
                  null).visitEnd();  // default value

    // Add proxy methods.
    addProxyMethods(cw, proxyClassName, interfaces);

    cw.visitEnd();
    byte[] data = cw.toByteArray();

    // Write class data to file.
    try {
      File file = new File("/usr/local/google/home/hangyuan/Desktop/Proxy.class");
      FileOutputStream fout = new FileOutputStream(file);
      fout.write(data);
      fout.close();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    Class<?> clazz = classLoader.defineClassForName(proxyClassName.replace("/", "."), data);
    return clazz;
  }

  // Constructor source code:
  //
  // public asm.proxy.ProxyClass(InvocationHandler handler) {
  //   this.handler = handler;
  // }
  //
  // Byte code:
  //   0: aload_0
  //   1: invokespecial #13    // Method java/lang/Object."<init>":()V
  //   4: aload_0
  //   5: aload_1
  //   6: putfield      #17    // Field handler:Lasm/InvocationHandler;
  //   9: return
  //
  private static boolean addConstructor(ClassWriter cw, String proxyClassName) {
    MethodVisitor constructor = cw.visitMethod(Opcodes.ACC_PUBLIC,
                                               "<init>",
                                               "(Lasm/InvocationHandler;)V",
                                               null, null);
    constructor.visitVarInsn(Opcodes.ALOAD, 0);
    constructor.visitMethodInsn(Opcodes.INVOKESPECIAL,
                                "java/lang/Object",
                                "<init>",
                                "()V",
                                /* is interface = */false);
    constructor.visitVarInsn(Opcodes.ALOAD, 0);
    constructor.visitVarInsn(Opcodes.ALOAD, 1);
    constructor.visitFieldInsn(Opcodes.PUTFIELD,
                               proxyClassName,
                               "handler",
                               Type.getDescriptor(InvocationHandler.class));
    constructor.visitInsn(Opcodes.RETURN);
    constructor.visitMaxs(1, 1);
    constructor.visitEnd();
    return true;
  }

  private static boolean addProxyMethods(ClassWriter cw,
                                         String proxyClassName,
                                         Class<?>[] interfaces) {
    int methodIndex = 0;
    for (Class<?> interfacee : interfaces) {
      for (Method method : interfacee.getMethods()) {
        // Save the method as a class field.
        cw.visitField(Opcodes.ACC_PRIVATE,  // access flags
                      "m" + String.valueOf(methodIndex++),  // field name
                      Type.getDescriptor(Method.class),  // field type
                      null,  // signature
                      null).visitEnd();  // default value
      }
    }
    return true;
  }

  private static class ProxyClassLoader extends ClassLoader {
    public ProxyClassLoader() {
      super(Thread.currentThread().getContextClassLoader());
    }
 
    public Class<?> defineClassForName(String name, byte[] data) {
      return this.defineClass(name, data, 0, data.length);
    }
  }
}

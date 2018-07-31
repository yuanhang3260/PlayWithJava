package aop;

public class Bean {
  private String id;
  private Class<?> clazz;
  private Object instance;

  public Bean(String id, Class<?> clazz) {
    this.id = id;
    this.clazz = clazz;
  }

  public Object getInstance(DefaultBeanFactory factory) {
    if (this.instance != null) {
      return this.instance;
    }

    synchronized(this) {
      if (this.instance != null) {
        return this.instance;
      }

      try {
        this.instance = (Object)clazz.getConstructor(new Class[]{}).newInstance();
        return this.instance;
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
  }
}

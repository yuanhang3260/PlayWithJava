package generic;

public class Pair<T> {
  private T v1;
  private T v2;

  public Pair() {}

  public Pair(T v1, T v2) {
    this.v1 = v1;
    this.v2 = v2;
  }

  public void foo(Object obj) {
    this.setV1((T)obj);
  }

  public T getV1() {
    return v1;
  }

  public void setV1(T v1) {
    this.v1 = v1;
    System.out.println("base method called with " + v1);
  }

  public T getV2() {
    return v2;
  }
}

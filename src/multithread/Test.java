package multithread;

import java.lang.Runnable;
import multithread.FixedThreadPool;

public class Test {
  private static int TASKS_NUM = 300;
  private static int CALCULATE_RANGE = 100000000;

  private int[] results;

  public Test() {
    this.results = new int[Test.TASKS_NUM];
  }

  public void calcualate(int index) {
    int sum = 0;
    for (int i = 0; i < Test.CALCULATE_RANGE; i++) {
      sum += i;
    }
    System.out.println("index " + index);
    this.results[index] = sum;
  }

  public void checkResults() {
    int sum = 0;
    for (int i = 0; i < Test.CALCULATE_RANGE; i++) {
      sum += i;
    }

    for (int i = 0; i < Test.TASKS_NUM; i++) {
      if (this.results[i] != sum) {
        System.out.println("Failed on " + i);
        System.out.println("Expect " + sum + ", actually " + this.results[i]);
        return;
      }
    }
    System.out.println("Passed ^_^");
  }

  public static void main(String[] args) {
    Test test = new Test();
    FixedThreadPool pool = new FixedThreadPool(Test.TASKS_NUM);

    for (int i = 0; i < Test.TASKS_NUM; i++) {
      int index = i;
      pool.execute(new Runnable() {
        @Override
        public void run() {
          test.calcualate(index);
        }
      });
    }

    pool.stop();
    pool.awaitTermination();

    test.checkResults();
  }
}
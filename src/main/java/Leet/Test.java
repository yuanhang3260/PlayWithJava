package Leet;

import java.lang.*;
import java.util.*;
import Leet.Sort;

public class Test {
  public static void TestSort() {
    System.out.println("Starting Testing Sort ..");
    // generate 100 random arrays and sort them
    for (int i = 0; i < 3000; i++) {
      int size = (int) (Math.random() * 3000);

      // generate a random array
      int[] array = new int[size];
      for (int j = 0; j < size; j++) {
        array[j] = (int) (Math.random() * 3000);
      }
      int[] a1 = array.clone();
      int[] a2 = array.clone();

      // sort arrays
      Arrays.sort(a1);
      Sort.quickSort(a2);

      // compare sort results
      for (int j = 0; j < size; j++) {
        if (a1[j] != a2[j]) {
          System.out.println("*** Error ***");
          return;
        }
      }
    }
    System.out.println("Passed ^_^");
  }
}
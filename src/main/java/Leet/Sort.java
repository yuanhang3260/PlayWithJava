package Leet;

import java.lang.*;

public class Sort {

  public static void bubbleSort(int[] a) {
    for (int i = a.length - 1; i >= 1; i--) {
      for (int j = 0; j < i; j++) {
        if (a[j] > a[i]) {
          int tmp = a[j];
          a[j] = a[i];
          a[i] = tmp;
        }
      }
    }
  }

  public static void selectSort(int[] a) {
    int temp;
    // min_index存放最小元素的index
    int min_index;
    for (int i = 0; i < a.length; i++) {
      min_index = i;
      for (int j = i + 1; j < a.length; j++) {
        if (a[j] < a[min_index]) {
          min_index = j;
        }
      }
      /* exchange the min to a[i] */
      int tmp = a[i];
      a[i] = a[min_index];
      a[min_index] = tmp;
    }
  }
  
  public static void insertSort(int[] a) {
    for(int i = 1; i < a.length; i++) {
      int key = a[i];
      int j = i - 1;
          for (j = i - 1; j >= 0 && a[j] > key; j--) {
            a[j + 1] = a[j];
          }
          a[j + 1] = key;
        }
  }

  public static void quickSort(int[] a) {
    _quickSort(a, 0, a.length - 1);
  }

  private static void _quickSort(int[] a, int start, int end) {
    if (start >= end) {
      return;
    }

    /* start partition */
    int pivot = (int) (a[start] + a[end]) / 2;
    int i = start, j = end;
    while (i <= j) {
      while (a[i] < pivot) {
        i++;
      }
      while (a[j] > pivot) {
        j--;
      }
      if (i <= j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
        i++; j--;
      }
    }

    _quickSort(a, start, i - 1);
    _quickSort(a, i, end);
  }

}
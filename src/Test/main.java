package Test;

import java.lang.*;
import Leet.Sort;
import IO.IOStream;
import Leet.Test;

public class main {
  public static void main(String[] args) {
    System.out.println("Hello Java");

    IOStream test_io = new IOStream();
    String data = test_io.serialize();
    test_io.deserialize(data);
  }
}
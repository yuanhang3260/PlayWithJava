package redis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Utils {

  public static String readResource(String filename) throws IOException {
    File file = new File(RedisLock.class.getClassLoader().getResource(filename).getFile());

    StringBuilder result = new StringBuilder();
    Scanner scanner = new Scanner(file);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      result.append(line).append("\n");
    }
    scanner.close();
    return result.toString();
  }
}
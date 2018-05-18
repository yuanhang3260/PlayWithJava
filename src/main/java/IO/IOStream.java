package IO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;

public class IOStream {
  public String serialize() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    try {
      out.writeInt(6336);
      out.flush();
    } catch (IOException e) {
      return "";
    }
    return Base64.getEncoder().encodeToString(baos.toByteArray());
  }

  public void deserialize(String data) {
      ByteArrayInputStream bais =
          new ByteArrayInputStream(Base64.getDecoder().decode(data));
      DataInputStream in = new DataInputStream(bais);
      int val = 0;
      try {
        val = in.readInt();
        System.out.println(val);
      } catch (IOException e) {
        return;
      }
  }
}

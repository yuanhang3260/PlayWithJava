package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Client {
  public static void main(String[] args) throws IOException {
    Client client = new Client();
    client.start();
  }

  public void start() throws IOException {
    SocketChannel clientChannel = SocketChannel.open();
    clientChannel.configureBlocking(false);
    clientChannel.connect(new InetSocketAddress("localhost", 8081));

    // Create Selector and register channel.
    Selector selector = Selector.open();
    clientChannel.register(selector,
        SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

    while (true) {
      // block.
      if (selector.select() == 0) {
        continue;
      }

      Set<SelectionKey> selectedKeys = selector.selectedKeys();
      Iterator<SelectionKey> it = selectedKeys.iterator();
      while (it.hasNext()) {
        SelectionKey key = it.next();
        it.remove();

        int re = processSelectedKey(key);
        if (re < 0) {
          return;
        }
      }
    }
  }

  private int processSelectedKey(SelectionKey key) 
      throws IOException {
    SocketChannel clientChannel = (SocketChannel)key.channel();

    if (key.isConnectable() && clientChannel.isConnectionPending()) {
      clientChannel.finishConnect();
      System.out.println("Connection established");
      key.interestOps(SelectionKey.OP_WRITE);
    } else if (key.isWritable()) {
      ByteBuffer sendBuffer = ByteBuffer.allocate(64);
      System.out.println("Sending \"Hello\"");
      sendBuffer.put("Hello".getBytes());
      sendBuffer.flip();
      clientChannel.write(sendBuffer);
      key.interestOps(SelectionKey.OP_READ);
    } else if (key.isReadable()) {
      ByteBuffer recvBuffer = ByteBuffer.allocate(64);
      int readBytes = clientChannel.read(recvBuffer);
      if (readBytes >= 0) {
        System.out.println("Got echo back " + readBytes + " bytes: " +
                           new String(recvBuffer.array()));
      } else {
        System.out.println("Close client channel");
        clientChannel.close();
        return -1;
      }
    }
    return 0;
  }
}

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

public class Server {
  public static final int PORT = 8081;

  private Selector selector;
  private ByteBuffer recvBuffer = ByteBuffer.allocate(1024);
  private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
  String data;

  public static void main(String[] args) throws IOException {
    Server server = new Server();
    server.start();
  }

  public void start() throws IOException {
    // Create server socket channel and set it as non-blocking mode.
    ServerSocketChannel serverChannel = ServerSocketChannel.open();
    serverChannel.configureBlocking(false);

    // Bind server socket to local listening address.
    serverChannel.bind(new InetSocketAddress("localhost", Server.PORT));

    // Create Selector.
    this.selector = Selector.open();

    // Register server channel to selector.
    serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    System.out.println("Start listening on port " + Server.PORT);

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

        processSelectedKey(key, selector);
      }
    }
  }

  private void processSelectedKey(SelectionKey key, Selector selector)
      throws IOException {
    if (key.isAcceptable()) {
      ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
      SocketChannel clientChannel = serverChannel.accept();
      clientChannel.configureBlocking(false);
      clientChannel.register(selector, SelectionKey.OP_READ);
    } else if (key.isReadable()) {
      System.out.println("[readable]");
      SocketChannel clientChannel = (SocketChannel)key.channel();
      recvBuffer.clear();
      int readBytes = clientChannel.read(recvBuffer);
      data = new String(recvBuffer.array(), 0, readBytes);
      System.out.println("Receive data: " + data);
      key.interestOps(SelectionKey.OP_WRITE);
    } else if (key.isWritable()) {
      System.out.println("[writable]");
      if (data != null) {
        SocketChannel clientChannel = (SocketChannel)key.channel();
        sendBuffer.clear();
        sendBuffer.put(data.getBytes());
        sendBuffer.flip();
        int sentBytes = clientChannel.write(sendBuffer);
        System.out.println("Sent " + sentBytes + " bytes");
        clientChannel.close();
      }
    }
  }
}

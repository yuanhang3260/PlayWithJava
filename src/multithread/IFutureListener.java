package multithread;

import multithread.IFuture;
import java.util.concurrent.CancellationException;

class interface IFutureListener<V> {
  // Callback to execute when future is done.
  void taskDone(Future<V> future);
}

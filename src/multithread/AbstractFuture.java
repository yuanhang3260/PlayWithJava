package multithread;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.CopyOnWriteArrayList;
import java.util.List;

import multithread.IFuture;
import multithread.IFutureListener;

class AbstractFuture<V> implements IFuture<V> {
  protected volatile Object result;

  List<IFutureListener<V>> listeners = new CopyOnWriteArrayList<IFutureListener<V>>();

  private static class SuccessSignal() {}
  private static final SuccessSignal SUCCESS_VOID = new SuccessSignal();

  @Override
  public boolean isDone() {
    return result != null;
  }

  @Override  
  public boolean isSuccess() {
    if (result == null) {
      return false;
    }
    return !(result instanceof FailureResult);  
  }

  // FailureResult is a holder of failure cause. The cause can be normal exceptions threw by task,
  // or a CancellationException after cancel() is called.
  private static final class FailureResult {  
    private final Throwable cause;  

    public FailureResult(Throwable cause) {  
      this.cause = cause;  
    }

    public Throwable getCause() {
      return this.cause;
    }
  }

  // TODO: implement interrupt.
  @Override
  boolean cancel(boolean mayInterruptIfRunning) {
    // IsCancelled is a subset of isDone.
    if (isDone()) {
      return false;
    }

    synchronized(this) {
      if (isDone()) {
        return false;
      }

      result = new FailureResult(new CancellationException());

      // Notify all threads that are blocking on get/await.
      notifyAll();
    }
    notifyListeners();
    return true;
  }

  public boolean cancel() {
    this.cancel(false);
  }

  @Override
  public boolean isCancelled() {
    return result != null && result instanceof FailureResult &&
           ((FailureResult)result).getCause() instanceof CancellationException;  
  }

  @Override
  public boolean isCancellable() {
    return !this.isDone();
  }

  @Override
  public Throwable cause() {
    if (result == null || !(result instanceof FailureResult)) {
      return null;
    }
    return ((FailureResult)result).getCause();
  }

  @Override
  public V get() throws InterruptedException, ExecutionException {
    await();
    return get0();
  }

  @Override  
  public V get(long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    await(timeout, unit);

    if (isDone()) {
      return get0();
    } else {
      // Task is not done after timeout.
      throw new TimeoutException();
    }
  }

  private V get0() throws InterruptedException, ExecutionException  {
    // Task is done and success, return the result. If return type is void, result will be set as
    // SUCCESS_VOID, and get() will return null.
    if (result && !(result instanceof FailureResult)) {
      return result == SUCCESS_VOID ? null : result;
    }

    Throwable cause = cause();
    if (cause instanceof CancellationException) {
      throw (CancellationException)cause;  
    }
    throw new ExecutionException(cause);
  }
}

package multithread;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

import multithread.AbstractFuture;

public class AbstractFutureTest {
  private static final Logger log = Logger.getLogger(AbstractFutureTest.class);

  private static final class TestFuture<Integer> extends AbstractFuture<Integer> {}

  private static final class MyException extends Exception {}

  @Test
  public void testFutureTaskSuccess() throws InterruptedException, ExecutionException {
    AbstractFuture<Integer> future = new TestFuture<Integer>();
    assertTrue(future.isCancellable());
    assertTrue(future.setSuccess(3));
    assertFalse(future.isCancellable());
    assertTrue(future.isDone());
    assertTrue(future.isSuccess());
    assertNull(future.getCause());

    assertFalse(future.setSuccess(1));
    assertFalse(future.setSuccess(2));
    assertFalse(future.setFailure(new Exception()));
    assertFalse(future.cancel());

    assertEquals((long)3, (long)future.get());
  }

  @Test
  public void testFutureTaskFailure() throws InterruptedException, ExecutionException {
    AbstractFuture<Integer> future = new TestFuture<Integer>();
    assertTrue(future.isCancellable());
    assertTrue(future.setFailure(new MyException()));
    assertFalse(future.isCancellable());
    assertTrue(future.isDone());
    assertFalse(future.isSuccess());
    assertTrue(future.getCause() instanceof MyException);

    assertFalse(future.setSuccess(1));
    assertFalse(future.setSuccess(2));
    assertFalse(future.setFailure(new MyException()));
    assertFalse(future.cancel());

    boolean exceptionCaught = false;
    try {
      Integer result = future.get();
    } catch (ExecutionException e) {
      exceptionCaught = true;
    } finally {
      assertTrue(exceptionCaught);
    }
  }

  @Test
  public void testFutureTaskCancelled() throws InterruptedException, ExecutionException {
    AbstractFuture<Integer> future = new TestFuture<Integer>();
    assertTrue(future.isCancellable());
    assertTrue(future.cancel());
    assertFalse(future.isCancellable());
    assertTrue(future.isDone());
    assertFalse(future.isSuccess());
    assertTrue(future.isCancelled());
    assertTrue(future.getCause() instanceof CancellationException);

    assertFalse(future.setSuccess(1));
    assertFalse(future.setSuccess(2));
    assertFalse(future.setFailure(new Exception()));
    assertFalse(future.cancel());

    boolean exceptionCaught = false;
    try {
      Integer result = future.get();
    } catch (CancellationException e) {
      exceptionCaught = true;
    } finally {
      assertTrue(exceptionCaught);
    }
  }

  @Test
  public void testFutureTaskTimeout() throws InterruptedException, ExecutionException {
    AbstractFuture<Integer> future = new TestFuture<Integer>();

    boolean exceptionCaught = false;
    try {
      Integer result = future.get(1000, TimeUnit.MILLISECONDS);
    } catch (TimeoutException e) {
      exceptionCaught = true;
    } finally {
      assertTrue(exceptionCaught);
    }
  }
}
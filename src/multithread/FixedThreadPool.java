package multithread;

import java.lang.Runnable;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FixedThreadPool {
  public static int DEFAULT_CAPACITY = 4;

  private int capacity = 1;
  private List<Thread> workers;

  private Queue<Runnable> tasks;
  private boolean stop = false;
  private Object lock;

  public FixedThreadPool() {
    this(FixedThreadPool.DEFAULT_CAPACITY);
  }

  public FixedThreadPool(int capacity) {
    if (capacity <= 0) {
      System.err.println("FixedThreadPool capacity must be >= 1");
      capacity = 1;
    }
    this.capacity = capacity;

    init();
  }

  private void init() {
    this.tasks = new LinkedList<Runnable>();
    this.workers = new ArrayList<Thread>();
    this.lock = new Object();
  }

  public void execute(Runnable task) {
    synchronized(this.lock) {
      if (this.stop) {
        System.err.println("Thread pool is stopped, cannot add task");
        return;
      }
      this.tasks.offer(task);
      this.lock.notify();
    }

    synchronized(this.workers) {
      if (this.workers.size() < this.capacity) {
        // // Traditional style: create anonymous Runnable class instance.
        // Thread worker = new Thread(new Runnable() {
        //   @Override
        //   public void run() {
        //     runWorker();
        //   }
        // });

        // Use lambda to create runnable.
        Thread worker = new Thread(() -> { this.runWorker(); });
        worker.start();
        this.workers.add(worker);
      }
    }
  }

  private void runWorker() {
    try {
      while (true) {
        Runnable task;

        // Wait for task to come in.
        synchronized(this.lock) {
          while (tasks.isEmpty() && !this.stop) {
            this.lock.wait();
          }

          // If threadpool is stopped, and there is no pending task in queue,
          // return and terminate this worker. Note we guarantee that all
          // queued tasks are executed before this threadpool is shutdown.
          if (this.stop && this.tasks.isEmpty()) {
            return;
          }

          task = this.tasks.poll();
        }

        // New task received, run it!
        task.run();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    synchronized(this.lock) {
      System.out.println("Stop threadpool");
      this.stop = true;
      this.lock.notifyAll();
    }
  }

  public void awaitTermination() {
    try {
      synchronized(this.workers) {
        for (Thread t: this.workers) {
          t.join();
        }
      }
      this.workers.clear();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  } 
}

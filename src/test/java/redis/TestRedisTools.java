package redis;

import java.util.UUID;

import static org.junit.Assert.*;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import redis.RedisLock;

public class TestRedisTools {
  private RedisLock redisLock = new RedisLock("localhost", 6379);

  @Test
  public void testRedisLockSimple() {
    String key = "key1";
    String id = "snoopy";
    assertTrue(redisLock.lock(key, id, 10000));
    assertFalse(redisLock.tryLock(key, id, 1000));

    assertTrue(redisLock.unlock(key, id));
    assertTrue(redisLock.tryLock(key, id, 1000));
  }

  private class Counter {
    public int count = 0;
  }

  @Test
  public void testRedisLockHighContention() throws Exception {
    Counter counter = new Counter();

    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxTotal(100);
    JedisPool pool = new JedisPool(jedisPoolConfig, "localhost", 6379);

    Runnable task = new Runnable() {
      @Override
      public void run() {
        Jedis jedis = pool.getResource();
        RedisLock l = new RedisLock(jedis);
        String id = UUID.randomUUID().toString();
        assertTrue(l.lock("count", id, 50000));
        // Increment counter by 1000, in a for loop.
        for (int i = 0; i < 1000; i++) {
          counter.count++;
        }
        assertTrue(l.unlock("count", id));
        jedis.close();
      }
    };

    // Create counters in many threads.
    Thread[] threads = new Thread[1000];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(task);
    }

    for (int i = 0; i < threads.length; i++) {
      threads[i].start();
    }
    for (int i = 0; i < threads.length; i++) {
      threads[i].join();
    }

    assertEquals(1000 * threads.length, counter.count);
  }
}

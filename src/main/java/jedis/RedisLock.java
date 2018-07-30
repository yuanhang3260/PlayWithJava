package redis;

import java.util.Collections;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.HostAndPort;

import redis.Utils;

public class RedisLock {
  private static final String SET_IF_NOT_EXIST = "NX";
  private static final String EXPIRE_MILLISECONDS = "PX";
  private static final String SET_SUCCESS = "OK";
  private static final Long UNLOCK_SUCCESS = 1L;

  private static final String LOCK_PREFIX = "lock_";

  private static final String UNLOCK_SCRIPT_PATH = "lua/unlock.lua";

  private static String unlockScript;

  // Load unlock lua script from resource path.
  static {
    // unlockScript = Utils.readResource(UNLOCK_SCRIPT_PATH);
    unlockScript = "if redis.call('get', KEYS[1]) == ARGV[1] then\n" +
                   "  return redis.call('del', KEYS[1])\n" +
                   "else\n" +
                   "  return 0\n" +
                   "end";
  }

  private Jedis jedis;

  public RedisLock(String host, int port) {
    this.jedis = new Jedis(host, port);
  }

  public RedisLock(Jedis jedis) {
    this.jedis = jedis;
  }

  public boolean tryLock(String key, String id, int expire) {
    String result = jedis.set(LOCK_PREFIX + key, id, SET_IF_NOT_EXIST, EXPIRE_MILLISECONDS, expire);
    return SET_SUCCESS.equals(result);
  }

  // Blocking lock.
  public boolean lock(String key, String id, int expire) {
    try {
      while (!tryLock(key, id, expire)) {
        // Non-busy spin, don't push CPU and redis.
        Thread.sleep(100);
      }
      return true;
    } catch (InterruptedException e) {
      return false;
    }
  }

  public boolean unlock(String key, String id) {
    Object result = jedis.eval(unlockScript,
                               Collections.singletonList(LOCK_PREFIX + key),
                               Collections.singletonList(id));
    return UNLOCK_SUCCESS.equals(result);
  }
}

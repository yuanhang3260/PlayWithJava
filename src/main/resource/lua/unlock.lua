if redis.call('get', KYES[1]) == ARGV[1] then
  return redis.call('del', KYES[1])
else
  return 0
end
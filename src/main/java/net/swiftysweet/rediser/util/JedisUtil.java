package net.swiftysweet.rediser.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.swiftysweet.rediser.annotation.Redis;
import redis.clients.jedis.Jedis;

import java.util.Map;

@UtilityClass
public class JedisUtil {

    public Jedis createJedis(@NonNull Redis redis) {
        Jedis jedis = new Jedis(redis.hostName(), redis.port());
        if (!redis.password().equals("unspecified")) {
            jedis.auth(redis.password());
        }
        return jedis;
    }

    public void set(@NonNull Redis redis, @NonNull String field, @NonNull Object object) {
        try (Jedis jedis = createJedis(redis)) {
            jedis.hset(redis.name(), field, JsonUtil.to(object));
        }
    }

    public String get(@NonNull Redis redis, @NonNull String field) {
        try (Jedis jedis = createJedis(redis)) {
            return jedis.hget(redis.name(), field);
        }
    }

    public void remove(@NonNull Redis redis, @NonNull String field) {
        try (Jedis jedis = createJedis(redis)) {
            jedis.hdel(redis.name(), field);
        }
    }

    public Map<String, String> getAll(@NonNull Redis redis) {
        try (Jedis jedis = createJedis(redis)) {
            return jedis.hgetAll(redis.name());
        }
    }
}

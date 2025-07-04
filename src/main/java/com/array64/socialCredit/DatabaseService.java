package com.array64.socialCredit;

import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.resps.Tuple;

import java.util.List;
import java.util.Map;

class DatabaseService {
    private final JedisPool pool;
    private final JavaPlugin plugin;

    public DatabaseService(JedisPool pool, JavaPlugin plugin) {
        this.pool = pool;
        if(pool == null)
            plugin.getLogger().warning("JedisPool is null. SocialCreditSystem cannot function.");
        this.plugin = plugin;
    }
    public List<Tuple> zrevrangeWithScores(String key, int start, int stop) {
        try(Jedis jedis = pool.getResource()) {
            return jedis.zrevrangeWithScores(key, start, stop);
        } catch(JedisConnectionException e) {
            plugin.getLogger().warning("Redis: Operation failed due to " + e);
            return List.of();
        }
    }
    public List<Tuple> zrangeWithScores(String key, int start, int stop) {
        try(Jedis jedis = pool.getResource()) {
            return jedis.zrangeWithScores(key, start, stop);
        } catch(JedisConnectionException e) {
            plugin.getLogger().warning("Redis: Operation failed due to " + e);
            return List.of();
        }
    }
    public void zadd(String key, String member, double score) {
        try(Jedis jedis = pool.getResource()) {
            jedis.zadd(key, score, member);
        } catch(JedisConnectionException e) {
            plugin.getLogger().warning("Redis: Operation failed due to " + e);
        }
    }
    public void close() {
        if(!pool.isClosed()) {
            pool.close();
            plugin.getLogger().info("Database closed.");
        }
    }
    public Map<String, String> hgetAll(String key) {
        try(Jedis jedis = pool.getResource()) {
            return jedis.hgetAll(key);
        } catch(JedisConnectionException e) {
            plugin.getLogger().warning("Redis: Operation failed due to " + e);
            return Map.of();
        }
    }
    public String hget(String key, String field) {
        try(Jedis jedis = pool.getResource()) {
            return jedis.hget(key, field);
        } catch(JedisConnectionException e) {
            plugin.getLogger().warning("Redis: Operation failed due to " + e);
            return null;
        }
    }
    public void hdel(String key, String field) {
        try(Jedis jedis = pool.getResource()) {
            jedis.hdel(key, field);
        } catch(JedisConnectionException e) {
            plugin.getLogger().warning("Redis: Operation failed due to " + e);
        }
    }
    public void hset(String key, String field, String value) {
        try(Jedis jedis = pool.getResource()) {
            jedis.hset(key, field, value);
        } catch(JedisConnectionException e) {
            plugin.getLogger().warning("Redis: Operation failed due to " + e);
        }
    }
    public Double zscore(String key, String member) {
        try(Jedis jedis = pool.getResource()) {
            return jedis.zscore(key, member);
        } catch(JedisConnectionException e) {
            plugin.getLogger().warning("Redis: Operation failed due to " + e);
            return null;
        }
    }
}

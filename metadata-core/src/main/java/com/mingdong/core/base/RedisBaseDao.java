package com.mingdong.core.base;

import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RedisBaseDao<K extends Serializable, V extends Serializable>
{
    @Resource
    private RedisTemplate<K, V> redisTemplate;

    private byte[] serialize(String s)
    {
        return redisTemplate.getStringSerializer().serialize(s);
    }

    private List<byte[]> serializeList(String... strs)
    {
        List<byte[]> list = new ArrayList<>();
        for(String item : strs)
        {
            list.add(redisTemplate.getStringSerializer().serialize(item));
        }
        return list;
    }

    private String deserialize(byte[] bytes)
    {
        return redisTemplate.getStringSerializer().deserialize(bytes);
    }

    private List<String> deserializeList(List<byte[]> byteList)
    {
        List<String> list = new ArrayList<>();
        for(byte[] item : byteList)
        {
            list.add(redisTemplate.getStringSerializer().deserialize(item));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    protected Set<String> limitScan(int db, long limit)
    {
        return (Set<String>) redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            Set<String> keys = new HashSet<>();
            Cursor<byte[]> cursor = conn.scan(ScanOptions.scanOptions().count(1000).build());
            while(cursor.hasNext() && keys.size() <= limit)
            {
                keys.add(deserialize(cursor.next()));
            }
            return keys;
        });
    }

    @SuppressWarnings("unchecked")
    protected void set(int db, String key, String value)
    {
        redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            byte[] k = serialize(key);
            byte[] v = serialize(value);
            conn.set(k, v);
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    protected void setExNx(int db, String key, String value, long seconds)
    {
        redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            byte[] k = serialize(key);
            byte[] v = serialize(value);
            conn.set(k, v, Expiration.seconds(seconds), RedisStringCommands.SetOption.SET_IF_ABSENT);
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    protected Long incr(int db, String key)
    {
        return (Long) redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            byte[] k = serialize(key);
            return conn.incr(k);
        });
    }

    @SuppressWarnings("unchecked")
    protected void setEx(int db, String key, String value, long seconds)
    {
        redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            byte[] k = serialize(key);
            byte[] v = serialize(value);
            conn.setEx(k, seconds, v);
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    protected Boolean setNx(int db, String key, String value)
    {
        return (Boolean) redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            byte[] k = serialize(key);
            byte[] v = serialize(value);
            return conn.setNX(k, v);
        });
    }

    @SuppressWarnings("unchecked")
    protected String get(int db, String key)
    {
        return (String) redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            byte[] k = serialize(key);
            byte[] value = conn.get(k);
            return value == null ? null : deserialize(value);
        });
    }

    @SuppressWarnings("unchecked")
    protected void del(int db, String... keys)
    {
        redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            byte[][] ks = new byte[keys.length][];
            for(int i = 0; i < keys.length; i++)
            {
                ks[i] = serialize(keys[i]);
            }
            conn.del(ks);
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    protected void hSet(int db, String key, String field, String value)
    {
        redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            byte[] k = serialize(key);
            byte[] f = serialize(field);
            byte[] v = serialize(value);
            return conn.hSet(k, f, v);
        });
    }

    @SuppressWarnings("unchecked")
    protected Long hIncrBy(int db, String key, String field, long number)
    {
        return (Long) redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            byte[] k = serialize(key);
            byte[] f = serialize(field);
            return conn.hIncrBy(k, f, number);
        });
    }

    @SuppressWarnings("unchecked")
    protected String hGet(int db, String key, String field)
    {
        return (String) redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            byte[] k = serialize(key);
            byte[] v = serialize(field);
            byte[] value = conn.hGet(k, v);
            return value == null ? null : deserialize(value);
        });
    }

    @SuppressWarnings("unchecked")
    protected List<String> hMGet(int db, String key, String... fields)
    {
        return (List<String>) redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            byte[] k = serialize(key);
            List<byte[]> v = serializeList(fields);
            List<byte[]> value = conn.hMGet(k, v.toArray(new byte[0][0]));
            return value == null ? null : deserializeList(value);
        });
    }

    @SuppressWarnings("unchecked")
    protected void hDel(int db, String key, String field)
    {
        redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            byte[] k = serialize(key);
            byte[] v = serialize(field);
            conn.hDel(k, v);
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    protected void expire(int db, String key, long seconds)
    {
        redisTemplate.execute((RedisCallback) conn -> {
            conn.select(db);
            byte[] k = serialize(key);
            conn.expire(k, seconds);
            return null;
        });
    }
}

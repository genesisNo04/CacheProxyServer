package com.example.CacheServer.CacheStore;

import com.example.CacheServer.DTO.CacheResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheStore {

    private static final Map<String, CacheResponse> cache = new ConcurrentHashMap<>();

    public static CacheResponse get(String key) {
        return cache.get(key);
    }

    public static void put(String key, CacheResponse value) {
        cache.put(key, value);
    }

    public static void clear() {
        cache.clear();
    }
}

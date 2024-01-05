package site.bleem.boot.demo.manager;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;


public class GuavaCacheManager<K, V> implements CacheManager<K, V> {

    private final LoadingCache<K, V> cache;

    public GuavaCacheManager() {
        this.cache = CacheBuilder.newBuilder()
//                .expireAfterWrite(5, TimeUnit.MINUTES) // 缓存项写入后5分钟过期
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K key) throws Exception {
                        // 默认加载逻辑，可以根据需求修改
                        return null;
                    }
                });
    }

    @Override
    public V get(K key) throws ExecutionException {
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void evict(K key) {
        cache.invalidate(key);
    }

}

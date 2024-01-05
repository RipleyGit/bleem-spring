package site.bleem.boot.demo.manager;


import java.util.concurrent.ExecutionException;

public interface CacheManager<K, V> {

    V get(K key) throws ExecutionException;

    void put(K key, V value);

    void evict(K key);
}
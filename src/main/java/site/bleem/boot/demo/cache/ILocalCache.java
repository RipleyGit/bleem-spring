package site.bleem.boot.demo.cache;

public interface ILocalCache {
    void put(String key, Integer value);

    Integer get(String key);
}
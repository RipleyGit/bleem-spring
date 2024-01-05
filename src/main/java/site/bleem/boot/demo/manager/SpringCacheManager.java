package site.bleem.boot.demo.manager;

import java.util.concurrent.ExecutionException;


public class SpringCacheManager<K, V> implements CacheManager<K, V>{
//    @Cacheable(value = "myCache", key = "#key")
//    public String getFromCache(String key) {
//        // 模拟从数据库或其他数据源获取数据的操作
//        System.out.println("Fetching data from source for key: " + key);
//        return "Data for key: " + key;
//    }
//
//    @CacheEvict(value = "myCache", key = "#key")
//    public void removeFromCache(String key) {
//        // 模拟从缓存中移除数据的操作
//        System.out.println("Removing data from cache for key: " + key);
//    }

    @Override
    public Object get(Object key) throws ExecutionException {
        return null;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public void evict(Object key) {

    }
}

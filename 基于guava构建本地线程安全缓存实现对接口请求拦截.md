# 基于guava构建本地线程安全缓存实现对接口请求拦截
## 需求
由于场景需要，把某个接口开放出来，但这个接口也是比较重要的接口，所以需要对它做一些防护处理，目前设计的一个方案就是：对这个接口的请求参数做MD5编码，加入到本地缓存中，3分钟内相同的参数请求来了，就给它拦截掉。
原因分析：
1. 为啥要做本地缓存？
这个功能不是项目的主要功能，目前项目体量不大，没有需要引入nosql中间件的必要，维护一个中间件集群也是费钱费事。
2. 接口拦截是否过于简单？
还是一样目，前体量不大。也不是核心功能，目前还不值得投入大精力来做防护，但也还是得做，毕竟还是有点影响，所以目前只针对短时间内批量相同的请求做拦截，后续有升级再做更新。

## 技术要点
1. 由于是HTTP接口，线程安全很重要，需要考虑线程安全问题；
2. 服务器内存有限，对内存中的数据需要做定期删除；
3. 防护性功能，不能影响主业务流程，对原代码需要做到零侵入，也是为后面升级做好规划；

## 技术选型
1. 使用guava的本地内存管理，针对内存大小和内存中的数据存储时间过期处理，已是业界有口皆碑；
2. 使用单例模式+ReentrantLock双重加锁保障线程安全的同时，也保障性能效率；
3. 使用spring中的注解+AOP技术实现，对代码的侵入性降到最低；

## 实现方案

### 需要引入的依赖
本项目为maven作为依赖管理的springboot项目，需要引入guava依赖和aop依赖
```
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>31.0.1-jre</version>
        </dependency>

      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
        <version>2.4.13</version>
      </dependency>
```
### 实现本地缓存管理器
1. [设计本地缓存接口，方便后面替换缓存的具体实现；](./src/main/java/site/bleem/boot/demo/cache/ILocalCache.java)
```
public interface ILocalCache {
    void put(String key, Integer value);

    Integer get(String key);
}
```
2. 基于缓存接口实现，缓存管理器。使用单例模式+可重入锁保证进程中有且只有一个缓存示例；
```

@Slf4j
public class LocalCacheManager implements ILocalCache {
    private static Cache<String, Integer> guavaCache = null;
    private static LocalCacheManager localCacheManager = null;
    private static final Lock reentrantLock = new ReentrantLock();

    private LocalCacheManager() {
        initCache();
    }

    public static LocalCacheManager getInstance() {
        if (null == localCacheManager) {
            reentrantLock.lock();
            log.debug("（线程安全，双重检测,可重入锁，高效）");
            try {
                if (null == localCacheManager) {
                    localCacheManager = new LocalCacheManager();
                }
            } finally {
                reentrantLock.unlock();
            }
        }
        return localCacheManager;
    }

    private void initCache() {
        log.debug("初始化缓存");
        log.debug("初始化缓存");
        guavaCache = CacheBuilder
                .newBuilder()            // 写缓存后多久过期
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public void put(String key, Integer value) {
        guavaCache.put(key, value);
    }

    @Override
    public Integer get(String key) {
        return guavaCache.getIfPresent(key);
    }
}
```
### 使用AOP拦截指定注解标注的请求
1. 设计拦截注解：
```
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
    int maxCount() default 1;

    String message();
}
```
2. 拦截AOP实现类：针对AccessLimit注解的接口在方法执行前，进行拦截，对请求参数进行MD5加密，放入本地缓存中，若缓存中存在该MD5值，则说明该请求已经处理过，将注解中的描述封装成异常进行抛出提示。
```
@Slf4j
@Aspect
@Configuration
public class AccessLimitAop {

    @Pointcut("@annotation(site.bleem.boot.web.common.AccessLimit)")
    public void limitAccess() {}

    @Before("limitAccess() && @annotation(accessLimit)")
    public void limitAccessAop(JoinPoint joinPoin, AccessLimit accessLimit) throws Throwable {
        int maxCount = accessLimit.maxCount();
        String message = accessLimit.message();
        Object[] args = joinPoin.getArgs();
        String string = args.toString();
        string = string.trim();
        String md5Hash = StringUtils.getMD5Hash(string);
        ILocalCache instance = LocalCacheManager.getInstance();
        Integer integer = instance.get(md5Hash);
        if (integer == null){
            integer = 0;
        }
        if (integer > maxCount){
            throw new RuntimeException(message);
        }else {
            integer++;
            instance.put(md5Hash,integer);
        }
    }

}
```
### 使用
```
    @AccessLimit(message="参数已处理过")
    @GetMapping("/hello/{word}")
    public ResponseEntity<String> returnWorld(@PathVariable("word") String word) {
        return ResponseEntity.ok("hello "+word);
    }
```

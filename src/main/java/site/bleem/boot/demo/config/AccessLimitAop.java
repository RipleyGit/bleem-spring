package site.bleem.boot.demo.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import site.bleem.boot.demo.cache.ILocalCache;
import site.bleem.boot.demo.cache.LocalCacheManager;
import site.bleem.boot.demo.util.StringUtils;


/**
 * @author yubs
 */
@Aspect
@Configuration
public class AccessLimitAop {

    @Pointcut("@annotation(site.bleem.boot.demo.config.AccessLimit)")
    public void limitAccess() {}

    @Before("limitAccess() && @annotation(accessLimit)")
    public void limitAccessAop(JoinPoint joinPoin, AccessLimit accessLimit) throws Throwable {
        int maxCount = accessLimit.maxCount();
        String message = accessLimit.message();
        Object[] args = joinPoin.getArgs();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i]);
        }
        String string = builder.toString();
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

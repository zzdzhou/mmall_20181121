package jack.project.mmall.common;


import ch.qos.logback.core.util.TimeUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-10
 */
public class TokenCache {


    private static LoadingCache<String, String> localCache = CacheBuilder
            .newBuilder()
            .initialCapacity(1000)
            .maximumSize(10_000)
            .expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                // 默认数据加载实现，当调用get()没有对应的key时，就返回这个
                @Override
                public String load(String key) throws Exception {
                    return null;
                }
            });

    public void add(String key, String value) {
        localCache.put(key, value);
    }

    public String get(String key) {
        try {
            return localCache.get(key);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


}

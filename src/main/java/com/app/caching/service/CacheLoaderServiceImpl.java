package com.app.caching.service;

import com.app.caches.ICache;
import com.app.caching.AppCache;
import com.app.caching.Cache;
import com.app.caching.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service("cacheLoaderService")
public class CacheLoaderServiceImpl {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    protected Map<String, String> cacheNameToCacheBeanNameMap = new HashMap<>();

    public boolean loadAll() {
        Map<String, Object> caches = applicationContext.getBeansWithAnnotation(Cache.class);
        List<Object> cacheList = new ArrayList<>(caches.values());
        Collections.sort(cacheList, Comparator.comparingInt(cache -> cache.getClass().getAnnotation(AppCache.class).PRIORITY()));
        boolean allCachesLoadedSuccessfully = true;
        for (Object cache : cacheList) {
            if (!loadCache(cache)) {
                allCachesLoadedSuccessfully = false;
            }
        }
        return allCachesLoadedSuccessfully;
    }

    private synchronized <T extends ICache> boolean loadCache(Object cache) {
        try {
            ((ICache) cache).load();
            CacheManager.getInstance().setCache(cache);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @PostConstruct
    public void initCacheMappings() {
        Map<String, Object> caches = applicationContext.getBeansWithAnnotation(Cache.class);
        caches.forEach((cacheBeanName, cache) -> {
            Cache cacheAnnotation = cache.getClass().getAnnotation(Cache.class);
            cacheNameToCacheBeanNameMap.put(cacheAnnotation.name(), cacheBeanName);
        });
    }

}

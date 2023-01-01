package com.app.caching;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheManager {

    private static final Logger LOG = LoggerFactory.getLogger(CacheManager.class);
    private static CacheManager _instance = new CacheManager();
    private Map<String, Object> _caches = new ConcurrentHashMap();
    private static final List<CacheChangeListner> _listeners = new ArrayList();

    private CacheManager() {
    }

    public static CacheManager getInstance() {
        return _instance;
    }

    public Object getCache(String cacheName) {
        Object cache = this._caches.get(cacheName);
        return cache;
    }

    public Object getCacheByName(String cacheName) {
        return this.getCache(cacheName);
    }

    public <T> T getCache(Class<T> cacheClass) {
        if (cacheClass.isAnnotationPresent(Cache.class)) {
            return (T) this.getCache(((Cache)cacheClass.getAnnotation(Cache.class)).name());
        } else {
            throw new IllegalArgumentException("@Cache annotation should be present for cache class:" + cacheClass.getName());
        }
    }

    public synchronized void setCache(Object cache) {
        Class<? extends Object> cacheClass = cache.getClass();
        if (cacheClass.isAnnotationPresent(Cache.class)) {
            Method[] arr$ = cacheClass.getDeclaredMethods();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Method m = arr$[i$];
                if ("freeze".equals(m.getName())) {
                    try {
                        m.invoke(cache);
                    } catch (Exception var8) {
                        LOG.error("unable to freeze cache:" + cacheClass.getName(), var8);
                    }
                }
            }

            Cache annotation = (Cache)cacheClass.getAnnotation(Cache.class);
            this._caches.put(annotation.name(), cache);
            this.signalListners();
        } else {
            throw new IllegalArgumentException("@Cache annotation should be present for cache class:" + cache.getClass().getName());
        }
    }

    private void signalListners() {
        Iterator i$ = _listeners.iterator();

        while(i$.hasNext()) {
            CacheChangeListner listener = (CacheChangeListner)i$.next();

            try {
                listener.reloadCache();
            } catch (Exception var4) {
                LOG.error("Error while signalling {} listener for cache change", listener.getClass().getName());
            }
        }

    }

    public static void registerListner(CacheChangeListner listener) {
        _listeners.add(listener);
    }

    public interface CacheChangeListner {
        void reloadCache();
    }
}

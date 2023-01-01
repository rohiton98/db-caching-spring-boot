package com.app.caching.autoreload;

import com.app.caching.service.CacheLoaderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("autoCacheReloadService")
public class AutoCacheReloadServiceImpl implements IAutoCacheReloadService {

    private static final Logger LOG = LoggerFactory.getLogger(AutoCacheReloadServiceImpl.class);

    @Autowired
    private CacheLoaderServiceImpl cacheLoaderService;

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void reload() {
        try {
            LOG.info("Reloading caches upon cron");
            cacheLoaderService.loadAll();
            LOG.info("Caches loaded'");
        } catch(Exception e) {
            LOG.error("Error occurred - ",e);
        }
    }
}
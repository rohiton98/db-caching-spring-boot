package com.app.controller;

import com.app.caching.CacheManager;
import com.app.caching.service.CacheLoaderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/service/myapp/cache/", method = { RequestMethod.GET })
public class CacheController {

    private static final Logger LOG = LoggerFactory.getLogger(CacheManager.class);

    @Autowired
    private CacheLoaderServiceImpl cacheLoaderService;

    @RequestMapping(value = "/reloadCache", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String reloadCache() {
        LOG.info("Cache loading manually");
        if(cacheLoaderService.loadAll()){
            return "Cache loaded successfully";
        } else{
            return "Cache loading failed";
        }
    }


}

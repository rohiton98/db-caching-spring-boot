package com.app.controller;

import com.app.caches.UsersCache;
import com.app.caching.CacheManager;
import com.app.caching.service.CacheLoaderServiceImpl;
import com.app.model.User;
import com.app.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private CacheLoaderServiceImpl cacheLoaderService;

    @Autowired
    UserRepository userRepository;

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/getUsersFromDB")
    @ResponseBody
    public String getUsersFromDB() {
        List<User> userList = userRepository.findAll();
        return userList.size()+" users fetched from DB";
    }

    @RequestMapping(value = "/getUsersFromCache")
    @ResponseBody
    public String getUsersFromCache() {
        UsersCache userCache = CacheManager.getInstance().getCache(UsersCache.class);
        if(userCache!=null){
            List<User> userList = userCache.getAllUsers();
            return userList.size()+" users fetched from Cache";
        }
        return null;
    }
}

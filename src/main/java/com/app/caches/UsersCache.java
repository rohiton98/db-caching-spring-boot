package com.app.caches;

import com.app.caching.Cache;
import com.app.caching.AppCache;
import com.app.model.User;
import com.app.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Cache(name = "usersCache")
@AppCache(PRIORITY = 1)
public class UsersCache implements ICache {

    private static final Logger LOG = LoggerFactory.getLogger(UsersCache.class);

    @Autowired
    private UserRepository userRepository;

    private Map<String,User> userFirstnameToUserMap = new HashMap<>();
    private List<User> users = new ArrayList<>();

    @Override public void load() throws Exception {
        try{
            LOG.info("Loading UsersCache");
            List<User> usersList = userRepository.findAll();
            if(CollectionUtils.isEmpty(usersList)){
                LOG.info("Nothing found in DB to put into cache. Skipping cache load.");
                return;
            }
            usersList.forEach(user -> {
                userFirstnameToUserMap.put(user.getFirstname(), user);
                users.add(user);
            });
            LOG.info("Loaded UsersCache");
        } catch (Exception e){
            LOG.error("Error occurred - ",e);
        }
    }

    public List<User> getAllUsers(){
        return users;
    }

    @Override
    public void freeze() {
        users = Collections.unmodifiableList(users);
        userFirstnameToUserMap = Collections.unmodifiableMap(userFirstnameToUserMap);
    }

}

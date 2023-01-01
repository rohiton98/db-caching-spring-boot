### DB Caching in Spring Boot

This project tells you how you can enable the custom database caching in your Spring Boot application so that instead of hitting queries on database to get the data, you use the cached data to make the response time very fast and avoid database hits frequently.

You can populated the caches with the database data in three ways:

1. Scheduled cron
2. Startup load
3. Manually hitting API

You can even prioritize the caches so that when reloading, the highest priority cache gets loaded first.

```
@Cache(name = "usersCache")
@AppCache(PRIORITY = 1)
public class UsersCache implements ICache {
```

Running this project:

1. Clone
2. Build
3. Update DB credentials
3. Run

Now a load a few users eg 10 in your database and run the below

```
curl localhost:8080/test/getUsersFromDB
```

This will give you the size of users present in your database. 

Now when you try to load the users from cache which is currently empty since we've not reloaded, this will give you the empty result

```
curl localhost:8080/test/getUsersFromCache
```

You can either setup the cron expression so that at that particular time, all your caches will get loaded with the data present in the database at that time. Or you can simply hit the below API to load the data.

```
curl localhost:8080/service/myapp/cache/reloadCache
```

This will populate the cache and when you fetch users now from the cache, you will get the result.

```
curl localhost:8080/test/getUsersFromCache
```

- - -

I hope this helps.

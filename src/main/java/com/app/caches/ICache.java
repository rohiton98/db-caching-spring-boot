package com.app.caches;

public interface ICache {

    void load() throws Exception;

    void freeze();

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.as.test.cdi.instanceCash;

import java.util.HashSet;

/**
 *
 * @author panos
 */
public class ClassInstanceCacheSingleton {
    private static ClassInstanceCacheSingleton cache = new ClassInstanceCacheSingleton();
    
    private HashSet<Object> instanceCache;
    
    private ClassInstanceCacheSingleton(){
        instanceCache = new HashSet<Object>();
    }
    
    public static ClassInstanceCacheSingleton getInstance(){
        return cache;
    }

    /**
     * @return the instanceCache
     */
    public HashSet<Object> getInstanceCache() {
        return instanceCache;
    }
    
    public void addInstanceCacheObject(Object cacheObject) {
        this.instanceCache.add(cacheObject);
    }
    
    public void removeInstanceCacheObject(Object cacheObject) {
        this.instanceCache.remove(cacheObject);
    }
}

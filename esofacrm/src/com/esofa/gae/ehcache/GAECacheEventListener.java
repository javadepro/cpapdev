package com.esofa.gae.ehcache;

import java.util.Properties;
import java.util.logging.Logger;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

import com.esofa.gae.security.GaeAuthenticationFilter;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class GAECacheEventListener implements CacheEventListener, Cloneable {

	private static final Logger log = Logger
			.getLogger(GaeAuthenticationFilter.class.getName());

    private Properties properties;

    /**
     * @param properties
     */
    public GAECacheEventListener(Properties properties) {
        super();
        this.properties = properties;
    }

    /**
     * {@inheritDoc}
     */
    public void dispose() {
        properties = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * {@inheritDoc}
     */
    public void notifyElementPut(Ehcache cache, Element element) throws CacheException {
        if (checkElementKeySerializable(element) && checkElementSerializable(element)) {
         
                log.info("put in MemCache :"+ cache.getName()+ " element with key: " + element.getKey());
            
            long ttlSeconds = cache.getCacheConfiguration().getTimeToLiveSeconds();
            MemcacheService memCache = MemcacheServiceFactory.getMemcacheService(cache.getName());
            memCache.put(element.getKey(), element.getValue(), Expiration.byDeltaSeconds((int) ttlSeconds));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void notifyElementUpdated(Ehcache cache, Element element) throws CacheException {
        if (checkElementKeySerializable(element) && checkElementSerializable(element)) {
           
                log.info("update in MemCache element with key: " + element.getKey());
      
            long ttlSeconds = cache.getCacheConfiguration().getTimeToLiveSeconds();
            MemcacheService memCache = MemcacheServiceFactory.getMemcacheService(cache.getName());
            memCache.put(element.getKey(), element.getValue(), Expiration.byDeltaSeconds((int) ttlSeconds));
        }
    }

    /**
     * {@inheritDoc}
     * Called when a put exceeds the MemoryStore's capacity.
     * This replicator does not propagate this event, as the element
     * is already in MemCache via a previous put().
     */
    public void notifyElementEvicted(Ehcache cache, Element element) {
        //noop
    }

    /**
     * {@inheritDoc}
     * This implementation does not propagate expiries.
     * The memcache and Ehcache expiries should be synced, so that if an element
     * expires in the L1 then it should also be expired in the L2.
     */
    public void notifyElementExpired(Ehcache cache, Element element) {
        //noop
    }

    /**
     * {@inheritDoc}
     */
    public void notifyElementRemoved(Ehcache cache, Element element) throws CacheException {
        if (checkElementKeySerializable(element)) {
                log.info("delete in MemCache element with key: " + element.getKey());
            
            MemcacheService memCache = MemcacheServiceFactory.getMemcacheService(cache.getName());
            memCache.delete(element.getKey());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void notifyRemoveAll(Ehcache cache) {
        // it is too late to do anything, as the cache is already empty
            //log.info("Can not propagate removeAll() for cache: " + cache.getName());

        log.info("removeall in MemCache: " + cache.getName());
            MemcacheService memCache = MemcacheServiceFactory.getMemcacheService(cache.getName());
            memCache.clearAll();
        
    }

    /**
     * Checks the key is Serializable
     *
     * @param element the Element the key is in
     * @return true if Serializable
     */
    protected boolean checkElementKeySerializable(Element element) {
        if (!element.isKeySerializable()) {
                log.info("Key " + element.getObjectKey() + " is not Serializable and cannot be replicated in MemCache.");
            
            return false;
        }
        return true;
    }

    /**
     * Checks the element is Serializable
     *
     * @param element the Element
     * @return true if Serializable
     */
    protected boolean checkElementSerializable(Element element) {
        if (!element.isSerializable()) {
                log.info("Object with key " + element.getObjectKey() + " is not Serializable and cannot be replicated in MemCache.");
            
            return false;
        }
        return true;
    }
}
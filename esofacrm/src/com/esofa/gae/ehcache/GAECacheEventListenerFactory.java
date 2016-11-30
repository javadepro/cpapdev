package com.esofa.gae.ehcache;

import java.util.Properties;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

public class GAECacheEventListenerFactory extends CacheEventListenerFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public CacheEventListener createCacheEventListener(Properties properties) {
        return new GAECacheEventListener(properties);
    }

}
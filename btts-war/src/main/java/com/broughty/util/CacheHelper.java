package com.broughty.util;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created with IntelliJ IDEA.
 * User: matbroughty
 * Date: 23/01/14
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */
public class CacheHelper {

    private static final Logger log = Logger.getLogger(CacheHelper.class.getName());

    public static final String CURRENT_WEEK_STR = "currentWeek";
    public static final String CURRENT_WEEK_WEEKDATA = "currentWeek";
    public static final String CURRENT_FIXTURES_LST = "currentFixtures";


    public static Cache getCache() {
        Cache cache = null;

        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());

        } catch (CacheException e) {
            log.log(Level.WARNING, "Couldn't get hold of cache.", e);
        }
        return cache;
    }


}

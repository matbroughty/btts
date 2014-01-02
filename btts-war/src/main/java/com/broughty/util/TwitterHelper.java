package com.broughty.util;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 21/12/13.
 */
public class TwitterHelper {
    private static final Logger log = Logger.getLogger(TwitterHelper.class.getName());

    private final static String CONSUMER_KEY = "dpXNaxRkySF4Zq48vR8w";
    private final static String CONSUMER_KEY_SECRET = "6EYqrFWrnqfq5DyBODAnYBng0s0NXA82wCHt4xFS4qA";

    private final static String ACCESS_KEY = "2256424050-vUkx6ZNE287kxfmGecuiOQIsHvxnGplBPUAF7bD";
    private final static String ACCESS_KEY_KEY_SECRET = "seU6z28JGfdhVJNudwi6Xgih16XRxiQCpEsV4DidpuUsd";

    public TwitterHelper() {

    }

    public static void updateStatus(String message) {
        try {

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(CONSUMER_KEY)
                    .setOAuthConsumerSecret(CONSUMER_KEY_SECRET)
                    .setOAuthAccessToken(ACCESS_KEY)
                    .setOAuthAccessTokenSecret(ACCESS_KEY_KEY_SECRET);
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();


            //twitter.updateStatus(message);

        } catch (Throwable t) {
            log.log(Level.WARNING, "failed to send twitter message -> " + message, t);

        }
    }

}

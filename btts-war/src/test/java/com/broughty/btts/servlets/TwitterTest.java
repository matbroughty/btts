package com.broughty.btts.servlets;

import com.broughty.util.TwitterHelper;
import net.unto.twitter.Api;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by matbroughty on 21/12/13.
 */
public class TwitterTest {

    private final static String CONSUMER_KEY = "dpXNaxRkySF4Zq48vR8w";
    private final static String CONSUMER_KEY_SECRET = "6EYqrFWrnqfq5DyBODAnYBng0s0NXA82wCHt4xFS4qA";


    @Test
    public void testTwitter() {
        try {
            Api api = Api.builder().username("broughty_btts").password("0Password1").build();
            net.unto.twitter.TwitterProtos.Status status = api.updateStatus("Hello BTTS!").build().post();
        } catch (Throwable t) {
            t.printStackTrace(System.out);
        }
    }


    @Test
    public void testtwitter4j() {


        TwitterHelper.updateStatus("Hello");
    }
}

package com.broughty.util;

import com.google.appengine.api.datastore.*;

/**
 * Created by matbroughty on 26/12/13.
 */
public class BTTSHelper {


    public static final int SCORELESS = 0;
    public static final int ONE_TEAM_SCORED = 1;
    public static final int BOTH_TEAMS_SCORED = 3;

    public static final String SUCCESS = "&#10004;";
    public static final String FAIL = "&#10008;";
    public static final String WAITING = "&#9749";


    public static String bothTeamsScored(Object choiceResult) {
        if (choiceResult == null || !(choiceResult instanceof Boolean)) {
            return WAITING;
        }
        return Boolean.valueOf((Boolean) choiceResult) ? SUCCESS : FAIL;
    }


    public static String bothTeamsScoredHuman(Object choiceResult) {
        if (choiceResult == null || !(choiceResult instanceof Boolean)) {
            return "?";
        }
        return Boolean.toString((Boolean) choiceResult);
    }


    public static String getCurrentWeek(){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query q = new Query("CurrentWeek");
        PreparedQuery pq = datastore.prepare(q);

        Entity currentWeek = pq.asSingleEntity();
        String week = "N/A";
        if (currentWeek != null) {
            week = (String) currentWeek.getProperty("week");
        }

        return week;
    }

}

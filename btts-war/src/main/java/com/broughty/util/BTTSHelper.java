package com.broughty.util;

import com.google.appengine.api.datastore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 26/12/13.
 */
public class BTTSHelper {
    private static final Logger log = Logger.getLogger(BTTSHelper.class.getName());

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


    public static List<Integer> getPlayerPoints(String playerName){
        log.log(Level.FINE, "getting player points for " + playerName);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Choices");
        query.addFilter("player", Query.FilterOperator.EQUAL, playerName);
        PreparedQuery pq = datastore.prepare(query);

        List<Integer> results = new ArrayList<Integer>();
        for (Entity choice : pq.asIterable()) {

            if(choice.getProperty("choice1Points") != null){
                results.add(sumPlayerWeeklyPoints(choice));
            }
        }

       log.log(Level.FINE, "player points for " + playerName + " = " + results);
       return results;

    }

    private static Integer sumPlayerWeeklyPoints(Entity choice) {
        Integer points = new Integer(0);
        points = points + (choice.getProperty("choice1Points") != null ? (Integer)choice.getProperty("choice1Points") : 0);
        points = points + (choice.getProperty("choice2Points") != null ? (Integer)choice.getProperty("choice1Points") : 0);
        points = points + (choice.getProperty("choice3Points") != null ? (Integer)choice.getProperty("choice1Points") : 0);
        points = points + (choice.getProperty("choice4Points") != null ? (Integer)choice.getProperty("choice1Points") : 0);
        return points;
    }


}

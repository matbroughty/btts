package com.broughty.util;

import com.broughty.model.PlayerChoicesData;
import com.google.appengine.api.datastore.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final Logger log = Logger.getLogger(BTTSHelper.class.getName());

    public static String bothTeamsScored(Object choiceResult) {
        if (choiceResult == null || !(choiceResult instanceof Boolean)) {
            return WAITING;
        }
        return Boolean.valueOf((Boolean) choiceResult) ? SUCCESS : FAIL;
    }

    public static ResultEnum bothTeamsScoredResult(Object choiceResult) {
        if (choiceResult == null || !(choiceResult instanceof Boolean)) {
            return ResultEnum.WAITING;
        }
        return Boolean.valueOf((Boolean) choiceResult) ? ResultEnum.SUCCESS : ResultEnum.FAIL;
    }

    public static String bothTeamsScoredHuman(Object choiceResult) {
        if (choiceResult == null || !(choiceResult instanceof Boolean)) {
            return "?";
        }
        return Boolean.toString((Boolean) choiceResult);
    }

    public static boolean entityPropertyAsBoolean(Object choiceResult) {
        if (choiceResult == null || !(choiceResult instanceof Boolean)) {
            return false;
        }
        return (Boolean) choiceResult;
    }

    public static Number entityPropertyAsNumber(Object choiceResult) {
        if (choiceResult == null) {
            return 0;
        }
        return (Number) choiceResult;
    }

    public static String getCurrentWeek() {
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

    public static List<Integer> getPlayerPoints(String playerName) {
        log.log(Level.FINE, "getting player points for " + playerName);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Choices");
        query.addFilter("player", Query.FilterOperator.EQUAL, playerName);
        PreparedQuery pq = datastore.prepare(query);

        List<Integer> results = new ArrayList<Integer>();
        for (Entity choice : pq.asIterable()) {

            if (choice.getProperty("choice1Points") != null) {
                results.add(sumPlayerWeeklyPoints(choice));
            }
        }

        log.log(Level.FINE, "player points for " + playerName + " = " + results);
        return results;

    }

    private static Integer sumPlayerWeeklyPoints(Entity choice) {
        Integer points = new Integer(0);
        points = points + (choice.getProperty("choice1Points") != null ? ((Number) choice.getProperty("choice1Points")).intValue() : 0);
        points = points + (choice.getProperty("choice2Points") != null ? ((Number) choice.getProperty("choice2Points")).intValue() : 0);
        points = points + (choice.getProperty("choice3Points") != null ? ((Number) choice.getProperty("choice3Points")).intValue() : 0);
        points = points + (choice.getProperty("choice4Points") != null ? ((Number) choice.getProperty("choice4Points")).intValue() : 0);
        return points;
    }

    public static List<PlayerChoicesData> getWeeksChoices(String week) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


        Key weekKey = KeyFactory.createKey("Week", week);
        Query query = new Query("Choices", weekKey).addSort("date", Query.SortDirection.DESCENDING);
        List<Entity> choices = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(50));
        List<PlayerChoicesData> weeksChoices = new ArrayList<PlayerChoicesData>();
        for (Entity choice : choices) {
            PlayerChoicesData playerChoicesData = new PlayerChoicesData(week);
            playerChoicesData.setPlayer((String) choice.getProperty(ChoicesEntityEnum.PLAYER.getFieldName()));
            playerChoicesData.setAlerted(BTTSHelper.entityPropertyAsBoolean(choice.getProperty(ChoicesEntityEnum.ALERTED.getFieldName())));
            playerChoicesData.setChoice1((String) choice.getProperty(ChoicesEntityEnum.CHOICE_ONE.getFieldName()));
            playerChoicesData.setChoice2((String) choice.getProperty(ChoicesEntityEnum.CHOICE_TWO.getFieldName()));
            playerChoicesData.setChoice3((String) choice.getProperty(ChoicesEntityEnum.CHOICE_THREE.getFieldName()));
            playerChoicesData.setChoice4((String) choice.getProperty(ChoicesEntityEnum.CHOICE_FOUR.getFieldName()));

            playerChoicesData.setChoice1Points(BTTSHelper.entityPropertyAsNumber(choice.getProperty(ChoicesEntityEnum.CHOICE_ONE_POINTS.getFieldName())).intValue());
            playerChoicesData.setChoice2Points(BTTSHelper.entityPropertyAsNumber(choice.getProperty(ChoicesEntityEnum.CHOICE_TWO_POINTS.getFieldName())).intValue());
            playerChoicesData.setChoice3Points(BTTSHelper.entityPropertyAsNumber(choice.getProperty(ChoicesEntityEnum.CHOICE_THREE_POINTS.getFieldName())).intValue());
            playerChoicesData.setChoice4Points(BTTSHelper.entityPropertyAsNumber(choice.getProperty(ChoicesEntityEnum.CHOICE_FOUR_POINTS.getFieldName())).intValue());

            playerChoicesData.setChoice1Result(ResultEnum.fromBoolean((Boolean) choice.getProperty(ChoicesEntityEnum.CHOICE_ONE_RESULT.getFieldName())));
            playerChoicesData.setChoice2Result(ResultEnum.fromBoolean((Boolean) choice.getProperty(ChoicesEntityEnum.CHOICE_TWO_RESULT.getFieldName())));
            playerChoicesData.setChoice3Result(ResultEnum.fromBoolean((Boolean) choice.getProperty(ChoicesEntityEnum.CHOICE_THREE_RESULT.getFieldName())));
            playerChoicesData.setChoice4Result(ResultEnum.fromBoolean((Boolean) choice.getProperty(ChoicesEntityEnum.CHOICE_FOUR_RESULT.getFieldName())));


            playerChoicesData.setDateEntered((Date) choice.getProperty(ChoicesEntityEnum.DATE.getFieldName()));

            playerChoicesData.setDefaultChoices(BTTSHelper.entityPropertyAsBoolean(choice.getProperty(ChoicesEntityEnum.DEFAULT.getFieldName())));

            weeksChoices.add(playerChoicesData);
        }


        return weeksChoices;
    }
}

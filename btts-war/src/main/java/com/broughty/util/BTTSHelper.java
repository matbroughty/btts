package com.broughty.util;

import com.broughty.model.PlayerChoicesData;
import com.broughty.model.PlayerData;
import com.google.appengine.api.datastore.*;
import org.apache.commons.lang3.StringUtils;

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
        query.addFilter(ChoicesEntityEnum.PLAYER.getFieldName(), Query.FilterOperator.EQUAL, playerName);
        PreparedQuery pq = datastore.prepare(query);

        List<Integer> results = new ArrayList<Integer>();
        for (Entity choice : pq.asIterable()) {

            // this will act as a week where we weren't checking....
            log.info("Checking choice1Points value : " + choice.getProperty("choice1Points"));
            if (choice.getProperty("choice1Points") != null) {
                results.add(sumPlayerWeeklyPoints(choice, true));
            }
        }

        log.info("player points for " + playerName + " = " + results);
        return results;

    }

    public static Integer getPlayerBothTeamScoredCount(String playerName) {
        log.log(Level.FINE, "getting number of BTTS for " + playerName);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Choices");
        query.addFilter(ChoicesEntityEnum.PLAYER.getFieldName(), Query.FilterOperator.EQUAL, playerName);
        PreparedQuery pq = datastore.prepare(query);
        int bttsCount = 0;
        for (Entity choice : pq.asIterable()) {

            if (choice.getProperty("choice1Points") != null) {
                bttsCount += sumPlayerWeeklyPoints(choice, false);
            }
        }

        log.info("player count btts for " + playerName + " = " + bttsCount);
        return bttsCount;

    }


    private static Integer sumPlayerWeeklyPoints(Entity choice, boolean pointSum) {
        Integer points = new Integer(0);
        // number of times got 3 points
        Integer bttsCount = 0;
        int choice1 = (choice.getProperty(ChoicesEntityEnum.CHOICE_ONE_POINTS.getFieldName()) != null ? ((Number) choice.getProperty(ChoicesEntityEnum.CHOICE_ONE_POINTS.getFieldName())).intValue() : 0);
        points = points + choice1;
        if (choice1 == BTTSHelper.BOTH_TEAMS_SCORED) {
            bttsCount++;
        }
        int choice2 = (choice.getProperty(ChoicesEntityEnum.CHOICE_TWO_POINTS.getFieldName()) != null ? ((Number) choice.getProperty(ChoicesEntityEnum.CHOICE_TWO_POINTS.getFieldName())).intValue() : 0);
        points = points + choice2;
        if (choice2 == BTTSHelper.BOTH_TEAMS_SCORED) {
            bttsCount++;
        }
        int choice3 = (choice.getProperty(ChoicesEntityEnum.CHOICE_THREE_POINTS.getFieldName()) != null ? ((Number) choice.getProperty(ChoicesEntityEnum.CHOICE_THREE_POINTS.getFieldName())).intValue() : 0);
        points = points + choice3;
        if (choice3 == BTTSHelper.BOTH_TEAMS_SCORED) {
            bttsCount++;
        }

        int choice4 = (choice.getProperty(ChoicesEntityEnum.CHOICE_FOUR_POINTS.getFieldName()) != null ? ((Number) choice.getProperty(ChoicesEntityEnum.CHOICE_FOUR_POINTS.getFieldName())).intValue() : 0);
        points = points + choice4;
        if (choice4 == BTTSHelper.BOTH_TEAMS_SCORED) {
            bttsCount++;
        }
        return pointSum ? points : bttsCount;
    }


    public static PlayerChoicesData getPlayersWeeksChoices(String week, String playerName) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        if(StringUtils.containsIgnoreCase(playerName, "Star")){
            playerName = PlayerEnum.Star.getName();
        }
        Key weekKey = KeyFactory.createKey("Week", week);
        Query query = new Query("Choices", weekKey).addFilter(ChoicesEntityEnum.PLAYER.getFieldName(), Query.FilterOperator.EQUAL, playerName);
        Entity choice = datastore.prepare(query).asSingleEntity();
        PlayerChoicesData playerChoicesData = createPlayerChoicesData(week, choice);
        return playerChoicesData;
    }


    public static List<PlayerChoicesData> getWeeksChoices(String week) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


        Key weekKey = KeyFactory.createKey("Week", week);
        Query query = new Query("Choices", weekKey).addSort(ChoicesEntityEnum.DATE.getFieldName(), Query.SortDirection.DESCENDING);
        List<Entity> choices = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(50));
        List<PlayerChoicesData> weeksChoices = new ArrayList<PlayerChoicesData>();
        for (Entity choice : choices) {
            weeksChoices.add(createPlayerChoicesData(week, choice));
        }
        return weeksChoices;
    }

    private static PlayerChoicesData createPlayerChoicesData(String week, Entity choice) {
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


        return playerChoicesData;
    }


    public static List<PlayerData> getPlayers() {
        List<PlayerData> players = new ArrayList<PlayerData>(PlayerEnum.values().length);
        for (PlayerEnum player : PlayerEnum.values()) {
            PlayerData playerData = new PlayerData(player.getName(), player.getEmail(), player.getMobile());
            players.add(playerData);
        }
        return players;
    }
}

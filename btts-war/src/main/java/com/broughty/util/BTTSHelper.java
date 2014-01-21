package com.broughty.util;

import com.broughty.model.FixtureData;
import com.broughty.model.PlayerChoicesData;
import com.broughty.model.PlayerData;
import com.broughty.model.WeekData;
import com.google.appengine.api.datastore.*;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 26/12/13.
 */
public class BTTSHelper {
    public static final int SCORELESS = 0;
    public static final int ONE_TEAM_SCORED = 1;
    public static final int BOTH_TEAMS_SCORED = 3;
    public static final String SUCCESS = "<i class=\"fa fa-thumbs-up\"></i>";
    public static final String FAIL = "<i class=\"fa fa-thumbs-down\"></i>";
    public static final String WAITING = "<i class=\"fa fa-question-circle\"></i>";
    private static final Logger log = Logger.getLogger(BTTSHelper.class.getName());

    /**
     * 10 seconds timeout...
     */
    public static int PAGE_READ_TIMEOUT = 30000;

    private static WeekData currentWeekData;

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
        if (StringUtils.containsIgnoreCase(playerName, "Star")) {
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

    /**
     * Get choices for all weeks...
     *
     * @param playerName
     * @return
     */
    public static List<PlayerChoicesData> getAllPlayersChoices(String playerName) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query query = new Query("Choices").addFilter(ChoicesEntityEnum.PLAYER.getFieldName(), Query.FilterOperator.EQUAL, playerName).addSort(ChoicesEntityEnum.DATE.getFieldName(), Query.SortDirection.DESCENDING);
        List<Entity> choices = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(100));
        List<PlayerChoicesData> allChoices = new ArrayList<PlayerChoicesData>();

        for (Entity choice : choices) {
            allChoices.add(createPlayerChoicesData(choice));
        }
        return allChoices;
    }

    private static PlayerChoicesData createPlayerChoicesData(Entity choice) {
        return createPlayerChoicesData(choice.getKey().getParent().getName(), choice);
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

        playerChoicesData.calculatePointsTotal();

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

    public static WeekData getCurrentWeekData() {

        WeekData weekData = new WeekData();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


        Query q = new Query("CurrentWeek");
        PreparedQuery pq = datastore.prepare(q);

        Entity currentWeek = pq.asSingleEntity();
        String week = "N/A";
        weekData.setWeekNumber(week);
        if (currentWeek != null) {
            weekData.setWeekNumber((String) currentWeek.getProperty("week"));
            weekData.setStartDate((Date) currentWeek.getProperty("startDate"));
            weekData.setEndDate((Date) currentWeek.getProperty("endDate"));
        }
        return weekData;
    }

    /**
     * Method to get the fixtures for a specified set of competitions and a date range
     * @param fromDate
     * @param toDate
     * @return Collection<FixtureData>
     */
    public static Collection<FixtureData> getFixtures(String url, Date fromDate, Date toDate) {
        log.info("Getting fixtures for URL '" + url + "' from '" + fromDate + "' to  '" + toDate + "'.");
        Collection<FixtureData> fixtureDataList = new ArrayList<FixtureData>();
        try {
            fixtureDataList.addAll(getFixtures(Jsoup.connect(url).timeout(PAGE_READ_TIMEOUT).get(),fromDate, toDate));
        } catch (IOException e) {
            log.log(Level.WARNING, "problem with getFixtures .", e);
        }
        return fixtureDataList;
    }

    /**
     * Method to get the fixtures for a specified competition and date range
     * @param document
     * @param fromDate
     * @param toDate
     * @return Collection<FixtureData>
     */
    private static Collection<FixtureData> getFixtures(Document document, Date fromDate, Date toDate) {
        Collection<FixtureData> fixtureDataList = new ArrayList<FixtureData>();
        int i = 0;
        // Get the fixtures-data section
        Element fixturesData = document.getElementById("fixtures-data");
        // Iterate through each table-stats section
        Elements tableStats = fixturesData.getElementsByClass("table-stats");
        for (Element table : tableStats) {
            // Get the fixture date (each table-stats has a corresponding table-header)
            String fixtureDate = null;
            if (document.getElementsByClass("table-header") != null && document.getElementsByClass("table-header").size() >= i) {
                fixtureDate = document.getElementsByClass("table-header").get(i).text().toString().trim();
            }
            // Get the competition title (each table has a competition title)
            String competitionTitle = "";
            if (table.getElementsByClass("competition-title") != null && table.getElementsByClass("competition-title").size() >= 1) {
                competitionTitle = table.getElementsByClass("competition-title").get(1).text().toString().trim();
            }
            // Only include fixtures on this date if within range
            if (includeFixture(getFixtureDate(fixtureDate),fromDate,toDate))	{
                // Iterate through each preview section
                Elements previews = table.getElementsByClass("preview");
                for (Element preview : previews) {
                    // Iterate through each fixture
                    Elements fixtures = preview.getElementsByClass("match-details");
                    for (Element fixture : fixtures) {
                        // Get the kick off time (each fixture has a corresponding kickoff section - unless postponed!)
                        String kickOffTime = "15:00";
                        if (preview.getElementsByClass("kickoff") != null && !preview.getElementsByClass("kickoff").isEmpty())	{
                            kickOffTime = preview.getElementsByClass("kickoff").get(0).text().toString().trim();
                        }
                        // Get the fixture Id
                        Elements teams = fixture.getElementsByTag("a");
                        if (teams != null & teams.size() == 2) {
                            // Add fixture to collection
                            fixtureDataList.add(new FixtureData(
                                    teams.get(0).getElementsByTag("a").text().trim(),
                                    teams.get(1).getElementsByTag("a").text().trim(),
                                    getFixtureDateTime(fixtureDate + " " + kickOffTime),
                                    preview.attributes().get("id").trim(),
                                    competitionTitle));
                        }
                    }
                }
            }
            i++;
        }
        return fixtureDataList;
    }

    /**
     * Method to convert fixture date string into a Date
     * @param fixtureDate (e.g. Saturday 18th January 2014)
     * @return Date
     */
    private static Date getFixtureDate(String fixtureDate) {
        fixtureDate = fixtureDate.replaceAll("(.*[0-9]{1,2})(st|nd|rd|th)(.*)","$1$3");
        try {
            return new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.ENGLISH).parse(fixtureDate);
        } catch (Exception e) {
            log.log(Level.WARNING, "problem parsing fixture date.", e);
            return null;
        }
    }

    /**
     * Method to convert fixture date/time string into a Date
     * @param fixtureDate (e.g. Saturday 18th January 2014 15:00)
     * @return Date
     */
    private static Date getFixtureDateTime(String fixtureDate) {
        fixtureDate = fixtureDate.replaceAll("(.*[0-9]{1,2})(st|nd|rd|th)(.*)","$1$3");
        try {
            return new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm", Locale.ENGLISH).parse(fixtureDate);
        } catch (Exception e) {
            log.log(Level.WARNING, "problem parsing fixture date.", e);
            return null;
        }
    }

    /**
     * Method to determine whether or not to include this fixture
     * @param fixtureDate
     * @param fromDate
     * @param toDate
     * @return boolean
     */
    private static boolean includeFixture(Date fixtureDate, Date fromDate, Date toDate) {
        if (fixtureDate != null && fromDate != null
                && fixtureDate.compareTo(fromDate) < 0) {
            return false;
        }
        if (fixtureDate != null && toDate != null
                && fixtureDate.compareTo(toDate) > 0) {
            return false;
        }
        return true;
    }

    private static boolean haveBothTeamsScored(String url, String fixtureId)	{
        try {
            Element matchRow = Jsoup.connect(url).timeout(PAGE_READ_TIMEOUT).get().getElementById(fixtureId);
            if (matchRow != null && matchRow.getElementsByClass("match-details") != null)	{
                Element matchDetails = matchRow.getElementsByClass("match-details").get(0);
                if (matchDetails != null && matchDetails.getElementsByTag("abbr") != null)	{
                    String[] scores = matchDetails.getElementsByTag("abbr").get(0).text().trim().split("-");
                    if (scores != null && scores.length > 1)	{
                        if (Integer.parseInt(scores[0]) > 0 && Integer.parseInt(scores[1]) > 0) {
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }



}

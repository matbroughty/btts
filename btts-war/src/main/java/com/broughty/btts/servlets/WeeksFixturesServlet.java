package com.broughty.btts.servlets;

import com.broughty.util.*;
import com.google.appengine.api.datastore.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: OPC
 * Date: 30/11/13
 * Time: 19:43
 * To change this template use File | Settings | File Templates.
 */
public class WeeksFixturesServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(WeeksFixturesServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.info("Initialising WeeksFixturesServlet");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        StringBuilder responseString = new StringBuilder();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("CurrentWeek");
        PreparedQuery pq = datastore.prepare(q);

        Entity currentWeek = pq.asSingleEntity();
        String weekNumber = "N/A";
        DateTime startDate = new DateTime();
        DateTime endDate = startDate.plusDays(3);
        if (currentWeek != null) {
            weekNumber = (String) currentWeek.getProperty("week");
            startDate = new DateTime(currentWeek.getProperty("startDate"));
            endDate = new DateTime(currentWeek.getProperty("endDate"));
        }


        String mobileNumber = request.getParameter("mobile");

        if (StringUtils.isNotBlank(mobileNumber)) {
            twitterAlert(new StringBuilder("Refresh request from mobile ending  ").append(StringUtils.right(mobileNumber, 5)));

        }

        twitterAlert(new StringBuilder("Checking live scores for week " + weekNumber + " at time " + new Date().toString()));

        Key weekKey = KeyFactory.createKey("Week", weekNumber);


        // work through all leagues
        for (ResultsWebPageEnum resultPage : ResultsWebPageEnum.values()) {
            try {
                log.info("Processing page " + resultPage.getPage());
                Connection connection = Jsoup.connect(resultPage.getPage());
                connection.timeout(30000);

                Document doc = connection.get();
                response.setContentType("text/plain");

                Elements elements = doc.getElementsByClass("match-details");

                for (Element element : elements) {

                    try {

                        if (!StringUtils.equalsIgnoreCase(element.text(), "fixture")) {

                            String fixtureDateStr = StringUtils.substringAfter(element.parent().parent().parent().child(0).text(),
                                    "This table charts the fixtures during ");


                            fixtureDateStr = StringUtils.substringAfter(fixtureDateStr, " ");
                            fixtureDateStr = StringUtils.remove(fixtureDateStr, "th");
                            fixtureDateStr = StringUtils.remove(fixtureDateStr, "nd");
                            fixtureDateStr = StringUtils.remove(fixtureDateStr, "rd");
                            fixtureDateStr = StringUtils.remove(fixtureDateStr, "st");
                            DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                                    .appendDayOfMonth(2)
                                    .appendLiteral(' ')
                                    .appendMonthOfYearText()
                                    .appendLiteral(' ')
                                    .appendYear(4, 4)
                                    .toFormatter();


                            DateTime fixtureDate = fmt.parseDateTime(fixtureDateStr);
                            DateTime currentDate = new DateTime();


                            // no point in processing any more data
                            if (fixtureDate.toDateMidnight().isBefore(startDate.toDateMidnight()) || fixtureDate.toDateMidnight().isAfter(endDate.toDateMidnight())) {
                                log.log(Level.FINE, "Fixture date  " + fixtureDate.toString() + " is before or after the cut off date.");
                                break;
                            }

                            log.log(Level.FINE, ("processing match " + element.text() + " on date: " + fixtureDateStr));

                            // if it contains a " V " then it isn't in progress...
                            if (!StringUtils.containsIgnoreCase(element.text(), " V ") && !StringUtils.containsIgnoreCase(element.text(), "P-P")) {

                                String homeTeam = StringUtils.substringBeforeLast(StringUtils.substringBefore(element.text(), "-"), " ");
                                Integer homeTeamScore = Integer.valueOf(StringUtils.substringAfterLast(StringUtils.substringBefore(element.text(), "-"), " "));
                                log.info("processing home match " + homeTeam + " on score : " + homeTeamScore);


                                String awayTeam = StringUtils.substringAfter(StringUtils.substringAfter(element.text(), "-"), " ");
                                System.out.println("processing away match " + awayTeam);
                                Integer awayTeamScore = Integer.valueOf(StringUtils.substringBefore(StringUtils.substringAfter(element.text(), "-"), " "));
                                log.info("processing away match " + awayTeam + " on score : " + awayTeamScore);

                                // game has at least kicked off so update

                                updateChoices(homeTeam, weekKey, responseString, homeTeamScore, awayTeamScore);


                            }


                        }


                    } catch (Throwable t) {
                        log.log(Level.WARNING, "Failed to process element " + element.text() + " on resultPage  " + resultPage.getPage(), t);
                    }

                }


            } catch (Throwable t) {
                // try the next page.
                log.log(Level.WARNING, "Failed to process fixture page for week " + weekNumber, t);
            }
        }


        alert(weekKey, weekNumber);


        response.getWriter().write(responseString.toString());
    }

    private void alert(Key weekKey, String weekNumber) {

        log.info("Checking for BTTS alerts:");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Choices", weekKey).addSort("date", Query.SortDirection.DESCENDING);
        List<Entity> choices = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(50));

        for (Entity choice : choices) {
            // This user has already been notified..
            String playerName = (String) choice.getProperty("player");
            Boolean alerted = (Boolean) choice.getProperty("alerted");
            if (alerted != null && alerted.booleanValue()) {
                log.info("Player " + playerName + " has already been notified they have all 4.");
                continue;
            }
            StringBuilder alertString = new StringBuilder();
            boolean[] allTeamsScored = new boolean[4];
            boolean globalAlert = false;
            if (StringUtils.contains(playerName, PlayerEnum.Star.toString())) {
                globalAlert = true;
                alertString.append("Yes!!!! The main bet came in!");
            } else {
                alertString.append(playerName);
                alertString.append(" got all 4 choices!");
            }
            for (int i = 1; i <= 4; i++) {
                allTeamsScored[i - 1] = (Boolean) choice.getProperty("choice" + i + "Result");
                alertString.append("\n");
                alertString.append(choice.getProperty("choice" + i));
            }

            alertString.append("\n");
            alertString.append("http://btts.broughty.com/summary.jsp?week=");
            alertString.append(weekNumber);

            if (allTeamsScored[0] && allTeamsScored[1] && allTeamsScored[2] && allTeamsScored[3]) {
                log.info("All 4 teams scored for " + alertString.toString());
                emailAlert(alertString, weekNumber, playerName);
                SmsHelper.mobileAlert(alertString, globalAlert, PlayerEnum.valueOf(playerName));
                twitterAlert(alertString);
                choice.setProperty("alerted", Boolean.TRUE);
                datastore.put(choice);
            }
        }
    }

    private void twitterAlert(StringBuilder alertString) {
        try {
            TwitterHelper.updateStatus(alertString.toString());
        } catch (Throwable t) {
            log.log(Level.WARNING, "Couldn't send twitter message -  " + alertString.toString(), t);
        }
    }

    private void emailAlert(StringBuilder alertString, String weekNumber, String playerName) {
        try {

            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            Message msg = new MimeMessage(session);


            msg.setFrom(new InternetAddress("broughty@broughtybtts.appspotmail.com", "Broughty.com Admin"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("btts@broughty.com"));
            msg.setSubject("BTTS: " + playerName + " got all 4 selections correct! Week " + weekNumber);

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(alertString.toString(), "text/html");

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(htmlPart);
            msg.setContent(mp);

            Transport.send(msg);

        } catch (AddressException e) {
            log.log(Level.SEVERE, "An email AddressException error message.", e);
        } catch (MessagingException e) {
            log.log(Level.SEVERE, "An email MessagingException error message.", e);
        } catch (Throwable t) {
            log.log(Level.SEVERE, "An unhandled email error message.", t);
        }


    }

    private void updateChoices(String homeTeam, Key weekKey, StringBuilder responseStr, int homeScore, int awayScore) {
        log.info("Checking choices records for BTTS for home team " + homeTeam);


        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        for (int i = 1; i <= 4; i++) {
            Query query = new Query("Choices", weekKey).addFilter("choice" + i, Query.FilterOperator.EQUAL, homeTeam);
            PreparedQuery pq = datastore.prepare(query);
            for (Entity choice : pq.asIterable()) {

                // if game hasn't been marked yet then show it is under way.
                if (choice.getProperty("choice" + i + "Result") == null){
                    choice.setProperty("choice" + i + "Result", Boolean.FALSE);
                }

                // update points
                if(homeScore > 0 || awayScore > 0){
                    if(homeScore > 0 && awayScore > 0){
                        choice.setProperty("choice" + i + "Points", BTTSHelper.BOTH_TEAMS_SCORED);
                    }else{
                        choice.setProperty("choice" + i + "Points", BTTSHelper.ONE_TEAM_SCORED);
                    }
                }

                if (homeScore > 0 && awayScore > 0) {
                    responseStr.append("Both teams scored in game : ");
                    responseStr.append(homeTeam);
                    responseStr.append("\n");

                    log.info("Player " + choice.getProperty("player") + " selected " + homeTeam + " and BTTS!");
                    responseStr.append("Player '");
                    responseStr.append(choice.getProperty("player"));

                    responseStr.append("' selected' ");
                    responseStr.append(homeTeam);
                    responseStr.append("' as choice" + i);
                    responseStr.append("\n");
                    if (!((Boolean) choice.getProperty("choice" + i + "Result")).booleanValue()) {
                        StringBuilder bttsMessage = new StringBuilder("Updating Player '");
                        bttsMessage.append(choice.getProperty("player"));
                        bttsMessage.append("' as both teams scored in home team game: ");
                        bttsMessage.append(choice.getProperty("choice" + i));


                        log.info(bttsMessage.toString() + "choice" + i + "Result");
                        twitterAlert(bttsMessage);

                        if (StringUtils.equalsIgnoreCase((String) choice.getProperty("player"), PlayerEnum.Mat.getName())) {
                            SmsHelper.mobileAlert(bttsMessage, false, PlayerEnum.Mat);
                        }

                        choice.setProperty("choice" + i + "Result", Boolean.TRUE);
                        datastore.put(choice);


                        // now tell the updated player how they are doing...
                        StringBuilder twitterPlayerResult = new StringBuilder();

                        String playerName = (String) choice.getProperty("player");

                        twitterPlayerResult.append(playerName);
                        twitterPlayerResult.append(" results so far ");
                        twitterPlayerResult.append("\n");

                        String choice1 = (String) choice.getProperty("choice1");
                        twitterPlayerResult.append(choice1);
                        twitterPlayerResult.append(":");
                        twitterPlayerResult.append(BTTSHelper.bothTeamsScoredHuman(choice.getProperty("choice1Result")));
                        twitterPlayerResult.append("\n");

                        String choice2 = (String) choice.getProperty("choice2");

                        twitterPlayerResult.append(choice2);
                        twitterPlayerResult.append(":");
                        twitterPlayerResult.append(BTTSHelper.bothTeamsScoredHuman(choice.getProperty("choice2Result")));
                        twitterPlayerResult.append("\n");

                        String choice3 = (String) choice.getProperty("choice3");

                        twitterPlayerResult.append(choice3);
                        twitterPlayerResult.append(":");
                        twitterPlayerResult.append(BTTSHelper.bothTeamsScoredHuman(choice.getProperty("choice3Result")));
                        twitterPlayerResult.append("\n");

                        String choice4 = (String) choice.getProperty("choice4");

                        twitterPlayerResult.append(choice4);
                        twitterPlayerResult.append(":");
                        twitterPlayerResult.append(BTTSHelper.bothTeamsScoredHuman(choice.getProperty("choice4Result")));
                        twitterPlayerResult.append("");

                        twitterAlert(twitterPlayerResult);

                    }
                }

            }
        }


    }

}

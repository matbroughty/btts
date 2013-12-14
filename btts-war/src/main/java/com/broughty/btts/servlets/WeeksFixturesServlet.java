package com.broughty.btts.servlets;

import com.broughty.util.ResultsWebPageEnum;
import com.google.appengine.api.datastore.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
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
            endDate =  new DateTime(currentWeek.getProperty("endDate"));
        }

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

                    try{

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
                        if (!StringUtils.contains(element.text(), " V ") && !StringUtils.contains(element.text(), "P-P")) {

                            String homeTeam = StringUtils.substringBeforeLast(StringUtils.substringBefore(element.text(), "-"), " ");
                            Integer homeTeamScore = Integer.valueOf(StringUtils.substringAfterLast(StringUtils.substringBefore(element.text(), "-"), " "));
                            log.info("processing home match " + homeTeam + " on score : " + homeTeamScore);


                            String awayTeam = StringUtils.substringAfter(StringUtils.substringAfter(element.text(), "-"), " ");
                            System.out.println("processing away match " + awayTeam);
                            Integer awayTeamScore = Integer.valueOf(StringUtils.substringBefore(StringUtils.substringAfter(element.text(), "-"), " "));
                            log.info("processing away match " + awayTeam + " on score : " + awayTeamScore);

                            // have we got a BTTS
                            if (homeTeamScore > 0 && awayTeamScore > 0) {


                                log.info("both teams scored : Home Game team =  " + homeTeam + " on date " + fixtureDateStr);

                                responseString.append("Both teams scored in game : ");
                                responseString.append(homeTeam);
                                responseString.append("\n");

                                updateChoices(homeTeam, weekKey, responseString);

                            }

                        }


                    }


                    }catch(Throwable t){
                        log.log(Level.WARNING, "Failed to process element " +  element.text() +   " on resultPage  " +    resultPage.getPage(), t);
                    }

                }


            } catch (Throwable t) {
                // try the next page.
                log.log(Level.WARNING, "Failed to process fixture page for week " + weekNumber, t);
            }
        }

        response.getWriter().write(responseString.toString());
    }

    private void updateChoices(String homeTeam, Key weekKey, StringBuilder responseStr) {

        log.info("Checking choices records for BTTS for home team");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        for (int i = 1; i <= 4; i++) {
            Query query = new Query("Choices", weekKey).addFilter("choice" + i, Query.FilterOperator.EQUAL, homeTeam);
            PreparedQuery pq = datastore.prepare(query);
            for (Entity choice : pq.asIterable()) {
                log.info("Player " + choice.getProperty("player") + " selected " + homeTeam + " and BTTS!");
                responseStr.append("Player '");
                responseStr.append(choice.getProperty("player"));

                responseStr.append("' selected' ");
                responseStr.append(homeTeam );
                responseStr.append("' as choice"+i);
                responseStr.append("\n");
                if (!((Boolean) choice.getProperty("choice" + i + "Result")).booleanValue()) {
                    log.info("Updating Player " + choice.getProperty("player") + " as home team " + homeTeam + " BTTS - this was " + "choice" + i + "Result");

                    choice.setProperty("choice" + i + "Result", Boolean.TRUE);
                    datastore.put(choice);
                }
            }


        }


    }
}

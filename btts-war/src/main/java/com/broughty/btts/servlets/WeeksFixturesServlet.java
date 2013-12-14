package com.broughty.btts.servlets;

import com.broughty.util.ResultsWebPageEnum;
import com.google.appengine.api.datastore.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("CurrentWeek");
        PreparedQuery pq = datastore.prepare(q);

        Entity currentWeek = pq.asSingleEntity();
        String weekNumber = "N/A";
        if (currentWeek != null) {
            weekNumber = (String) currentWeek.getProperty("week");
        }

        Key weekKey = KeyFactory.createKey("Week", weekNumber);


        // work through all leagues
        for (ResultsWebPageEnum resultPage : ResultsWebPageEnum.values()) {
            log.info("Processing page " + resultPage.getPage());
            Document doc = Jsoup.connect(resultPage.getPage()).get();
            response.setContentType("text/plain");

            Elements elements = doc.getElementsByClass("match-details");

            for (Element element : elements) {
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


                    // no point in processing
                    if (Days.daysBetween(currentDate.toDateMidnight(), fixtureDate.toDateMidnight()).getDays() > 4) {
                        log.log(Level.FINE, "days between currentDate.toDateMidnight() " + currentDate.toDateMidnight().toString() + " and fixtureDate.toDateMidnight() " +
                                fixtureDate.toDateMidnight().toString() + " is greater than 4.");
                        break;
                    }

                    log.log(Level.FINE, ("processing match " + element.text() + " on date: " + fixtureDateStr));

                    // if it contains a " V " then it isn't in progress...
                    if (!StringUtils.contains(element.text(), " V ") && !StringUtils.contains(element.text(), "P-P")) {

                        String homeTeam = StringUtils.substringBeforeLast(StringUtils.substringBefore(element.text(), "-"), " ");
                        Integer homeTeamScore = Integer.valueOf(StringUtils.substringAfterLast(StringUtils.substringBefore(element.text(), "-"), " "));
                        log.info("processing home match " + homeTeam + " on score : " + homeTeamScore);


                        String awayTeam = StringUtils.substringAfterLast(StringUtils.substringAfter(element.text(), "-"), " ");
                        Integer awayTeamScore = Integer.valueOf(StringUtils.substringBeforeLast(StringUtils.substringAfter(element.text(), "-"), " "));
                        log.info("processing away match " + awayTeam + " on score : " + awayTeamScore);

                        // have we got a BTTS
                        if (homeTeamScore > 0 && awayTeamScore > 0) {


                            log.info("both teams scored : Home Game team =  " + homeTeam + " on date " + fixtureDateStr);

                            updateChoices(homeTeam, weekKey);


                        }

                    }


                }


            }


        }
    }

    private void updateChoices(String homeTeam, Key weekKey) {

        log.info("Checking choices records for BTTS for home team");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        for (int i = 1; i <= 4; i++) {
            Query query = new Query("Choices", weekKey).addFilter("choice" + i, Query.FilterOperator.EQUAL, homeTeam);
            PreparedQuery pq = datastore.prepare(query);
            for (Entity choice : pq.asIterable()) {
                log.info("Player " + choice.getProperty("player") + " selected " + homeTeam + " and BTTS!");
                if(!((Boolean)choice.getProperty("choice" + i+"Result")).booleanValue()){
                    log.info("Updating Player " + choice.getProperty("player") + " as home team " + homeTeam + " BTTS - this was " + "choice" + i+"Result");
                    choice.setProperty("choice" + i+"Result", Boolean.TRUE);
                    datastore.put(choice);
                }
            }


        }


    }
}

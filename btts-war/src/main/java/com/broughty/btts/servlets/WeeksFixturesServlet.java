package com.broughty.btts.servlets;

import com.broughty.util.PlayerEnum;
import com.broughty.util.ResultsWebPageEnum;
import com.broughty.util.TwitterHelper;
import com.google.appengine.api.datastore.*;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Sms;
import net.unto.twitter.Api;
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

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
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


    public static final String SMS_ACCOUNT_SID = "AC72bc71950d5db7125e5669b797a9ea26";

    public static final String SMS_AUTH_TOKEN = "9ef3f6d72fffc0bf21283e1e668aba93";

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
            Boolean alerted = (Boolean)choice.getProperty("alerted");
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
                allTeamsScored[i-1] = (Boolean) choice.getProperty("choice" + i + "Result");
                alertString.append("\n");
                alertString.append(choice.getProperty("choice" + i));
            }

            alertString.append("\n");
            alertString.append("http://btts.broughty.com/summary.jsp?week=");
            alertString.append(weekNumber);

            if (allTeamsScored[0] && allTeamsScored[1] && allTeamsScored[2] && allTeamsScored[3]) {
                log.info("All 4 teams scored for " + alertString.toString());
                emailAlert(alertString, weekNumber, playerName);
                mobileAlert(alertString, globalAlert, PlayerEnum.valueOf(playerName));
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

    private void mobileAlert(StringBuilder alertString, boolean globalAlert, PlayerEnum playerEnum) {
        try{

            TwilioRestClient client = new TwilioRestClient(SMS_ACCOUNT_SID, SMS_AUTH_TOKEN);


            Map<String, String> params = new HashMap<String, String>();

            params.put("Body", alertString.toString());

            params.put("To", playerEnum.getMobile());

            params.put("From", "+441604422945");

            SmsFactory messageFactory = client.getAccount().getSmsFactory();

            if(StringUtils.isBlank(playerEnum.getMobile())){
                log.info(playerEnum.getName() + " doesn't have a mobile set.");
            }else{
                Sms message = messageFactory.create(params);
                log.info("Sent SMS message to player " + playerEnum.getName() + " mobile " +
                        playerEnum.getMobile()+ " price = " + message.getPrice() + " message->" +alertString.toString());
            }


            if(globalAlert){
                for(PlayerEnum player : PlayerEnum.values()){
                    if(!StringUtils.isBlank(player.getEmail())){
                        params.put("To", player.getMobile());
                        Sms message = messageFactory.create(params);
                        log.info("Sent SMS message to player " + player.getName() + " mobile " +
                                player.getMobile()+ " price = " + message.getPrice() + " message->" +alertString.toString());

                    }


                }
            }

        }catch(Throwable t){
            log.log(Level.SEVERE, "An unhandled mobileAlert error message for alert -> " + alertString, t);
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
        }  catch(Throwable t){
            log.log(Level.SEVERE, "An unhandled email error message.", t);
        }



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

                    if(StringUtils.equalsIgnoreCase((String)choice.getProperty("player"), PlayerEnum.Mat.getName())){
                        mobileAlert(bttsMessage, false, PlayerEnum.Mat);
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
                    boolean success1 = bothTeamsScored(choice.getProperty("choice1Result"));
                    twitterPlayerResult.append(choice1);
                    twitterPlayerResult.append(":");
                    twitterPlayerResult.append(Boolean.toString(success1));
                    twitterPlayerResult.append("\n");

                    String choice2 = (String) choice.getProperty("choice2");
                    boolean success2 = bothTeamsScored(choice.getProperty("choice2Result"));

                    twitterPlayerResult.append(choice2);
                    twitterPlayerResult.append(":");
                    twitterPlayerResult.append(Boolean.toString(success2));
                    twitterPlayerResult.append("\n");

                    String choice3 = (String) choice.getProperty("choice3");
                    boolean success3 = bothTeamsScored(choice.getProperty("choice3Result"));

                    twitterPlayerResult.append(choice3);
                    twitterPlayerResult.append(":");
                    twitterPlayerResult.append(Boolean.toString(success3));
                    twitterPlayerResult.append("\n");

                    String choice4 = (String) choice.getProperty("choice4");
                    boolean success4 = bothTeamsScored(choice.getProperty("choice4Result"));

                    twitterPlayerResult.append(choice4);
                    twitterPlayerResult.append(":");
                    twitterPlayerResult.append(Boolean.toString(success4));
                    twitterPlayerResult.append("");

                    twitterAlert(twitterPlayerResult);

                }
            }


        }


    }


    private boolean bothTeamsScored(Object choiceResult) {
        return choiceResult != null ? Boolean.valueOf((Boolean) choiceResult) : false;
    }


}

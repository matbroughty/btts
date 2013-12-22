package com.broughty.btts.servlets;

import com.broughty.util.PlayerEnum;
import com.broughty.util.TwitterHelper;
import com.google.appengine.api.datastore.*;
import net.unto.twitter.Api;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 15/12/13.
 */
public class EmailResultsServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(ReminderServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.info("Initialising EmailResultsServlet");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query q = new Query("CurrentWeek");
        PreparedQuery pq = datastore.prepare(q);

        Entity currentWeek = pq.asSingleEntity();
        String weekNumber = "N/A";
        if (currentWeek != null) {
            weekNumber = (String) currentWeek.getProperty("week");
        }

        log.info("Processing reminder for week " + weekNumber);

        StringBuilder resultsTwitter  = new StringBuilder("Final results in for week' ");
        resultsTwitter.append(weekNumber);
        resultsTwitter.append("' are in!\n. http://btts.broughty.com/summary.jsp?week=");
        resultsTwitter.append(weekNumber);
        twitterAlert(resultsTwitter);


        Key weekKey = KeyFactory.createKey("Week", weekNumber);
        Query query = new Query("Choices", weekKey).addSort("date", Query.SortDirection.DESCENDING);
        List<Entity> choices = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(50));

        StringBuilder playerTable = new StringBuilder("<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <link rel=\"stylesheet\" href=\"http://yui.yahooapis.com/pure/0.1.0/pure-min.css\">\n" +
                "    <link rel=\"stylesheet\" href=\"http://weloveiconfonts.com/api/?family=fontawesome\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"pure-u-1\" id=\"main\">\n" +
                "\n" +
                "    <div class=\"pure-g \">\n" +
                "        <div class=\"pure-u-1\">");

        playerTable.append("<a href=\"http://btts.broughty.com\"><h2 class=\"content-subhead\">Week " + weekNumber + " player choices </h2></a>");
        playerTable.append("<table class=\"pure-table pure-table-bordered\">");
        playerTable.append("<thead><tr><th>Player</th> <th>Date Entered</th> <th>Choice One</th><th>Result</th><th>Choice Two</th><th>Result</th><th>Choice Three</th><th>Result</th><th>Choice Four</th><th>Result</th></tr> </thead> ");
        playerTable.append("<tbody>");
        int i = 1;
        for (Entity choice : choices) {


            StringBuilder twitterPlayerResult = new StringBuilder();

            String playerName = (String) choice.getProperty("player");

            twitterPlayerResult.append(playerName);
            twitterPlayerResult.append(" results week ");
            twitterPlayerResult.append(weekNumber);
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

            if (i % 2 == 0) {
                playerTable.append("<tr class=\"pure-table-odd\">");
            } else {
                playerTable.append("<tr>");
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");

            playerTable.append("<td>").append(playerName).append("</td>");
            playerTable.append("<td>").append(simpleDateFormat.format(choice.getProperty("date"))).append("</td>");
            playerTable.append("<td>").append(choice1).append("</td>");
            playerTable.append("<td>").append(success1 ? "&#10004;" : "&#10008;").append("</td>");
            playerTable.append("<td>").append(choice2).append("</td>");
            playerTable.append("<td>").append(success2 ? "&#10004;" : "&#10008;").append("</td>");
            playerTable.append("<td>").append(choice3).append("</td>");
            playerTable.append("<td>").append(success3 ? "&#10004;" : "&#10008;").append("</td>");
            playerTable.append("<td>").append(choice4).append("</td>");
            playerTable.append("<td>").append(success4? "&#10004;" : "&#10008;").append("</td>");
            playerTable.append("</tr>");

            i++;




            twitterAlert(twitterPlayerResult);

        }

        playerTable.append("</tbody></table>");
        playerTable.append("        </div>\n" +
                "    </div>\n" +
                "\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>");


        log.info("Emailing Results : \n" + playerTable.toString());
        try {
            Message msg = new MimeMessage(session);


            msg.setFrom(new InternetAddress("broughty@broughtybtts.appspotmail.com", "Broughty.com Admin"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("btts@broughty.com"));
            msg.setSubject("BTTS: Results for week " + weekNumber);

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(playerTable.toString(), "text/html");

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(htmlPart);
            msg.setContent(mp);

            Transport.send(msg);



        } catch (AddressException e) {
            log.log(Level.SEVERE, "An email AddressException error message.", e);
        } catch (MessagingException e) {
            log.log(Level.SEVERE, "An email MessagingException error message.", e);
        }


        response.sendRedirect("/emailresponse.jsp?message=" + playerTable.toString());


    }

    private void twitterAlert(StringBuilder alertString) {
        try {
            TwitterHelper.updateStatus(alertString.toString());
        } catch (Throwable t) {
            log.log(Level.WARNING, "Couldn't send twitter message -  " + alertString.toString(), t);
        }
    }


    private boolean bothTeamsScored(Object choiceResult) {
        return choiceResult != null ? Boolean.valueOf((Boolean) choiceResult) : false;
    }
}

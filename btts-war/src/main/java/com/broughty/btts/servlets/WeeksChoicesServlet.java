package com.broughty.btts.servlets;

import com.broughty.util.PlayerEnum;
import com.google.appengine.api.datastore.*;
import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 09/12/13.
 */
public class WeeksChoicesServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(WeeksChoicesServlet.class.getName());

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String playerName = req.getParameter("player");
        String weekNumber = req.getParameter("week");
        String choice1 = req.getParameter("choice1");
        String choice2 = req.getParameter("choice2");
        String choice3 = req.getParameter("choice3");
        String choice4 = req.getParameter("choice4");

        log.info(playerName);
        log.info(choice1);
        log.info(choice2);
        log.info(choice3);
        log.info(choice4);


        Key weekKey = KeyFactory.createKey("Week", weekNumber);


        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Date date = new Date();

        Query query = new Query("Choices");
        query.setAncestor(weekKey).addFilter("player", Query.FilterOperator.EQUAL, playerName);
        PreparedQuery pq = datastore.prepare(query);
        Entity playerChoice = pq.asSingleEntity();

        // if user has already entered a choice for this week then stuff it back.
        if (playerChoice != null) {
            playerChoice.setProperty("date", date);
            playerChoice.setProperty("choice1", choice1);
            playerChoice.setProperty("choice2", choice2);
            playerChoice.setProperty("choice3", choice3);
            playerChoice.setProperty("choice4", choice4);
            playerChoice.setProperty("choice1Result", Boolean.FALSE);
            playerChoice.setProperty("choice2Result", Boolean.FALSE);
            playerChoice.setProperty("choice3Result", Boolean.FALSE);
            playerChoice.setProperty("choice4Result", Boolean.FALSE);
        } else {

            // if we don't have a user then this is just a request for info
            if (StringUtils.isNotBlank(playerName)) {
                playerChoice = new Entity("Choices", weekKey);
                playerChoice.setProperty("player", playerName);
                playerChoice.setProperty("date", date);
                playerChoice.setProperty("choice1", choice1);
                playerChoice.setProperty("choice2", choice2);
                playerChoice.setProperty("choice3", choice3);
                playerChoice.setProperty("choice4", choice4);
                playerChoice.setProperty("choice1Result", Boolean.FALSE);
                playerChoice.setProperty("choice2Result", Boolean.FALSE);
                playerChoice.setProperty("choice3Result", Boolean.FALSE);
                playerChoice.setProperty("choice4Result", Boolean.FALSE);
            }
        }

        if (playerChoice != null) {
            log.info("Storing week " + weekNumber + " choices for player " + playerName);
            datastore.put(playerChoice);
        } else {
            return;
        }


        StringBuilder playerChoiceTable = new StringBuilder("<html>\n" +
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

        playerChoiceTable.append("<a href=\"http://btts.broughty.com\"><h2 class=\"content-subhead\">Week " + weekNumber + " player " + playerName + " choices </h2></a>");
        playerChoiceTable.append("<table class=\"pure-table pure-table-bordered\">");
        playerChoiceTable.append("<thead><tr><th>Player</th> <th>Date Entered</th> <th>Choice One</th><th>Result</th><th>Choice Two</th><th>Result</th><th>Choice Three</th><th>Result</th><th>Choice Four</th><th>Result</th></tr> </thead> ");
        playerChoiceTable.append("<tbody>");


        playerChoiceTable.append("<tr>");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");

        playerChoiceTable.append("<td>").append((String) playerChoice.getProperty("player")).append("</td>");
        playerChoiceTable.append("<td>").append(simpleDateFormat.format(playerChoice.getProperty("date"))).append("</td>");
        playerChoiceTable.append("<td>").append(choice1).append("</td>");
        playerChoiceTable.append("<td>").append("&#10008;").append("</td>");
        playerChoiceTable.append("<td>").append(choice2).append("</td>");
        playerChoiceTable.append("<td>").append("&#10008;").append("</td>");
        playerChoiceTable.append("<td>").append(choice3).append("</td>");
        playerChoiceTable.append("<td>").append("&#10008;").append("</td>");
        playerChoiceTable.append("<td>").append(choice4).append("</td>");
        playerChoiceTable.append("<td>").append("&#10008;").append("</td>");
        playerChoiceTable.append("</tr>");

        playerChoiceTable.append("</tbody></table>");
        playerChoiceTable.append("</div></div>");

        playerChoiceTable.append("    <div class=\"pure-g \">\n" +
                "        <div class=\"pure-u-1\">");


        playerChoiceTable.append(previousSelections(datastore, weekKey, weekNumber));

        playerChoiceTable.append("</div></div>");
        playerChoiceTable.append("</div>\n" +
                "</body>\n" +
                "</html>");


        log.info("Emailing Choices for player : \n" + playerChoiceTable.toString());


        try {
            Message msg = new MimeMessage(session);


            msg.setFrom(new InternetAddress("broughty@broughtybtts.appspotmail.com", "Broughty.com Admin"));
            msg.addRecipient(Message.RecipientType.TO, PlayerEnum.valueOf(playerName).getMailAddress());
            msg.addRecipients(Message.RecipientType.CC, PlayerEnum.getMailAddresses());


            msg.setSubject("BTTS: Player " + playerName + " submitted choices for week " + weekNumber);

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(playerChoiceTable.toString(), "text/html");

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(htmlPart);

            msg.setContent(mp);

            Transport.send(msg);

        } catch (AddressException e) {
            log.log(Level.SEVERE, "An email AddressException error message.", e);
        } catch (MessagingException e) {
            log.log(Level.SEVERE, "An email MessagingException error message.", e);
        }


        resp.sendRedirect("/summary.jsp?player=" + playerName + "&week=" + weekNumber);


    }

    private String previousSelections(DatastoreService datastore, Key weekKey, String weekNumber) {

        Query query = new Query("Choices", weekKey).addSort("date", Query.SortDirection.DESCENDING);
        List<Entity> choices = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(50));

        StringBuilder allPlayerTable = new StringBuilder();
        allPlayerTable.append("<a href=\"http://btts.broughty.com\"><h2 class=\"content-subhead\">Week " + weekNumber + " player choices </h2></a>");
        allPlayerTable.append("<table class=\"pure-table pure-table-bordered\">");
        allPlayerTable.append("<thead><tr><th>Player</th> <th>Date Entered</th> <th>Choice One</th><th>Result</th><th>Choice Two</th><th>Result</th><th>Choice Three</th><th>Result</th><th>Choice Four</th><th>Result</th></tr> </thead> ");
        allPlayerTable.append("<tbody>");
        int i = 1;
        for (Entity choice : choices) {


            String choice1 = (String) choice.getProperty("choice1");

            String choice2 = (String) choice.getProperty("choice2");

            String choice3 = (String) choice.getProperty("choice3");

            String choice4 = (String) choice.getProperty("choice4");
            if (i % 2 == 0) {
                allPlayerTable.append("<tr class=\"pure-table-odd\">");
            } else {
                allPlayerTable.append("<tr>");
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");

            allPlayerTable.append("<td>").append((String) choice.getProperty("player")).append("</td>");
            allPlayerTable.append("<td>").append(simpleDateFormat.format(choice.getProperty("date"))).append("</td>");
            allPlayerTable.append("<td>").append(choice1).append("</td>");
            allPlayerTable.append("<td>").append("&#10008;").append("</td>");
            allPlayerTable.append("<td>").append(choice2).append("</td>");
            allPlayerTable.append("<td>").append("&#10008;").append("</td>");
            allPlayerTable.append("<td>").append(choice3).append("</td>");
            allPlayerTable.append("<td>").append("&#10008;").append("</td>");
            allPlayerTable.append("<td>").append(choice4).append("</td>");
            allPlayerTable.append("<td>").append("&#10008;").append("</td>");
            allPlayerTable.append("</tr>");

            i++;

        }

        allPlayerTable.append("</tbody></table>");

        return allPlayerTable.toString();
    }

}

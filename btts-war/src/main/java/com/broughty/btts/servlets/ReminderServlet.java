package com.broughty.btts.servlets;

import com.broughty.util.PlayerEnum;
import com.broughty.util.TwitterHelper;
import com.google.appengine.api.datastore.*;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 12/12/13.
 */
public class ReminderServlet extends HttpServlet {


    private static final Logger log = Logger.getLogger(ReminderServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.info("Initialising ReminderServlet");
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

        Key weekKey = KeyFactory.createKey("Week", weekNumber);
        Query query = new Query("Choices", weekKey).addSort("date", Query.SortDirection.DESCENDING);
        List<Entity> choices = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(50));


        List<String> weeksPlayers = new ArrayList<String>();
        for (Entity choice : choices) {
            weeksPlayers.add((String) choice.getProperty("player"));
        }


        StringBuilder playersNotPickedYet = new StringBuilder("The following people have yet to pick their teams for week: ");
        playersNotPickedYet.append(weekNumber);
        playersNotPickedYet.append("'s games. ");
        for (PlayerEnum player : PlayerEnum.values()) {
            if (!weeksPlayers.contains(player.getName())) {
                playersNotPickedYet.append(player.getName());
                playersNotPickedYet.append(" - ");

            }
        }


        try {
            Message msg = new MimeMessage(session);


            msg.setFrom(new InternetAddress("broughty@broughtybtts.appspotmail.com", "Broughty.com Admin"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("btts@broughty.com"));
            msg.setSubject("BTTS: Reminder for week " + weekNumber);
            msg.setText(playersNotPickedYet.toString());
            Transport.send(msg);

            TwitterHelper.updateStatus("BTTS: Reminder for week " + weekNumber + "!\n" + playersNotPickedYet.toString());

        } catch (AddressException e) {
            log.log(Level.SEVERE, "An email AddressException error message.", e);
        } catch (MessagingException e) {
            log.log(Level.SEVERE, "An email MessagingException error message.", e);
        }


        response.sendRedirect("/emailresponse.jsp?message=" + playersNotPickedYet.toString());


    }

}

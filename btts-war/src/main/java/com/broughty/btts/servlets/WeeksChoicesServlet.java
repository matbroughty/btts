package com.broughty.btts.servlets;

import com.google.appengine.api.datastore.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 09/12/13.
 */
public class WeeksChoicesServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(WeeksChoicesServlet.class.getName());

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

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
        Date date = new Date();
        Entity choices = new Entity("Choices", weekKey);
        choices.setProperty("player", playerName);
        choices.setProperty("date", date);
        choices.setProperty("choice1", choice1);
        choices.setProperty("choice2", choice2);
        choices.setProperty("choice3", choice3);
        choices.setProperty("choice4", choice4);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(choices);


        resp.sendRedirect("/summary.jsp?player=" + playerName + "&week=" + weekNumber);


    }

}

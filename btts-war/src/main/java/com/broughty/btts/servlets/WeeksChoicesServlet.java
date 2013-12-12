package com.broughty.btts.servlets;

import com.google.appengine.api.datastore.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;

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


        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Date date = new Date();

        Query query = new Query("Choices");
        query.setAncestor(weekKey).addFilter("player", Query.FilterOperator.EQUAL, playerName);
        PreparedQuery pq = datastore.prepare(query);
        Entity choices = pq.asSingleEntity();

        // if user has already entered a choice for this week then stuff it back.
        if (choices != null) {
            choices.setProperty("date", date);
            choices.setProperty("choice1", choice1);
            choices.setProperty("choice2", choice2);
            choices.setProperty("choice3", choice3);
            choices.setProperty("choice4", choice4);
            choices.setProperty("choice1Result", Boolean.FALSE);
            choices.setProperty("choice2Result", Boolean.FALSE);
            choices.setProperty("choice3Result", Boolean.FALSE);
            choices.setProperty("choice4Result", Boolean.FALSE);
        } else {

            // if we don't have a user then this is just a request for info
            if (StringUtils.isNotBlank(playerName)) {
                choices = new Entity("Choices", weekKey);
                choices.setProperty("player", playerName);
                choices.setProperty("date", date);
                choices.setProperty("choice1", choice1);
                choices.setProperty("choice2", choice2);
                choices.setProperty("choice3", choice3);
                choices.setProperty("choice4", choice4);
                choices.setProperty("choice1Result", Boolean.FALSE);
                choices.setProperty("choice2Result", Boolean.FALSE);
                choices.setProperty("choice3Result", Boolean.FALSE);
                choices.setProperty("choice4Result", Boolean.FALSE);
            }
        }

        if (choices != null) {
            datastore.put(choices);
        }


        resp.sendRedirect("/summary.jsp?player=" + playerName + "&week=" + weekNumber);


    }

}

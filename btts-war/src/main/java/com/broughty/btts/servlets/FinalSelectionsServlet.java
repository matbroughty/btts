package com.broughty.btts.servlets;

import com.broughty.util.MapUtil;
import com.broughty.util.PlayerEnum;
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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 12/12/13.
 */
public class FinalSelectionsServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(FinalSelectionsServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.info("Initialising FinalSelectionsServlet");
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
        }else{
            log.warning("Week not set " + weekNumber);
            return;
        }


        // Update the week number so no more changes can be made.
        //currentWeek.setProperty("week", Integer.toString(Integer.valueOf(weekNumber) + 1));
        //datastore.put(currentWeek);
        //log.info("Incremented current week to " + Integer.toString(Integer.valueOf(weekNumber) + 1));



        log.info("Processing Final Selections for week " + weekNumber);

        Key weekKey = KeyFactory.createKey("Week", weekNumber);
        Query query = new Query("Choices", weekKey).addSort("date", Query.SortDirection.DESCENDING);
        List<Entity> choices = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(50));

        Entity primeSelections = datastore.prepare(new Query("PrimeSelections", weekKey)).asSingleEntity();
        if(primeSelections == null){
            primeSelections = new Entity("PrimeSelections", weekKey);
        }

        Entity secondarySelections = datastore.prepare(new Query("SecondarySelections", weekKey)).asSingleEntity();
        if(secondarySelections == null){
            secondarySelections = new Entity("SecondarySelections", weekKey);
        }



        // get the start player i.e. the top 4 choices.
        query = new Query("Choices");
        query.setAncestor(weekKey).addFilter("player", Query.FilterOperator.EQUAL, PlayerEnum.Star.getName());
        pq = datastore.prepare(query);
        Entity starPlayerChoice = pq.asSingleEntity();
        if(starPlayerChoice == null){
            starPlayerChoice = new Entity("Choices", weekKey);
            starPlayerChoice.setProperty("player", PlayerEnum.Star.getName());
            starPlayerChoice.setProperty("date", new Date());
            starPlayerChoice.setProperty("choice1", null);
            starPlayerChoice.setProperty("choice2", null);
            starPlayerChoice.setProperty("choice3", null);
            starPlayerChoice.setProperty("choice4", null);
            starPlayerChoice.setProperty("choice1Result", Boolean.FALSE);
            starPlayerChoice.setProperty("choice2Result", Boolean.FALSE);
            starPlayerChoice.setProperty("choice3Result", Boolean.FALSE);
            starPlayerChoice.setProperty("choice4Result", Boolean.FALSE);

        }


        Map<String, Integer> teamCount = new HashMap<String, Integer>();
        for (Entity choice : choices) {


            String choice1 = (String) choice.getProperty("choice1");
            if (teamCount.containsKey(choice1)) {
                teamCount.put(choice1, new Integer(teamCount.get(choice1).intValue() + 1));
            } else {
                teamCount.put(choice1, new Integer(1));
            }


            String choice2 = (String) choice.getProperty("choice2");

            if (teamCount.containsKey(choice2)) {
                teamCount.put(choice2, new Integer(teamCount.get(choice2).intValue() + 1));
            } else {
                teamCount.put(choice2, new Integer(1));
            }


            String choice3 = (String) choice.getProperty("choice3");

            if (teamCount.containsKey(choice3)) {
                teamCount.put(choice3, new Integer(teamCount.get(choice3).intValue() + 1));
            } else {
                teamCount.put(choice3, new Integer(1));
            }


            String choice4 = (String) choice.getProperty("choice4");

            if (teamCount.containsKey(choice4)) {
                teamCount.put(choice4, new Integer(teamCount.get(choice4).intValue() + 1));
            } else {
                teamCount.put(choice4, new Integer(1));
            }

        }


        teamCount = MapUtil.sortByValue(teamCount);

        StringBuilder selections = new StringBuilder();
        int count = 1;
        int secondarySelectionsCount = 1;
        for (String team : teamCount.keySet()) {
            if(count == 1){
                selections.append("Prime Selections Start:  \n");
            }

            if(count == 5){
                selections.append("Secondary Selections Start: \n");
            }

            // end of processing - single selection teams don't get a look in.
            if(teamCount.get(team).intValue() == 1){
                selections.append("Secondary Selections End: \n");
                break;
            }

            selections.append("Team - ");
            selections.append(team);
            selections.append(" - Selected '");
            selections.append(teamCount.get(team));
            selections.append("' times. \n");

            if(count >= 1 && count < 5){
                starPlayerChoice.setProperty("choice" + count, team);
                primeSelections.setProperty("choice" + count, team);
                primeSelections.setProperty("choice"+count+"count", teamCount.get(team));
                primeSelections.setProperty("choice"+count+"success", Boolean.FALSE);
            }else{
                secondarySelections.setProperty("choice" + secondarySelectionsCount, team);
                secondarySelections.setProperty("choice"+secondarySelectionsCount+"count", teamCount.get(team));
                secondarySelections.setProperty("choice"+secondarySelectionsCount+"success", Boolean.FALSE);
                secondarySelectionsCount ++;
            }

            if(count == 4){
                selections.append("Prime Selections End. \n");
            }

            count ++;

        }

        datastore.put(primeSelections);
        datastore.put(secondarySelections);
        datastore.put(starPlayerChoice);


        try {
            Message msg = new MimeMessage(session);


            msg.setFrom(new InternetAddress("broughty@broughtybtts.appspotmail.com", "Broughty.com Admin"));
            msg.addRecipients(Message.RecipientType.TO, PlayerEnum.getMailAddresses());
            msg.setSubject("BTTS: Final selections for week " + weekNumber);
            msg.setText(selections.toString());
            Transport.send(msg);

        } catch (AddressException e) {
            log.log(Level.SEVERE, "An email AddressException error message.", e);
        } catch (MessagingException e) {
            log.log(Level.SEVERE, "An email MessagingException error message.", e);
        }





        response.sendRedirect("/emailresponse.jsp?message=" + selections.toString());


    }
}

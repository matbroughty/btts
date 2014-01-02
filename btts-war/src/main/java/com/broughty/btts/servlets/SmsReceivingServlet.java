package com.broughty.btts.servlets;

import com.broughty.util.BTTSHelper;
import com.broughty.util.TwitterHelper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 22/12/13.
 */
public class SmsReceivingServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(SmsReceivingServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {

            String paramName = parameterNames.nextElement();
            log.info("SMS Param name ->" + paramName);


            String[] paramValues = request.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                log.info("SMS Param value ->" + paramValue);
            }


        }


        if (StringUtils.containsIgnoreCase(request.getParameter("Body"), "Refresh")) {
            log.info("Requesting a refresh via number " + StringUtils.right(request.getParameter("From"), 6));
            response.sendRedirect("/fixtures?mobile=" + request.getParameter("From"));
            return;
        }
        if (StringUtils.containsIgnoreCase(request.getParameter("Body"), "Results")) {
            log.info("Requesting results via number " + StringUtils.right(request.getParameter("From"), 6));
            response.sendRedirect("/emailresults?mobile=" + request.getParameter("From"));
            return;
        }
        if (StringUtils.containsIgnoreCase(request.getParameter("Body"), "Reminder")) {
            log.info("Requesting reminders via number " + StringUtils.right(request.getParameter("From"), 6));
            response.sendRedirect("/reminders?mobile=" + request.getParameter("From"));
            return;
        }
        if (StringUtils.containsIgnoreCase(request.getParameter("Body"), "Final")) {
            log.info("Requesting final selections via number " + StringUtils.right(request.getParameter("From"), 6));
            response.sendRedirect("/selections?mobile=" + request.getParameter("From"));
            return;
        }
        if (StringUtils.containsIgnoreCase(request.getParameter("Body"), ";")) {
            log.info("Requesting choices via number " + StringUtils.right(request.getParameter("From"), 6));
            String choicesUrl = "/choices?" + buildPlayerChoiceRequest(request.getParameter("Body"));
            log.info("Calling choices URL " + choicesUrl);
            response.sendRedirect(choicesUrl);
            return;

        }


    }

    private String buildPlayerChoiceRequest(String body) {

        StringBuilder playerChoicesParams = new StringBuilder("week=");
        playerChoicesParams.append(BTTSHelper.getCurrentWeek());
        try {
            log.info("Choices request via mobile = " + body);
            String[] result = body.split(";");
            if (result.length > 4) {
                playerChoicesParams.append("&player=");
                playerChoicesParams.append(StringUtils.trim(result[0]));
                playerChoicesParams.append("&choice1=");
                playerChoicesParams.append(StringUtils.trim(result[1]));
                playerChoicesParams.append("&choice2=");
                playerChoicesParams.append(StringUtils.trim(result[2]));
                playerChoicesParams.append("&choice3=");
                playerChoicesParams.append(StringUtils.trim(result[3]));
                playerChoicesParams.append("&choice4=");
                playerChoicesParams.append(StringUtils.trim(result[4]));
            } else {
                log.log(Level.WARNING, "Not enough params in: " + body);
                throw new IllegalArgumentException("Not enough params to choose teams.");

            }
        } catch (Throwable t) {
            log.warning("Iffy looking sms choice request " + body);
            TwitterHelper.updateStatus("!! Dodgy sms choice request of " + body);
        }

        log.info("The choices servlet params are " + playerChoicesParams.toString());
        return playerChoicesParams.toString();

    }
}

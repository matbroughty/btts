package com.broughty.btts.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 12/12/13.
 */
public class MaintenanceServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(MaintenanceServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.info("Initialising MaintenanceServlet");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        String currentWeek = request.getParameter("current_week");
        log.info("MaintenanceServlet - current week is " + currentWeek);


        if (StringUtils.isNotBlank(currentWeek)) {


            Query query = new Query("CurrentWeek");

            Entity currentWeekEntity = datastore.prepare(query).asSingleEntity();

            if (currentWeekEntity != null) {

                log.info("MaintenanceServlet - Old week " + currentWeekEntity.getProperty("week") + " is being replaced by " + currentWeek);

                currentWeekEntity.setProperty("week", currentWeek);
                currentWeekEntity.setProperty("date", new Date());

            } else {

                log.info("MaintenanceServlet - New current week is being set at " + currentWeek);
                currentWeekEntity = new Entity("CurrentWeek");
                currentWeekEntity.setProperty("week", currentWeek);
                currentWeekEntity.setProperty("date", new Date());

            }

            datastore.put(currentWeekEntity);

        }

        response.sendRedirect("/choices.jsp");

    }
}

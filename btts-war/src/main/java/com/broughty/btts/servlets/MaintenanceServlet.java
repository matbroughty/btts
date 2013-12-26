package com.broughty.btts.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
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

        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yy");

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        String currentWeek = request.getParameter("current_week");

        DateTime startDate = new DateTime();
        DateTime endDate = startDate.plusDays(4);
        try {
            startDate = fmt.parseDateTime(request.getParameter("start_date"));
            endDate = fmt.parseDateTime(request.getParameter("end_date"));
        } catch (Throwable t) {
            log.log(Level.WARNING, "Couldn't parse dates sent to Maintenance server.", t);
        }


        log.info("MaintenanceServlet - current week is " + currentWeek);


        if (StringUtils.isNotBlank(currentWeek)) {


            Query query = new Query("CurrentWeek");

            Entity currentWeekEntity = datastore.prepare(query).asSingleEntity();

            if (currentWeekEntity != null) {

                log.info("MaintenanceServlet - Old week " + currentWeekEntity.getProperty("week") + " is being replaced by " + currentWeek);

                currentWeekEntity.setProperty("week", currentWeek);
                currentWeekEntity.setProperty("date", new Date());
                currentWeekEntity.setProperty("startDate", startDate.toDate());
                currentWeekEntity.setProperty("endDate", endDate.toDate());
            } else {

                log.info("MaintenanceServlet - New current week is being set at " + currentWeek);
                currentWeekEntity = new Entity("CurrentWeek");
                currentWeekEntity.setProperty("week", currentWeek);
                currentWeekEntity.setProperty("date", new Date());
                currentWeekEntity.setProperty("startDate", startDate.toDate());
                currentWeekEntity.setProperty("endDate", endDate.toDate());
            }

            datastore.put(currentWeekEntity);

        }

        response.sendRedirect("/choices.jsp");

    }
}

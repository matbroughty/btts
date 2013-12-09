package com.broughty.btts.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 09/12/13.
 */
public class WeeksChoicesServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(WeeksChoicesServlet.class.getName());

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        log.info(req.getParameter("player"));
        log.info(req.getParameter("choice1"));
        log.info(req.getParameter("choice2"));
        log.info(req.getParameter("choice3"));
        log.info(req.getParameter("choice4"));


        resp.sendRedirect("/summary.jsp");
    }

}

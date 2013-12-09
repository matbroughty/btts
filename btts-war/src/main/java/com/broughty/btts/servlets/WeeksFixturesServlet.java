package com.broughty.btts.servlets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: OPC
 * Date: 30/11/13
 * Time: 19:43
 * To change this template use File | Settings | File Templates.
 */
public class WeeksFixturesServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(WeeksFixturesServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.info("Initialising WeeksFixturesServlet");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Document doc = Jsoup.connect("http://www.bbc.co.uk/sport/football/premier-league/fixtures").get();
        response.setContentType("text/plain");

        Elements elements = doc.getElementsByClass("match-details");


        StringBuilder stringBuilder = new StringBuilder();
        for (Element element : elements) {
            stringBuilder.append(element.text());

        }

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);


        try {
            Message msg = new MimeMessage(session);


            msg.setFrom(new InternetAddress("broughty@broughtybtts.appspotmail.com", "Broughty.com Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("mat@broughty.com", "Mr. Broughton"));
            msg.setSubject("Fixture lists stuff.");
            msg.setText(stringBuilder.toString());
            Transport.send(msg);

        } catch (AddressException e) {
            log.log(Level.SEVERE, "An email AddressException error message.", e);
        } catch (MessagingException e) {
            log.log(Level.SEVERE, "An email MessagingException error message.", e);
        }

        response.getWriter().println("Email contents.");
        response.getWriter().println(stringBuilder.toString());


    }
}

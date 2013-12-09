<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

<body>

<%
    String playerName = request.getParameter("player");
    String weekNumber = request.getParameter("week");
%>

Hello!  Player <%= playerName %>

    <%
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key weekKey = KeyFactory.createKey("Week", weekNumber);
    // Run an ancestor query to ensure we see the most up-to-date
    // view of the Greetings belonging to the selected Guestbook.
    Query query = new Query("Choices", weekKey).addSort("date", Query.SortDirection.DESCENDING);
    List<Entity> choices = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(15));
    if (choices.isEmpty()) {
        %>
<p>Choices '${fn:escapeXml(weekNumber)}' has none .</p>
    <%
    } else {
        %>
<p>Choices for <%= weekNumber %></p>
    <%
        for (Entity choice : choices) {

               String player = (String)choice.getProperty("player");
               String choice1  = (String)choice.getProperty("choice1");
               String choice2  = (String)choice.getProperty("choice2");
               String choice3  = (String)choice.getProperty("choice3");
               String choice4  = (String)choice.getProperty("choice4");
                %>


<section>
    <h3>Player <%= player %></h3>
           <OL>
               <LI><%= choice1 %></LI>
               <LI><%= choice2 %></LI>
               <LI><%= choice3 %></LI>
               <LI><%= choice4 %></LI>
           </OL>
    </section>
    <%
        }
    }
%>


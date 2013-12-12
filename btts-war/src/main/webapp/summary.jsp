<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title></title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">

    <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->

    <link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.1.0/pure-min.css">
    <link rel="stylesheet" href="http://weloveiconfonts.com/api/?family=fontawesome">
    <link rel="stylesheet" href="css/main.css">
    <script src="js/vendor/modernizr-2.6.2.min.js"></script>
    <script src="js/Chart.js"></script>
</head>

<body>

<div class="pure-u-1" id="main">

<div class="pure-menu pure-menu-open pure-menu-horizontal pure-menu-blackbg">
    <a class="pure-menu-heading" href="/">BTTS</a>
    <ul>
        <li><a href="/viewchoices.jsp">Summary</a></li>
        <li><a href="#">User Picks</a></li>
        <li><a href="#">Graphs</a></li>
        <li><a href="mailto:mat@broughty.com?Subject=Shit Hot">Email</a></li>
    </ul>
</div>


<%

    StringBuilder playerTable = new StringBuilder();

    Map<String, Integer> teamCount = new HashMap<String, Integer>();

    StringBuilder graphTable = new StringBuilder();
    String weekNumber = request.getParameter("week");


    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key weekKey = KeyFactory.createKey("Week", weekNumber);
    Query query = new Query("Choices", weekKey).addSort("date", Query.SortDirection.DESCENDING);
    List<Entity> choices = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(50));
    if (choices.isEmpty()) {
    } else {



        playerTable.append("<table class=\"pure-table pure-table-bordered\">");
        playerTable.append("<thead><tr><th>Player</th> <th>Date</th> <th>Choice1</th> <th>Choice2</th> <th>Choice3</th> <th>Choice4</th>  </tr> </thead> ");
        playerTable.append("<tbody>");
        int i = 1;
        for (Entity choice : choices) {




            String choice1 = (String) choice.getProperty("choice1");
            if(teamCount.containsKey(choice1)){
                teamCount.put(choice1, new Integer(teamCount.get(choice1).intValue() + 1));
            }else{
                teamCount.put(choice1, new Integer(1));
            }


            String choice2 = (String) choice.getProperty("choice2");

            if(teamCount.containsKey(choice2)){
                teamCount.put(choice2, new Integer(teamCount.get(choice2).intValue() + 1));
            }else{
                teamCount.put(choice2, new Integer(1));
            }


            String choice3 = (String) choice.getProperty("choice3");

            if(teamCount.containsKey(choice3)){
                teamCount.put(choice3, new Integer(teamCount.get(choice3).intValue() + 1));
            }else{
                teamCount.put(choice3, new Integer(1));
            }



            String choice4 = (String) choice.getProperty("choice4");

            if(teamCount.containsKey(choice4)){
                teamCount.put(choice4, new Integer(teamCount.get(choice4).intValue() + 1));
            }else{
                teamCount.put(choice4, new Integer(1));
            }

            if(i % 2 == 0){
                playerTable.append("<tr class=\"pure-table-odd\">");
            }else{
                playerTable.append("<tr>");
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");

            playerTable.append("<td>").append((String)choice.getProperty("player")).append("</td>");
            playerTable.append("<td>").append(simpleDateFormat.format(choice.getProperty("date"))).append("</td>");
            playerTable.append("<td>").append(choice1).append("</td>");
            playerTable.append("<td>").append(choice2).append("</td>");
            playerTable.append("<td>").append(choice3).append("</td>");
            playerTable.append("<td>").append(choice4).append("</td>");

            playerTable.append("</tr>");

            i++;

        }
        playerTable.append("</tbody></table>");

    }

    graphTable.append("<table class=\"pure-table\">");
    graphTable.append("<thead><tr><th>Team</th> <th>Score</th> </tr> </thead> ");
    graphTable.append("<tbody>");
    for(String team : teamCount.keySet()){
        graphTable.append("<tr>");
        graphTable.append("<td>").append(team).append("</td>");
        graphTable.append("<td>").append(teamCount.get(team)).append("</td>");
        graphTable.append("</tr>");
    }
    graphTable.append("</tbody></table>");

%>


    <div class="pure-g l-box">
        <div class="pure-u-1 l-box">
            <h2 class="content-subhead">Week <%= weekNumber%> player choices</h2>
            <%= playerTable.toString() %>
        </div>
    </div>

    <div class="pure-g l-box">
        <div class="pure-u-1 l-box">
            <h2 class="content-subhead">Week <%= weekNumber%> combined selections</h2>
            <%= graphTable.toString() %>
        </div>
    </div>

</div>
<footer>
    &copy; 2013 Broughty Co
</footer>
</body>
</html>


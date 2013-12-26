<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="com.broughty.util.MapUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.google.appengine.api.datastore.*" %>
<%@ page import="com.broughty.util.BTTSHelper" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title></title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width">
<script src="http://yui.yahooapis.com/3.14.0/build/yui/yui-min.js"></script>
<script>
    YUI({
        classNamePrefix: 'pure'
    }).use('gallery-sm-menu', function (Y) {

                var horizontalMenu = new Y.Menu({
                    container: '#demo-horizontal-menu',
                    sourceNode: '#std-menu-items',
                    orientation: 'horizontal',
                    hideOnOutsideClick: false,
                    hideOnClick: false
                });

                horizontalMenu.render();
                horizontalMenu.show();

            });
</script>
<%

    StringBuilder playerTable = new StringBuilder();

    Map<String, Integer> teamCount = new HashMap<String, Integer>();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    StringBuilder graphTable = new StringBuilder();
    String weekNumber = request.getParameter("week");
    if (StringUtils.isBlank(weekNumber)) {
        weekNumber = BTTSHelper.getCurrentWeek();
    }


    Key weekKey = KeyFactory.createKey("Week", weekNumber);
    Query query = new Query("Choices", weekKey).addSort("date", Query.SortDirection.DESCENDING);
    List<Entity> choices = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(50));

    // Keep track of which teams scrored.
    List<String> homeTeamsBothScored = new ArrayList<String>();

    if (choices.isEmpty()) {

        // do nowt...todo do something...

    } else {


        playerTable.append("<table class=\"pure-table pure-table-bordered\">");
        playerTable.append("<thead><tr><th>Player</th> <th>Date Entered</th> <th>Choice One</th><th>Result</th><th>Choice Two</th><th>Result</th><th>Choice Three</th><th>Result</th><th>Choice Four</th><th>Result</th></tr> </thead> ");
        playerTable.append("<tbody>");
        int i = 1;
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

            if (i % 2 == 0) {
                playerTable.append("<tr class=\"pure-table-odd\">");
            } else {
                playerTable.append("<tr>");
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");

            playerTable.append("<td>").append((String) choice.getProperty("player")).append("</td>");
            playerTable.append("<td>").append(simpleDateFormat.format(choice.getProperty("date"))).append("</td>");
            playerTable.append("<td>").append(choice1).append("</td>");
            playerTable.append("<td>").append(BTTSHelper.bothTeamsScored(choice.getProperty("choice1Result"))).append("</td>");
            playerTable.append("<td>").append(choice2).append("</td>");
            playerTable.append("<td>").append(BTTSHelper.bothTeamsScored(choice.getProperty("choice2Result"))).append("</td>");
            playerTable.append("<td>").append(choice3).append("</td>");
            playerTable.append("<td>").append(BTTSHelper.bothTeamsScored(choice.getProperty("choice3Result"))).append("</td>");
            playerTable.append("<td>").append(choice4).append("</td>");
            playerTable.append("<td>").append(BTTSHelper.bothTeamsScored(choice.getProperty("choice4Result"))).append("</td>");
            playerTable.append("</tr>");


            // Quickly keep track of which teams have scored.  Very inneficient!
            for (int j = 1; j <= 4; j++) {
                if ((Boolean) choice.getProperty("choice" + j + "Result")) {
                    homeTeamsBothScored.add((String) choice.getProperty("choice" + j));
                }
            }

            i++;

        }
        playerTable.append("</tbody></table>");

    }

    // most popular first
    teamCount = MapUtil.sortByValue(teamCount);

    StringBuilder graphData = new StringBuilder("google.visualization.arrayToDataTable([['Team', 'Votes',{ role: 'style' } ],");

    graphTable.append("<table class=\"pure-table pure-table-bordered\">");
    graphTable.append("<caption>Primary Bet Choices</caption>");
    graphTable.append("<thead><tr><th>Team</th> <th>Votes</th> <th>Result</th> </tr> </thead> ");
    graphTable.append("<tbody>");
    int count = 1;
    boolean singleChoiceHeaderWritten = false;
    for (String team : teamCount.keySet()) {



        if(count == 5){
            graphTable.append("</tbody></table>");
            graphTable.append("<table class=\"pure-table pure-table-bordered\">");
            graphTable.append("<caption>Secondary Bet Choices</caption>");
            graphTable.append("<thead><tr><th>Team</th> <th>Votes</th> <th>Result</th> </tr> </thead> ");
            graphTable.append("<tbody>");
        }

        // end of processing - single selection teams don't get a look in.
        if(teamCount.get(team).intValue() == 1 && !singleChoiceHeaderWritten){
            graphTable.append("</tbody></table>");
            graphTable.append("<table class=\"pure-table pure-table-bordered\">");
            graphTable.append("<caption>Single Vote Choices</caption>");
            graphTable.append("<thead><tr><th>Team</th> <th>Votes</th> <th>Result</th> </tr> </thead> ");
            graphTable.append("<tbody>");
            singleChoiceHeaderWritten = true;
        }



        if (count % 2 == 0) {
            graphTable.append("<tr class=\"pure-table-odd\">");
        } else {
            graphTable.append("<tr>");
        }


        graphTable.append("<td>").append(team).append("</td>");
        graphTable.append("<td>").append(teamCount.get(team)).append("</td>");
        graphTable.append("<td>").append(homeTeamsBothScored.contains(team) ? "&#10004;" : "&#10008;").append("</td>");
        graphTable.append("</tr>");

        graphData.append("['");
        graphData.append(team);
        graphData.append("',");
        graphData.append(teamCount.get(team));
        graphData.append(", '");
        graphData.append(homeTeamsBothScored.contains(team) ? "red" : "black");
        graphData.append("'],");
        count++;
    }
    graphTable.append("</tbody></table>");
    graphData.deleteCharAt(graphData.length() - 1);
    graphData.append("]);");






%>


<!-- Place favicon.ico and apple-touch-icon.png in the root directory -->

<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.1.0/pure-min.css">
<link rel="stylesheet" href="http://weloveiconfonts.com/api/?family=fontawesome">
<link rel="stylesheet" href="css/main.css">
<script src="js/vendor/modernizr-2.6.2.min.js"></script>
<script src="js/Chart.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
    google.load("visualization", "1", {packages: ["corechart"]});
    google.setOnLoadCallback(drawChart);
    function drawChart() {
        var data =
        <%= graphData.toString()%>

        var options = {
            title: 'Combined choices',
            vAxis: {title: 'Team', titleTextStyle: {color: 'black'}},
            ledgend: {position: 'top', textStyle: {color: 'black', fontSize: 16}},
            colors: ['black'],
            hAxis: {minValue: 0, format: '0'}
        };

        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
        chart.draw(data, options);
    }
</script>

</head>

<body>

<div class="pure-u-1" id="main">

    <div id="demo-horizontal-menu" class="pure-menu pure-menu-open pure-menu-horizontal pure-menu-blackbg">
        <a class="pure-menu-heading" href="/choices.jsp">BTTS Choose</a>
        <ul id="std-menu-items">
            <li><a href="/summary.jsp">Current Week</a></li>
            <li><a href="/viewchoices.jsp">Previous Weeks</a></li>

            <li>
                <a href="">Maintenance</a>
                <ul>
                    <li class="pure-menu-heading">Maintenance Stuff - Keep Out!</li>
                    <li class="pure-menu-separator"></li>
                    <li><a href="/maintenance.jsp">Maintenance</a></li>
                    <li><a href="/reminders">Reminder</a></li>
                    <li><a href="/selections">Selections</a></li>
                    <li>
                        <a href="mailto:mat@broughty.com?Subject=Shit Hot">Email Me</a>
                    </li>
                </ul>
            </li>
        </ul>
    </div>

    <div class="pure-g l-box">
        <div class="pure-u-1 l-box">
            <h2 class="content-subhead">Week <%= weekNumber%> player choices</h2>
            <%= playerTable.toString() %>
        </div>
    </div>

    <div class="pure-g l-box">
        <div class="pure-u-1 l-box">
            <h2 class="content-subhead">Week <%= weekNumber%> combined table</h2>

            <div id="chart_div" style="width: 1300px; height: 650px;"></div>
        </div>

    </div>
    <div class="pure-g l-box">
        <div class="pure-u-1 l-box">
            <%= graphTable.toString() %>
        </div>
    </div>
</div>
<footer>
    &copy; 2013 Broughty Co
</footer>
</body>
</html>

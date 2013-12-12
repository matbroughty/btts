<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
</head>
<body>

<div class="pure-u-1" id="main">
<div class="pure-menu pure-menu-open pure-menu-horizontal pure-menu-blackbg">
    <a class="pure-menu-heading" href="/">BTTS Choose</a>
    <ul>
        <li><a href="/viewchoices.jsp">View</a></li>
        <li><a href="#">User Picks</a></li>
        <li><a href="/maintenance.jsp">Maintenance</a></li>
        <li><a href="/reminders">Reminder</a></li>
        <li><a href="/selections">Selections</a></li>
        <li><a href="mailto:mat@broughty.com?Subject=Shit Hot">Email</a></li>
    </ul>
</div>


<div class="pure-g">
<div class="pure-u-1">
<form action="/choices" method="post" class="pure-form pure-form-aligned">
<fieldset>
<div class="pure-control-group">
    <label for="week">Week</label>
    <select id="week" name="week">
        <option>18</option>
        <option>19</option>
        <option>20</option>
    </select>
</div>
<div class="pure-controls">
    <button type="submit" class="pure-button" value="Get selections">Get Selections</button>
</div>
</fieldset>
</form>
</div>
</div>
</div>
<footer>
    &copy; 2013 Broughty Com
</footer>
</body>
</html>
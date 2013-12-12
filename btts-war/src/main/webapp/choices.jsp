<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
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
    <a class="pure-menu-heading" href="/">BTTS</a>
    <ul>
        <li><a href="/viewchoices.jsp">Summary</a></li>
        <li><a href="#">User Picks</a></li>
        <li><a href="#">Graphs</a></li>
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
<div class="pure-control-group">
    <label for="player">Player</label>
    <select id="player" name="player">
        <option>Bhavesh</option>
        <option>Charles</option>
        <option>Dave</option>
        <option>Frank</option>
        <option>Gerald</option>
        <option>Jason</option>
        <option>Jonathon</option>
        <option>Jose</option>
        <option>Mat</option>
        <option>Pia</option>
        <option>Stu</option>
        <option>Tim</option>
        <option>Toby</option>
    </select>
</div>
<div class="pure-control-group">
    <label for="choice1">Choice One</label>
    <select id="choice1" name="choice1">
        <optgroup label="Premiership">
            <option>Arsenal</option>
            <option>Aston Villa</option>
            <option>Cardiff City</option>
            <option>Chelsea</option>
            <option>Crystal Palace</option>
            <option>Everton</option>
            <option>Fulham</option>
            <option>Hull City</option>
            <option>Liverpool</option>
            <option>Manchester City</option>
            <option>Manchester United</option>
            <option>Newcastle United</option>
            <option>Norwich City</option>
            <option>Southampton</option>
            <option>Stoke City</option>
            <option>Sunderland</option>
            <option>Swansea City</option>
            <option>Tottenham Hotspur</option>
            <option>West Bromwich Albion</option>
            <option>West Ham United</option>
        </optgroup>
        <optgroup label="Championship">
            <option>Barnsley</option>
            <option>Birmingham City</option>
            <option>Blackburn Rovers</option>
            <option>Blackpool</option>
            <option>Bolton Wanderers</option>
            <option>AFC Bournemouth</option>
            <option>Brighton and Hove Albion</option>
            <option>Burnley</option>
            <option>Charlton Athletic</option>
            <option>Derby County</option>
            <option>Doncaster Rovers</option>
            <option>Huddersfield Town</option>
            <option>Ipswich Town</option>
            <option>Leeds United</option>
            <option>Leicester City</option>
            <option>Middlesbrough</option>
            <option>Millwall</option>
            <option>Nottingham Forest</option>
            <option>Queens Park Rangers</option>
            <option>Reading</option>
            <option>Sheffield Wednesday</option>
            <option>Watford</option>
            <option>Wigan Athletic</option>
            <option>Yeovil Town</option>
        </optgroup>
        <optgroup label="League One">
            <option>Bradford City</option>
            <option>Brentford</option>
            <option>Bristol City</option>
            <option>Carlisle United</option>
            <option>Colchester United</option>
            <option>Coventry City</option>
            <option>Crawley Town</option>
            <option>Crewe Alexandra</option>
            <option>Gillingham</option>
            <option>Leyton Orient</option>
            <option>Milton Keynes Dons</option>
            <option>Notts County</option>
            <option>Oldham Athletic</option>
            <option>Peterborough United</option>
            <option>Port Vale</option>
            <option>Preston North End</option>
            <option>Rotherham United</option>
            <option>Sheffield United</option>
            <option>Shrewsbury Town</option>
            <option>Stevenage</option>
            <option>Swindon Town</option>
            <option>Tranmere Rovers</option>
            <option>Walsall</option>
            <option>Wolverhampton Wanderers</option>
        </optgroup>
        <optgroup label="League Two">
            <option>Accrington Stanley</option>
            <option>AFC Wimbledon</option>
            <option>Bristol Rovers</option>
            <option>Burton Albion</option>
            <option>Bury</option>
            <option>Cheltenham Town</option>
            <option>Chesterfield</option>
            <option>Dagenham and Redbridge</option>
            <option>Exeter City</option>
            <option>Fleetwood Town</option>
            <option>Hartlepool United</option>
            <option>Mansfield Town</option>
            <option>Morecambe</option>
            <option>Newport County AFC</option>
            <option>Northampton Town</option>
            <option>Oxford United</option>
            <option>Plymouth Argyle</option>
            <option>Portsmouth</option>
            <option>Rochdale</option>
            <option>Scunthorpe United</option>
            <option>Southend United</option>
            <option>Torquay United</option>
            <option>Wycombe Wanderers</option>
            <option>York City</option>
        </optgroup>
        <optgroup label="Scottish Premiership">
            <option>Aberdeen</option>
            <option>Celtic</option>
            <option>Dundee United</option>
            <option>Heart of Midlothian</option>
            <option>Hibernian</option>
            <option>Inverness Caledonian Thistle</option>
            <option>Kilmarnock</option>
            <option>Motherwell</option>
            <option>Partick Thistle</option>
            <option>Ross County</option>
            <option>St. Johnstone</option>
            <option>St. Mirren</option>
        </optgroup>
        <optgroup label="Scottish Championship">
            <option>Alloa Athletic</option>
            <option>Cowdenbeath</option>
            <option>Dumbarton</option>
            <option>Dundee</option>
            <option>Falkirk</option>
            <option>Greenock Morton</option>
            <option>Hamilton Academical</option>
            <option>Livingston</option>
            <option>Queen of the South</option>
            <option>Raith Rovers</option>
        </optgroup>
        <optgroup label="Scottish League One">
            <option>Airdrieonians</option>
            <option>Arbroath</option>
            <option>Ayr United</option>
            <option>Brechin City</option>
            <option>Dunfermline Athletic</option>
            <option>East Fife</option>
            <option>Forfar Athletic</option>
            <option>Rangers</option>
            <option>Stenhousemuir</option>
            <option>Stranraer</option>
        </optgroup>
        <optgroup label="Scottish League Two">
            <option>Albion Rovers</option>
            <option>Annan Athletic</option>
            <option>Berwick Rangers</option>
            <option>Clyde</option>
            <option>East Stirlingshire</option>
            <option>Elgin City</option>
            <option>Montrose</option>
            <option>Peterhead</option>
            <option>Queen's Park</option>
            <option>Stirling Albion</option>
        </optgroup>
    </select>
</div>
<div class="pure-control-group">
    <label for="choice2">Choice Two</label>
    <select id="choice2" name="choice2">
        <optgroup label="Premiership">
            <option>Arsenal</option>
            <option>Aston Villa</option>
            <option>Cardiff City</option>
            <option>Chelsea</option>
            <option>Crystal Palace</option>
            <option>Everton</option>
            <option>Fulham</option>
            <option>Hull City</option>
            <option>Liverpool</option>
            <option>Manchester City</option>
            <option>Manchester United</option>
            <option>Newcastle United</option>
            <option>Norwich City</option>
            <option>Southampton</option>
            <option>Stoke City</option>
            <option>Sunderland</option>
            <option>Swansea City</option>
            <option>Tottenham Hotspur</option>
            <option>West Bromwich Albion</option>
            <option>West Ham United</option>
        </optgroup>
        <optgroup label="Championship">
            <option>Barnsley</option>
            <option>Birmingham City</option>
            <option>Blackburn Rovers</option>
            <option>Blackpool</option>
            <option>Bolton Wanderers</option>
            <option>AFC Bournemouth</option>
            <option>Brighton and Hove Albion</option>
            <option>Burnley</option>
            <option>Charlton Athletic</option>
            <option>Derby County</option>
            <option>Doncaster Rovers</option>
            <option>Huddersfield Town</option>
            <option>Ipswich Town</option>
            <option>Leeds United</option>
            <option>Leicester City</option>
            <option>Middlesbrough</option>
            <option>Millwall</option>
            <option>Nottingham Forest</option>
            <option>Queens Park Rangers</option>
            <option>Reading</option>
            <option>Sheffield Wednesday</option>
            <option>Watford</option>
            <option>Wigan Athletic</option>
            <option>Yeovil Town</option>
        </optgroup>
        <optgroup label="League One">
            <option>Bradford City</option>
            <option>Brentford</option>
            <option>Bristol City</option>
            <option>Carlisle United</option>
            <option>Colchester United</option>
            <option>Coventry City</option>
            <option>Crawley Town</option>
            <option>Crewe Alexandra</option>
            <option>Gillingham</option>
            <option>Leyton Orient</option>
            <option>Milton Keynes Dons</option>
            <option>Notts County</option>
            <option>Oldham Athletic</option>
            <option>Peterborough United</option>
            <option>Port Vale</option>
            <option>Preston North End</option>
            <option>Rotherham United</option>
            <option>Sheffield United</option>
            <option>Shrewsbury Town</option>
            <option>Stevenage</option>
            <option>Swindon Town</option>
            <option>Tranmere Rovers</option>
            <option>Walsall</option>
            <option>Wolverhampton Wanderers</option>
        </optgroup>
        <optgroup label="League Two">
            <option>Accrington Stanley</option>
            <option>AFC Wimbledon</option>
            <option>Bristol Rovers</option>
            <option>Burton Albion</option>
            <option>Bury</option>
            <option>Cheltenham Town</option>
            <option>Chesterfield</option>
            <option>Dagenham and Redbridge</option>
            <option>Exeter City</option>
            <option>Fleetwood Town</option>
            <option>Hartlepool United</option>
            <option>Mansfield Town</option>
            <option>Morecambe</option>
            <option>Newport County AFC</option>
            <option>Northampton Town</option>
            <option>Oxford United</option>
            <option>Plymouth Argyle</option>
            <option>Portsmouth</option>
            <option>Rochdale</option>
            <option>Scunthorpe United</option>
            <option>Southend United</option>
            <option>Torquay United</option>
            <option>Wycombe Wanderers</option>
            <option>York City</option>
        </optgroup>
        <optgroup label="Scottish Premiership">
            <option>Aberdeen</option>
            <option>Celtic</option>
            <option>Dundee United</option>
            <option>Heart of Midlothian</option>
            <option>Hibernian</option>
            <option>Inverness Caledonian Thistle</option>
            <option>Kilmarnock</option>
            <option>Motherwell</option>
            <option>Partick Thistle</option>
            <option>Ross County</option>
            <option>St. Johnstone</option>
            <option>St. Mirren</option>
        </optgroup>
        <optgroup label="Scottish Championship">
            <option>Alloa Athletic</option>
            <option>Cowdenbeath</option>
            <option>Dumbarton</option>
            <option>Dundee</option>
            <option>Falkirk</option>
            <option>Greenock Morton</option>
            <option>Hamilton Academical</option>
            <option>Livingston</option>
            <option>Queen of the South</option>
            <option>Raith Rovers</option>
        </optgroup>
        <optgroup label="Scottish League One">
            <option>Airdrieonians</option>
            <option>Arbroath</option>
            <option>Ayr United</option>
            <option>Brechin City</option>
            <option>Dunfermline Athletic</option>
            <option>East Fife</option>
            <option>Forfar Athletic</option>
            <option>Rangers</option>
            <option>Stenhousemuir</option>
            <option>Stranraer</option>
        </optgroup>
        <optgroup label="Scottish League Two">
            <option>Albion Rovers</option>
            <option>Annan Athletic</option>
            <option>Berwick Rangers</option>
            <option>Clyde</option>
            <option>East Stirlingshire</option>
            <option>Elgin City</option>
            <option>Montrose</option>
            <option>Peterhead</option>
            <option>Queen's Park</option>
            <option>Stirling Albion</option>
        </optgroup>
    </select>
</div>
<div class="pure-control-group">
    <label for="choice3">Choice Three</label>
    <select id="choice3" name="choice3">
        <optgroup label="Premiership">
            <option>Arsenal</option>
            <option>Aston Villa</option>
            <option>Cardiff City</option>
            <option>Chelsea</option>
            <option>Crystal Palace</option>
            <option>Everton</option>
            <option>Fulham</option>
            <option>Hull City</option>
            <option>Liverpool</option>
            <option>Manchester City</option>
            <option>Manchester United</option>
            <option>Newcastle United</option>
            <option>Norwich City</option>
            <option>Southampton</option>
            <option>Stoke City</option>
            <option>Sunderland</option>
            <option>Swansea City</option>
            <option>Tottenham Hotspur</option>
            <option>West Bromwich Albion</option>
            <option>West Ham United</option>
        </optgroup>
        <optgroup label="Championship">
            <option>Barnsley</option>
            <option>Birmingham City</option>
            <option>Blackburn Rovers</option>
            <option>Blackpool</option>
            <option>Bolton Wanderers</option>
            <option>AFC Bournemouth</option>
            <option>Brighton and Hove Albion</option>
            <option>Burnley</option>
            <option>Charlton Athletic</option>
            <option>Derby County</option>
            <option>Doncaster Rovers</option>
            <option>Huddersfield Town</option>
            <option>Ipswich Town</option>
            <option>Leeds United</option>
            <option>Leicester City</option>
            <option>Middlesbrough</option>
            <option>Millwall</option>
            <option>Nottingham Forest</option>
            <option>Queens Park Rangers</option>
            <option>Reading</option>
            <option>Sheffield Wednesday</option>
            <option>Watford</option>
            <option>Wigan Athletic</option>
            <option>Yeovil Town</option>
        </optgroup>
        <optgroup label="League One">
            <option>Bradford City</option>
            <option>Brentford</option>
            <option>Bristol City</option>
            <option>Carlisle United</option>
            <option>Colchester United</option>
            <option>Coventry City</option>
            <option>Crawley Town</option>
            <option>Crewe Alexandra</option>
            <option>Gillingham</option>
            <option>Leyton Orient</option>
            <option>Milton Keynes Dons</option>
            <option>Notts County</option>
            <option>Oldham Athletic</option>
            <option>Peterborough United</option>
            <option>Port Vale</option>
            <option>Preston North End</option>
            <option>Rotherham United</option>
            <option>Sheffield United</option>
            <option>Shrewsbury Town</option>
            <option>Stevenage</option>
            <option>Swindon Town</option>
            <option>Tranmere Rovers</option>
            <option>Walsall</option>
            <option>Wolverhampton Wanderers</option>
        </optgroup>
        <optgroup label="League Two">
            <option>Accrington Stanley</option>
            <option>AFC Wimbledon</option>
            <option>Bristol Rovers</option>
            <option>Burton Albion</option>
            <option>Bury</option>
            <option>Cheltenham Town</option>
            <option>Chesterfield</option>
            <option>Dagenham and Redbridge</option>
            <option>Exeter City</option>
            <option>Fleetwood Town</option>
            <option>Hartlepool United</option>
            <option>Mansfield Town</option>
            <option>Morecambe</option>
            <option>Newport County AFC</option>
            <option>Northampton Town</option>
            <option>Oxford United</option>
            <option>Plymouth Argyle</option>
            <option>Portsmouth</option>
            <option>Rochdale</option>
            <option>Scunthorpe United</option>
            <option>Southend United</option>
            <option>Torquay United</option>
            <option>Wycombe Wanderers</option>
            <option>York City</option>
        </optgroup>
        <optgroup label="Scottish Premiership">
            <option>Aberdeen</option>
            <option>Celtic</option>
            <option>Dundee United</option>
            <option>Heart of Midlothian</option>
            <option>Hibernian</option>
            <option>Inverness Caledonian Thistle</option>
            <option>Kilmarnock</option>
            <option>Motherwell</option>
            <option>Partick Thistle</option>
            <option>Ross County</option>
            <option>St. Johnstone</option>
            <option>St. Mirren</option>
        </optgroup>
        <optgroup label="Scottish Championship">
            <option>Alloa Athletic</option>
            <option>Cowdenbeath</option>
            <option>Dumbarton</option>
            <option>Dundee</option>
            <option>Falkirk</option>
            <option>Greenock Morton</option>
            <option>Hamilton Academical</option>
            <option>Livingston</option>
            <option>Queen of the South</option>
            <option>Raith Rovers</option>
        </optgroup>
        <optgroup label="Scottish League One">
            <option>Airdrieonians</option>
            <option>Arbroath</option>
            <option>Ayr United</option>
            <option>Brechin City</option>
            <option>Dunfermline Athletic</option>
            <option>East Fife</option>
            <option>Forfar Athletic</option>
            <option>Rangers</option>
            <option>Stenhousemuir</option>
            <option>Stranraer</option>
        </optgroup>
        <optgroup label="Scottish League Two">
            <option>Albion Rovers</option>
            <option>Annan Athletic</option>
            <option>Berwick Rangers</option>
            <option>Clyde</option>
            <option>East Stirlingshire</option>
            <option>Elgin City</option>
            <option>Montrose</option>
            <option>Peterhead</option>
            <option>Queen's Park</option>
            <option>Stirling Albion</option>
        </optgroup>
    </select>
</div>
<div class="pure-control-group">
    <label for="choice4">Choice Four</label>
    <select id="choice4" name="choice4">
        <optgroup label="Premiership">
            <option>Arsenal</option>
            <option>Aston Villa</option>
            <option>Cardiff City</option>
            <option>Chelsea</option>
            <option>Crystal Palace</option>
            <option>Everton</option>
            <option>Fulham</option>
            <option>Hull City</option>
            <option>Liverpool</option>
            <option>Manchester City</option>
            <option>Manchester United</option>
            <option>Newcastle United</option>
            <option>Norwich City</option>
            <option>Southampton</option>
            <option>Stoke City</option>
            <option>Sunderland</option>
            <option>Swansea City</option>
            <option>Tottenham Hotspur</option>
            <option>West Bromwich Albion</option>
            <option>West Ham United</option>
        </optgroup>
        <optgroup label="Championship">
            <option>Barnsley</option>
            <option>Birmingham City</option>
            <option>Blackburn Rovers</option>
            <option>Blackpool</option>
            <option>Bolton Wanderers</option>
            <option>AFC Bournemouth</option>
            <option>Brighton and Hove Albion</option>
            <option>Burnley</option>
            <option>Charlton Athletic</option>
            <option>Derby County</option>
            <option>Doncaster Rovers</option>
            <option>Huddersfield Town</option>
            <option>Ipswich Town</option>
            <option>Leeds United</option>
            <option>Leicester City</option>
            <option>Middlesbrough</option>
            <option>Millwall</option>
            <option>Nottingham Forest</option>
            <option>Queens Park Rangers</option>
            <option>Reading</option>
            <option>Sheffield Wednesday</option>
            <option>Watford</option>
            <option>Wigan Athletic</option>
            <option>Yeovil Town</option>
        </optgroup>
        <optgroup label="League One">
            <option>Bradford City</option>
            <option>Brentford</option>
            <option>Bristol City</option>
            <option>Carlisle United</option>
            <option>Colchester United</option>
            <option>Coventry City</option>
            <option>Crawley Town</option>
            <option>Crewe Alexandra</option>
            <option>Gillingham</option>
            <option>Leyton Orient</option>
            <option>Milton Keynes Dons</option>
            <option>Notts County</option>
            <option>Oldham Athletic</option>
            <option>Peterborough United</option>
            <option>Port Vale</option>
            <option>Preston North End</option>
            <option>Rotherham United</option>
            <option>Sheffield United</option>
            <option>Shrewsbury Town</option>
            <option>Stevenage</option>
            <option>Swindon Town</option>
            <option>Tranmere Rovers</option>
            <option>Walsall</option>
            <option>Wolverhampton Wanderers</option>
        </optgroup>
        <optgroup label="League Two">
            <option>Accrington Stanley</option>
            <option>AFC Wimbledon</option>
            <option>Bristol Rovers</option>
            <option>Burton Albion</option>
            <option>Bury</option>
            <option>Cheltenham Town</option>
            <option>Chesterfield</option>
            <option>Dagenham and Redbridge</option>
            <option>Exeter City</option>
            <option>Fleetwood Town</option>
            <option>Hartlepool United</option>
            <option>Mansfield Town</option>
            <option>Morecambe</option>
            <option>Newport County AFC</option>
            <option>Northampton Town</option>
            <option>Oxford United</option>
            <option>Plymouth Argyle</option>
            <option>Portsmouth</option>
            <option>Rochdale</option>
            <option>Scunthorpe United</option>
            <option>Southend United</option>
            <option>Torquay United</option>
            <option>Wycombe Wanderers</option>
            <option>York City</option>
        </optgroup>
        <optgroup label="Scottish Premiership">
            <option>Aberdeen</option>
            <option>Celtic</option>
            <option>Dundee United</option>
            <option>Heart of Midlothian</option>
            <option>Hibernian</option>
            <option>Inverness Caledonian Thistle</option>
            <option>Kilmarnock</option>
            <option>Motherwell</option>
            <option>Partick Thistle</option>
            <option>Ross County</option>
            <option>St. Johnstone</option>
            <option>St. Mirren</option>
        </optgroup>
        <optgroup label="Scottish Championship">
            <option>Alloa Athletic</option>
            <option>Cowdenbeath</option>
            <option>Dumbarton</option>
            <option>Dundee</option>
            <option>Falkirk</option>
            <option>Greenock Morton</option>
            <option>Hamilton Academical</option>
            <option>Livingston</option>
            <option>Queen of the South</option>
            <option>Raith Rovers</option>
        </optgroup>
        <optgroup label="Scottish League One">
            <option>Airdrieonians</option>
            <option>Arbroath</option>
            <option>Ayr United</option>
            <option>Brechin City</option>
            <option>Dunfermline Athletic</option>
            <option>East Fife</option>
            <option>Forfar Athletic</option>
            <option>Rangers</option>
            <option>Stenhousemuir</option>
            <option>Stranraer</option>
        </optgroup>
        <optgroup label="Scottish League Two">
            <option>Albion Rovers</option>
            <option>Annan Athletic</option>
            <option>Berwick Rangers</option>
            <option>Clyde</option>
            <option>East Stirlingshire</option>
            <option>Elgin City</option>
            <option>Montrose</option>
            <option>Peterhead</option>
            <option>Queen's Park</option>
            <option>Stirling Albion</option>
        </optgroup>
    </select>
</div>
<div class="pure-controls">
    <button type="submit" class="pure-button" value="Post Choices">Post Choices
    </button>
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
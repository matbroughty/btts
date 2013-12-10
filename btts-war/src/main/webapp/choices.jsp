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
<header>
    <nav class="pure-menu pure-menu-open pure-menu-horizontal pure-menu-blackbg">
        <a href="#" class="pure-menu-heading">Both teams to score.</a>
        <ul>
            <li class="pure-menu-selected"><a href="/summary.jsp">Summary</a></li>
            <li><a href="#">User Picks</a></li>
            <li><a href="#">Graphs</a></li>
            <li><a href="mailto:mat@broughty.com?Subject=Shit Hot">Email</a></li>
        </ul>
    </nav>
</header>
<section class="dashboard">
            <form action="/choices" method="post" class="pure-form pure-form-aligned">
                <fieldset>
                    <div class="pure-control-group">
                        <label for="week">Week</label>
                        <select id="week" name="week">
                            <option>2</option>
                            <option>3</option>
                            <option>4</option>
                            <option>5</option>
                            <option>6</option>
                            <option>7</option>
                        </select>
                    </div>
                    <div class="pure-control-group">
                        <label for="player">Player</label>
                        <select id="player" name="player">
                            <option>Mat</option>
                            <option>Frank</option>
                            <option>Pia</option>
                            <option>Jason</option>
                            <option>Jonathon</option>
                            <option>Dave</option>
                            <option>Tim</option>
                            <option>Jose</option>
                            <option>Charles</option>
                            <option>Stu</option>
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
                        </select>
                    </div>
                    <div class="pure-control-group">
                        <label for="choice4">Choice Three</label>
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
                        </select>
                    </div>
                    <div class="pure-controls">
                    <button type="submit" class="pure-button pure-button-primary" value="Post Choices">Post Choices
                    </button>
                        </div>
                </fieldset>
            </form>
</section>

<footer>
    &copy; 2013 Broughty Com
</footer>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script>window.jQuery || document.write('<script src="js/vendor/jquery-1.9.1.min.js"><\/script>')</script>
<script src="js/plugins.js"></script>
<script src="js/main.js"></script>

</body>
</html>
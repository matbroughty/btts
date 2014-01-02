<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.broughty.util.BTTSHelper" %>
<%@ page import="com.broughty.util.PlayerEnum" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
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
</head>
<body>

<%

    String week = BTTSHelper.getCurrentWeek();

    StringBuilder playerListOption = new StringBuilder();

    for (PlayerEnum playerEnum : PlayerEnum.values()) {

        if (playerEnum.compareTo(PlayerEnum.Star) == 0) {
            playerListOption.append("<option disabled>");
        } else {
            String playerName = request.getParameter("playerName");
            if (StringUtils.equalsIgnoreCase(playerName, playerEnum.getName())) {
                playerListOption.append("<option selected>");
            } else {
                playerListOption.append("<option>");
            }
        }
        playerListOption.append(playerEnum.getName());
        playerListOption.append("</option>");

    }


%>


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


<div class="pure-g">
<div class="pure-u-1">
<form action="/choices" method="post" class="pure-form pure-form-aligned">
<fieldset>
<div class="pure-control-group">
    <label for="week">Week</label>
    <select id="week" name="week">
        <option selected><%= week %>
        </option>
    </select>
</div>
<div class="pure-control-group">
    <label for="player">Player</label>
    <select id="player" name="player">
        <%= playerListOption.toString()%>
    </select>
</div>
<div class="pure-control-group">
    <label for="choice1">Choice One</label>
    <select id="choice1" name="choice1">
        <optgroup label="Premiership">
            <option>Arsenal</option>
            <option>Aston Villa</option>
            <option>Cardiff</option>
            <option>Chelsea</option>
            <option>Crystal Palace</option>
            <option>Everton</option>
            <option>Fulham</option>
            <option>Hull</option>
            <option>Liverpool</option>
            <option>Man City</option>
            <option>Man Utd</option>
            <option>Newcastle</option>
            <option>Norwich</option>
            <option>Southampton</option>
            <option>Stoke</option>
            <option>Sunderland</option>
            <option>Swansea</option>
            <option>Tottenham</option>
            <option>West Brom</option>
            <option>West Ham</option>
        </optgroup>
        <optgroup label="Championship">
            <option>Barnsley</option>
            <option>Birmingham</option>
            <option>Blackburn</option>
            <option>Blackpool</option>
            <option>Bolton</option>
            <option>Bournemouth</option>
            <option>Brighton</option>
            <option>Burnley</option>
            <option>Charlton</option>
            <option>Derby</option>
            <option>Doncaster</option>
            <option>Huddersfield</option>
            <option>Ipswich</option>
            <option>Leeds</option>
            <option>Leicester</option>
            <option>Middlesbrough</option>
            <option>Millwall</option>
            <option>Nottm Forest</option>
            <option>QPR</option>
            <option>Reading</option>
            <option>Sheff Wed</option>
            <option>Watford</option>
            <option>Wigan</option>
            <option>Yeovil</option>
        </optgroup>
        <optgroup label="League One">
            <option>Bradford</option>
            <option>Brentford</option>
            <option>Bristol City</option>
            <option>Carlisle</option>
            <option>Colchester</option>
            <option>Coventry</option>
            <option>Crawley</option>
            <option>Crewe</option>
            <option>Gillingham</option>
            <option>Leyton Orient</option>
            <option>MK Dons</option>
            <option>Notts County</option>
            <option>Oldham</option>
            <option>Peterborough</option>
            <option>Port Vale</option>
            <option>Preston</option>
            <option>Rotherham</option>
            <option>Sheffield</option>
            <option>Shrewsbury</option>
            <option>Stevenage</option>
            <option>Swindon</option>
            <option>Tranmere</option>
            <option>Walsall</option>
            <option>Wolves</option>
        </optgroup>
        <optgroup label="League Two">
            <option>Accrington</option>
            <option>Wimbledon</option>
            <option>Bristol Rovers</option>
            <option>Burton</option>
            <option>Bury</option>
            <option>Cheltenham</option>
            <option>Chesterfield</option>
            <option>Dag & Red</option>
            <option>Exeter</option>
            <option>Fleetwood</option>
            <option>Hartlepool</option>
            <option>Mansfield</option>
            <option>Morecambe</option>
            <option>Newport</option>
            <option>Northampton</option>
            <option>Oxford Utd</option>
            <option>Plymouth</option>
            <option>Portsmouth</option>
            <option>Rochdale</option>
            <option>Scunthorpe</option>
            <option>Southend</option>
            <option>Torquay</option>
            <option>Wycombe</option>
            <option>York</option>
        </optgroup>
        <optgroup label="Scottish Premiership">
            <option>Aberdeen</option>
            <option>Celtic</option>
            <option>Dundee Utd</option>
            <option>Hearts</option>
            <option>Hibernian</option>
            <option>Inverness CT</option>
            <option>Kilmarnock</option>
            <option>Motherwell</option>
            <option>Partick Thistle</option>
            <option>Ross County</option>
            <option>St Johnstone</option>
            <option>St Mirren</option>
        </optgroup>
        <optgroup label="Scottish Championship">
            <option>Dundee</option>
            <option>Hamilton</option>
            <option>Falkirk</option>
            <option>Raith Rovers</option>
            <option>Alloa</option>
            <option>Livingston</option>
            <option>Queen of Sth</option>
            <option>Dumbarton</option>
            <option>Cowdenbeath</option>
            <option>Morton</option>
        </optgroup>
        <optgroup label="Scottish League One">
            <option>Airdrieonians</option>
            <option>Arbroath</option>
            <option>Ayr</option>
            <option>Brechin</option>
            <option>Dunfermline</option>
            <option>East Fife</option>
            <option>Forfar</option>
            <option>Rangers</option>
            <option>Stenhousemuir</option>
            <option>Stranraer</option>
        </optgroup>
        <optgroup label="Scottish League Two">
            <option>Albion</option>
            <option>Annan</option>
            <option>Berwick</option>
            <option>Clyde</option>
            <option>East Stirling</option>
            <option>Elgin</option>
            <option>Montrose</option>
            <option>Peterhead</option>
            <option>Queen's Park</option>
            <option>Stirling</option>
        </optgroup>
        <optgroup label="Conference">
            <option>Luton</option>
            <option>Cambridge</option>
            <option>Alfreton</option>
            <option>Grimsby</option>
            <option>Halifax</option>
            <option>Kidderminster</option>
            <option>Nuneaton</option>
            <option>Salisbury</option>
            <option>Barnet</option>
            <option>Braintree</option>
            <option>Macclesfield</option>
            <option>Gateshead</option>
            <option>Welling</option>
            <option>Forest Green</option>
            <option>Wrexham</option>
            <option>Hereford</option>
            <option>Woking</option>
            <option>Lincoln City</option>
            <option>Aldershot</option>
            <option>Southport</option>
            <option>Chester</option>
            <option>Tamworth</option>
            <option>Dartford</option>
            <option>Hyde</option>
        </optgroup>
        <optgroup label="La Liga">
            <option>Barcelona</option>
            <option>Atlético Madrid</option>
            <option>Real Madrid</option>
            <option>Athletic Club</option>
            <option>Real Sociedad</option>
            <option>Villarreal</option>
            <option>Sevilla</option>
            <option>Getafe</option>
            <option>Espanyol</option>
            <option>Málaga</option>
            <option>Valencia CF</option>
            <option>Granada CF</option>
            <option>Levante</option>
            <option>Elche</option>
            <option>Celta de Vigo</option>
            <option>Almería</option>
            <option>Real Valladolid</option>
            <option>Osasuna</option>
            <option>Rayo Vallecano</option>
            <option>Real Betis</option>
        </optgroup>
    </select>
</div>
<div class="pure-control-group">
    <label for="choice2">Choice Two</label>
    <select id="choice2" name="choice2">
        <optgroup label="Premiership">
            <option>Arsenal</option>
            <option>Aston Villa</option>
            <option>Cardiff</option>
            <option>Chelsea</option>
            <option>Crystal Palace</option>
            <option>Everton</option>
            <option>Fulham</option>
            <option>Hull</option>
            <option>Liverpool</option>
            <option>Man City</option>
            <option>Man Utd</option>
            <option>Newcastle</option>
            <option>Norwich</option>
            <option>Southampton</option>
            <option>Stoke</option>
            <option>Sunderland</option>
            <option>Swansea</option>
            <option>Tottenham</option>
            <option>West Brom</option>
            <option>West Ham</option>
        </optgroup>
        <optgroup label="Championship">
            <option>Barnsley</option>
            <option>Birmingham</option>
            <option>Blackburn</option>
            <option>Blackpool</option>
            <option>Bolton</option>
            <option>Bournemouth</option>
            <option>Brighton</option>
            <option>Burnley</option>
            <option>Charlton</option>
            <option>Derby</option>
            <option>Doncaster</option>
            <option>Huddersfield</option>
            <option>Ipswich</option>
            <option>Leeds</option>
            <option>Leicester</option>
            <option>Middlesbrough</option>
            <option>Millwall</option>
            <option>Nottm Forest</option>
            <option>QPR</option>
            <option>Reading</option>
            <option>Sheff Wed</option>
            <option>Watford</option>
            <option>Wigan</option>
            <option>Yeovil</option>
        </optgroup>
        <optgroup label="League One">
            <option>Bradford</option>
            <option>Brentford</option>
            <option>Bristol City</option>
            <option>Carlisle</option>
            <option>Colchester</option>
            <option>Coventry</option>
            <option>Crawley</option>
            <option>Crewe</option>
            <option>Gillingham</option>
            <option>Leyton Orient</option>
            <option>MK Dons</option>
            <option>Notts County</option>
            <option>Oldham</option>
            <option>Peterborough</option>
            <option>Port Vale</option>
            <option>Preston</option>
            <option>Rotherham</option>
            <option>Sheffield</option>
            <option>Shrewsbury</option>
            <option>Stevenage</option>
            <option>Swindon</option>
            <option>Tranmere</option>
            <option>Walsall</option>
            <option>Wolves</option>
        </optgroup>
        <optgroup label="League Two">
            <option>Accrington</option>
            <option>Wimbledon</option>
            <option>Bristol Rovers</option>
            <option>Burton</option>
            <option>Bury</option>
            <option>Cheltenham</option>
            <option>Chesterfield</option>
            <option>Dag & Red</option>
            <option>Exeter</option>
            <option>Fleetwood</option>
            <option>Hartlepool</option>
            <option>Mansfield</option>
            <option>Morecambe</option>
            <option>Newport</option>
            <option>Northampton</option>
            <option>Oxford Utd</option>
            <option>Plymouth</option>
            <option>Portsmouth</option>
            <option>Rochdale</option>
            <option>Scunthorpe</option>
            <option>Southend</option>
            <option>Torquay</option>
            <option>Wycombe</option>
            <option>York</option>
        </optgroup>
        <optgroup label="Scottish Premiership">
            <option>Aberdeen</option>
            <option>Celtic</option>
            <option>Dundee Utd</option>
            <option>Hearts</option>
            <option>Hibernian</option>
            <option>Inverness CT</option>
            <option>Kilmarnock</option>
            <option>Motherwell</option>
            <option>Partick Thistle</option>
            <option>Ross County</option>
            <option>St Johnstone</option>
            <option>St Mirren</option>
        </optgroup>
        <optgroup label="Scottish Championship">
            <option>Dundee</option>
            <option>Hamilton</option>
            <option>Falkirk</option>
            <option>Raith Rovers</option>
            <option>Alloa</option>
            <option>Livingston</option>
            <option>Queen of Sth</option>
            <option>Dumbarton</option>
            <option>Cowdenbeath</option>
            <option>Morton</option>
        </optgroup>
        <optgroup label="Scottish League One">
            <option>Airdrieonians</option>
            <option>Arbroath</option>
            <option>Ayr</option>
            <option>Brechin</option>
            <option>Dunfermline</option>
            <option>East Fife</option>
            <option>Forfar</option>
            <option>Rangers</option>
            <option>Stenhousemuir</option>
            <option>Stranraer</option>
        </optgroup>
        <optgroup label="Scottish League Two">
            <option>Albion</option>
            <option>Annan</option>
            <option>Berwick</option>
            <option>Clyde</option>
            <option>East Stirling</option>
            <option>Elgin</option>
            <option>Montrose</option>
            <option>Peterhead</option>
            <option>Queen's Park</option>
            <option>Stirling</option>
        </optgroup>
        <optgroup label="Conference">
            <option>Luton</option>
            <option>Cambridge</option>
            <option>Alfreton</option>
            <option>Grimsby</option>
            <option>Halifax</option>
            <option>Kidderminster</option>
            <option>Nuneaton</option>
            <option>Salisbury</option>
            <option>Barnet</option>
            <option>Braintree</option>
            <option>Macclesfield</option>
            <option>Gateshead</option>
            <option>Welling</option>
            <option>Forest Green</option>
            <option>Wrexham</option>
            <option>Hereford</option>
            <option>Woking</option>
            <option>Lincoln City</option>
            <option>Aldershot</option>
            <option>Southport</option>
            <option>Chester</option>
            <option>Tamworth</option>
            <option>Dartford</option>
            <option>Hyde</option>
        </optgroup>
        <optgroup label="La Liga">
            <option>Barcelona</option>
            <option>Atlético Madrid</option>
            <option>Real Madrid</option>
            <option>Athletic Club</option>
            <option>Real Sociedad</option>
            <option>Villarreal</option>
            <option>Sevilla</option>
            <option>Getafe</option>
            <option>Espanyol</option>
            <option>Málaga</option>
            <option>Valencia CF</option>
            <option>Granada CF</option>
            <option>Levante</option>
            <option>Elche</option>
            <option>Celta de Vigo</option>
            <option>Almería</option>
            <option>Real Valladolid</option>
            <option>Osasuna</option>
            <option>Rayo Vallecano</option>
            <option>Real Betis</option>
        </optgroup>
    </select>
</div>
<div class="pure-control-group">
    <label for="choice3">Choice Three</label>
    <select id="choice3" name="choice3">
        <optgroup label="Premiership">
            <option>Arsenal</option>
            <option>Aston Villa</option>
            <option>Cardiff</option>
            <option>Chelsea</option>
            <option>Crystal Palace</option>
            <option>Everton</option>
            <option>Fulham</option>
            <option>Hull</option>
            <option>Liverpool</option>
            <option>Man City</option>
            <option>Man Utd</option>
            <option>Newcastle</option>
            <option>Norwich</option>
            <option>Southampton</option>
            <option>Stoke</option>
            <option>Sunderland</option>
            <option>Swansea</option>
            <option>Tottenham</option>
            <option>West Brom</option>
            <option>West Ham</option>
        </optgroup>
        <optgroup label="Championship">
            <option>Barnsley</option>
            <option>Birmingham</option>
            <option>Blackburn</option>
            <option>Blackpool</option>
            <option>Bolton</option>
            <option>Bournemouth</option>
            <option>Brighton</option>
            <option>Burnley</option>
            <option>Charlton</option>
            <option>Derby</option>
            <option>Doncaster</option>
            <option>Huddersfield</option>
            <option>Ipswich</option>
            <option>Leeds</option>
            <option>Leicester</option>
            <option>Middlesbrough</option>
            <option>Millwall</option>
            <option>Nottm Forest</option>
            <option>QPR</option>
            <option>Reading</option>
            <option>Sheff Wed</option>
            <option>Watford</option>
            <option>Wigan</option>
            <option>Yeovil</option>
        </optgroup>
        <optgroup label="League One">
            <option>Bradford</option>
            <option>Brentford</option>
            <option>Bristol City</option>
            <option>Carlisle</option>
            <option>Colchester</option>
            <option>Coventry</option>
            <option>Crawley</option>
            <option>Crewe</option>
            <option>Gillingham</option>
            <option>Leyton Orient</option>
            <option>MK Dons</option>
            <option>Notts County</option>
            <option>Oldham</option>
            <option>Peterborough</option>
            <option>Port Vale</option>
            <option>Preston</option>
            <option>Rotherham</option>
            <option>Sheffield</option>
            <option>Shrewsbury</option>
            <option>Stevenage</option>
            <option>Swindon</option>
            <option>Tranmere</option>
            <option>Walsall</option>
            <option>Wolves</option>
        </optgroup>
        <optgroup label="League Two">
            <option>Accrington</option>
            <option>Wimbledon</option>
            <option>Bristol Rovers</option>
            <option>Burton</option>
            <option>Bury</option>
            <option>Cheltenham</option>
            <option>Chesterfield</option>
            <option>Dag & Red</option>
            <option>Exeter</option>
            <option>Fleetwood</option>
            <option>Hartlepool</option>
            <option>Mansfield</option>
            <option>Morecambe</option>
            <option>Newport</option>
            <option>Northampton</option>
            <option>Oxford Utd</option>
            <option>Plymouth</option>
            <option>Portsmouth</option>
            <option>Rochdale</option>
            <option>Scunthorpe</option>
            <option>Southend</option>
            <option>Torquay</option>
            <option>Wycombe</option>
            <option>York</option>
        </optgroup>
        <optgroup label="Scottish Premiership">
            <option>Aberdeen</option>
            <option>Celtic</option>
            <option>Dundee Utd</option>
            <option>Hearts</option>
            <option>Hibernian</option>
            <option>Inverness CT</option>
            <option>Kilmarnock</option>
            <option>Motherwell</option>
            <option>Partick Thistle</option>
            <option>Ross County</option>
            <option>St Johnstone</option>
            <option>St Mirren</option>
        </optgroup>
        <optgroup label="Scottish Championship">
            <option>Dundee</option>
            <option>Hamilton</option>
            <option>Falkirk</option>
            <option>Raith Rovers</option>
            <option>Alloa</option>
            <option>Livingston</option>
            <option>Queen of Sth</option>
            <option>Dumbarton</option>
            <option>Cowdenbeath</option>
            <option>Morton</option>
        </optgroup>
        <optgroup label="Scottish League One">
            <option>Airdrieonians</option>
            <option>Arbroath</option>
            <option>Ayr</option>
            <option>Brechin</option>
            <option>Dunfermline</option>
            <option>East Fife</option>
            <option>Forfar</option>
            <option>Rangers</option>
            <option>Stenhousemuir</option>
            <option>Stranraer</option>
        </optgroup>
        <optgroup label="Scottish League Two">
            <option>Albion</option>
            <option>Annan</option>
            <option>Berwick</option>
            <option>Clyde</option>
            <option>East Stirling</option>
            <option>Elgin</option>
            <option>Montrose</option>
            <option>Peterhead</option>
            <option>Queen's Park</option>
            <option>Stirling</option>
        </optgroup>
        <optgroup label="Conference">
            <option>Luton</option>
            <option>Cambridge</option>
            <option>Alfreton</option>
            <option>Grimsby</option>
            <option>Halifax</option>
            <option>Kidderminster</option>
            <option>Nuneaton</option>
            <option>Salisbury</option>
            <option>Barnet</option>
            <option>Braintree</option>
            <option>Macclesfield</option>
            <option>Gateshead</option>
            <option>Welling</option>
            <option>Forest Green</option>
            <option>Wrexham</option>
            <option>Hereford</option>
            <option>Woking</option>
            <option>Lincoln City</option>
            <option>Aldershot</option>
            <option>Southport</option>
            <option>Chester</option>
            <option>Tamworth</option>
            <option>Dartford</option>
            <option>Hyde</option>
        </optgroup>
        <optgroup label="La Liga">
            <option>Barcelona</option>
            <option>Atlético Madrid</option>
            <option>Real Madrid</option>
            <option>Athletic Club</option>
            <option>Real Sociedad</option>
            <option>Villarreal</option>
            <option>Sevilla</option>
            <option>Getafe</option>
            <option>Espanyol</option>
            <option>Málaga</option>
            <option>Valencia CF</option>
            <option>Granada CF</option>
            <option>Levante</option>
            <option>Elche</option>
            <option>Celta de Vigo</option>
            <option>Almería</option>
            <option>Real Valladolid</option>
            <option>Osasuna</option>
            <option>Rayo Vallecano</option>
            <option>Real Betis</option>
        </optgroup>
    </select>
</div>
<div class="pure-control-group">
    <label for="choice4">Choice Four</label>
    <select id="choice4" name="choice4">
        <optgroup label="Premiership">
            <option>Arsenal</option>
            <option>Aston Villa</option>
            <option>Cardiff</option>
            <option>Chelsea</option>
            <option>Crystal Palace</option>
            <option>Everton</option>
            <option>Fulham</option>
            <option>Hull</option>
            <option>Liverpool</option>
            <option>Man City</option>
            <option>Man Utd</option>
            <option>Newcastle</option>
            <option>Norwich</option>
            <option>Southampton</option>
            <option>Stoke</option>
            <option>Sunderland</option>
            <option>Swansea</option>
            <option>Tottenham</option>
            <option>West Brom</option>
            <option>West Ham</option>
        </optgroup>
        <optgroup label="Championship">
            <option>Barnsley</option>
            <option>Birmingham</option>
            <option>Blackburn</option>
            <option>Blackpool</option>
            <option>Bolton</option>
            <option>Bournemouth</option>
            <option>Brighton</option>
            <option>Burnley</option>
            <option>Charlton</option>
            <option>Derby</option>
            <option>Doncaster</option>
            <option>Huddersfield</option>
            <option>Ipswich</option>
            <option>Leeds</option>
            <option>Leicester</option>
            <option>Middlesbrough</option>
            <option>Millwall</option>
            <option>Nottm Forest</option>
            <option>QPR</option>
            <option>Reading</option>
            <option>Sheff Wed</option>
            <option>Watford</option>
            <option>Wigan</option>
            <option>Yeovil</option>
        </optgroup>
        <optgroup label="League One">
            <option>Bradford</option>
            <option>Brentford</option>
            <option>Bristol City</option>
            <option>Carlisle</option>
            <option>Colchester</option>
            <option>Coventry</option>
            <option>Crawley</option>
            <option>Crewe</option>
            <option>Gillingham</option>
            <option>Leyton Orient</option>
            <option>MK Dons</option>
            <option>Notts County</option>
            <option>Oldham</option>
            <option>Peterborough</option>
            <option>Port Vale</option>
            <option>Preston</option>
            <option>Rotherham</option>
            <option>Sheffield</option>
            <option>Shrewsbury</option>
            <option>Stevenage</option>
            <option>Swindon</option>
            <option>Tranmere</option>
            <option>Walsall</option>
            <option>Wolves</option>
        </optgroup>
        <optgroup label="League Two">
            <option>Accrington</option>
            <option>Wimbledon</option>
            <option>Bristol Rovers</option>
            <option>Burton</option>
            <option>Bury</option>
            <option>Cheltenham</option>
            <option>Chesterfield</option>
            <option>Dag & Red</option>
            <option>Exeter</option>
            <option>Fleetwood</option>
            <option>Hartlepool</option>
            <option>Mansfield</option>
            <option>Morecambe</option>
            <option>Newport</option>
            <option>Northampton</option>
            <option>Oxford Utd</option>
            <option>Plymouth</option>
            <option>Portsmouth</option>
            <option>Rochdale</option>
            <option>Scunthorpe</option>
            <option>Southend</option>
            <option>Torquay</option>
            <option>Wycombe</option>
            <option>York</option>
        </optgroup>
        <optgroup label="Scottish Premiership">
            <option>Aberdeen</option>
            <option>Celtic</option>
            <option>Dundee Utd</option>
            <option>Hearts</option>
            <option>Hibernian</option>
            <option>Inverness CT</option>
            <option>Kilmarnock</option>
            <option>Motherwell</option>
            <option>Partick Thistle</option>
            <option>Ross County</option>
            <option>St Johnstone</option>
            <option>St Mirren</option>
        </optgroup>
        <optgroup label="Scottish Championship">
            <option>Dundee</option>
            <option>Hamilton</option>
            <option>Falkirk</option>
            <option>Raith Rovers</option>
            <option>Alloa</option>
            <option>Livingston</option>
            <option>Queen of Sth</option>
            <option>Dumbarton</option>
            <option>Cowdenbeath</option>
            <option>Morton</option>
        </optgroup>
        <optgroup label="Scottish League One">
            <option>Airdrieonians</option>
            <option>Arbroath</option>
            <option>Ayr</option>
            <option>Brechin</option>
            <option>Dunfermline</option>
            <option>East Fife</option>
            <option>Forfar</option>
            <option>Rangers</option>
            <option>Stenhousemuir</option>
            <option>Stranraer</option>
        </optgroup>
        <optgroup label="Scottish League Two">
            <option>Albion</option>
            <option>Annan</option>
            <option>Berwick</option>
            <option>Clyde</option>
            <option>East Stirling</option>
            <option>Elgin</option>
            <option>Montrose</option>
            <option>Peterhead</option>
            <option>Queen's Park</option>
            <option>Stirling</option>
        </optgroup>
        <optgroup label="Conference">
            <option>Luton</option>
            <option>Cambridge</option>
            <option>Alfreton</option>
            <option>Grimsby</option>
            <option>Halifax</option>
            <option>Kidderminster</option>
            <option>Nuneaton</option>
            <option>Salisbury</option>
            <option>Barnet</option>
            <option>Braintree</option>
            <option>Macclesfield</option>
            <option>Gateshead</option>
            <option>Welling</option>
            <option>Forest Green</option>
            <option>Wrexham</option>
            <option>Hereford</option>
            <option>Woking</option>
            <option>Lincoln City</option>
            <option>Aldershot</option>
            <option>Southport</option>
            <option>Chester</option>
            <option>Tamworth</option>
            <option>Dartford</option>
            <option>Hyde</option>
        </optgroup>
        <optgroup label="La Liga">
            <option>Barcelona</option>
            <option>Atlético Madrid</option>
            <option>Real Madrid</option>
            <option>Athletic Club</option>
            <option>Real Sociedad</option>
            <option>Villarreal</option>
            <option>Sevilla</option>
            <option>Getafe</option>
            <option>Espanyol</option>
            <option>Málaga</option>
            <option>Valencia CF</option>
            <option>Granada CF</option>
            <option>Levante</option>
            <option>Elche</option>
            <option>Celta de Vigo</option>
            <option>Almería</option>
            <option>Real Valladolid</option>
            <option>Osasuna</option>
            <option>Rayo Vallecano</option>
            <option>Real Betis</option>
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
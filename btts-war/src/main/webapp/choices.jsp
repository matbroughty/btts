<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

<body>

<form action="/choices" method="post">
    <fieldset>
        <legend>Choices</legend>
        <span><label for="player">Player</label></span>
        <select id="player" name="player">
                <option>Mat</option>
                <option>Frank</option>
                <option>Pia</option>
                <option>Jason</option>
                <option>Jonathon</option>
                <option>Dave</option>
        </select>
        <label for="choice1">choice1</label>
        <select id="choice1" name="choice1">
            <optgroup label="Premiership">
                <option>Arsenal</option>
                <option>Spurs</option>
                <option>Man U</option>
            </optgroup>
            <optgroup label="Championship">
                <option>QPR</option>
                <option>Wigan</option>
                <option>Brighton</option>
            </optgroup>
        </select>
        <label for="choice2">choice2</label>
        <select id="choice2" name="choice2">
            <optgroup label="Premiership">
                <option>Arsenal</option>
                <option>Spurs</option>
                <option>Man U</option>
            </optgroup>
            <optgroup label="Championship">
                <option>QPR</option>
                <option>Wigan</option>
                <option>Brighton</option>
            </optgroup>
        </select>
        <label for="choice3">choice3</label>
        <select id="choice3" name="choice3">
            <optgroup label="Premiership">
                <option>Arsenal</option>
                <option>Spurs</option>
                <option>Man U</option>
            </optgroup>
            <optgroup label="Championship">
                <option>QPR</option>
                <option>Wigan</option>
                <option>Brighton</option>
            </optgroup>
        </select>
        <label for="choice4">choice4</label>
        <select id="choice4" name="choice4">
            <optgroup label="Premiership">
                <option>Arsenal</option>
                <option>Spurs</option>
                <option>Man U</option>
            </optgroup>
            <optgroup label="Championship">
                <option>QPR</option>
                <option>Wigan</option>
                <option>Brighton</option>
            </optgroup>
        </select>
    </fieldset>
    <div><input type="submit" value="Post Choices" /></div>
</form>

</body>
</html>
package com.broughty.btts.servlets;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;


/**
 * Created by matbroughty on 14/12/13.
 */

public class FixtureResultsTest {


    @Test
    public void testGetPremResults() {


        try {

            Document doc = Jsoup.connect("http://www.bbc.co.uk/sport/football/premier-league/fixtures").get();

            Elements elements = doc.getElementsByClass("match-details");

            for (Element element : elements) {


                if (!StringUtils.equalsIgnoreCase(element.text(), "fixture")) {


                    String fixtureDateStr = StringUtils.substringAfter(element.parent().parent().parent().child(0).text(), "This table charts the fixtures during ");


                    System.out.println(fixtureDateStr);
                    System.out.println(element.text());


                    fixtureDateStr = StringUtils.substringAfter(fixtureDateStr, " ");
                    fixtureDateStr = StringUtils.remove(fixtureDateStr, "th");
                    fixtureDateStr = StringUtils.remove(fixtureDateStr, "nd");
                    fixtureDateStr = StringUtils.remove(fixtureDateStr, "rd");
                    fixtureDateStr = StringUtils.remove(fixtureDateStr, "st");




                    DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                            .appendDayOfMonth(2)
                            .appendLiteral(' ')
                            .appendMonthOfYearText()
                            .appendLiteral(' ')
                            .appendYear(4, 4)
                            .toFormatter();


                    DateTime fixtureDate = fmt.parseDateTime(fixtureDateStr);


                    DateTime currentDate = new DateTime();

                    // no point in processing
                    if(Days.daysBetween(currentDate.toDateMidnight(), fixtureDate.toDateMidnight()).getDays() > 4){
                        System.out.println("days between currentDate.toDateMidnight() " + currentDate.toDateMidnight().toString() + " and fixtureDate.toDateMidnight() " +
                                fixtureDate.toDateMidnight().toString() + " is greater than 4.");
                        break;
                    }

                    System.out.println("processing match " + element.text() + " on date: " + fixtureDateStr);


                    // if it contains a " V " then it isn't in progress...
                    if(!StringUtils.contains(element.text(), " V ") && !StringUtils.contains(element.text(), "P-P")){

                        String homeTeam = StringUtils.substringBeforeLast(StringUtils.substringBefore(element.text(), "-"), " ");
                        Integer homeTeamScore = Integer.valueOf(StringUtils.substringAfterLast(StringUtils.substringBefore(element.text(), "-"), " "));
                        System.out.println("processing home match " + homeTeam + " on score : " + homeTeamScore);


                        String awayTeam = StringUtils.substringAfter(StringUtils.substringAfter(element.text(), "-"), " ");
                        System.out.println("processing away match " + awayTeam);
                        Integer awayTeamScore = Integer.valueOf(StringUtils.substringBefore(StringUtils.substringAfter(element.text(), "-"), " "));




                        System.out.println("processing away match " + awayTeam + " on score : " + awayTeamScore);

                    }


                }


            }

        } catch (Throwable t) {

            t.printStackTrace(System.out);

        }

    }


    @Test
    public void testDateFormat(){
        String fixtureDate = "Saturday 14th December 2013";

        fixtureDate = StringUtils.substringAfter(fixtureDate, " ");
        fixtureDate = StringUtils.remove(fixtureDate, "th");
        fixtureDate = StringUtils.remove(fixtureDate, "nd");
        fixtureDate = StringUtils.remove(fixtureDate, "rd");
        fixtureDate = StringUtils.remove(fixtureDate, "st");




        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendDayOfMonth(2)
                .appendLiteral(' ')
                .appendMonthOfYearText()
                .appendLiteral(' ')
                .appendYear(4, 4)
                .toFormatter();


        DateTime dt = fmt.parseDateTime(fixtureDate);

        System.out.println(dt.toString());

    }

    @Test
    public void testGetChampionshipResults() {

        try {
            Document doc = Jsoup.connect("http://www.bbc.co.uk/sport/football/scottish-league-two/fixtures").get();

            Elements elements = doc.getElementsByClass("match-details");

            for (Element element : elements) {


                if (!StringUtils.equalsIgnoreCase(element.text(), "fixture")) {


                    String fixtureDateStr = StringUtils.substringAfter(element.parent().parent().parent().child(0).text(), "This table charts the fixtures during ");


                    System.out.println(fixtureDateStr);
                    System.out.println(element.text());


                    fixtureDateStr = StringUtils.substringAfter(fixtureDateStr, " ");
                    fixtureDateStr = StringUtils.remove(fixtureDateStr, "th");
                    fixtureDateStr = StringUtils.remove(fixtureDateStr, "nd");
                    fixtureDateStr = StringUtils.remove(fixtureDateStr, "rd");
                    fixtureDateStr = StringUtils.remove(fixtureDateStr, "st");




                    DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                            .appendDayOfMonth(2)
                            .appendLiteral(' ')
                            .appendMonthOfYearText()
                            .appendLiteral(' ')
                            .appendYear(4, 4)
                            .toFormatter();


                    DateTime fixtureDate = fmt.parseDateTime(fixtureDateStr);


                    DateTime currentDate = new DateTime();

                    // no point in processing
                    if(Days.daysBetween(currentDate.toDateMidnight(), fixtureDate.toDateMidnight()).getDays() > 4){
                        System.out.println("days between currentDate.toDateMidnight() " + currentDate.toDateMidnight().toString() + " and fixtureDate.toDateMidnight() " +
                                fixtureDate.toDateMidnight().toString() + " is greater than 4.");
                        break;
                    }

                    System.out.println("processing match " + element.text() + " on date: " + fixtureDateStr);


                    // if it contains a " V " then it isn't in progress...
                    if(!StringUtils.contains(element.text(), " V ") && !StringUtils.contains(element.text(), "P-P")){

                        String homeTeam = StringUtils.substringBeforeLast(StringUtils.substringBefore(element.text(), "-"), " ");
                        Integer homeTeamScore = Integer.valueOf(StringUtils.substringAfterLast(StringUtils.substringBefore(element.text(), "-"), " "));
                        System.out.println("processing home match " + homeTeam + " on score : " + homeTeamScore);


                        String awayTeam = StringUtils.substringAfter(StringUtils.substringAfter(element.text(), "-"), " ");
                        System.out.println("processing away match " + awayTeam);
                        Integer awayTeamScore = Integer.valueOf(StringUtils.substringBefore(StringUtils.substringAfter(element.text(), "-"), " "));




                        System.out.println("processing away match " + awayTeam + " on score : " + awayTeamScore);

                    }


                }

            }

        } catch (Throwable t) {

            t.printStackTrace(System.out);

        }



    }

}

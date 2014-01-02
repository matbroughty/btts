package com.broughty.util;

/**
 * Created by matbroughty on 14/12/13.
 */
public enum ResultsWebPageEnum {

    PREM("http://www.bbc.co.uk/sport/football/premier-league/fixtures"),
    CHAMPIONSHIP("http://www.bbc.co.uk/sport/football/championship/fixtures"),
    LEAGUE_ONE("http://www.bbc.co.uk/sport/football/league-one/fixtures"),
    LEAGUE_TWO("http://www.bbc.co.uk/sport/football/league-two/fixtures"),
    SCOT_PREM("http://www.bbc.co.uk/sport/football/scottish-premiership/fixtures"),
    SCOT_CHAMP("http://www.bbc.co.uk/sport/football/scottish-championship/fixtures"),
    SCOT_ONE("http://www.bbc.co.uk/sport/football/scottish-league-one/fixtures"),
    SCOT_TWO("http://www.bbc.co.uk/sport/football/scottish-league-two/fixtures"),
    CONFERENCE("http://www.bbc.co.uk/sport/football/conference/fixtures"),
    FA_CUP("http://www.bbc.co.uk/sport/football/fa-cup/fixtures"),
    EUROPE("http://www.bbc.co.uk/sport/football/european/fixtures");

    String page;

    ResultsWebPageEnum(String page){
        this.page = page;
    }


    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}

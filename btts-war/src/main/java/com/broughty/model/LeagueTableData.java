package com.broughty.model;

import java.util.List;

/**
 * Created by matbroughty on 27/12/13.
 */
public class LeagueTableData {

    String playerName;
    List<Integer> points;

    public LeagueTableData(String playerName, List<Integer> points) {
        this.playerName = playerName;
        this.points = points;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public void setPoints(List<Integer> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "LeagueTableData{" +
                "playerName='" + playerName + '\'' +
                ", points=" + points +
                '}';
    }
}

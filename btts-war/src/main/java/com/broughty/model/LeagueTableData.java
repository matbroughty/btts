package com.broughty.model;

import com.broughty.util.PlayerEnum;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by matbroughty on 27/12/13.
 */
public class LeagueTableData {

    String playerName;
    Integer total;
    List<Integer> points;

    public LeagueTableData(String playerName, List<Integer> points) {
        setPlayerName(playerName);
        this.points = points;
        this.total = sum(points);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = StringUtils.containsIgnoreCase(playerName, "Star") ? PlayerEnum.Star.toString() : playerName;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public void setPoints(List<Integer> points) {
        this.points = points;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer sum(List<Integer> list) {
        Integer sum = 0;
        for (Integer i : list) {
            if (i != null) {
                sum = sum + i;
            }
        }
        return sum;
    }

    @Override
    public String toString() {
        return "LeagueTableData{" +
                "playerName='" + playerName + '\'' +
                ", total=" + total +
                ", points=" + points +
                '}';
    }
}

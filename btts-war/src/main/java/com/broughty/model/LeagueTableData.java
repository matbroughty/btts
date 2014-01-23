package com.broughty.model;

import com.broughty.util.PlayerEnum;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by matbroughty on 27/12/13.
 */
public class LeagueTableData implements Serializable {

    String playerName;
    Integer total;
    Integer bttsCount;
    List<Integer> points;

    public LeagueTableData(String playerName, List<Integer> points, Integer bttsCount) {
        setPlayerName(playerName);
        this.points = points;
        this.total = sum(points);
        this.bttsCount = bttsCount;
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

    public Integer getBttsCount() {
        return bttsCount;
    }

    public void setBttsCount(Integer bttsCount) {
        this.bttsCount = bttsCount;
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
                ", bttsCount=" + bttsCount +
                ", points=" + points +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeagueTableData that = (LeagueTableData) o;

        if (!bttsCount.equals(that.bttsCount)) return false;
        if (!playerName.equals(that.playerName)) return false;
        if (!points.equals(that.points)) return false;
        if (!total.equals(that.total)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerName.hashCode();
        result = 31 * result + total.hashCode();
        result = 31 * result + bttsCount.hashCode();
        result = 31 * result + points.hashCode();
        return result;
    }
}

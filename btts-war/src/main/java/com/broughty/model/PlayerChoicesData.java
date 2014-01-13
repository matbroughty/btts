package com.broughty.model;

import com.broughty.util.PlayerEnum;
import com.broughty.util.ResultEnum;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * Created by matbroughty on 04/01/14.
 */
public class PlayerChoicesData {

    Date dateEntered;
    String week;
    String player;
    String choice1;
    String choice2;
    String choice3;
    String choice4;
    ResultEnum choice1Result = ResultEnum.WAITING;
    ResultEnum choice2Result = ResultEnum.WAITING;
    ResultEnum choice3Result = ResultEnum.WAITING;
    ResultEnum choice4Result = ResultEnum.WAITING;
    int choice1Points = 0;
    int choice2Points = 0;
    int choice3Points = 0;
    int choice4Points = 0;
    int pointsTotal = 0;
    boolean alerted;
    boolean defaultChoices;

    public PlayerChoicesData(String week) {
        this.week = week;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = StringUtils.containsIgnoreCase(player, "Star") ? PlayerEnum.Star.toString() : player;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

    public String getChoice4() {
        return choice4;
    }

    public void setChoice4(String choice4) {
        this.choice4 = choice4;
    }

    public ResultEnum getChoice1Result() {
        return choice1Result;
    }

    public void setChoice1Result(ResultEnum choice1Result) {
        this.choice1Result = choice1Result;
    }

    public ResultEnum getChoice2Result() {
        return choice2Result;
    }

    public void setChoice2Result(ResultEnum choice2Result) {
        this.choice2Result = choice2Result;
    }

    public ResultEnum getChoice3Result() {
        return choice3Result;
    }

    public void setChoice3Result(ResultEnum choice3Result) {
        this.choice3Result = choice3Result;
    }

    public ResultEnum getChoice4Result() {
        return choice4Result;
    }

    public void setChoice4Result(ResultEnum choice4Result) {
        this.choice4Result = choice4Result;
    }

    public int getChoice1Points() {
        return choice1Points;
    }

    public void setChoice1Points(int choice1Points) {
        this.choice1Points = choice1Points;
    }

    public int getChoice2Points() {
        return choice2Points;
    }

    public void setChoice2Points(int choice2Points) {
        this.choice2Points = choice2Points;
    }

    public int getChoice3Points() {
        return choice3Points;
    }

    public void setChoice3Points(int choice3Points) {
        this.choice3Points = choice3Points;
    }

    public int getChoice4Points() {
        return choice4Points;
    }

    public void setChoice4Points(int choice4Points) {
        this.choice4Points = choice4Points;
    }

    public boolean isAlerted() {
        return alerted;
    }

    public void setAlerted(boolean alerted) {
        this.alerted = alerted;
    }

    public boolean isDefaultChoices() {
        return defaultChoices;
    }

    public void setDefaultChoices(boolean defaultChoices) {
        this.defaultChoices = defaultChoices;
    }

    public Date getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    public int getPointsTotal() {
        return pointsTotal;
    }

    public void setPointsTotal(int pointsTotal) {
        this.pointsTotal = pointsTotal;
    }

    @Override
    public String toString() {
        return super.toString();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void calculatePointsTotal() {
        setPointsTotal(getChoice1Points() + getChoice2Points() + getChoice3Points() + getChoice4Points());
    }
}

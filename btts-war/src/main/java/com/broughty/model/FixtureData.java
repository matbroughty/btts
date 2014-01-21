package com.broughty.model;

import java.util.Date;

public class FixtureData {
    String homeTeam;
    String awayTeam;
    Date date;
    String id;
    String type;
	
	public FixtureData(String homeTeam, String awayTeam, Date date, String id, String type) {
		this.setHomeTeam(homeTeam);
		this.setAwayTeam(awayTeam);
		this.setDate(date);
		this.setId(id);
		this.setType(type);
	}
	public String getHomeTeam() {
		return homeTeam;
	}
	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}
	public String getAwayTeam() {
		return awayTeam;
	}
	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

    @Override
    public String toString() {
        return "FixtureData{" +
                "homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", date=" + date +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

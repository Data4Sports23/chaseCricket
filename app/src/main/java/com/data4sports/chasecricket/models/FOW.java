package com.data4sports.chasecricket.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FOW extends RealmObject {

    @PrimaryKey
    private int fowID;
    private int matchid;
    private String matchID;
    private int innings;
    private int team;
//    private int syncstataus;
    private int run;
    public int wicket;
    private int dismissedPlayerID;
    private int bowlerID;
    private String fielderID;
    private String over;

    private int balls;  // Added on 27/07/2021 (for 100s)
//    private String batsmanName;

//    private boolean SUPER_OVER;


    public FOW() {
    }


    public int getFowID() {
        return fowID;
    }

    public void setFowID(int fowID) {
        this.fowID = fowID;
    }

//    public int getSyncstataus() {
//        return syncstataus;
//    }

//    public void setSyncstataus(int syncstataus) {
//        this.syncstataus = syncstataus;
//    }

    public int getMatchid() {
        return matchid;
    }

    public void setMatchid(int matchid) {
        this.matchid = matchid;
    }

    public int getInnings() {
        return innings;
    }

    public void setInnings(int innings) {
        this.innings = innings;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getWicket() {
        return wicket;
    }

    public void setWicket(int wicket) {
        this.wicket = wicket;
    }

    public int getDismissedPlayerID() {
        return dismissedPlayerID;
    }

    public void setDismissedPlayerID(int dismissedPlayerID) {
        this.dismissedPlayerID = dismissedPlayerID;
    }

    //    public String getBatsmanName() {
//        return batsmanName;
//    }

//    public void setBatsmanName(String batsmanName) {
//        this.batsmanName = batsmanName;
//    }


    public int getBowlerID() {
        return bowlerID;
    }

    public void setBowlerID(int bowlerID) {
        this.bowlerID = bowlerID;
    }

    public String getFielderID() {
        return fielderID;
    }

    public void setFielderID(String fielderID) {
        this.fielderID = fielderID;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

//    public boolean isSUPER_OVER() {
//        return SUPER_OVER;
//    }

//    public void setSUPER_OVER(boolean SUPER_OVER) {
//        this.SUPER_OVER = SUPER_OVER;
//    }


    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }
}

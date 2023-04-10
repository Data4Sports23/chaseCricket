package com.data4sports.chasecricket.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Bowler extends RealmObject {

    @PrimaryKey
    private int bowlerID;
    private int matchid;
    private String matchID;
    private int playerID;
    private int team;
    private int over;
    private int balls;
    private int runs;
    private int wicket;
    private int maidenOver;
    private int dots;
    private int F4s;
    private int S6s;
    private int wides;
    private int noBalls;
    private int innings;

    private int totalBalls; // for 100s

    private boolean SUPER_OVER;

    // Added on 24/09/2021
    // added for storing current over status of a bowler
    private int cv_ball;
    private int cv_run;
    private int cv_wicket;
    private int cv_dots;
    private int cv_F4s;
    private int cv_S6s;
    private int cv_wides;
    private int cv_noball;

    public Bowler() {
        init();
    }


    private void init(){

        matchid = 0;
        matchID = "";
        playerID = 0;
        team = 0;
        over = 0;
        balls = 0;
        runs = 0;
        wicket = 0;
        maidenOver = 0;
        dots = 0;
        F4s = 0;
        S6s = 0;
        wides = 0;
        noBalls = 0;
        innings = 0;
        SUPER_OVER = false;
        totalBalls = 0;

        cv_ball = 0;
        cv_run = 0;
        cv_wicket = 0;
        cv_dots = 0;
        cv_F4s = 0;
        cv_S6s = 0;
        cv_wides = 0;
        cv_noball = 0;
    }

    public int getMatchid() {
        return matchid;
    }

    public void setMatchid(int matchid) {
        this.matchid = matchid;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getBowlerID() {
        return bowlerID;
    }

    public void setBowlerID(int bowlerID) {
        this.bowlerID = bowlerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public int getOver() {
        return over;
    }

    public void setOver(int over) {
        this.over = over;
    }

    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }

    public int getMaidenOver() {
        return maidenOver;
    }

    public void setMaidenOver(int maidenOver) {
        this.maidenOver = maidenOver;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getWicket() {
        return wicket;
    }

    public void setWicket(int wicket) {
        this.wicket = wicket;
    }

    public int getDots() {
        return dots;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }

    public int getF4s() {
        return F4s;
    }

    public void setF4s(int f4s) {
        F4s = f4s;
    }

    public int getS6s() {
        return S6s;
    }

    public void setS6s(int s6s) {
        S6s = s6s;
    }

    public int getWides() {
        return wides;
    }

    public void setWides(int wides) {
        this.wides = wides;
    }

    public int getNoBalls() {
        return noBalls;
    }

    public void setNoBalls(int noBalls) {
        this.noBalls = noBalls;
    }

    public int getInnings() {
        return innings;
    }

    public void setInnings(int innings) {
        this.innings = innings;
    }

    public boolean isSUPER_OVER() {
        return SUPER_OVER;
    }

    public void setSUPER_OVER(boolean SUPER_OVER) {
        this.SUPER_OVER = SUPER_OVER;
    }

    public int getTotalBalls() {
        return totalBalls;
    }

    public void setTotalBalls(int totalBalls) {
        this.totalBalls = totalBalls;
    }

    public int getCv_ball() {
        return cv_ball;
    }

    public void setCv_ball(int cv_ball) {
        this.cv_ball = cv_ball;
    }

    public int getCv_run() {
        return cv_run;
    }

    public void setCv_run(int cv_run) {
        this.cv_run = cv_run;
    }

    public int getCv_wicket() {
        return cv_wicket;
    }

    public void setCv_wicket(int cv_wicket) {
        this.cv_wicket = cv_wicket;
    }

    public int getCv_dots() {
        return cv_dots;
    }

    public void setCv_dots(int cv_dots) {
        this.cv_dots = cv_dots;
    }

    public int getCv_F4s() {
        return cv_F4s;
    }

    public void setCv_F4s(int cv_F4s) {
        this.cv_F4s = cv_F4s;
    }

    public int getCv_S6s() {
        return cv_S6s;
    }

    public void setCv_S6s(int cv_S6s) {
        this.cv_S6s = cv_S6s;
    }

    public int getCv_wides() {
        return cv_wides;
    }

    public void setCv_wides(int cv_wides) {
        this.cv_wides = cv_wides;
    }

    public int getCv_noball() {
        return cv_noball;
    }

    public void setCv_noball(int cv_noball) {
        this.cv_noball = cv_noball;
    }
}

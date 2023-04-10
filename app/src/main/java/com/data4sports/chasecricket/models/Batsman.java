package com.data4sports.chasecricket.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Updated on 18/03/2021
 */

public class Batsman extends RealmObject {

    @PrimaryKey
    private int batsmanID;
    private int matchid;
    private String matchID;
    private int batsman_pID;
    private int batsman_d4sID;
    private int bowler_pID;
    private int bowler_D4SID;
    private String fielder_pID;
    private String fielder_D4SID;
    private int runs;
    private int balls;
    private int dots;
    private int F4s;
    private int S6s;
    private int outType;
    private int team;
    private int innings;
    private int battingOrder;

    private boolean out;
    private boolean playing;
    private boolean retired;
    private boolean toBeBatted;
    private boolean SUPER_OVER;

    // Added on 18/03/2021
    private boolean wk_fielder;

    public Batsman() {

        init();
    }


    public void init(){

        batsmanID = 0;
        matchid = 0;
        matchID = "";
        batsman_pID = 0;
//        batsman_d4sID = 0;
        bowler_pID = 0;
//        bowler_d4sID = 0;
        fielder_pID = null;
//        fielder_d4sID = null;
        runs = 0;
        balls = 0;
        dots = 0;
        F4s = 0;
        S6s = 0;
        outType = -1;
        team = 0;
        innings = 0;
        battingOrder = 100;

        out = false;
        playing = false;
        retired = false;
        toBeBatted = true;
        SUPER_OVER = false;
        wk_fielder = false;
    }


    public int getBatsmanID() {
        return batsmanID;
    }

    public void setBatsmanID(int batsmanID) {
        this.batsmanID = batsmanID;
    }

    public int getMatchid() {
        return matchid;
    }

    public void setMatchid(int matchid) {
        this.matchid = matchid;
    }

    public int getBatsman_pID() {
        return batsman_pID;
    }

    public void setBatsman_pID(int batsman_pID) {
        this.batsman_pID = batsman_pID;
    }

    public int getBowler_pID() {
        return bowler_pID;
    }

    public void setBowler_pID(int bowler_pID) {
        this.bowler_pID = bowler_pID;
    }

    public String getFielder_pID() {
        return fielder_pID;
    }

    public void setFielder_pID(String fielder_pID) {
        this.fielder_pID = fielder_pID;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
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

    public int getOutType() {
        return outType;
    }

    public void setOutType(int outType) {
        this.outType = outType;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getInnings() {
        return innings;
    }

    public void setInnings(int innings) {
        this.innings = innings;
    }

    public int getBattingOrder() {
        return battingOrder;
    }

    public void setBattingOrder(int battingOrder) {
        this.battingOrder = battingOrder;
    }

  /*  public int getBatsman_d4sID() {
        return batsman_d4sID;
    }

    public void setBatsman_d4sID(int batsman_d4sID) {
        this.batsman_d4sID = batsman_d4sID;
    }

    public int getBowler_d4sID() {
        return bowler_d4sID;
    }

    public void setBowler_d4sID(int bowler_d4sID) {
        this.bowler_d4sID = bowler_d4sID;
    }

    public String getFielder_d4sID() {
        return fielder_d4sID;
    }

    public void setFielder_d4sID(String fielder_d4sID) {
        this.fielder_d4sID = fielder_d4sID;
    }*/

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public boolean isToBeBatted() {
        return toBeBatted;
    }

    public void setToBeBatted(boolean toBeBatted) {
        this.toBeBatted = toBeBatted;
    }


    public boolean isSUPER_OVER() {
        return SUPER_OVER;
    }

    public void setSUPER_OVER(boolean SUPER_OVER) {
        this.SUPER_OVER = SUPER_OVER;
    }

    public boolean isWk_fielder() {
        return wk_fielder;
    }

    public void setWk_fielder(boolean wk_fielder) {
        this.wk_fielder = wk_fielder;
    }
}

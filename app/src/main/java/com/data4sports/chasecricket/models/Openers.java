package com.data4sports.chasecricket.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Openers extends RealmObject {

    @PrimaryKey
    private int openersID;

    private int matchid;
    private int innings;
    private String matchID;
    private int strikerID;
//    private String striker;
    private int nonStrikerID;
//    private String nonStriker;
    private int bowlerID;
//    private String bowler;
//    private String nextBowler;
    private boolean post;

    public Openers() {
        init();
    }

    public void init() {

        matchid = 0;
        innings = 0;
        matchID = "";
        strikerID = 0;
//        striker = "";
        nonStrikerID = 0;
//        nonStriker = "";
        bowlerID = 0;
//        bowler = "";
//        nextBowler = "";
        post = false;
    }

    public int getOpenersID() {
        return openersID;
    }

    public void setOpenersID(int openersID) {
        this.openersID = openersID;
    }

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

    public int getStrikerID() {
        return strikerID;
    }

    public void setStrikerID(int strikerID) {
        this.strikerID = strikerID;
    }

    /*public String getStriker() {
        return striker;
    }

    public void setStriker(String striker) {
        this.striker = striker;
    }*/

    public int getNonStrikerID() {
        return nonStrikerID;
    }

    public void setNonStrikerID(int nonStrikerID) {
        this.nonStrikerID = nonStrikerID;
    }

    /*public String getNonStriker() {
        return nonStriker;
    }

    public void setNonStriker(String nonStriker) {
        this.nonStriker = nonStriker;
    }*/

    public int getBowlerID() {
        return bowlerID;
    }

    public void setBowlerID(int bowlerID) {
        this.bowlerID = bowlerID;
    }

   /* public String getBowler() {
        return bowler;
    }

    public void setBowler(String bowler) {
        this.bowler = bowler;
    }*/

/*    public String getNextBowler() {
        return nextBowler;
    }

    public void setNextBowler(String nextBowler) {
        this.nextBowler = nextBowler;
    }*/

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public boolean isPost() {
        return post;
    }

    public void setPost(boolean post) {
        this.post = post;
    }
}

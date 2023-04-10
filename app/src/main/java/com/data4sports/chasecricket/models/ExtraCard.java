package com.data4sports.chasecricket.models;

import io.realm.RealmObject;

public class ExtraCard extends RealmObject {

    private int byes;
    private int lb;
    private int noBall;
    private int wide;
    private int penalty;
    private int team;
    private int innings;
    private int matchid;
    private int syncstatus;

    private float over;

    private String matchID;

    private boolean SUPER_OVER;

    public ExtraCard() {

        init();
    }


    public void init(){

        byes = 0;
        lb = 0;
        noBall = 0;
        wide = 0;
        penalty = 0;
        team = 0;
        innings = 0;
        syncstatus = 0;
        SUPER_OVER = false;
        over = 0f;
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

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String match_id) {
        this.matchID = match_id;
    }

    public int getByes() {
        return byes;
    }

    public void setByes(int byes) {
        this.byes = byes;
    }

    public int getLb() {
        return lb;
    }

    public void setLb(int lb) {
        this.lb = lb;
    }

    public int getNoBall() {
        return noBall;
    }

    public void setNoBall(int noBall) {
        this.noBall = noBall;
    }

    public int getWide() {
        return wide;
    }

    public void setWide(int wide) {
        this.wide = wide;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public int getInnings() {
        return innings;
    }

    public void setInnings(int innings) {
        this.innings = innings;
    }

    public int getSyncstatus() {
        return syncstatus;
    }

    public void setSyncstatus(int syncstatus) {
        this.syncstatus = syncstatus;
    }

    public float getOver() {
        return over;
    }

    public void setOver(float over) {
        this.over = over;
    }

    public boolean isSUPER_OVER() {
        return SUPER_OVER;
    }

    public void setSUPER_OVER(boolean SUPER_OVER) {
        this.SUPER_OVER = SUPER_OVER;
    }
}

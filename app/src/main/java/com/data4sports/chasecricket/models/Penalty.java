package com.data4sports.chasecricket.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Penalty extends RealmObject {

    @PrimaryKey
    private int id;
    private int matchid;
    private String matchID;
    private int innings;
    private int type;              // 1--> Fielding Penalty, 2--> Batting Penalty
    private int penaltyRun;
    private int forTeam;           // penalty for team, means run goes to opposite team
    private int benefitTeam;       // the team gets benefit for penalty

    private boolean sync;
//    private boolean applied;
    private boolean ballCount;
    private boolean bowball;
    private boolean batsball;

    public Penalty() {
        init();
    }

    public void init() {

        matchid = 0;
        matchID = "";
        innings = 0;
        type = 0;
        penaltyRun = 0;
        forTeam = 0;
        benefitTeam = 0;
        sync = false;
//        applied = false;
        ballCount = false;
        bowball = false;
        batsball = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatchid() {
        return matchid;
    }

    public void setMatchid(int matchid) {
        this.matchid = matchid;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public int getInnings() {
        return innings;
    }

    public void setInnings(int innings) {
        this.innings = innings;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPenaltyRun() {
        return penaltyRun;
    }

    public void setPenaltyRun(int penaltyRun) {
        this.penaltyRun = penaltyRun;
    }

    public int getForTeam() {
        return forTeam;
    }

    public void setForTeam(int forTeam) {
        this.forTeam = forTeam;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

//    public boolean isApplied() {
//        return applied;
//    }

//    public void setApplied(boolean applied) {
//        this.applied = applied;
//    }

    public int getBenefitTeam() {
        return benefitTeam;
    }

    public void setBenefitTeam(int benefitTeam) {
        this.benefitTeam = benefitTeam;
    }

    public boolean isBallCount() {
        return ballCount;
    }

    public void setBallCount(boolean ballCount) {
        this.ballCount = ballCount;
    }

    public boolean isBowball() {
        return bowball;
    }

    public void setBowball(boolean bowball) {
        this.bowball = bowball;
    }

    public boolean isBatsball() {
        return batsball;
    }

    public void setBatsball(boolean batsball) {
        this.batsball = batsball;
    }
}

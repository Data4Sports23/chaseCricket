package com.data4sports.chasecricket.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Partnership extends RealmObject {

    @PrimaryKey
    private int partnershipID;
    private int partnershipSequence;
    private int matchid;
    private String matchID;

    private int innings;

    private int sync;

    //partnership details
    private int partnershipRuns;
    private int partnershipBalls;
    private int partnershipOver;
    private int partnershipTotalBalls;  // Added on 28/07/2021 (for HUNDRED)

    //player 1 details
    private int player1ID;
    private int player1Runs;
    private int player1Balls;
    private int player1ContributionRuns;
    private int player1ContributionBalls;

    // player 2 details
    private int player2ID;
    private int player2Runs;
    private int player2Balls;
    private int player2ContributionRuns;
    private int player2ContributionBalls;

    //wicket details
    private int wicket;
    private int dismissedPlayerID;
    private int outType;    // Added on 15/09/2021

    private boolean SUPER_OVER;

    public Partnership() {
        init();
    }


    public void init() {

        partnershipSequence = 0;
        matchid = 0;
        matchID = "";

        innings = 0;

        sync = 0;

        player1ID = -1;
        player1Runs = 0;
        player1Balls = 0;

        player2ID = -1;
        player2Runs = 0;
        player2Balls = 0;

        wicket = 0;
        outType = 0;
        dismissedPlayerID = 0;

        partnershipRuns = 0;
        partnershipBalls = 0;
        player1ContributionRuns = 0;
        player1ContributionBalls = 0;
        player2ContributionRuns = 0;
        player2ContributionBalls = 0;
        partnershipOver = 0;
        partnershipTotalBalls = 0;

        SUPER_OVER = false;
    }


    public int getPartnershipID() {
        return partnershipID;
    }

    public void setPartnershipID(int partnershipID) {
        this.partnershipID = partnershipID;
    }

    public int getPartnershipSequence() {
        return partnershipSequence;
    }

    public void setPartnershipSequence(int partnershipSequence) {
        this.partnershipSequence = partnershipSequence;
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

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public int getPlayer1ID() {
        return player1ID;
    }

    public void setPlayer1ID(int player1ID) {
        this.player1ID = player1ID;
    }

    public int getPlayer1Runs() {
        return player1Runs;
    }

    public void setPlayer1Runs(int player1Runs) {
        this.player1Runs = player1Runs;
    }

    public int getPlayer1Balls() {
        return player1Balls;
    }

    public void setPlayer1Balls(int player1Balls) {
        this.player1Balls = player1Balls;
    }

    public int getPlayer2ID() {
        return player2ID;
    }

    public void setPlayer2ID(int player2ID) {
        this.player2ID = player2ID;
    }

    public int getPlayer2Runs() {
        return player2Runs;
    }

    public void setPlayer2Runs(int player2Runs) {
        this.player2Runs = player2Runs;
    }

    public int getPlayer2Balls() {
        return player2Balls;
    }

    public void setPlayer2Balls(int player2Balls) {
        this.player2Balls = player2Balls;
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

    public int getPartnershipRuns() {
        return partnershipRuns;
    }

    public void setPartnershipRuns(int partnershipRuns) {
        this.partnershipRuns = partnershipRuns;
    }

    public int getPartnershipBalls() {
        return partnershipBalls;
    }

    public void setPartnershipBalls(int partnershipBalls) {
        this.partnershipBalls = partnershipBalls;
    }

    public int getPlayer1ContributionRuns() {
        return player1ContributionRuns;
    }

    public void setPlayer1ContributionRuns(int player1ContributionRuns) {
        this.player1ContributionRuns = player1ContributionRuns;
    }

    public int getPlayer1ContributionBalls() {
        return player1ContributionBalls;
    }

    public void setPlayer1ContributionBalls(int player1ContributionBalls) {
        this.player1ContributionBalls = player1ContributionBalls;
    }

    public int getPlayer2ContributionRuns() {
        return player2ContributionRuns;
    }

    public void setPlayer2ContributionRuns(int player2ContributionRuns) {
        this.player2ContributionRuns = player2ContributionRuns;
    }

    public int getPlayer2ContributionBalls() {
        return player2ContributionBalls;
    }

    public void setPlayer2ContributionBalls(int player2ContributionBalls) {
        this.player2ContributionBalls = player2ContributionBalls;
    }

    public int getPartnershipOver() {
        return partnershipOver;
    }

    public void setPartnershipOver(int partnershipOver) {
        this.partnershipOver = partnershipOver;
    }

    public boolean isSUPER_OVER() {
        return SUPER_OVER;
    }

    public void setSUPER_OVER(boolean SUPER_OVER) {
        this.SUPER_OVER = SUPER_OVER;
    }

    public int getPartnershipTotalBalls() {
        return partnershipTotalBalls;
    }

    public void setPartnershipTotalBalls(int partnershipTotalBalls) {
        this.partnershipTotalBalls = partnershipTotalBalls;
    }

    public int getOutType() {
        return outType;
    }

    public void setOutType(int outType) {
        this.outType = outType;
    }
}

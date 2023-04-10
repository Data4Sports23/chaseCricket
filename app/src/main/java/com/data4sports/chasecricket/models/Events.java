package com.data4sports.chasecricket.models;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Events extends RealmObject {


    @PrimaryKey
    private int eventID;
    private int matchid;
    private String matchID;
    private int syncstatus;
    private int innings;

    private int innings1Runs;
    private int innings2Runs;
    private int innings3Runs;
    private int innings4Runs;
    private int preInningsRuns;
    private int remainingRuns;
    private int remainingBalls;
    private float remainingOvers;
    private int leadingRuns;

    private int battingTeamNo;
    private int fieldingTeamNo;

    private int ballType;

    private float overs;
    private int totalRuns;
    private int balls;
    private int temp_balls;
    private int wicket;

    private int currentOverBalls;
    private int currentRun;
    private String wagonWheelRegion;
    private int strokeDirection;

    private float tco;
//    private float tbo;
//    private float tpbo;

    private int player1ID;
    private int player2ID;

    private int strikerID;
    private int strikerD4SID;   // Added on 13/11/2021 to correct undo
    private int strikerRuns;
    private int strikerBalls;
    private int strikerBattingOrder;
    private int strikerDots;
    private int strikerF4s;
    private int strikerS6s;
    private int strikerOutType; // Added on 10/09/2021 to correct display
    private String strikerNotOutIndicator; // Added on 10/09/2021 to correct display

    private int nonStrikerID;
    private int nonStrikerD4SID;   // Added on 13/11/2021 to correct undo
    private int nonStrikerRuns;
    private int nonStrikerBalls;
    private int nonStrikerBattingOrder;
    private int nonStrikerDots;
    private int nonStrikerF4s;
    private int nonStrikerS6s;
    private int nonStrikerOutType; // Added on 10/09/2021 to correct display
    private String nonStrikerNotOutIndicator; // Added on 10/09/2021 to correct display

    private int bowlerID;
    private int bowlerD4SID;   // Added on 13/11/2021 to correct undo
    private int bowlerRuns;
    private int bowlerBalls;
    private int bowlerTotalBalls; // Added on 26/07/2021
    private int bowlerOver;
    private int bowlerWicket;
    private int bowlerDots;
    private int bowlerMO;
    private int bowlerWides;
    private int bowlerNoball;

    private int prevBowlerID;
    private int prevBowlerD4SID;   // Added on 13/11/2021 to correct undo
    private int preBowlerRuns;
    private int preBowlerBalls;
    private int preBowlerTotalBalls; // Added on 26/07/2021
    private int preBowlerOver;
    private int preBowlerWicket;
    private int preBowlerDots;
    private int preBowlerMO;
    private int preBowlerWides;
    private int preBowlerNoball;

    private int lastPreBowlerID;
    private int lastPreBowlerD4SID;   // Added on 13/11/2021 to correct undo
//    private int nextBowlerID;
    private int newBowlerID;
    private int newBowlerD4SID;   // Added on 13/11/2021 to correct undo

    private int extraType;
    private int extraRuns;  // innings extra
    private int extraBye;
    private int extraLb;
    private int extraWd;
    private int extraNb;
    private int extraP;

    private int outType;
    private int dismissedPlayerID;
    private int dismissedPlayerD4SID;   // Added on 13/11/2021 to correct undo
//    private int dismissedPlayerBattingOrder;    // Added on 28/08/2021 (for undo of balltype 7)
    private int disNewBatsmanID;
    private int disNewBatsmanD4SID;   // Added on 13/11/2021 to correct undo
    private int disNewBatsmanBattingOrder;      // Added on 28/08/2021 (for undo of balltype 7)
    private String fielderID;
    private String fielderD4SID;   // Added on 13/11/2021 to correct undo


//    private int penaltyID;                // added on 01/12/2021
    private int penaltyType;                // no penalty type
    private int penaltyRuns;                // normal penalty run
    private int penaltyTeam;                // penalty given to teamB/ teamA
    private int penaltyRunTeam;             // penalty run goes to teamA/ teamB
    private int penaltyBallCounted;         // counted penalty ball
    private boolean penaltyBallCount;       // whether penalty ball counted or not


    private String commentary;

    private String currentOver;
    private String undoDisplay;

    private int mo; // overSum
    private boolean maidenOver;
    private boolean SUPER_OVER;

    private String session;
    private int sessionId;
    private int intervalId;

    private String postArray;
    private String undoArray;

    private boolean endOfDay;
    private boolean declared;
    private boolean newPartnership;

    private int super_over_innings1runs;
    private int super_over_innings2runs;

    private boolean freeHit;
    private boolean inning_started;

    private int substitutionID;

    // for current partnership
    private int p_wicket_no;
    private int p_sequence_no;
    private int p_run;
    private int p_ball;
    private int p_over;
    private int p_p1Id;
    private int p_p1D4SID;   // Added on 13/11/2021 to correct undo
    private int p_p2Id;
    private int p_p2D4SID;   // Added on 13/11/2021 to correct undo
    private int p_disId;
    private int p_disD4SID;   // Added on 13/11/2021 to correct undo
    private int p_p1cr;
    private int p_p1cb;
    private int p_p2cr;
    private int p_p2cb;
    private boolean p_broken;

    private int power_id;
    // ======= added on 14/05/2020
    // for power play details
    private int power_start_over;
    private int power_end_over;
    private int power_sequence;

    // for concussion-substitution details
    private int sub_team;
    private int sub_playerout_id;
    private int sub_playerin_id;


    // ======== till here
    private int revisedTarget;
    private float reducedOver;
    private String appliedRainRule;

    private boolean pre_out;
    private boolean callBowlerAlert;

    //ADDED ON 27/02/2021
    private int wkID;
    private int wkD4SID;   // Added on 13/11/2021 to correct undo
    private int wk_position;

    // ADDED ON  01/03/2021
    private int preWkID;
    private int preWk_position;

    // Added on 02/03/2021
    private boolean forfeit;
    private int forfeit_team;
    private int forfeit_innings;
    private boolean CONCEDE;                // whether the match is conceded or not
    private int conceded_team;              // name of the team which declare concede
    private int forceEndingType;           // reason to force end current playing innings


    // Added on 28/04/2021
    private int lunch_flag;                 // flag will set to 1 if a lunch break is selected, so that it can not be selected again
    private int tea_flag;
    private int dinner_flag;

    private boolean change_end_flag;    // Added on 26/07/2021
                                        // understand whether the 10th ball is just over or not

    // Added on 22/09/2021
    // for display side change, like 0.0.1 or 2.1.1
    private int ball_count;

    public Events() {

        init();
    }


    private void init(){

        eventID = 0;
        matchid = 0;
        matchID = "";
        syncstatus = 0;
        innings = 0;

        innings1Runs = 0;
        innings2Runs = 0;
        innings3Runs = 0;
        innings4Runs = 0;
        preInningsRuns = 0;
        remainingRuns = 0;
        remainingBalls = 0;
        remainingOvers = 0f;
        leadingRuns = 0;

        battingTeamNo = 0;
        fieldingTeamNo = 0;

        ballType = 0;

        overs = 0f;
        totalRuns = 0;
        balls = 0;
        temp_balls = 0;
        wicket = 0;

        currentOverBalls = 0;
        currentRun = 0;
        wagonWheelRegion = "";
        strokeDirection = -1;

        tco = 0;
//        tbo = 0 ;
//        tpbo = 0;

        player1ID = 0;
        player2ID = 0;

        strikerID = 0;
        strikerD4SID = 0;   // Added on 13/11/2021 to correct undo
        strikerRuns = 0;
        strikerBalls = 0;
        // addde on 14/05/2020
        strikerBattingOrder = 0;
        strikerDots = 0;
        strikerF4s = 0;
        strikerS6s = 0;     // till here
        strikerOutType = 0; // Added on 10/09/2021 to correct display
        strikerNotOutIndicator = "y"; // Added on 10/09/2021 to correct display

        nonStrikerID = 0;
        nonStrikerD4SID = 0;   // Added on 13/11/2021 to correct undo
        nonStrikerRuns = 0;
        nonStrikerBalls = 0;
        // added on 14/05/2020
        nonStrikerBattingOrder = 0;
        nonStrikerDots = 0;
        nonStrikerF4s = 0;
        nonStrikerS6s = 0;  // till here
        nonStrikerOutType = 0; // Added on 10/09/2021 to correct display
        nonStrikerNotOutIndicator = "y"; // Added on 10/09/2021 to correct display

        bowlerID = 0;
        bowlerD4SID = 0;   // Added on 13/11/2021 to correct undo
        bowlerRuns = 0;
        bowlerBalls = 0;
        bowlerTotalBalls = 0; // Added on 26/07/2021
        bowlerOver = 0;
        bowlerWicket = 0;
        // added on 14/05/2020
        bowlerDots = 0;
        bowlerMO = 0;
        bowlerWides = 0;
        bowlerNoball = 0;   // till here

        prevBowlerID = 0;
        prevBowlerD4SID = 0;   // Added on 13/11/2021 to correct undo
        preBowlerRuns = 0;
        preBowlerBalls = 0;
        preBowlerTotalBalls = 0; // Added on 26/07/2021
        preBowlerOver = 0;
        preBowlerWicket = 0;
        // added 0n 14/05/2020
        preBowlerDots = 0;
        preBowlerMO = 0;
        preBowlerWides = 0;
        preBowlerNoball = 0;    // till here

        lastPreBowlerID = 0;
        lastPreBowlerD4SID = 0;   // Added on 13/11/2021 to correct undo

//        nextBowlerID = 0;

        newBowlerID = 0;
        newBowlerD4SID = 0;   // Added on 13/11/2021 to correct undo

        extraType = 0;
        extraRuns = 0;
        // added on 14/05/2020
        extraBye = 0;
        extraLb = 0;
        extraWd = 0;
        extraNb = 0;
        extraP = 0;     // till here

        outType = 0;
        dismissedPlayerID = 0;
        dismissedPlayerD4SID = 0;   // Added on 13/11/2021 to correct undo
//        dismissedPlayerBattingOrder = 0;    // Added on 28/08/2021 (for undo of balltype 7)
        disNewBatsmanBattingOrder = 0;        // Added on 28/08/2021 (for undo of balltype 7)
        disNewBatsmanID = 0;
        disNewBatsmanD4SID = 0;   // Added on 13/11/2021 to correct undo
        fielderID = null;
        fielderD4SID = null;   // Added on 13/11/2021 to correct undo

        penaltyType = 0;
        penaltyRuns = 0;
        penaltyRunTeam = 0;
        penaltyBallCounted = 0;
        penaltyBallCount = false;

        commentary = "";

        currentOver = "";
        undoDisplay = "";

        mo = 0;
        maidenOver = false;
        SUPER_OVER = false;

        session = null;
        sessionId = 0;
        intervalId = 0;

        postArray = null;
        undoArray = null;

        endOfDay = false;
        declared = false;
        newPartnership = false;

        super_over_innings1runs = 0;
        super_over_innings2runs = 0;

        freeHit = false;
        inning_started = false;

        substitutionID = 0;
        power_id = 0;
        revisedTarget = 0;
        reducedOver = 0;
        appliedRainRule = null;

        pre_out = false;
        callBowlerAlert = false;

        // added on 14/05/2020
        p_wicket_no = 0;
        p_sequence_no = 0;
        p_run = 0;
        p_ball = 0;
        p_over = 0;
        p_p1Id = 0;
        p_p1D4SID = 0;   // Added on 13/11/2021 to correct undo
        p_p2Id = 0;
        p_p2D4SID = 0;   // Added on 13/11/2021 to correct undo
        p_disId = 0;
        p_disD4SID = 0;   // Added on 13/11/2021 to correct undo
        p_p1cr = 0;
        p_p1cb = 0;
        p_p2cr = 0;
        p_p2cb = 0;
        p_broken = false;

        power_start_over = 0;
        power_end_over = 0;
        power_sequence = 0;

        sub_team = 0;
        sub_playerout_id = 0;
        sub_playerin_id = 0;    // till here
        wkID = 0;
        wkD4SID = 0;   // Added on 13/11/2021 to correct undo
        wk_position = 0;
        preWkID = 0;
        preWk_position = 0;

        forfeit = false;
        forfeit_team = 0;
        forfeit_innings = 0;
        CONCEDE = false;
        conceded_team = 0;
        forceEndingType = 0;

        lunch_flag = 0;
        tea_flag = 0;
        dinner_flag = 0;

        change_end_flag = false;

        ball_count = 0; // 1 => yes
                        // 0 => no (includes end of over and others)
    }


    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventId) {
        this.eventID = eventId;
    }

    public int getSyncstatus() {
        return syncstatus;
    }

    public void setSyncstatus(int syncstatus) {
        this.syncstatus = syncstatus;
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

    public int getInnings1Runs() {
        return innings1Runs;
    }

    public void setInnings1Runs(int innings1Runs) {
        this.innings1Runs = innings1Runs;
    }

    public int getInnings2Runs() {
        return innings2Runs;
    }

    public void setInnings2Runs(int innings2Runs) {
        this.innings2Runs = innings2Runs;
    }

    public int getInnings3Runs() {
        return innings3Runs;
    }

    public void setInnings3Runs(int innings3Runs) {
        this.innings3Runs = innings3Runs;
    }

    public int getInnings4Runs() {
        return innings4Runs;
    }

    public void setInnings4Runs(int innings4Runs) {
        this.innings4Runs = innings4Runs;
    }

    public int getPreInningsRuns() {
        return preInningsRuns;
    }

    public void setPreInningsRuns(int preInningsRuns) {
        this.preInningsRuns = preInningsRuns;
    }

    public int getBattingTeamNo() {
        return battingTeamNo;
    }

    public void setBattingTeamNo(int battingTeamNo) {
        this.battingTeamNo = battingTeamNo;
    }

    public int getFieldingTeamNo() {
        return fieldingTeamNo;
    }

    public void setFieldingTeamNo(int fieldingTeamNo) {
        this.fieldingTeamNo = fieldingTeamNo;
    }

    public int getRemainingRuns() {
        return remainingRuns;
    }

    public void setRemainingRuns(int remainingRuns) {
        this.remainingRuns = remainingRuns;
    }

    public int getRemainingBalls() {
        return remainingBalls;
    }

    public void setRemainingBalls(int remainingBalls) {
        this.remainingBalls = remainingBalls;
    }

    public float getRemainingOvers() {
        return remainingOvers;
    }

    public void setRemainingOvers(float remainingOvers) {
        this.remainingOvers = remainingOvers;
    }

    public int getLeadingRuns() {
        return leadingRuns;
    }

    public void setLeadingRuns(int leadingRuns) {
        this.leadingRuns = leadingRuns;
    }

    public int getPlayer1ID() {
        return player1ID;
    }

    public void setPlayer1ID(int player1ID) {
        this.player1ID = player1ID;
    }

    public int getPlayer2ID() {
        return player2ID;
    }

    public void setPlayer2ID(int player2ID) {
        this.player2ID = player2ID;
    }

    public int getStrikerID() {
        return strikerID;
    }

    public void setStrikerID(int strikerID) {
        this.strikerID = strikerID;
    }

    public int getNonStrikerID() {
        return nonStrikerID;
    }

    public void setNonStrikerID(int nonStrikerID) {
        this.nonStrikerID = nonStrikerID;
    }

    public int getBowlerID() {
        return bowlerID;
    }

    public void setBowlerID(int bowlerID) {
        this.bowlerID = bowlerID;
    }


    public int getPrevBowlerID() {
        return prevBowlerID;
    }

    public void setPrevBowlerID(int prevBowlerID) {
        this.prevBowlerID = prevBowlerID;
    }

    public int getDismissedPlayerID() {
        return dismissedPlayerID;
    }

    public void setDismissedPlayerID(int dismissedPlayerID) {
        this.dismissedPlayerID = dismissedPlayerID;
    }

    public int getLastPreBowlerID() {
        return lastPreBowlerID;
    }

    public void setLastPreBowlerID(int lastPreBowlerID) {
        this.lastPreBowlerID = lastPreBowlerID;
    }

    public int getNewBowlerID() {
        return newBowlerID;
    }

    public void setNewBowlerID(int newBowlerID) {
        this.newBowlerID = newBowlerID;
    }

    public int getDisNewBatsmanID() {
        return disNewBatsmanID;
    }

    public void setDisNewBatsmanID(int disNewBatsmanID) {
        this.disNewBatsmanID = disNewBatsmanID;
    }

    public String getFielderID() {
        return fielderID;
    }

    public void setFielderID(String fielderID) {
        this.fielderID = fielderID;
    }

//    public int getNextBowlerID() {
//        return nextBowlerID;
//    }

//    public void setNextBowlerID(int nextBowlerID) {
//        this.nextBowlerID = nextBowlerID;
//    }

    public float getOvers() {
        return overs;
    }

    public void setOvers(float overs) {
        this.overs = overs;
    }

    public float getTco() {
        return tco;
    }

    public void setTco(float tco) {
        this.tco = tco;
    }

//    (Player table id) public float getTbo() {
//        return tbo;
//    }

//    public void setTbo(float tbo) {
//        this.tbo = tbo;
//    }

//    public float getTpbo() {
//        return tpbo;
//    }

//    public void setTpbo(float tpbo) {
//        this.tpbo = tpbo;
//    }

    public int getCurrentOverBalls() {
        return currentOverBalls;
    }

    public void setCurrentOverBalls(int currentOverBalls) {
        this.currentOverBalls = currentOverBalls;
    }

    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }

    public int getTemp_balls() {
        return temp_balls;
    }

    public void setTemp_balls(int temp_balls) {
        this.temp_balls = temp_balls;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public int getCurrentRun() {
        return currentRun;
    }

    public void setCurrentRun(int currentRun) {
        this.currentRun = currentRun;
    }

    public String getWagonWheelRegion() {
        return wagonWheelRegion;
    }

    public void setWagonWheelRegion(String wagonWheelRegion) {
        this.wagonWheelRegion = wagonWheelRegion;
    }

    public int getStrokeDirection() {
        return strokeDirection;
    }

    public void setStrokeDirection(int strokeDirection) {
        this.strokeDirection = strokeDirection;
    }

    public int getWicket() {
        return wicket;
    }

    public void setWicket(int wicket) {
        this.wicket = wicket;
    }

    public int getStrikerRuns() {
        return strikerRuns;
    }

    public void setStrikerRuns(int strikerRuns) {
        this.strikerRuns = strikerRuns;
    }

    public int getStrikerBalls() {
        return strikerBalls;
    }

    public void setStrikerBalls(int strikerBalls) {
        this.strikerBalls = strikerBalls;
    }

    public int getStrikerBattingOrder() {
        return strikerBattingOrder;
    }

    public void setStrikerBattingOrder(int strikerBattingOrder) {
        this.strikerBattingOrder = strikerBattingOrder;
    }

    public int getStrikerDots() {
        return strikerDots;
    }

    public void setStrikerDots(int strikerDots) {
        this.strikerDots = strikerDots;
    }

    public int getStrikerF4s() {
        return strikerF4s;
    }

    public void setStrikerF4s(int strikerF4s) {
        this.strikerF4s = strikerF4s;
    }

    public int getStrikerS6s() {
        return strikerS6s;
    }

    public void setStrikerS6s(int strikerS6s) {
        this.strikerS6s = strikerS6s;
    }

    public int getNonStrikerRuns() {
        return nonStrikerRuns;
    }

    public void setNonStrikerRuns(int nonStrikerRuns) {
        this.nonStrikerRuns = nonStrikerRuns;
    }

    public int getNonStrikerBalls() {
        return nonStrikerBalls;
    }

    public void setNonStrikerBalls(int nonStrikerBalls) {
        this.nonStrikerBalls = nonStrikerBalls;
    }

    public int getNonStrikerBattingOrder() {
        return nonStrikerBattingOrder;
    }

    public void setNonStrikerBattingOrder(int nonStrikerBattingOrder) {
        this.nonStrikerBattingOrder = nonStrikerBattingOrder;
    }

    public int getNonStrikerDots() {
        return nonStrikerDots;
    }

    public void setNonStrikerDots(int nonStrikerDots) {
        this.nonStrikerDots = nonStrikerDots;
    }

    public int getNonStrikerF4s() {
        return nonStrikerF4s;
    }

    public void setNonStrikerF4s(int nonStrikerF4s) {
        this.nonStrikerF4s = nonStrikerF4s;
    }

    public int getNonStrikerS6s() {
        return nonStrikerS6s;
    }

    public void setNonStrikerS6s(int nonStrikerS6s) {
        this.nonStrikerS6s = nonStrikerS6s;
    }

    public int getBowlerRuns() {
        return bowlerRuns;
    }

    public void setBowlerRuns(int bowlerRuns) {
        this.bowlerRuns = bowlerRuns;
    }

    public int getBowlerBalls() {
        return bowlerBalls;
    }

    public void setBowlerBalls(int bowlerBalls) {
        this.bowlerBalls = bowlerBalls;
    }

    // Added on 26/07/2021


    public int getBowlerTotalBalls() {
        return bowlerTotalBalls;
    }

    public void setBowlerTotalBalls(int bowlerTotalBalls) {
        this.bowlerTotalBalls = bowlerTotalBalls;
    }

    public int getBowlerOver() {
        return bowlerOver;
    }

    public void setBowlerOver(int bowlerOver) {
        this.bowlerOver = bowlerOver;
    }

    public int getBowlerWicket() {
        return bowlerWicket;
    }

    public void setBowlerWicket(int bowlerWicket) {
        this.bowlerWicket = bowlerWicket;
    }

    public int getBowlerDots() {
        return bowlerDots;
    }

    public void setBowlerDots(int bowlerDots) {
        this.bowlerDots = bowlerDots;
    }

    public int getBowlerMO() {
        return bowlerMO;
    }

    public void setBowlerMO(int bowlerMO) {
        this.bowlerMO = bowlerMO;
    }

    public int getBowlerWides() {
        return bowlerWides;
    }

    public void setBowlerWides(int bowlerWides) {
        this.bowlerWides = bowlerWides;
    }

    public int getBowlerNoball() {
        return bowlerNoball;
    }

    public void setBowlerNoball(int bowlerNoball) {
        this.bowlerNoball = bowlerNoball;
    }

    public int getPreBowlerRuns() {
        return preBowlerRuns;
    }

    public void setPreBowlerRuns(int preBowlerRuns) {
        this.preBowlerRuns = preBowlerRuns;
    }

    public int getPreBowlerBalls() {
        return preBowlerBalls;
    }

    public void setPreBowlerBalls(int preBowlerBalls) {
        this.preBowlerBalls = preBowlerBalls;
    }

    // Added on 26/07/2021


    public int getPreBowlerTotalBalls() {
        return preBowlerTotalBalls;
    }

    public void setPreBowlerTotalBalls(int preBowlerTotalBalls) {
        this.preBowlerTotalBalls = preBowlerTotalBalls;
    }

    public int getPreBowlerOver() {
        return preBowlerOver;
    }

    public void setPreBowlerOver(int preBowlerOver) {
        this.preBowlerOver = preBowlerOver;
    }

    public int getPreBowlerWicket() {
        return preBowlerWicket;
    }

    public void setPreBowlerWicket(int preBowlerWicket) {
        this.preBowlerWicket = preBowlerWicket;
    }

    public int getPreBowlerDots() {
        return preBowlerDots;
    }

    public void setPreBowlerDots(int preBowlerDots) {
        this.preBowlerDots = preBowlerDots;
    }

    public int getPreBowlerMO() {
        return preBowlerMO;
    }

    public void setPreBowlerMO(int preBowlerMO) {
        this.preBowlerMO = preBowlerMO;
    }

    public int getPreBowlerWides() {
        return preBowlerWides;
    }

    public void setPreBowlerWides(int preBowlerWides) {
        this.preBowlerWides = preBowlerWides;
    }

    public int getPreBowlerNoball() {
        return preBowlerNoball;
    }

    public void setPreBowlerNoball(int preBowlerNoball) {
        this.preBowlerNoball = preBowlerNoball;
    }

    public int getExtraRuns() {
        return extraRuns;
    }

    public void setExtraRuns(int extraRuns) {
        this.extraRuns = extraRuns;
    }

    public int getPenaltyRuns() {
        return penaltyRuns;
    }

    public void setPenaltyRuns(int penaltyRuns) {
        this.penaltyRuns = penaltyRuns;
    }

    public int getBallType() {
        return ballType;
    }

    public void setBallType(int ballType) {
        this.ballType = ballType;
    }

    public int getOutType() {
        return outType;
    }

    public void setOutType(int outType) {
        this.outType = outType;
    }

    public int getExtraType() {
        return extraType;
    }

    public void setExtraType(int extraType) {
        this.extraType = extraType;
    }

    public int getPenaltyType() {
        return penaltyType;
    }

    public void setPenaltyType(int penaltyType) {
        this.penaltyType = penaltyType;
    }

    public int getExtraBye() {
        return extraBye;
    }

    public void setExtraBye(int extraBye) {
        this.extraBye = extraBye;
    }

    public int getExtraLb() {
        return extraLb;
    }

    public void setExtraLb(int extraLb) {
        this.extraLb = extraLb;
    }

    public int getExtraWd() {
        return extraWd;
    }

    public void setExtraWd(int extraWd) {
        this.extraWd = extraWd;
    }

    public int getExtraNb() {
        return extraNb;
    }

    public void setExtraNb(int extraNb) {
        this.extraNb = extraNb;
    }

    public int getExtraP() {
        return extraP;
    }

    public void setExtraP(int extraP) {
        this.extraP = extraP;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public int getPenaltyRunTeam() {
        return penaltyRunTeam;
    }

    public void setPenaltyRunTeam(int penaltyRunTeam) {
        this.penaltyRunTeam = penaltyRunTeam;
    }

    public int getPenaltyBallCounted() {
        return penaltyBallCounted;
    }

    public void setPenaltyBallCounted(int penaltyBallCounted) {
        this.penaltyBallCounted = penaltyBallCounted;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public int getMo() {
        return mo;
    }

    public void setMo(int mo) {
        this.mo = mo;
    }

    public boolean isMaidenOver() {
        return maidenOver;
    }

    public void setMaidenOver(boolean maidenOver) {
        this.maidenOver = maidenOver;
    }

    public boolean isPenaltyBallCount() {
        return penaltyBallCount;
    }

    public void setPenaltyBallCount(boolean penaltyBallCount) {
        this.penaltyBallCount = penaltyBallCount;
    }

    public String getCurrentOver() {
        return currentOver;
    }

    public void setCurrentOver(String currentOver) {
        this.currentOver = currentOver;
    }

    public String getUndoDisplay() {
        return undoDisplay;
    }

    public void setUndoDisplay(String undoDisplay) {
        this.undoDisplay = undoDisplay;
    }

    public boolean isSUPER_OVER() {
        return SUPER_OVER;
    }

    public void setSUPER_OVER(boolean SUPER_OVER) {
        this.SUPER_OVER = SUPER_OVER;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getIntervalId() {
        return intervalId;
    }

    public void setIntervalId(int intervalId) {
        this.intervalId = intervalId;
    }

    public String getPostArray() {
        return postArray;
    }

    public void setPostArray(String postArray) {
        this.postArray = postArray;
    }

    public String getUndoArray() {
        return undoArray;
    }

    public void setUndoArray(String undoArray) {
        this.undoArray = undoArray;
    }

    public boolean isEndOfDay() {
        return endOfDay;
    }

    public void setEndOfDay(boolean endOfDay) {
        this.endOfDay = endOfDay;
    }

    public boolean isDeclared() {
        return declared;
    }

    public void setDeclared(boolean declared) {
        this.declared = declared;
    }

    public int getSuper_over_innings1runs() {
        return super_over_innings1runs;
    }

    public void setSuper_over_innings1runs(int super_over_innings1runs) {
        this.super_over_innings1runs = super_over_innings1runs;
    }

    public int getSuper_over_innings2runs() {
        return super_over_innings2runs;
    }

    public void setSuper_over_innings2runs(int super_over_innings2runs) {
        this.super_over_innings2runs = super_over_innings2runs;
    }

    public boolean isFreeHit() {
        return freeHit;
    }

    public void setFreeHit(boolean freeHit) {
        this.freeHit = freeHit;
    }

    public boolean isNewPartnership() {
        return newPartnership;
    }

    public void setNewPartnership(boolean newPartnership) {
        this.newPartnership = newPartnership;
    }

    public boolean isInning_started() {
        return inning_started;
    }

    public void setInning_started(boolean inning_started) {
        this.inning_started = inning_started;
    }

    public int getSubstitutionID() {
        return substitutionID;
    }

    public void setSubstitutionID(int substitutionID) {
        this.substitutionID = substitutionID;
    }

    public int getP_wicket_no() {
        return p_wicket_no;
    }

    public void setP_wicket_no(int p_wicket_no) {
        this.p_wicket_no = p_wicket_no;
    }

    public int getP_sequence_no() {
        return p_sequence_no;
    }

    public void setP_sequence_no(int p_sequence_no) {
        this.p_sequence_no = p_sequence_no;
    }

    public int getP_run() {
        return p_run;
    }

    public void setP_run(int p_run) {
        this.p_run = p_run;
    }

    public int getP_ball() {
        return p_ball;
    }

    public void setP_ball(int p_ball) {
        this.p_ball = p_ball;
    }

    public int getP_over() {
        return p_over;
    }

    public void setP_over(int p_over) {
        this.p_over = p_over;
    }

    public int getP_p1Id() {
        return p_p1Id;
    }

    public void setP_p1Id(int p_p1Id) {
        this.p_p1Id = p_p1Id;
    }

    public int getP_p2Id() {
        return p_p2Id;
    }

    public void setP_p2Id(int p_p2Id) {
        this.p_p2Id = p_p2Id;
    }

    public int getP_disId() {
        return p_disId;
    }

    public void setP_disId(int p_disId) {
        this.p_disId = p_disId;
    }

    public int getP_p1cr() {
        return p_p1cr;
    }

    public void setP_p1cr(int p_p1cr) {
        this.p_p1cr = p_p1cr;
    }

    public int getP_p1cb() {
        return p_p1cb;
    }

    public void setP_p1cb(int p_p1cb) {
        this.p_p1cb = p_p1cb;
    }

    public int getP_p2cr() {
        return p_p2cr;
    }

    public void setP_p2cr(int p_p2cr) {
        this.p_p2cr = p_p2cr;
    }

    public int getP_p2cb() {
        return p_p2cb;
    }

    public void setP_p2cb(int p_p2cb) {
        this.p_p2cb = p_p2cb;
    }

    public boolean isP_broken() {
        return p_broken;
    }

    public void setP_broken(boolean p_broken) {
        this.p_broken = p_broken;
    }

    public int getPower_id() {
        return power_id;
    }

    public void setPower_id(int power_id) {
        this.power_id = power_id;
    }

    public int getRevisedTarget() {
        return revisedTarget;
    }

    public void setRevisedTarget(int revisedTarget) {
        this.revisedTarget = revisedTarget;
    }

    public float getReducedOver() {
        return reducedOver;
    }

    public void setReducedOver(float reducedOver) {
        this.reducedOver = reducedOver;
    }

    public String getAppliedRainRule() {
        return appliedRainRule;
    }

    public void setAppliedRainRule(String appliedRainRule) {
        this.appliedRainRule = appliedRainRule;
    }

    public boolean isPre_out() {
        return pre_out;
    }

    public void setPre_out(boolean pre_out) {
        this.pre_out = pre_out;
    }

    public boolean isCallBowlerAlert() {
        return callBowlerAlert;
    }

    public void setCallBowlerAlert(boolean callBowlerAlert) {
        this.callBowlerAlert = callBowlerAlert;
    }

    public int getPower_start_over() {
        return power_start_over;
    }

    public void setPower_start_over(int power_start_over) {
        this.power_start_over = power_start_over;
    }

    public int getPower_end_over() {
        return power_end_over;
    }

    public void setPower_end_over(int power_end_over) {
        this.power_end_over = power_end_over;
    }

    public int getPower_sequence() {
        return power_sequence;
    }

    public void setPower_sequence(int power_sequence) {
        this.power_sequence = power_sequence;
    }

    public int getSub_team() {
        return sub_team;
    }

    public void setSub_team(int sub_team) {
        this.sub_team = sub_team;
    }

    public int getSub_playerout_id() {
        return sub_playerout_id;
    }

    public void setSub_playerout_id(int sub_playerout_id) {
        this.sub_playerout_id = sub_playerout_id;
    }

    public int getSub_playerin_id() {
        return sub_playerin_id;
    }

    public void setSub_playerin_id(int sub_playerin_id) {
        this.sub_playerin_id = sub_playerin_id;
    }

    public int getWkID() {
        return wkID;
    }

    public void setWkID(int wkID) {
        this.wkID = wkID;
    }

    public int getWk_position() {
        return wk_position;
    }

    public void setWk_position(int wk_position) {
        this.wk_position = wk_position;
    }

    public int getPreWkID() {
        return preWkID;
    }

    public void setPreWkID(int preWkID) {
        this.preWkID = preWkID;
    }

    public int getPreWk_position() {
        return preWk_position;
    }

    public void setPreWk_position(int preWk_position) {
        this.preWk_position = preWk_position;
    }

    public int getForfeit_team() {
        return forfeit_team;
    }

    public void setForfeit_team(int forfeit_team) {
        this.forfeit_team = forfeit_team;
    }

    public int getForfeit_innings() {
        return forfeit_innings;
    }

    public void setForfeit_innings(int forfeit_innings) {
        this.forfeit_innings = forfeit_innings;
    }

    public boolean isForfeit() {
        return forfeit;
    }

    public void setForfeit(boolean forfeit) {
        this.forfeit = forfeit;
    }

    public boolean isCONCEDE() {
        return CONCEDE;
    }

    public void setCONCEDE(boolean CONCEDE) {
        this.CONCEDE = CONCEDE;
    }

    public int getConceded_team() {
        return conceded_team;
    }

    public void setConceded_team(int conceded_team) {
        this.conceded_team = conceded_team;
    }

    public int getForceEndingType() {
        return forceEndingType;
    }

    public void setForceEndingType(int forceEndingType) {
        this.forceEndingType = forceEndingType;
    }

    public int getLunch_flag() {
        return lunch_flag;
    }

    public void setLunch_flag(int lunch_flag) {
        this.lunch_flag = lunch_flag;
    }

    public int getTea_flag() {
        return tea_flag;
    }

    public void setTea_flag(int tea_flag) {
        this.tea_flag = tea_flag;
    }

    public int getDinner_flag() {
        return dinner_flag;
    }

    public void setDinner_flag(int dinner_flag) {
        this.dinner_flag = dinner_flag;
    }

    public boolean isChange_end_flag() {
        return change_end_flag;
    }

    public void setChange_end_flag(boolean change_end_flag) {
        this.change_end_flag = change_end_flag;
    }

    /*public int getDismissedPlayerBattingOrder() {
        return dismissedPlayerBattingOrder;
    }

    public void setDismissedPlayerBattingOrder(int dismissedPlayerBattingOrder) {
        this.dismissedPlayerBattingOrder = dismissedPlayerBattingOrder;
    }*/

    public int getDisNewBatsmanBattingOrder() {
        return disNewBatsmanBattingOrder;
    }

    public void setDisNewBatsmanBattingOrder(int disNewBatsmanBattingOrder) {
        this.disNewBatsmanBattingOrder = disNewBatsmanBattingOrder;
    }

    public int getStrikerOutType() {
        return strikerOutType;
    }

    public void setStrikerOutType(int strikerOutType) {
        this.strikerOutType = strikerOutType;
    }

    public int getNonStrikerOutType() {
        return nonStrikerOutType;
    }

    public void setNonStrikerOutType(int nonStrikerOutType) {
        this.nonStrikerOutType = nonStrikerOutType;
    }

    public String getStrikerNotOutIndicator() {
        return strikerNotOutIndicator;
    }

    public void setStrikerNotOutIndicator(String strikerNotOutIndicator) {
        this.strikerNotOutIndicator = strikerNotOutIndicator;
    }

    public String getNonStrikerNotOutIndicator() {
        return nonStrikerNotOutIndicator;
    }

    public void setNonStrikerNotOutIndicator(String nonStrikerNotOutIndicator) {
        this.nonStrikerNotOutIndicator = nonStrikerNotOutIndicator;
    }

    public int getBall_count() {
        return ball_count;
    }

    public void setBall_count(int ball_count) {
        this.ball_count = ball_count;
    }

    public int getStrikerD4SID() {
        return strikerD4SID;
    }

    public void setStrikerD4SID(int strikerD4SID) {
        this.strikerD4SID = strikerD4SID;
    }

    public int getNonStrikerD4SID() {
        return nonStrikerD4SID;
    }

    public void setNonStrikerD4SID(int nonStrikerD4SID) {
        this.nonStrikerD4SID = nonStrikerD4SID;
    }

    public int getBowlerD4SID() {
        return bowlerD4SID;
    }

    public void setBowlerD4SID(int bowlerD4SID) {
        this.bowlerD4SID = bowlerD4SID;
    }

    public int getPrevBowlerD4SID() {
        return prevBowlerD4SID;
    }

    public void setPrevBowlerD4SID(int prevBowlerD4SID) {
        this.prevBowlerD4SID = prevBowlerD4SID;
    }

    public int getLastPreBowlerD4SID() {
        return lastPreBowlerD4SID;
    }

    public void setLastPreBowlerD4SID(int lastPreBowlerD4SID) {
        this.lastPreBowlerD4SID = lastPreBowlerD4SID;
    }

    public int getNewBowlerD4SID() {
        return newBowlerD4SID;
    }

    public void setNewBowlerD4SID(int newBowlerD4SID) {
        this.newBowlerD4SID = newBowlerD4SID;
    }

    public int getDismissedPlayerD4SID() {
        return dismissedPlayerD4SID;
    }

    public void setDismissedPlayerD4SID(int dismissedPlayerD4SID) {
        this.dismissedPlayerD4SID = dismissedPlayerD4SID;
    }

    public int getDisNewBatsmanD4SID() {
        return disNewBatsmanD4SID;
    }

    public void setDisNewBatsmanD4SID(int disNewBatsmanD4SID) {
        this.disNewBatsmanD4SID = disNewBatsmanD4SID;
    }

    public String getFielderD4SID() {
        return fielderD4SID;
    }

    public void setFielderD4SID(String fielderD4SID) {
        this.fielderD4SID = fielderD4SID;
    }

    public int getP_p1D4SID() {
        return p_p1D4SID;
    }

    public void setP_p1D4SID(int p_p1D4SID) {
        this.p_p1D4SID = p_p1D4SID;
    }

    public int getP_p2D4SID() {
        return p_p2D4SID;
    }

    public void setP_p2D4SID(int p_p2D4SID) {
        this.p_p2D4SID = p_p2D4SID;
    }

    public int getP_disD4SID() {
        return p_disD4SID;
    }

    public void setP_disD4SID(int p_disD4SID) {
        this.p_disD4SID = p_disD4SID;
    }

    public int getWkD4SID() {
        return wkD4SID;
    }

    public void setWkD4SID(int wkD4SID) {
        this.wkD4SID = wkD4SID;
    }

}

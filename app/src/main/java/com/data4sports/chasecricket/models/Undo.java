package com.data4sports.chasecricket.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Undo extends RealmObject {

    @PrimaryKey
    private int undoID;
    private int matchid;
    private String matchID;
    private int innings;
    private int eventID;
    private int balltype;
    private int runs;
    private int extras;
    private int extratype;
    private int outtype;
    private int dismissedbatsmanid;
    private int dismissedbatsmanD4SID;   // Added on 13/11/2021 to correct undo
    private String fielderids;
    private String fielderD4SID;   // Added on 13/11/2021 to correct undo
    private int disnewbatsmanid;
    private int disnewbatsmanD4SID;   // Added on 13/11/2021 to correct undo

    private int bowler_pID;
    private int bowler_D4SID;   // Added on 13/11/2021 to correct undo
    //added on 14/05/2020
    private int bowlerRuns;
    private int bowlerBalls;
    private int bowlerOver;
    private int bowlerWicket;
    private int bowlerDots;
    private int bowlerMO;
    private int bowlerWides;
    private int bowlerNoball;   // till here
    // added on  16/07/2020
    private int bowlerF4s;
    private int bowlerS6s;  // --- till here

    private int preBowler_pID;
    private int preBowler_D4SID;   // Added on 13/11/2021 to correct undo
    // added on 14/05/2020
    private int preBowlerRuns;
    private int preBowlerBalls;
    private int preBowlerOver;
    private int preBowlerWicket;
    private int preBowlerDots;
    private int preBowlerMO;
    private int preBowlerWides;
    private int preBowlerNoball;    // till here
    // added on  16/07/2020
    private int preBowlerF4s;
    private int preBowlerS6s;  // --- till here

    private int striker_pID;
    private int striker_D4SID;   // Added on 13/11/2021 to correct undo
    //added on 14/05/2020
    private int strikerRuns;
    private int strikerBalls;
    private int strikerBattingOrder;
    private int strikerDots;
    private int strikerF4s;
    private int strikerS6s;     // till here
    // added on 11/08/2020
    private int strikerOutType;

    private int nonStriker_pID;
    private int nonStriker_D4SID;   // Added on 13/11/2021 to correct undo
    //added on 14/05/2020
    private int nonStrikerRuns;
    private int nonStrikerBalls;
    private int nonStrikerBattingOrder;
    private int nonStrikerDots;
    private int nonStrikerF4s;
    private int nonStrikerS6s;     // till here
    // added on 11/08/2020
    private int nonStrikerOutType;

    private int totalscore;
    private int totalwicket;

    private float totalovers;

    private int sync;

    private boolean SUPER_OVER;
    private boolean preOut;

    // added on 14/05/2020

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

    private int extraType;
    private int extraRuns;  // innings extra
    private int extraBye;
    private int extraLb;
    private int extraWd;
    private int extraNb;
    private int extraP;     // till here


    // added on 26/06/2020
    private int batting_team;
    private int over;
    private int ball;
//    private int strikerid;
//    private int nonstrikerid;
//    private int bowlerid;





    private boolean freehit;
    private int penaltybool;
    private int penaltytype;

    private int penalty;
    private String commentary;
    private int strokedirection;
    private int sessionId;
    private int intervalId;

    // ======= added on 27/06/2020

    private int overScore;

    // for power play details
    private int power_id;
    private int power_start_over;
    private int power_end_over;
    private int power_sequence;
//    private int power_typee;

    // for concussion-substitution details
    private int sub_team;
    private int sub_playerout_id;
    private int sub_playerin_id;


    // ======== till here
    private int revisedTarget;
    private float reducedOver;
    private String appliedRainRule;

    // Added on 21/04/2021
    private int post;

    private int undo_last_event_id;

    // Added on 28/08/2021 (for undo of balltype 7)
    private int disNewBatsmanBattingOrder;
    private int dismissedPlayerBattingOrder;


    public Undo() {
        init();
    }

    private void init(){
        matchid = 0;
        matchID = "";
        innings = 0;
        eventID = 0;

        bowler_pID = 0;
        bowler_D4SID = 0;   // Added on 13/11/2021 to correct undo
        preBowler_pID = 0;
        preBowler_D4SID = 0;   // Added on 13/11/2021 to correct undo
        striker_pID = 0;
        striker_D4SID = 0;   // Added on 13/11/2021 to correct undo
        nonStriker_pID = 0;
        nonStriker_D4SID = 0;   // Added on 13/11/2021 to correct undo
        totalscore = 0;
        totalwicket = 0;

        totalovers = 0f;

        sync = 0;

        SUPER_OVER = false;
        preOut = false;


        // added on 26/06/2020
        batting_team = 0;
        over = 0;
        ball = 0;
//        strikerid = 0;
//        nonstrikerid = 0;
//        bowlerid = 0;
        balltype = -1;
        dismissedbatsmanid = 0;
        dismissedbatsmanD4SID = 0;   // Added on 13/11/2021 to correct undo
        disnewbatsmanid = 0;
        disnewbatsmanD4SID = 0;   // Added on 13/11/2021 to correct undo
        outtype = -1;
        fielderids = null;
        fielderD4SID = null;   // Added on 13/11/2021 to correct undo
        extratype = -1;
        freehit = false;
        penaltybool = 0;
        penaltytype = 0;
        runs = 0;
        extras = 0;
        penalty = 0;
        commentary = "";
        strokedirection = -1;
        sessionId = 0;
        intervalId = 0;
        overScore = 0;

        // added on 27/06/2020
        power_id = 0;
        power_start_over = 0;
        power_end_over = 0;
        power_sequence = 0;

        sub_team = 0;
        sub_playerout_id = 0;
        sub_playerin_id = 0;

        revisedTarget = 0;
        reducedOver = 0;
        appliedRainRule = null;     // till here

        post = 0;
        undo_last_event_id = 0;

        dismissedPlayerBattingOrder = 0;
        disNewBatsmanBattingOrder = 0;

        // Added on 13/11/2021
        p_wicket_no = 0;
        p_sequence_no = 0;
        p_run = 0;
        p_ball = 0;
        p_over = 0;
        p_p1Id = 0;
        p_p1D4SID = 0;   //  to correct undo
        p_p2Id = 0;
        p_p2D4SID = 0;   // to correct undo
        p_disId = 0;
        p_disD4SID = 0;   // to correct undo
        p_p1cr = 0;
        p_p1cb = 0;
        p_p2cr = 0;
        p_p2cb = 0;
        p_broken = false;
    }


    public int getUndoID() {
        return undoID;
    }

    public void setUndoID(int undoID) {
        this.undoID = undoID;
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

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getSync() {
        return sync;
    }

    public int getBowler_pID() {
        return bowler_pID;
    }

    public void setBowler_pID(int bowler_pID) {
        this.bowler_pID = bowler_pID;
    }

    public int getPreBowler_pID() {
        return preBowler_pID;
    }

    public void setPreBowler_pID(int preBowler_pID) {
        this.preBowler_pID = preBowler_pID;
    }

    public int getStriker_pID() {
        return striker_pID;
    }

    public void setStriker_pID(int striker_pID) {
        this.striker_pID = striker_pID;
    }

    public int getNonStriker_pID() {
        return nonStriker_pID;
    }

    public void setNonStriker_pID(int nonStriker_pID) {
        this.nonStriker_pID = nonStriker_pID;
    }

    public int getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(int totalscore) {
        this.totalscore = totalscore;
    }

    public int getTotalwicket() {
        return totalwicket;
    }

    public void setTotalwicket(int totalwicket) {
        this.totalwicket = totalwicket;
    }

    public float getTotalovers() {
        return totalovers;
    }

    public void setTotalovers(float totalovers) {
        this.totalovers = totalovers;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public boolean isSUPER_OVER() {
        return SUPER_OVER;
    }

    public void setSUPER_OVER(boolean SUPER_OVER) {
        this.SUPER_OVER = SUPER_OVER;
    }

    public boolean isPreOut() {
        return preOut;
    }

    public void setPreOut(boolean preOut) {
        this.preOut = preOut;
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

    public int getBowlerF4s() {
        return bowlerF4s;
    }

    public void setBowlerF4s(int bowlerF4s) {
        this.bowlerF4s = bowlerF4s;
    }

    public int getBowlerS6s() {
        return bowlerS6s;
    }

    public void setBowlerS6s(int bowlerS6s) {
        this.bowlerS6s = bowlerS6s;
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

    public int getPreBowlerF4s() {
        return preBowlerF4s;
    }

    public void setPreBowlerF4s(int preBowlerF4s) {
        this.preBowlerF4s = preBowlerF4s;
    }

    public int getPreBowlerS6s() {
        return preBowlerS6s;
    }

    public void setPreBowlerS6s(int preBowlerS6s) {
        this.preBowlerS6s = preBowlerS6s;
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

    public int getStrikerOutType() {
        return strikerOutType;
    }

    public void setStrikerOutType(int strikerOutType) {
        this.strikerOutType = strikerOutType;
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

    public int getNonStrikerOutType() {
        return nonStrikerOutType;
    }

    public void setNonStrikerOutType(int nonStrikerOutType) {
        this.nonStrikerOutType = nonStrikerOutType;
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

    public int getExtraType() {
        return extraType;
    }

    public void setExtraType(int extraType) {
        this.extraType = extraType;
    }

    public int getExtraRuns() {
        return extraRuns;
    }

    public void setExtraRuns(int extraRuns) {
        this.extraRuns = extraRuns;
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


    public int getBatting_team() {
        return batting_team;
    }

    public void setBatting_team(int batting_team) {
        this.batting_team = batting_team;
    }

    public int getOver() {
        return over;
    }

    public void setOver(int over) {
        this.over = over;
    }

    public int getBall() {
        return ball;
    }

    public void setBall(int ball) {
        this.ball = ball;
    }

    /*public int getStrikerid() {
        return strikerid;
    }

    public void setStrikerid(int strikerid) {
        this.strikerid = strikerid;
    }

    public int getNonstrikerid() {
        return nonstrikerid;
    }

    public void setNonstrikerid(int nonstrikerid) {
        this.nonstrikerid = nonstrikerid;
    }

    public int getBowlerid() {
        return bowlerid;
    }

    public void setBowlerid(int bowlerid) {
        this.bowlerid = bowlerid;
    }*/

    public int getBalltype() {
        return balltype;
    }

    public void setBalltype(int balltype) {
        this.balltype = balltype;
    }

    public int getDismissedbatsmanid() {
        return dismissedbatsmanid;
    }

    public void setDismissedbatsmanid(int dismissedbatsmanid) {
        this.dismissedbatsmanid = dismissedbatsmanid;
    }

    public int getDisnewbatsmanid() {
        return disnewbatsmanid;
    }

    public void setDisnewbatsmanid(int disnewbatsmanid) {
        this.disnewbatsmanid = disnewbatsmanid;
    }

    public int getOuttype() {
        return outtype;
    }

    public void setOuttype(int outtype) {
        this.outtype = outtype;
    }

    public String getFielderids() {
        return fielderids;
    }

    public void setFielderids(String fielderids) {
        this.fielderids = fielderids;
    }

    public int getExtratype() {
        return extratype;
    }

    public void setExtratype(int extratype) {
        this.extratype = extratype;
    }

    public boolean isFreehit() {
        return freehit;
    }

    public void setFreehit(boolean freehit) {
        this.freehit = freehit;
    }

    public int getPenaltybool() {
        return penaltybool;
    }

    public void setPenaltybool(int penaltybool) {
        this.penaltybool = penaltybool;
    }

    public int getPenaltytype() {
        return penaltytype;
    }

    public void setPenaltytype(int penaltytype) {
        this.penaltytype = penaltytype;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getExtras() {
        return extras;
    }

    public void setExtras(int extras) {
        this.extras = extras;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public int getStrokedirection() {
        return strokedirection;
    }

    public void setStrokedirection(int strokedirection) {
        this.strokedirection = strokedirection;
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

    public int getOverScore() {
        return overScore;
    }

    public void setOverScore(int overScore) {
        this.overScore = overScore;
    }

    public int getPower_id() {
        return power_id;
    }

    public void setPower_id(int power_id) {
        this.power_id = power_id;
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

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getUndo_last_event_id() {
        return undo_last_event_id;
    }

    public void setUndo_last_event_id(int undo_last_event_id) {
        this.undo_last_event_id = undo_last_event_id;
    }

    public int getDisNewBatsmanBattingOrder() {
        return disNewBatsmanBattingOrder;
    }

    public void setDisNewBatsmanBattingOrder(int disNewBatsmanBattingOrder) {
        this.disNewBatsmanBattingOrder = disNewBatsmanBattingOrder;
    }

    public int getDismissedPlayerBattingOrder() {
        return dismissedPlayerBattingOrder;
    }

    public void setDismissedPlayerBattingOrder(int dismissedPlayerBattingOrder) {
        this.dismissedPlayerBattingOrder = dismissedPlayerBattingOrder;
    }

    public int getDismissedbatsmanD4SID() {
        return dismissedbatsmanD4SID;
    }

    public void setDismissedbatsmanD4SID(int dismissedbatsmanD4SID) {
        this.dismissedbatsmanD4SID = dismissedbatsmanD4SID;
    }

    public String getFielderD4SID() {
        return fielderD4SID;
    }

    public void setFielderD4SID(String fielderD4SID) {
        this.fielderD4SID = fielderD4SID;
    }

    public int getDisnewbatsmanD4SID() {
        return disnewbatsmanD4SID;
    }

    public void setDisnewbatsmanD4SID(int disnewbatsmanD4SID) {
        this.disnewbatsmanD4SID = disnewbatsmanD4SID;
    }

    public int getBowler_D4SID() {
        return bowler_D4SID;
    }

    public void setBowler_D4SID(int bowler_D4SID) {
        this.bowler_D4SID = bowler_D4SID;
    }

    public int getPreBowler_D4SID() {
        return preBowler_D4SID;
    }

    public void setPreBowler_D4SID(int preBowler_D4SID) {
        this.preBowler_D4SID = preBowler_D4SID;
    }

    public int getStriker_D4SID() {
        return striker_D4SID;
    }

    public void setStriker_D4SID(int striker_D4SID) {
        this.striker_D4SID = striker_D4SID;
    }

    public int getNonStriker_D4SID() {
        return nonStriker_D4SID;
    }

    public void setNonStriker_D4SID(int nonStriker_D4SID) {
        this.nonStriker_D4SID = nonStriker_D4SID;
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
}

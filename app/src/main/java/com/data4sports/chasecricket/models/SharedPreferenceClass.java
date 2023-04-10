package com.data4sports.chasecricket.models;

import android.util.Log;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SharedPreferenceClass extends RealmObject {


    @PrimaryKey
    private long id;

    private int match_id;
    private String match_ID;
    private String teamA;
    private String teamB;
    private String spInnings;
    private String battingTeam;
    private String fieldingTeam;

    private int battingTeamNo;
    private int fieldingTeamNo;
    private String player1;
    private String player2;
    private int player1ID;
    private int player2ID;
    private int totalInnings;
    private int player_count;
    private Boolean HUNDRED;
    private String striker;
    private String nonStriker;
    private int strID;
    private int nstrID;
    private int ps1ID;
    private int ps2ID;
    private String bowler;
    private int bowlerID;
    private Boolean just_started;
    private String status;
    private String matchtype;
    private int ballsPerOver;
    private int currentInnings;
    private int noballRun;
    private int wideRun;
    private int penaltyRun;
    private String wicketkeeper;
    private int wk_id;
    private int keeper_position;
    private int battingOrder;
    private Boolean RUN_ONCE;
    private Boolean initialize;
    private Boolean score;
    private Boolean inningsNotStarted;
    private Boolean SET_OVER;
    private int eventID;
    private Boolean firstbowler;
    private Boolean first_batsman;
    private Boolean interval;
    private Boolean session;
    private Boolean new_innings;
    private Boolean concussion;
    private Boolean batting;
    private Boolean fielding;
    private int sout_id;
    private int sin_id;
    private Boolean new_bowler;
    private Boolean substitution;
    private int subID;
    private int ballType;
    private int pp_id;
    private Boolean power;
    private Boolean efo;
    private String wheelRegion;
    private int stroke_direction;
    private Boolean wheel;
    private Boolean limited_over;
    private int playerA;
    private int playerB;
    private Boolean followon;
    private Boolean SUPER_OVER;


    private int sessionID;
    private String innings;
    private Float totalOver;
    private int innings1Runs;
    private int innings2Runs;
    private int innings3Runs;
    private int innings4Runs;
    private int preInningsRuns;
    private int so_inninngs1Runs;
    private int so_inninngs2Runs;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public String getMatch_ID() {
        return match_ID;
    }

    public void setMatch_ID(String match_ID) {
        this.match_ID = match_ID;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public String getSpInnings() {
        return spInnings;
    }

    public void setSpInnings(String spInnings) {
        this.spInnings = spInnings;
    }

    public String getBattingTeam() {
        return battingTeam;
    }

    public void setBattingTeam(String battingTeam) {
        this.battingTeam = battingTeam;
    }

    public String getFieldingTeam() {
        return fieldingTeam;
    }

    public void setFieldingTeam(String fieldingTeam) {
        this.fieldingTeam = fieldingTeam;
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

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
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

    public int getTotalInnings() {
        return totalInnings;
    }

    public void setTotalInnings(int totalInnings) {
        this.totalInnings = totalInnings;
    }

    public int getPlayer_count() {
        return player_count;
    }

    public void setPlayer_count(int player_count) {
        this.player_count = player_count;
    }

    public Boolean getHUNDRED() {
        return HUNDRED;
    }

    public void setHUNDRED(Boolean HUNDRED) {
        this.HUNDRED = HUNDRED;
    }

    public String getStriker() {
        return striker;
    }

    public void setStriker(String striker) {
        this.striker = striker;
    }

    public String getNonStriker() {
        return nonStriker;
    }

    public void setNonStriker(String nonStriker) {
        this.nonStriker = nonStriker;
    }

    public int getStrID() {
        return strID;
    }

    public void setStrID(int strID) {
        this.strID = strID;
    }

    public int getNstrID() {
        return nstrID;
    }

    public void setNstrID(int nstrID) {
        this.nstrID = nstrID;
    }

    public int getPs1ID() {
        return ps1ID;
    }

    public void setPs1ID(int ps1ID) {
        this.ps1ID = ps1ID;
    }

    public int getPs2ID() {
        return ps2ID;
    }

    public void setPs2ID(int ps2ID) {
        this.ps2ID = ps2ID;
    }

    public String getBowler() {
        return bowler;
    }

    public void setBowler(String bowler) {
        this.bowler = bowler;
    }

    public int getBowlerID() {
        return bowlerID;
    }

    public void setBowlerID(int bowlerID) {
        this.bowlerID = bowlerID;
    }

    public Boolean getJust_started() {
        return just_started;
    }

    public void setJust_started(Boolean just_started) {
        this.just_started = just_started;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMatchtype() {
        return matchtype;
    }

    public void setMatchtype(String matchtype) {
        this.matchtype = matchtype;
    }

    public int getBallsPerOver() {
        return ballsPerOver;
    }

    public void setBallsPerOver(int ballsPerOver) {
        this.ballsPerOver = ballsPerOver;
    }

    public int getCurrentInnings() {
        return currentInnings;
    }

    public void setCurrentInnings(int currentInnings) {
        this.currentInnings = currentInnings;
    }

    public int getNoballRun() {
        return noballRun;
    }

    public void setNoballRun(int noballRun) {
        this.noballRun = noballRun;
    }

    public int getWideRun() {
        return wideRun;
    }

    public void setWideRun(int wideRun) {
        this.wideRun = wideRun;
    }

    public int getPenaltyRun() {
        return penaltyRun;
    }

    public void setPenaltyRun(int penaltyRun) {
        this.penaltyRun = penaltyRun;
    }

    public String getWicketkeeper() {
        return wicketkeeper;
    }

    public void setWicketkeeper(String wicketkeeper) {
        this.wicketkeeper = wicketkeeper;
    }

    public int getWk_id() {
        return wk_id;
    }

    public void setWk_id(int wk_id) {
        this.wk_id = wk_id;
    }

    public int getKeeper_position() {
        return keeper_position;
    }

    public void setKeeper_position(int keeper_position) {
        this.keeper_position = keeper_position;
    }

    public int getBattingOrder() {
        return battingOrder;
    }

    public void setBattingOrder(int battingOrder) {
        this.battingOrder = battingOrder;
    }

    public Boolean getRUN_ONCE() {
        return RUN_ONCE;
    }

    public void setRUN_ONCE(Boolean RUN_ONCE) {
        this.RUN_ONCE = RUN_ONCE;
    }

    public Boolean getInitialize() {
        return initialize;
    }

    public void setInitialize(Boolean initialize) {
        this.initialize = initialize;
    }

    public Boolean getScore() {
        return score;
    }

    public void setScore(Boolean score) {
        this.score = score;
    }

    public Boolean getInningsNotStarted() {
        return inningsNotStarted;
    }

    public void setInningsNotStarted(Boolean inningsNotStarted) {
        this.inningsNotStarted = inningsNotStarted;
    }

    public Boolean getSET_OVER() {
        return SET_OVER;
    }

    public void setSET_OVER(Boolean SET_OVER) {
        this.SET_OVER = SET_OVER;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public Boolean getFirstbowler() {
        return firstbowler;
    }

    public void setFirstbowler(Boolean firstbowler) {
        this.firstbowler = firstbowler;
    }

    public Boolean getFirst_batsman() {
        return first_batsman;
    }

    public void setFirst_batsman(Boolean first_batsman) {
        this.first_batsman = first_batsman;
    }

    public Boolean getInterval() {
        return interval;
    }

    public void setInterval(Boolean interval) {
        this.interval = interval;
    }

    public Boolean getSession() {
        return session;
    }

    public void setSession(Boolean session) {
        this.session = session;
    }

    public Boolean getNew_innings() {
        return new_innings;
    }

    public void setNew_innings(Boolean new_innings) {
        this.new_innings = new_innings;
    }

    public Boolean getConcussion() {
        return concussion;
    }

    public void setConcussion(Boolean concussion) {
        this.concussion = concussion;
    }

    public Boolean getBatting() {
        return batting;
    }

    public void setBatting(Boolean batting) {
        this.batting = batting;
    }

    public Boolean getFielding() {
        return fielding;
    }

    public void setFielding(Boolean fielding) {
        this.fielding = fielding;
    }

    public int getSout_id() {
        return sout_id;
    }

    public void setSout_id(int sout_id) {
        this.sout_id = sout_id;
    }

    public int getSin_id() {
        return sin_id;
    }

    public void setSin_id(int sin_id) {
        this.sin_id = sin_id;
    }

    public Boolean getNew_bowler() {
        return new_bowler;
    }

    public void setNew_bowler(Boolean new_bowler) {
        this.new_bowler = new_bowler;
    }

    public Boolean getSubstitution() {
        return substitution;
    }

    public void setSubstitution(Boolean substitution) {
        this.substitution = substitution;
    }

    public int getSubID() {
        return subID;
    }

    public void setSubID(int subID) {
        this.subID = subID;
    }

    public int getBallType() {
        return ballType;
    }

    public void setBallType(int ballType) {
        this.ballType = ballType;
    }

    public int getPp_id() {
        return pp_id;
    }

    public void setPp_id(int pp_id) {
        this.pp_id = pp_id;
    }

    public Boolean getPower() {
        return power;
    }

    public void setPower(Boolean power) {
        this.power = power;
    }

    public Boolean getEfo() {
        return efo;
    }

    public void setEfo(Boolean efo) {
        this.efo = efo;
    }

    public String getWheelRegion() {
        return wheelRegion;
    }

    public void setWheelRegion(String wheelRegion) {
        this.wheelRegion = wheelRegion;
    }

    public int getStroke_direction() {
        return stroke_direction;
    }

    public void setStroke_direction(int stroke_direction) {
        this.stroke_direction = stroke_direction;
    }

    public Boolean getWheel() {
        return wheel;
    }

    public void setWheel(Boolean wheel) {
        this.wheel = wheel;
    }

    public Boolean getLimited_over() {
        return limited_over;
    }

    public void setLimited_over(Boolean limited_over) {
        this.limited_over = limited_over;
    }

    public int getPlayerA() {
        return playerA;
    }

    public void setPlayerA(int playerA) {
        this.playerA = playerA;
    }

    public int getPlayerB() {
        return playerB;
    }

    public void setPlayerB(int playerB) {
        this.playerB = playerB;
    }

    public Boolean getFollowon() {
        return followon;
    }

    public void setFollowon(Boolean followon) {
        this.followon = followon;
    }

    public Boolean getSUPER_OVER() {
        return SUPER_OVER;
    }

    public void setSUPER_OVER(Boolean SUPER_OVER) {
        this.SUPER_OVER = SUPER_OVER;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public String getInnings() {
        return innings;
    }

    public void setInnings(String innings) {
        this.innings = innings;
    }

    public Float getTotalOver() {
        return totalOver;
    }

    public void setTotalOver(Float totalOver) {
        this.totalOver = totalOver;
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

    public int getSo_inninngs1Runs() {
        return so_inninngs1Runs;
    }

    public void setSo_inninngs1Runs(int so_inninngs1Runs) {
        this.so_inninngs1Runs = so_inninngs1Runs;
    }

    public int getSo_inninngs2Runs() {
        return so_inninngs2Runs;
    }

    public void setSo_inninngs2Runs(int so_inninngs2Runs) {
        this.so_inninngs2Runs = so_inninngs2Runs;
    }

}



package com.data4sports.chasecricket.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Updated on 23/07/2021
 */

public class Match extends RealmObject {

    @PrimaryKey
    private int matchid;

    private String matchID;
    private int d4s_matchid;
    private int d4s_userid;

    // synced with server
    private int matchSync;
    private int playerSync;
    private int tossSync;
    private int openerSync;

    private int userId;
    private int matchStatus;
    private String teamA;
    private int teamAId;
    private String teamB;
    private int teamBId;
    private String venue;
    private int venueId;
    private String end1;
    private String end2;
    private String phase;
    private String event;
    private int eventId;
    private String matchType;
    private String innings;
    private int totalInnings;
    private int /*player,*/ playerA, playerB/*, subst, substA, substB*/;
    private float over;
    private int balls, actual_over; // balls => balls per over
    private int widerun, noballrun, penaltyrun;
    private String date;

    // Added on 03/03/2021
    private boolean limited_overs;

    //a dded on 19/10/2020
    private boolean noToss;
    private boolean unknownToss;
    private boolean tossMade; // === till here
    private String toss_winner, decision;

    private boolean SUPER_OVER;
    private String match_note;
    private boolean post_matchnote;

    // for setting current match status
    private String status;
    private int statusId;

    // last paused activity
    private boolean CreateMatch;
    private boolean AddPlayers;
    private boolean AddPlayersA;
    private boolean AddPlayersB;
    private boolean Captain;
    private boolean Toss;
    private boolean Openers;
    private boolean Scoring;
    private boolean ScoreCard;
    private boolean PulledMatch;
    private boolean AddSquad;
    private boolean SelectAXI;
    private boolean SelectBXI;
    private boolean ConfirmAXI;
    private boolean ConfirmBXI;

    private boolean justStarted;
    private boolean wagonwheel;
    private boolean followon;

    private int innings1Runs;
    private int innings2Runs;
    private int innings3Runs;
    private int innings4Runs;
    private int revisedTarget;

    private int playingInnings;
    private boolean endOfInnings;
    private boolean reducedOver;
    private boolean reducedRuns;
    private String appliedRainRule;

    // for match result
    private String type;
    private int margin;  // run or wicket
    private int result_sync;        // Added on 08/10/2021 to sync result editing
    private int editResult;     // Added on 08/10/2021 to identify whether the result is edited or not
                                // 0 => not edited
                                // 1 => edited
    private String result;
//    private String matchResult;
    private int winner_team;
    private int win_by_run;
    private int win_by_wicket;

    // for man of the match
    private int mom_team;
    private int mom_pid;
    private int mom_d4sID;
    private int mom_sync;
    private int editMOM;            // Added on 08/10/2021 to identify whether the man of the match is edited or not
                                    // 0 => not edited
                                    // 1 => edited
    private String man_of_the_match;

    private int super_over_innings1runs;
    private int super_over_innings2runs;

    private boolean post;
    private boolean player_post;

    // added on 14/09/2020
    private int teamA_sync;
    private int teamB_sync;

    // added on 22/09/2020
    private int matchNote_sync;

    // added on 23/09/2020
    private boolean pulled;  // for resisting the editing of (both) team names
    private boolean pulled_squad;  // for resisting the editing of player name (but can allow to add new payers)
    private boolean pulled_squadA;  // to know if squad A is available; true = if squad available
    private boolean pulled_squadB;  // to know if squad B is available; true = if squad available

    // Added on 02/03/2021
    private int teamA_forfeit;      // to know whether team A has any chance left to forfeit
    private int teamB_forfeit;      // to know whether team B has any chance left to forfeit
    private int teamA_forfeit_innings;      // innings which forfeit by teamA
    private int teamB_forfeit_innings;      // innings which forfeit by teamB
    private boolean CONCEDE;                // whether the match is conceded or not
    private int conceded_team;              // name of the team which declare concede
    private int conceded_innings;           // innings in which concede declared

    // Added on 28/04/2021
    private int lunch_flag;
    private int tea_flag;
    private int dinner_flag;

    // Added on 08/07/2021
    private int max_opb;    // maximum overs per bowler

    // Added on 23/07/2021
    private boolean hundred;    // hundred = 1 if it is a hundred ball match
    private int max_bpb;        // maximum balls per bowler


    // Added on 02/08/2021
    // to know the synced pages
    private int addPOfficials;
    private int addPlayers;
    private int addToss;
    private int addEvents;
    private int UpdateTeamNames;

    private int abandoned_match_flag;   // 0 (normal)
                                        // 1 (match abandoned before start)

    private int abandoned_after_toss_match_flag;    // 0 (normal)
                                                    // 1 (match abandoned before start)

    public Match() {
        init();
    }

    public void init() {

//        matchid = 0;finish();
        matchID = "";
        d4s_matchid = 0;
        d4s_userid = 0;
        matchSync = 0;
        playerSync = 0;
        tossSync = 0;
        openerSync = 0;

        userId = 0;
        matchStatus = 0;

        teamA = null;
        teamAId = 0;
        teamB = null;
        teamBId = 0;
        venue = null;
        venueId = 0;
        end1 = null;
        end2 = null;
        phase = null;
        event = null;
        eventId = 0;
        matchType = null;
        innings = null;
        totalInnings = 0;
//        player = 0;
        playerA = 0;
        playerB = 0;
//        subst = 0;
//        substA = 0;
//        substB = 0;
        over = 0;
        balls = 0;
        actual_over = 0;
        widerun = 1;
        noballrun = 1;
        penaltyrun = 5;
        date = null;
//        umpire1 = null;
//        umpire2 = null;
//        umpire3 = null;
//        umpire4 = null;
//        scorer = null;
//        matchReferee = null;
        noToss = false;
        unknownToss = false;
        toss_winner = null;
        decision = null;
        match_note = null;
        post_matchnote = false;

        type = null;
        result_sync = 0;
        margin = 0;
        result = null;
//        matchResult = null;

        mom_team = 0;
        mom_pid = 0;
        mom_d4sID = 0;
        mom_sync = 0;
        man_of_the_match = null;

        SUPER_OVER = false;
        status = null;
        statusId = 0;

        CreateMatch = false;
        AddPlayers = false;
        AddPlayersA = false;
        AddPlayersB = false;
        Captain = false;
        Toss = false;
        Openers = false;
        Scoring = false;
        ScoreCard = false;
        PulledMatch = false;
        AddSquad = false;
        SelectAXI = false;
        SelectBXI = false;
        ConfirmAXI = false;
        ConfirmBXI = false;

        justStarted = false;
        wagonwheel = false;
        followon = false;

        innings1Runs = 0;
        innings2Runs = 0;
        innings3Runs = 0;
        innings4Runs = 0;

        revisedTarget = 0;

        playingInnings = 0;
        endOfInnings = false;

        winner_team = 0;
        win_by_run = 0;
        win_by_wicket = 0;

        super_over_innings1runs = 0;
        super_over_innings2runs = 0;

        post = false;
        player_post = false;
        reducedOver = false;
        reducedRuns = false;
        appliedRainRule = null;

        teamA_sync = 0;
        teamB_sync = 0;

        matchNote_sync = 0;

        pulled = false;
        pulled_squad = false;
        pulled_squadA = false;
        pulled_squadB = false;

        teamA_forfeit = 0;
        teamB_forfeit = 0;
        teamA_forfeit_innings = 0;
        teamB_forfeit_innings = 0;
        CONCEDE = false;
        conceded_team = 0;
        conceded_innings = 0;
        limited_overs = false;

        lunch_flag = 0;
        tea_flag = 0;
        dinner_flag = 0;

        max_opb = 0;

        hundred = false;
        max_bpb = 0;

        addPOfficials = 0;
        addPlayers = 0;
        addToss = 0;
        addEvents = 0;
        UpdateTeamNames = 0;

        abandoned_match_flag = 0;
        abandoned_after_toss_match_flag = 0;

        editResult = 0;
        editResult = 0;
    }


    public int getMatchid() {
        return matchid;
    }

    public void setMatchid(int matchid) {
        this.matchid = matchid;
    }

    public int getD4s_matchid() {
        return d4s_matchid;
    }

    public void setD4s_matchid(int d4s_matchid) {
        this.d4s_matchid = d4s_matchid;
    }

    public int getD4s_userid() {
        return d4s_userid;
    }

    public void setD4s_userid(int d4s_userid) {
        this.d4s_userid = d4s_userid;
    }

    public int getMatchSync() {
        return matchSync;
    }

    public void setMatchSync(int matchSync) {
        this.matchSync = matchSync;
    }

    public int getPlayerSync() {
        return playerSync;
    }

    public void setPlayerSync(int playerSync) {
        this.playerSync = playerSync;
    }

    public int getTossSync() {
        return tossSync;
    }

    public void setTossSync(int tossSync) {
        this.tossSync = tossSync;
    }

    public int getOpenerSync() {
        return openerSync;
    }

    public void setOpenerSync(int openerSync) {
        this.openerSync = openerSync;
    }

    public String getMatchID(){
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        this.matchStatus = matchStatus;
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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getEnd1() {
        return end1;
    }

    public void setEnd1(String end1) {
        this.end1 = end1;
    }

    public String getEnd2() {
        return end2;
    }

    public void setEnd2(String end2) {
        this.end2 = end2;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getInnings() {
        return innings;
    }

    public void setInnings(String innings) {
        this.innings = innings;
    }

    public int getTotalInnings() {
        return totalInnings;
    }

    public void setTotalInnings(int totalInnings) {
        this.totalInnings = totalInnings;
    }

   /* public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getSubst() {
        return subst;
    }

    public void setSubst(int subst) {
        this.subst = subst;
    }*/

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

    /*public int getSubstA() {
        return substA;
    }

    public void setSubstA(int substA) {
        this.substA = substA;
    }

    public int getSubstB() {
        return substB;
    }

    public void setSubstB(int substB) {
        this.substB = substB;
    }*/

    public float getOver() {
        return over;
    }

    public void setOver(float over) {
        this.over = over;
    }

    public int getActual_over() {
        return actual_over;
    }

    public void setActual_over(int actual_over) {
        this.actual_over = actual_over;
    }

    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }

    public int getWiderun() {
        return widerun;
    }

    public void setWiderun(int widerun) {
        this.widerun = widerun;
    }

    public int getNoballrun() {
        return noballrun;
    }

    public void setNoballrun(int noballrun) {
        this.noballrun = noballrun;
    }

    public int getPenaltyrun() {
        return penaltyrun;
    }

    public void setPenaltyrun(int penaltyrun) {
        this.penaltyrun = penaltyrun;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isNoToss() {
        return noToss;
    }

    public void setNoToss(boolean noToss) {
        this.noToss = noToss;
    }

    public boolean isUnknownToss() {
        return unknownToss;
    }

    public void setUnknownToss(boolean unknownToss) {
        this.unknownToss = unknownToss;
    }

    public boolean isTossMade() {
        return tossMade;
    }

    public void setTossMade(boolean tossMade) {
        this.tossMade = tossMade;
    }

    public String getToss_winner() {
        return toss_winner;
    }

    public void setToss_winner(String toss_winner) {
        this.toss_winner = toss_winner;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getMatch_note() {
        return match_note;
    }

    public void setMatch_note(String match_note) {
        this.match_note = match_note;
    }

    public boolean isPost_matchnote() {
        return post_matchnote;
    }

    public void setPost_matchnote(boolean post_matchnote) {
        this.post_matchnote = post_matchnote;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getType() {
        return type;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public String getResult() {
        return result;
    }

    public int getMom_team() {
        return mom_team;
    }
//
//    public String getMatchResult() {
//        return matchResult;
//    }
//
//    public void setMatchResult(String matchResult) {
//        this.matchResult = matchResult;
//    }

    public void setMom_team(int mom_team) {
        this.mom_team = mom_team;
    }

    public int getMom_pid() {
        return mom_pid;
    }

    public void setMom_pid(int mom_pid) {
        this.mom_pid = mom_pid;
    }

    public int getMom_d4sID() {
        return mom_d4sID;
    }

    public void setMom_d4sID(int mom_d4sID) {
        this.mom_d4sID = mom_d4sID;
    }

    public int getMom_sync() {
        return mom_sync;
    }

    public void setMom_sync(int mom_sync) {
        this.mom_sync = mom_sync;
    }

    public String getMan_of_the_match() {
        return man_of_the_match;
    }

    public void setMan_of_the_match(String man_of_the_match) {
        this.man_of_the_match = man_of_the_match;
    }

    public boolean isSUPER_OVER() {
        return SUPER_OVER;
    }

    public void setSUPER_OVER(boolean SUPER_OVER) {
        this.SUPER_OVER = SUPER_OVER;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public boolean isCreateMatch() {
        return CreateMatch;
    }

    public void setCreateMatch(boolean createMatch) {
        CreateMatch = createMatch;
    }

    public boolean isAddPlayers() {
        return AddPlayers;
    }

    public void setAddPlayers(boolean addPlayers) {
        AddPlayers = addPlayers;
    }

    public boolean isAddPlayersA() {
        return AddPlayersA;
    }

    public void setAddPlayersA(boolean addPlayersA) {
        AddPlayersA = addPlayersA;
    }

    public boolean isAddPlayersB() {
        return AddPlayersB;
    }

    public void setAddPlayersB(boolean addPlayersB) {
        AddPlayersB = addPlayersB;
    }

    public boolean isCaptain() {
        return Captain;
    }

    public void setCaptain(boolean captain) {
        Captain = captain;
    }

    public boolean isToss() {
        return Toss;
    }

    public void setToss(boolean toss) {
        Toss = toss;
    }

    public boolean isOpeners() {
        return Openers;
    }

    public void setOpeners(boolean openers) {
        Openers = openers;
    }

    public boolean isScoring() {
        return Scoring;
    }

    public void setScoring(boolean scoring) {
        Scoring = scoring;
    }

    public boolean isScoreCard() {
        return ScoreCard;
    }

    public void setScoreCard(boolean scoreCard) {
        ScoreCard = scoreCard;
    }

    public boolean isPulledMatch() {
        return PulledMatch;
    }

    public void setPulledMatch(boolean pulledMatch) {
        PulledMatch = pulledMatch;
    }

    public boolean isAddSquad() {
        return AddSquad;
    }

    public void setAddSquad(boolean addSquad) {
        AddSquad = addSquad;
    }

    public boolean isSelectAXI() {
        return SelectAXI;
    }

    public void setSelectAXI(boolean selectAXI) {
        SelectAXI = selectAXI;
    }

    public boolean isSelectBXI() {
        return SelectBXI;
    }

    public void setSelectBXI(boolean selectBXI) {
        SelectBXI = selectBXI;
    }

    public boolean isJustStarted() {
        return justStarted;
    }

    public void setJustStarted(boolean justStarted) {
        this.justStarted = justStarted;
    }

    public boolean isWagonwheel() {
        return wagonwheel;
    }

    public void setWagonwheel(boolean wagonwheel) {
        this.wagonwheel = wagonwheel;
    }

    public boolean isFollowon() {
        return followon;
    }

    public void setFollowon(boolean followon) {
        this.followon = followon;
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

    public int getRevisedTarget() {
        return revisedTarget;
    }

    public void setRevisedTarget(int revisedTarget) {
        this.revisedTarget = revisedTarget;
    }

    public int getPlayingInnings() {
        return playingInnings;
    }

    public void setPlayingInnings(int playingInnings) {
        this.playingInnings = playingInnings;
    }

    public boolean isEndOfInnings() {
        return endOfInnings;
    }

    public void setEndOfInnings(boolean endOfInnings) {
        this.endOfInnings = endOfInnings;
    }

    public int getWinner_team() {
        return winner_team;
    }

    public void setWinner_team(int winner_team) {
        this.winner_team = winner_team;
    }

    public int getWin_by_run() {
        return win_by_run;
    }

    public void setWin_by_run(int win_by_run) {
        this.win_by_run = win_by_run;
    }

    public int getWin_by_wicket() {
        return win_by_wicket;
    }

    public void setWin_by_wicket(int win_by_wicket) {
        this.win_by_wicket = win_by_wicket;
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

    public int getTeamAId() {
        return teamAId;
    }

    public void setTeamAId(int teamAId) {
        this.teamAId = teamAId;
    }

    public int getTeamBId() {
        return teamBId;
    }

    public void setTeamBId(int teamBId) {
        this.teamBId = teamBId;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public boolean isPost() {
        return post;
    }

    public void setPost(boolean post) {
        this.post = post;
    }

    public boolean isPlayer_post() {
        return player_post;
    }

    public void setPlayer_post(boolean player_post) {
        this.player_post = player_post;
    }

    public boolean isReducedOver() {
        return reducedOver;
    }

    public void setReducedOver(boolean reducedOver) {
        this.reducedOver = reducedOver;
    }

    public boolean isReducedRuns() {
        return reducedRuns;
    }

    public void setReducedRuns(boolean reducedRuns) {
        this.reducedRuns = reducedRuns;
    }

    public String getAppliedRainRule() {
        return appliedRainRule;
    }

    public void setAppliedRainRule(String appliedRainRule) {
        this.appliedRainRule = appliedRainRule;
    }

    public int getTeamA_sync() {
        return teamA_sync;
    }

    public void setTeamA_sync(int teamA_sync) {
        this.teamA_sync = teamA_sync;
    }

    public int getTeamB_sync() {
        return teamB_sync;
    }

    public void setTeamB_sync(int teamB_sync) {
        this.teamB_sync = teamB_sync;
    }

    public int getMatchNote_sync() {
        return matchNote_sync;
    }

    public void setMatchNote_sync(int matchNote_sync) {
        this.matchNote_sync = matchNote_sync;
    }

    public boolean isPulled() {
        return pulled;
    }

    public void setPulled(boolean pulled) {
        this.pulled = pulled;
    }

    public boolean isPulled_squad() {
        return pulled_squad;
    }

    public void setPulled_squad(boolean pulled_squad) {
        this.pulled_squad = pulled_squad;
    }

    public boolean isPulled_squadA() {
        return pulled_squadA;
    }

    public void setPulled_squadA(boolean pulled_squadA) {
        this.pulled_squadA = pulled_squadA;
    }

    public boolean isPulled_squadB() {
        return pulled_squadB;
    }

    public void setPulled_squadB(boolean pulled_squadB) {
        this.pulled_squadB = pulled_squadB;
    }

    public int getTeamA_forfeit() {
        return teamA_forfeit;
    }

    public void setTeamA_forfeit(int teamA_forfeit) {
        this.teamA_forfeit = teamA_forfeit;
    }

    public int getTeamB_forfeit() {
        return teamB_forfeit;
    }

    public void setTeamB_forfeit(int teamB_forfeit) {
        this.teamB_forfeit = teamB_forfeit;
    }

    public int getTeamA_forfeit_innings() {
        return teamA_forfeit_innings;
    }

    public void setTeamA_forfeit_innings(int teamA_forfeit_innings) {
        this.teamA_forfeit_innings = teamA_forfeit_innings;
    }

    public int getTeamB_forfeit_innings() {
        return teamB_forfeit_innings;
    }

    public void setTeamB_forfeit_innings(int teamB_forfeit_innings) {
        this.teamB_forfeit_innings = teamB_forfeit_innings;
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

    public int getConceded_innings() {
        return conceded_innings;
    }

    public void setConceded_innings(int conceded_innings) {
        this.conceded_innings = conceded_innings;
    }

    public boolean isLimited_overs() {
        return limited_overs;
    }

    public void setLimited_overs(boolean limited_overs) {
        this.limited_overs = limited_overs;
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

    public int getMax_opb() {
        return max_opb;
    }

    public void setMax_opb(int max_opb) {
        this.max_opb = max_opb;
    }

    public boolean isHundred() {
        return hundred;
    }

    public void setHundred(boolean hundred) {
        this.hundred = hundred;
    }

    public int getMax_bpb() {
        return max_bpb;
    }

    public void setMax_bpb(int max_bpb) {
        this.max_bpb = max_bpb;
    }

    public int getAddPOfficials() {
        return addPOfficials;
    }

    public void setAddPOfficials(int addPOfficials) {
        this.addPOfficials = addPOfficials;
    }

    public int getAddPlayers() {
        return addPlayers;
    }

    public void setAddPlayers(int addPlayers) {
        this.addPlayers = addPlayers;
    }

    public int getAddToss() {
        return addToss;
    }

    public void setAddToss(int addToss) {
        this.addToss = addToss;
    }

    public int getAddEvents() {
        return addEvents;
    }

    public void setAddEvents(int addEvents) {
        this.addEvents = addEvents;
    }

    public int getUpdateTeamNames() {
        return UpdateTeamNames;
    }

    public void setUpdateTeamNames(int updateTeamNames) {
        UpdateTeamNames = updateTeamNames;
    }

    public int getAbandoned_match_flag() {
        return abandoned_match_flag;
    }

    public void setAbandoned_match_flag(int abandoned_match_flag) {
        this.abandoned_match_flag = abandoned_match_flag;
    }

    public int getAbandoned_after_toss_match_flag() {
        return abandoned_after_toss_match_flag;
    }

    public void setAbandoned_after_toss_match_flag(int abandoned_after_toss_match_flag) {
        this.abandoned_after_toss_match_flag = abandoned_after_toss_match_flag;
    }

    public int getResult_sync() {
        return result_sync;
    }

    public void setResult_sync(int result_sync) {
        this.result_sync = result_sync;
    }

    public int getEditResult() {
        return editResult;
    }

    public void setEditResult(int editResult) {
        this.editResult = editResult;
    }

    public int getEditMOM() {
        return editMOM;
    }

    public void setEditMOM(int editMOM) {
        this.editMOM = editMOM;
    }

    public boolean isConfirmAXI() {
        return ConfirmAXI;
    }

    public void setConfirmAXI(boolean confirmAXI) {
        ConfirmAXI = confirmAXI;
    }

    public boolean isConfirmBXI() {
        return ConfirmBXI;
    }

    public void setConfirmBXI(boolean confirmBXI) {
        ConfirmBXI = confirmBXI;
    }
}

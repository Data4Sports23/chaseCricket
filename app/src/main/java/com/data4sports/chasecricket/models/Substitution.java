package com.data4sports.chasecricket.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Substitution extends RealmObject {

    @PrimaryKey
    private int subID;

    private int matchid;
    private String matchID;

    private int eventID;

    private int team;
    private int innings;

    private int player_OUT_ID;
    private int d4s_player_OUT_ID;
    private int player_IN_ID;
    private int d4s_player_IN_ID;
    private int new_bowler_id;
    private int d4s_new_bowler_id;

    private boolean concussion;

    private int sync;


    public Substitution() {
        init();
    }


    public void init() {

        matchid = 0;
        matchID = "";
        eventID = 0;
        team = 0;
        innings = 0;
        player_OUT_ID = 0;
        d4s_player_OUT_ID = 0;
        player_IN_ID = 0;
        d4s_player_IN_ID = 0;
        new_bowler_id = 0;
        d4s_new_bowler_id = 0;
        concussion = false;
        sync = 0;
    }


    public int getSubID() {
        return subID;
    }

    public void setSubID(int subID) {
        this.subID = subID;
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

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
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

    public int getPlayer_OUT_ID() {
        return player_OUT_ID;
    }

    public void setPlayer_OUT_ID(int player_OUT_ID) {
        this.player_OUT_ID = player_OUT_ID;
    }

    public int getPlayer_IN_ID() {
        return player_IN_ID;
    }

    public void setPlayer_IN_ID(int player_IN_ID) {
        this.player_IN_ID = player_IN_ID;
    }

    public int getD4s_player_OUT_ID() {
        return d4s_player_OUT_ID;
    }

    public void setD4s_player_OUT_ID(int d4s_player_OUT_ID) {
        this.d4s_player_OUT_ID = d4s_player_OUT_ID;
    }

    public int getD4s_player_IN_ID() {
        return d4s_player_IN_ID;
    }

    public void setD4s_player_IN_ID(int d4s_player_IN_ID) {
        this.d4s_player_IN_ID = d4s_player_IN_ID;
    }

    public int getNew_bowler_id() {
        return new_bowler_id;
    }

    public void setNew_bowler_id(int new_bowler_id) {
        this.new_bowler_id = new_bowler_id;
    }

    public int getD4s_new_bowler_id() {
        return d4s_new_bowler_id;
    }

    public void setD4s_new_bowler_id(int d4s_new_bowler_id) {
        this.d4s_new_bowler_id = d4s_new_bowler_id;
    }

    public boolean isConcussion() {
        return concussion;
    }

    public void setConcussion(boolean concussion) {
        this.concussion = concussion;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }
}

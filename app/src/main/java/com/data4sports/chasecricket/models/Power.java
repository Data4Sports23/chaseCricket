package com.data4sports.chasecricket.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Power extends RealmObject {

    @PrimaryKey
    private int id;
    private int matchid;
    private String matchID;
    private int innings;
    private int count;  // sequence
    private int start;
    private int end;
    private int sync;
//    private int sequence;

//    private String type;

    public Power() {
        init();
    }


    public void init() {

        matchid = 0;
        matchID = "";
        innings = 0;
        count = 0;
        start = 0;
        end = 0;
        sync = 0;
//        sequence = 0;

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

    public int getInnings() {
        return innings;
    }

    public void setInnings(int innings) {
        this.innings = innings;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

 /*   public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }*/
}

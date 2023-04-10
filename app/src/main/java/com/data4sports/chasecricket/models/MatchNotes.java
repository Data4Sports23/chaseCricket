package com.data4sports.chasecricket.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MatchNotes extends RealmObject {

    @PrimaryKey
    private int note_id;
    private int matchid;
    private String matchID;
    private int innings;
    private float over;
    private int sequence;
    private String note;
    private int sync;
    private boolean add;
    private boolean edit;
    private boolean delete;
    private int posted; // 0 = default value
                        // 1 = from AddPlayerA
                        // 2 = from AddPlayerB
                        // 3 = from Captain
                        // 4 = from Toss
                        // 5 = from Openers
                        // 6 = from Scoring

    public MatchNotes() {
        init();
    }

    public void init() {

        matchid = 0;
        matchID = "";
        innings = 0;
        over = 0f;
        sequence = 0;
        note ="";
        sync = 0;
        add = false;
        edit =false;
        delete =false;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
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

    public float getOver() {
        return over;
    }

    public void setOver(float over) {
        this.over = over;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}

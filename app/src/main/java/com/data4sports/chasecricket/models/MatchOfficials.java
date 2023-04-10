package com.data4sports.chasecricket.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MatchOfficials extends RealmObject {

    @PrimaryKey
    private int officialID;
    private int matchid;
    private String matchID;

    private int d4s_id;
    private String officialName;
    private String status;  // u for umpire 1 & 2
                            // t for 3rd umpire
                            // f for 4th umpire
                            // r for match referee
    private int edit;       // if edit = 0, official is not added yet
                            // if edit = 1, new official is added
                            // if edit = 2, new official name is updated
    private int sync;
    private boolean post;
    private boolean delete; // to delete it from server when the network would not available
                            // at the time of deleting the official
                            // false => valid data
                            // true => deleted data, and should not display while listing officials



    public MatchOfficials() {
        init();
    }


    void init(){

        matchid = 0;
        matchID = "";
        officialName = "";
        status = "";
        d4s_id = 0;
        post = false;
        sync = 0;
        edit = 0;
        delete = false;
    }


    public int getOfficialID() {
        return officialID;
    }

    public void setOfficialID(int officialID) {
        this.officialID = officialID;
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

    public int getD4s_id() {
        return d4s_id;
    }

    public void setD4s_id(int d4s_id) {
        this.d4s_id = d4s_id;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPost() {
        return post;
    }

    public void setPost(boolean post) {
        this.post = post;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    //added on 04/12/2020

    public int getEdit() {
        return edit;
    }

    public void setEdit(int edit) {
        this.edit = edit;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}

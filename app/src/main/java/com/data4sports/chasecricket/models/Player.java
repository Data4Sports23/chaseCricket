package com.data4sports.chasecricket.models;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Player extends RealmObject {

    @PrimaryKey
    private int playerID;
//    private int OPID;
    private int matchid;
    private int gameid;
    private int d4s_teamID;
    private String matchID;
    private int d4s_playerid;

    private String playerName;
    private int team;
    private int position;
    private boolean captain;
    private boolean viceCaptain;
    private boolean wicketKeeper;
    private boolean old_wk;                 //ADDED ON 27/02/2021
    private boolean new_wk;                 //ADDED ON 27/02/2021
    private int wicketkeeping_position;     //ADDED ON 27/02/2021
    private boolean substitute;
    private boolean retired;    // normal retired or injured
    private boolean retired_concussion;
    private boolean playing;
    private int retired_innings;
    private int sync;
    private int pulled;

    // added on 09/09/2020
    private boolean edit;
//    private boolean SUPER_OVER;
//    private boolean addedToTable;
//
//    private boolean post;


    public Player() {
        init();
    }


    void init(){

//        playerID = 0;
//        OPID = 0;
        matchid = 0;
        gameid = 0;
        d4s_teamID = 0;
        matchID = "";
        playerName = "";
        team = 0;
        captain = false;
        viceCaptain = false;
        wicketKeeper = false;
        substitute = true;
        retired = false;
        retired_concussion = false;
        playing = false;
        retired_innings = 0;
        sync = 0;
        pulled = 0;
//        SUPER_OVER = false;
//        addedToTable = false;
        d4s_playerid = 0;
//        post = false;
        edit = false;

        old_wk = false;
        new_wk = false;
        wicketkeeping_position = 0;
    }

//    public Player(int playerID, String matchID, String playerName, int team, String captain, String wicketKeeper, String substitute) {
//        this.playerID = playerID;
//        this.matchID = matchID;
//        this.playerName = playerName;
//        this.team = team;
//        this.captain = captain;
//        this.wicketKeeper = wicketKeeper;
//        this.substitute = substitute;
//    }


    public int getMatchid() {
        return matchid;
    }

    public void setMatchid(int matchid) {
        this.matchid = matchid;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

 /*   public int getOPID() {
        return OPID;
    }

    public void setOPID(int OPID) {
        this.OPID = OPID;
    }*/

    public int getD4s_playerid() {
        return d4s_playerid;
    }

    public void setD4s_playerid(int d4s_playerid) {
        this.d4s_playerid = d4s_playerid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isCaptain() {
        return captain;
    }

    public void setCaptain(boolean captain) {
        this.captain = captain;
    }

    public boolean isViceCaptain() {
        return viceCaptain;
    }

    public void setViceCaptain(boolean viceCaptain) {
        this.viceCaptain = viceCaptain;
    }

    public boolean isWicketKeeper() {
        return wicketKeeper;
    }

    public void setWicketKeeper(boolean wicketKeeper) {
        this.wicketKeeper = wicketKeeper;
    }

    public boolean isSubstitute() {
        return substitute;
    }

    public void setSubstitute(boolean substitute) {
        this.substitute = substitute;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public boolean isRetired_concussion() {
        return retired_concussion;
    }

    public void setRetired_concussion(boolean retired_concussion) {
        this.retired_concussion = retired_concussion;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getRetired_innings() {
        return retired_innings;
    }

    public void setRetired_innings(int retired_innings) {
        this.retired_innings = retired_innings;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    /*public boolean isSUPER_OVER() {
        return SUPER_OVER;
    }

    public void setSUPER_OVER(boolean SUPER_OVER) {
        this.SUPER_OVER = SUPER_OVER;
    }

    public boolean isAddedToTable() {
        return addedToTable;
    }

    public void setAddedToTable(boolean addedToTable) {
        this.addedToTable = addedToTable;
    }

    public boolean isPost() {
        return post;
    }

    public void setPost(boolean post) {
        this.post = post;
    }*/

    //ADDED ON 27/02/2021

    public boolean isOld_wk() {
        return old_wk;
    }

    public void setOld_wk(boolean old_wk) {
        this.old_wk = old_wk;
    }

    public boolean isNew_wk() {
        return new_wk;
    }

    public void setNew_wk(boolean new_wk) {
        this.new_wk = new_wk;
    }

    public int getWicketkeeping_position() {
        return wicketkeeping_position;
    }

    public void setWicketkeeping_position(int wicketkeeping_position) {
        this.wicketkeeping_position = wicketkeeping_position;
    }

    public int getPulled() {
        return pulled;
    }

    public void setPulled(int pulled) {
        this.pulled = pulled;
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public int getD4s_teamID() {
        return d4s_teamID;
    }

    public void setD4s_teamID(int d4s_teamID) {
        this.d4s_teamID = d4s_teamID;
    }
}

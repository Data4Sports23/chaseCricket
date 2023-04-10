package com.data4sports.chasecricket.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private int userId;
    private int d4s_userid;
    private String token, access_token, refresh_token;
    private String username, email, password, phonenumber, address;
    private boolean loggedInStatus = false;

    public User(){

    }

    public User(int d4s_userid, String username,  String email,String phonenumber,String address, String token, String access_token) {

        this.d4s_userid = d4s_userid;
        this.username = username;
        this.email = email;
        this.token = token;
        this.access_token = access_token;
        this.phonenumber = phonenumber;
        this.address = address;
    }

    public User(String username,  String email,String phonenumber,String address, int userId) {

        this.username = username;
        this.email = email;
        this.userId = userId;
        this.phonenumber = phonenumber;
        this.address = address;

    }

    public User(String username,  String email, String phonenumber, String address,int userId, int d4s_userid) {

        this.username = username;
        this.email = email;
        this.userId = userId;
        this.d4s_userid = d4s_userid;
        this.phonenumber = phonenumber;
        this.address = address;

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedInStatus() {
        return loggedInStatus;
    }

    public void setLoggedInStatus(boolean loggedInStatus) {
        this.loggedInStatus = loggedInStatus;
    }

    public int getD4s_userid() {
        return d4s_userid;
    }

    public void setD4s_userid(int d4s_userid) {
        this.d4s_userid = d4s_userid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}

package com.example.projekta.model;

public class MListUser {
    private String id_user,username,user_level,nama;
    public MListUser(){

    }

    public MListUser(String id_user, String username, String user_level, String nama) {
        this.id_user = id_user;
        this.username = username;
        this.user_level = user_level;
        this.nama = nama;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}

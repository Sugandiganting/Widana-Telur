package com.example.projekta.model;

public class MDataUser {
    private String id_user,nama,alamat,no_hp,tanggal_lahir,username,password,user_level;
    public MDataUser(){
    }

    public MDataUser(String id_user, String nama, String alamat, String no_hp, String tanggal_lahir, String username, String password, String user_level) {
        this.id_user = id_user;
        this.nama = nama;
        this.alamat = alamat;
        this.no_hp = no_hp;
        this.tanggal_lahir = tanggal_lahir;
        this.username = username;
        this.password = password;
        this.user_level = user_level;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }
}

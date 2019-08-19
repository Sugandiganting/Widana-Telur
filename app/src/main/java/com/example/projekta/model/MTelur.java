package com.example.projekta.model;

public class MTelur {
    private String id_telur,ukuran_telur,harga_beli,harga_jual;

    public MTelur(){
    }

    public MTelur(String id_telur, String ukuran_telur, String harga_beli, String harga_jual) {
        this.id_telur = id_telur;
        this.ukuran_telur = ukuran_telur;
        this.harga_beli = harga_beli;
        this.harga_jual = harga_jual;
    }

    public String getId_telur() {
        return id_telur;
    }

    public void setId_telur(String id_telur) {
        this.id_telur = id_telur;
    }

    public String getUkuran_telur() {
        return ukuran_telur;
    }

    public void setUkuran_telur(String ukuran_telur) {
        this.ukuran_telur = ukuran_telur;
    }

    public String getHarga_beli() {
        return harga_beli;
    }

    public void setHarga_beli(String harga_beli) {
        this.harga_beli = harga_beli;
    }

    public String getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(String harga_jual) {
        this.harga_jual = harga_jual;
    }
}

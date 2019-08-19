package com.example.projekta.model;

public class MResponse {
    private int kode;
    private String pesan;

    public MResponse(int kode, String pesan) {
        this.kode = kode;
        this.pesan = pesan;
    }

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
}

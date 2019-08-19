package com.example.projekta.database;

import com.orm.SugarRecord;

public class Pengaturan extends SugarRecord {
    private String ip_address;
    private String invoice;
    private String nama_toko;
    private String alamat_toko;

    public Pengaturan(){
    }

    public Pengaturan(String ip_address, String invoice, String nama_toko, String alamat_toko) {
        this.ip_address = ip_address;
        this.invoice = invoice;
        this.nama_toko = nama_toko;
        this.alamat_toko = alamat_toko;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getNama_toko() {
        return nama_toko;
    }

    public void setNama_toko(String nama_toko) {
        this.nama_toko = nama_toko;
    }

    public String getAlamat_toko() {
        return alamat_toko;
    }

    public void setAlamat_toko(String alamat_toko) {
        this.alamat_toko = alamat_toko;
    }
}

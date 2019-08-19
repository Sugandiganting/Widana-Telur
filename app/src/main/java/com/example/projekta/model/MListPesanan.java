package com.example.projekta.model;

public class MListPesanan {

    private String id_transaksi,no_transaksi,type_pengambilan,tanggal_pemesanan,tanggal_pengiriman,biaya_jasa,total,status,no_hp,alamat_pengiriman,alasan_batal;

    public MListPesanan(){

    }

    public MListPesanan(String id_transaksi, String no_transaksi, String type_pengambilan, String tanggal_pemesanan, String tanggal_pengiriman, String biaya_jasa, String total, String status, String no_hp, String alamat_pengiriman, String alasan_batal) {
        this.id_transaksi = id_transaksi;
        this.no_transaksi = no_transaksi;
        this.type_pengambilan = type_pengambilan;
        this.tanggal_pemesanan = tanggal_pemesanan;
        this.tanggal_pengiriman = tanggal_pengiriman;
        this.biaya_jasa = biaya_jasa;
        this.total = total;
        this.status = status;
        this.no_hp = no_hp;
        this.alamat_pengiriman = alamat_pengiriman;
        this.alasan_batal = alasan_batal;
    }

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getNo_transaksi() {
        return no_transaksi;
    }

    public void setNo_transaksi(String no_transaksi) {
        this.no_transaksi = no_transaksi;
    }

    public String getType_pengambilan() {
        return type_pengambilan;
    }

    public void setType_pengambilan(String type_pengambilan) {
        this.type_pengambilan = type_pengambilan;
    }

    public String getTanggal_pemesanan() {
        return tanggal_pemesanan;
    }

    public void setTanggal_pemesanan(String tanggal_pemesanan) {
        this.tanggal_pemesanan = tanggal_pemesanan;
    }

    public String getTanggal_pengiriman() {
        return tanggal_pengiriman;
    }

    public void setTanggal_pengiriman(String tanggal_pengiriman) {
        this.tanggal_pengiriman = tanggal_pengiriman;
    }

    public String getBiaya_jasa() {
        return biaya_jasa;
    }

    public void setBiaya_jasa(String biaya_jasa) {
        this.biaya_jasa = biaya_jasa;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getAlamat_pengiriman() {
        return alamat_pengiriman;
    }

    public void setAlamat_pengiriman(String alamat_pengiriman) {
        this.alamat_pengiriman = alamat_pengiriman;
    }

    public String getAlasan_batal() {
        return alasan_batal;
    }

    public void setAlasan_batal(String alasan_batal) {
        this.alasan_batal = alasan_batal;
    }
}

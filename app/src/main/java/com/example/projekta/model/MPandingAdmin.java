package com.example.projekta.model;

public class MPandingAdmin {
    private String id_transaksi,no_transaksi,customer_name,type_pengambilan,tanggal_pemesanan,tanggal_pengiriman,biaya_jasa,total,no_hp,alamat_pengiriman,alasan_batal,nama_kurir,type_payment,amount,payment_term;

    public MPandingAdmin(){

    }

    public MPandingAdmin(String id_transaksi, String no_transaksi, String customer_name, String type_pengambilan, String tanggal_pemesanan, String tanggal_pengiriman, String biaya_jasa, String total, String no_hp, String alamat_pengiriman, String alasan_batal, String nama_kurir, String type_payment, String amount, String payment_term) {
        this.id_transaksi = id_transaksi;
        this.no_transaksi = no_transaksi;
        this.customer_name = customer_name;
        this.type_pengambilan = type_pengambilan;
        this.tanggal_pemesanan = tanggal_pemesanan;
        this.tanggal_pengiriman = tanggal_pengiriman;
        this.biaya_jasa = biaya_jasa;
        this.total = total;
        this.no_hp = no_hp;
        this.alamat_pengiriman = alamat_pengiriman;
        this.alasan_batal = alasan_batal;
        this.nama_kurir = nama_kurir;
        this.type_payment = type_payment;
        this.amount = amount;
        this.payment_term = payment_term;
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

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
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

    public String getNama_kurir() {
        return nama_kurir;
    }

    public void setNama_kurir(String nama_kurir) {
        this.nama_kurir = nama_kurir;
    }

    public String getType_payment() {
        return type_payment;
    }

    public void setType_payment(String type_payment) {
        this.type_payment = type_payment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayment_term() {
        return payment_term;
    }

    public void setPayment_term(String payment_term) {
        this.payment_term = payment_term;
    }
}

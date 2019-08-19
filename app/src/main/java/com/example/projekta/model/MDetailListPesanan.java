package com.example.projekta.model;

public class MDetailListPesanan {
    private String ukuran_telur,qty_order,harga_jual,total;

    public MDetailListPesanan(){

    }

    public MDetailListPesanan(String ukuran_telur, String qty_order, String harga_jual, String total) {
        this.ukuran_telur = ukuran_telur;
        this.qty_order = qty_order;
        this.harga_jual = harga_jual;
        this.total = total;
    }

    public String getUkuran_telur() {
        return ukuran_telur;
    }

    public void setUkuran_telur(String ukuran_telur) {
        this.ukuran_telur = ukuran_telur;
    }

    public String getQty_order() {
        return qty_order;
    }

    public void setQty_order(String qty_order) {
        this.qty_order = qty_order;
    }

    public String getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(String harga_jual) {
        this.harga_jual = harga_jual;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

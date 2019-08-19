package com.example.projekta.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.projekta.DetailTransaksiKurir;
import com.example.projekta.R;
import com.example.projekta.controller.CPenagihanCreditKurir;
import com.example.projekta.model.MPandingAdmin;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ACreditKurir extends RecyclerView.Adapter<ACreditKurir.ViewHolder> {
    private Context context;
    private List<MPandingAdmin> pandingAdminList;
    Locale localeID = new Locale("in", "ID");
    String id_transaksi, total_transaksi, batas_pembayaran,uang_muka,biaya_jasa;
    int tot_pembayaran,dp,sisa;
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public ACreditKurir(Context context, List<MPandingAdmin> pandingAdminList){
        this.context = context;
        this.pandingAdminList = pandingAdminList;
    }

    @NonNull
    @Override
    public ACreditKurir.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_credit_kurir, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ACreditKurir.ViewHolder holder, final int i) {

        tot_pembayaran = Integer.valueOf(pandingAdminList.get(i).getTotal());
        if (pandingAdminList.get(i).getAmount() != null){
            dp =Integer.valueOf(pandingAdminList.get(i).getAmount());
        }else {
            dp = 0;
        }
        sisa = tot_pembayaran-dp;

        holder.tv_noTransaksi.setText(pandingAdminList.get(i).getNo_transaksi());
        holder.tv_noHp.setText(pandingAdminList.get(i).getNo_hp());
        holder.tv_user.setText(pandingAdminList.get(i).getCustomer_name());
        holder.tv_alamatPengiriman.setText(pandingAdminList.get(i).getAlamat_pengiriman());
        holder.tv_tglPengiriman.setText(pandingAdminList.get(i).getTanggal_pengiriman());
        holder.tv_biayaJasa.setText("" + formatRupiah.format(Double.valueOf(pandingAdminList.get(i).getBiaya_jasa())));
        holder.tv_total.setText("" + formatRupiah.format(Double.valueOf(pandingAdminList.get(i).getTotal())));
        holder.tv_uangDp.setText(""+formatRupiah.format(Double.valueOf(pandingAdminList.get(i).getAmount())));
        holder.tv_tanggalPenagihan.setText(""+pandingAdminList.get(i).getPayment_term());
        holder.tv_sisaPembayaran.setText(""+sisa);

        holder.btn_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_transaksi = pandingAdminList.get(i).getId_transaksi();
                total_transaksi = pandingAdminList.get(i).getTotal();
                batas_pembayaran = pandingAdminList.get(i).getPayment_term();
                String sisa_pembayaran =holder.tv_sisaPembayaran.getText().toString();
                uang_muka = pandingAdminList.get(i).getAmount();

                CPenagihanCreditKurir cPenagihanCreditKurir = new CPenagihanCreditKurir(context);
                cPenagihanCreditKurir.penagihanCreditKurir(id_transaksi,batas_pembayaran,total_transaksi,uang_muka,sisa_pembayaran);

            }
        });

        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_transaksi = pandingAdminList.get(i).getId_transaksi();
                total_transaksi = pandingAdminList.get(i).getTotal();
                biaya_jasa = pandingAdminList.get(i).getBiaya_jasa();
                Intent intent = new Intent(context, DetailTransaksiKurir.class);
                intent.putExtra("id", id_transaksi);
                intent.putExtra("total_transaksi", total_transaksi);
                intent.putExtra("biaya_jasa", biaya_jasa);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pandingAdminList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_noTransaksi, tv_tglPengiriman, tv_total, tv_user, tv_noHp, tv_alamatPengiriman, tv_biayaJasa,tv_uangDp,tv_sisaPembayaran,tv_tanggalPenagihan;
        private Button btn_detail,btn_bayar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_noTransaksi = itemView.findViewById(R.id.tv_noTransaksiCrK);
            tv_tglPengiriman = itemView.findViewById(R.id.tv_tglPengirimanCrK);
            tv_total = itemView.findViewById(R.id.tv_totalCrK);
            tv_user = itemView.findViewById(R.id.tv_userCrK);
            tv_noHp = itemView.findViewById(R.id.tv_noHpCrK);
            tv_alamatPengiriman = itemView.findViewById(R.id.tv_alamatPengirimanCrK);
            tv_biayaJasa = itemView.findViewById(R.id.tv_biayaJasaCrK);
            tv_uangDp= itemView.findViewById(R.id.tv_uangDpCrK);
            tv_sisaPembayaran = itemView.findViewById(R.id.tv_sisaPembayaranCrK);
            tv_tanggalPenagihan = itemView.findViewById(R.id.tv_batasPembayaranCrK);
            btn_detail = itemView.findViewById(R.id.btn_detailCrK);
            btn_bayar = itemView.findViewById(R.id.btn_bayarCrK);
        }
    }
}

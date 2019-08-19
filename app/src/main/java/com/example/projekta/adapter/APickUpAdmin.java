package com.example.projekta.adapter;

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
import com.example.projekta.controller.CPembayaranPickUpAdmin;
import com.example.projekta.model.MPandingAdmin;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class APickUpAdmin extends RecyclerView.Adapter<APickUpAdmin.ViewHolder> {

    private Context context;
    private List<MPandingAdmin> pandingAdminList;
    Locale localeID = new Locale("in", "ID");
    String id_transaksi, total_transaksi, biaya_jasa;
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public APickUpAdmin(Context context, List<MPandingAdmin> pandingAdminList) {
        this.context = context;
        this.pandingAdminList = pandingAdminList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_pick_up_admin, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        holder.tv_noTransaksi.setText(pandingAdminList.get(i).getNo_transaksi());
        holder.tv_noHp.setText(pandingAdminList.get(i).getNo_hp());
        holder.tv_user.setText(pandingAdminList.get(i).getCustomer_name());
        holder.tv_tglPemesanan.setText(pandingAdminList.get(i).getTanggal_pemesanan());
        holder.tv_tglPengiriman.setText(pandingAdminList.get(i).getTanggal_pengiriman());
        holder.tv_biayaJasa.setText("" + formatRupiah.format(Double.valueOf(pandingAdminList.get(i).getBiaya_jasa())));
        holder.tv_total.setText("" + formatRupiah.format(Double.valueOf(pandingAdminList.get(i).getTotal())));

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

        holder.btn_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_transaksi = pandingAdminList.get(i).getId_transaksi();
                total_transaksi = pandingAdminList.get(i).getTotal();
                CPembayaranPickUpAdmin cPembayaranPickUpAdmin = new CPembayaranPickUpAdmin(context);
                cPembayaranPickUpAdmin.bayarPickUp(id_transaksi,total_transaksi);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pandingAdminList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_noTransaksi, tv_tglPemesanan, tv_tglPengiriman, tv_total, tv_user, tv_noHp, tv_biayaJasa;
        private Button btn_detail,btn_bayar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_noTransaksi = itemView.findViewById(R.id.tv_noTransaksiPA);
            tv_tglPemesanan = itemView.findViewById(R.id.tv_tglPemesananPA);
            tv_tglPengiriman = itemView.findViewById(R.id.tv_tglPengirimanPA);
            tv_total = itemView.findViewById(R.id.tv_totalPA);
            tv_user = itemView.findViewById(R.id.tv_userPA);
            tv_noHp = itemView.findViewById(R.id.tv_noHpPA);
            tv_biayaJasa = itemView.findViewById(R.id.tv_biayaJasaPA);
            btn_detail = itemView.findViewById(R.id.btn_detailPA);
            btn_bayar = itemView.findViewById(R.id.btn_bayarPA);
        }
    }
}

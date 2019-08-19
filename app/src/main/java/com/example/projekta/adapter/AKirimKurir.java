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
import com.example.projekta.controller.CPembayaranKurir;
import com.example.projekta.model.MPandingAdmin;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AKirimKurir extends RecyclerView.Adapter<AKirimKurir.ViewHolder> {
    private Context context;
    private List<MPandingAdmin> pandingAdminList;
    Locale localeID = new Locale("in", "ID");
    private String id_transaksi, total_transaksi, biaya_jasa;
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public AKirimKurir(Context context, List<MPandingAdmin> pandingAdminList) {
        this.context = context;
        this.pandingAdminList = pandingAdminList;
    }

    @NonNull
    @Override
    public AKirimKurir.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_kiriman_kurir, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AKirimKurir.ViewHolder holder, final int i) {
        holder.tv_noTransaksi.setText(pandingAdminList.get(i).getNo_transaksi());
        holder.tv_noHp.setText(pandingAdminList.get(i).getNo_hp());
        holder.tv_user.setText(pandingAdminList.get(i).getCustomer_name());
        holder.tv_alamatPengiriman.setText(pandingAdminList.get(i).getAlamat_pengiriman());
        holder.tv_tglPemesanan.setText(pandingAdminList.get(i).getTanggal_pemesanan());
        holder.tv_tglPengiriman.setText(pandingAdminList.get(i).getTanggal_pengiriman());
        holder.tv_biayaJasa.setText("" + formatRupiah.format(Double.valueOf(pandingAdminList.get(i).getBiaya_jasa())));
        holder.tv_total.setText("" + formatRupiah.format(Double.valueOf(pandingAdminList.get(i).getTotal())));
        if (pandingAdminList.get(i).getType_pengambilan().equals("1")) {
            holder.tv_typePengambilan.setText("Delivery");
        } else {
            holder.tv_typePengambilan.setText("Pick Up");
        }
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

        holder.btn_terkirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_transaksi = pandingAdminList.get(i).getId_transaksi();
                total_transaksi = pandingAdminList.get(i).getTotal();
                CPembayaranKurir cPembayaranKurir = new CPembayaranKurir(context);
                cPembayaranKurir.PembayaranKurir(id_transaksi,total_transaksi);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pandingAdminList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_noTransaksi, tv_tglPemesanan, tv_tglPengiriman, tv_total, tv_user, tv_noHp, tv_alamatPengiriman, tv_typePengambilan, tv_biayaJasa;
        private Button btn_detail,btn_terkirim;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_noTransaksi = itemView.findViewById(R.id.tv_noTransaksiK);
            tv_tglPemesanan = itemView.findViewById(R.id.tv_tglPemesananK);
            tv_tglPengiriman = itemView.findViewById(R.id.tv_tglPengirimanK);
            tv_total = itemView.findViewById(R.id.tv_totalK);
            tv_user = itemView.findViewById(R.id.tv_userK);
            tv_noHp = itemView.findViewById(R.id.tv_noHpK);
            tv_alamatPengiriman = itemView.findViewById(R.id.tv_alamatPengirimanK);
            tv_typePengambilan = itemView.findViewById(R.id.tv_typePengambilanK);
            tv_biayaJasa = itemView.findViewById(R.id.tv_biayaJasaK);
            btn_detail = itemView.findViewById(R.id.btn_detailK);
            btn_terkirim = itemView.findViewById(R.id.btn_kirim);
        }
    }
}

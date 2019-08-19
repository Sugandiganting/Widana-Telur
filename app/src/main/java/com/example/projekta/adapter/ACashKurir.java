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
import com.example.projekta.model.MPandingAdmin;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ACashKurir extends RecyclerView.Adapter<ACashKurir.ViewHolder> {
    private Context context;
    private List<MPandingAdmin> pandingAdminList;
    Locale localeID = new Locale("in", "ID");
    String id_transaksi, total_transaksi, biaya_jasa;
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public ACashKurir(Context context, List<MPandingAdmin> pandingAdminList){
        this.context = context;
        this.pandingAdminList = pandingAdminList;
    }
    @NonNull
    @Override
    public ACashKurir.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_cash_kurir, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ACashKurir.ViewHolder holder, final int i) {
        holder.tv_noTransaksi.setText(pandingAdminList.get(i).getNo_transaksi());
        holder.tv_noHp.setText(pandingAdminList.get(i).getNo_hp());
        holder.tv_user.setText(pandingAdminList.get(i).getCustomer_name());
        holder.tv_alamatPengiriman.setText(pandingAdminList.get(i).getAlamat_pengiriman());
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
    }

    @Override
    public int getItemCount() {
        return pandingAdminList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_noTransaksi, tv_tglPengiriman, tv_total, tv_user, tv_noHp, tv_alamatPengiriman, tv_biayaJasa;
        private Button btn_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_noTransaksi = itemView.findViewById(R.id.tv_noTransaksiCK);
            tv_tglPengiriman = itemView.findViewById(R.id.tv_tglPengirimanCK);
            tv_total = itemView.findViewById(R.id.tv_totalCK);
            tv_user = itemView.findViewById(R.id.tv_userCK);
            tv_noHp = itemView.findViewById(R.id.tv_noHpCK);
            tv_alamatPengiriman = itemView.findViewById(R.id.tv_alamatPengirimanCK);
            tv_biayaJasa = itemView.findViewById(R.id.tv_biayaJasaCK);
            btn_detail = itemView.findViewById(R.id.btn_detailCK);
        }
    }
}

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

public class ARejectAdmin extends RecyclerView.Adapter<ARejectAdmin.ViewHolder> {

    private Context context;
    private List<MPandingAdmin> pandingAdminList;
    Locale localeID = new Locale("in", "ID");
    String id_transaksi, total_transaksi, biaya_jasa;
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public ARejectAdmin(Context context, List<MPandingAdmin> pandingAdminList) {
        this.context = context;
        this.pandingAdminList = pandingAdminList;
    }
    @NonNull
    @Override
    public ARejectAdmin.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_reject_admin, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ARejectAdmin.ViewHolder holder, final int i) {
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
        holder.tv_alasan.setText(pandingAdminList.get(i).getAlasan_batal());

        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_transaksi = pandingAdminList.get(i).getId_transaksi();
                biaya_jasa = pandingAdminList.get(i).getBiaya_jasa();
                total_transaksi = pandingAdminList.get(i).getTotal();
//                Intent intent = new Intent(context, DetailRejectAdmin.class);
                Intent intent = new Intent(context, DetailTransaksiKurir.class);
                intent.putExtra("id", id_transaksi);
                intent.putExtra("biaya_jasa", biaya_jasa);
                intent.putExtra("total_transaksi", total_transaksi);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pandingAdminList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_noTransaksi, tv_tglPemesanan, tv_tglPengiriman, tv_total, tv_user, tv_noHp, tv_alamatPengiriman, tv_typePengambilan, tv_biayaJasa,tv_alasan;
        private Button btn_detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_alasan = itemView.findViewById(R.id.tv_alasanR);
            tv_noTransaksi = itemView.findViewById(R.id.tv_noTransaksiR);
            tv_tglPemesanan = itemView.findViewById(R.id.tv_tglPemesananR);
            tv_tglPengiriman = itemView.findViewById(R.id.tv_tglPengirimanR);
            tv_total = itemView.findViewById(R.id.tv_totalR);
            tv_user = itemView.findViewById(R.id.tv_userR);
            tv_noHp = itemView.findViewById(R.id.tv_noHpR);
            tv_alamatPengiriman = itemView.findViewById(R.id.tv_alamatPengirimanR);
            tv_typePengambilan = itemView.findViewById(R.id.tv_typePengambilanR);
            tv_biayaJasa = itemView.findViewById(R.id.tv_biayaJasaR);
            btn_detail = itemView.findViewById(R.id.btn_detailR);
        }
    }
}

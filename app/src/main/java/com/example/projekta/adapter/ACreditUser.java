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

public class ACreditUser extends RecyclerView.Adapter<ACreditUser.ViewHolder> {
    private Context context;
    private List<MPandingAdmin> pandingAdminList;
    Locale localeID = new Locale("in", "ID");
    String id_transaksi, total_transaksi,biaya_jasa;
    int tot_pembayaran,dp,sisa;
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public ACreditUser (Context context,List<MPandingAdmin> pandingAdminList){
        this.context = context;
        this.pandingAdminList = pandingAdminList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_kredit_user, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        tot_pembayaran = Integer.valueOf(pandingAdminList.get(i).getTotal());
        if (pandingAdminList.get(i).getAmount() != null){
            dp =Integer.valueOf(pandingAdminList.get(i).getAmount());
        }else {
            dp = 0;
        }
        sisa = tot_pembayaran-dp;
        holder.tv_kurir.setText(pandingAdminList.get(i).getNama_kurir());
        holder.tv_noTransaksi.setText(pandingAdminList.get(i).getNo_transaksi());
        holder.tv_noHp.setText(pandingAdminList.get(i).getNo_hp());
        holder.tv_alamatPengiriman.setText(pandingAdminList.get(i).getAlamat_pengiriman());
        holder.tv_tglPengiriman.setText(pandingAdminList.get(i).getTanggal_pengiriman());
        holder.tv_biayaJasa.setText("" + formatRupiah.format(Double.valueOf(pandingAdminList.get(i).getBiaya_jasa())));
        holder.tv_total.setText("" + formatRupiah.format(Double.valueOf(pandingAdminList.get(i).getTotal())));
        holder.tv_uangDp.setText(""+formatRupiah.format(Double.valueOf(pandingAdminList.get(i).getAmount())));
        holder.tv_tanggalPenagihan.setText(""+pandingAdminList.get(i).getPayment_term());
        holder.tv_sisaPembayaran.setText(""+formatRupiah.format(Double.valueOf(sisa)));

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
        private TextView tv_noTransaksi, tv_tglPengiriman, tv_total, tv_noHp, tv_alamatPengiriman, tv_biayaJasa,tv_uangDp,tv_sisaPembayaran,tv_tanggalPenagihan,tv_kurir;
        private Button btn_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_kurir = itemView.findViewById(R.id.tv_kurirMengantarU);
            tv_noTransaksi = itemView.findViewById(R.id.tv_noTransaksiU);
            tv_tglPengiriman = itemView.findViewById(R.id.tv_tglPengirimanU);
            tv_total = itemView.findViewById(R.id.tv_totalU);
            tv_noHp = itemView.findViewById(R.id.tv_noHpU);
            tv_alamatPengiriman = itemView.findViewById(R.id.tv_alamatPengirimanU);
            tv_biayaJasa = itemView.findViewById(R.id.tv_biayaJasaU);
            tv_uangDp= itemView.findViewById(R.id.tv_uangDpU);
            tv_sisaPembayaran = itemView.findViewById(R.id.tv_sisaPembayaranU);
            tv_tanggalPenagihan = itemView.findViewById(R.id.tv_batasPembayaranU);
            btn_detail = itemView.findViewById(R.id.btn_detailU);
        }
    }
}

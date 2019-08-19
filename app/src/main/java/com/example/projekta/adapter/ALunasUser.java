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

public class ALunasUser extends RecyclerView.Adapter<ALunasUser.ViewHolder> {
    private Context context;
    private List<MPandingAdmin> pandingAdminList;
    Locale localeID = new Locale("in", "ID");
    String id_transaksi, total_transaksi,biaya_jasa;
    int tot_pembayaran,dp,sisa;
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public ALunasUser (Context context,List<MPandingAdmin> pandingAdminList){
        this.context = context;
        this.pandingAdminList = pandingAdminList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_lunas_user, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        if (pandingAdminList.get(i).getNama_kurir() != null){
            holder.tv_kurir.setText(pandingAdminList.get(i).getNama_kurir());
        }else {
            holder.tv_kurir.setText("Pick Up");
            holder.tv_tanggalPengiriman.setText("Tgl. Pengambilan");
        }
        holder.tv_noTransaksi.setText(pandingAdminList.get(i).getNo_transaksi());
        holder.tv_noHp.setText(pandingAdminList.get(i).getNo_hp());
        holder.tv_alamatPengiriman.setText(pandingAdminList.get(i).getAlamat_pengiriman());
        holder.tv_tglPengiriman.setText(pandingAdminList.get(i).getTanggal_pengiriman());
        holder.tv_biayaJasa.setText("" + formatRupiah.format(Double.valueOf(pandingAdminList.get(i).getBiaya_jasa())));
        holder.tv_total.setText("" + formatRupiah.format(Double.valueOf(pandingAdminList.get(i).getTotal())));
        if (pandingAdminList.get(i).getType_payment().equals("1")){
            holder.tv_type_pembayaran.setText("Kredit");
        }else {
            holder.tv_type_pembayaran.setText("Lunas");
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


    }

    @Override
    public int getItemCount() {
        return pandingAdminList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_noTransaksi, tv_tglPengiriman, tv_total, tv_noHp, tv_alamatPengiriman, tv_biayaJasa,tv_kurir,tv_type_pembayaran,tv_tanggalPengiriman;
        private Button btn_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_kurir = itemView.findViewById(R.id.tv_kurirMengantarUL);
            tv_noTransaksi = itemView.findViewById(R.id.tv_noTransaksiUL);
            tv_tglPengiriman = itemView.findViewById(R.id.tv_tglPengirimanUL);
            tv_total = itemView.findViewById(R.id.tv_totalUL);
            tv_noHp = itemView.findViewById(R.id.tv_noHpUL);
            tv_alamatPengiriman = itemView.findViewById(R.id.tv_alamatPengirimanUL);
            tv_biayaJasa = itemView.findViewById(R.id.tv_biayaJasaUL);
            tv_type_pembayaran = itemView.findViewById(R.id.tv_typePembayaranUL);
            tv_tanggalPengiriman = itemView.findViewById(R.id.tv_tglPengirimanLU);
            btn_detail = itemView.findViewById(R.id.btn_detailUL);
        }
    }
}

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

import com.example.projekta.DetailListOrderanUser;
import com.example.projekta.DetailTransaksiKurir;
import com.example.projekta.R;
import com.example.projekta.model.MListPesanan;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AListPesanan extends RecyclerView.Adapter<AListPesanan.ViewHolder> {

    private Context context;
    private List<MListPesanan> listPesanan;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    String id_transaksi,total_transaksi,biaya_jasa;
    public AListPesanan(Context context, List<MListPesanan> listPesanan) {
        this.context = context;
        this.listPesanan = listPesanan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater =LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_listorderan,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        holder.tv_noTransaksi.setText(listPesanan.get(i).getNo_transaksi());
        holder.tv_noHp.setText(listPesanan.get(i).getNo_hp());
        holder.tv_alamatPengiriman.setText(listPesanan.get(i).getAlamat_pengiriman());
        holder.tv_tglPemesanan.setText(listPesanan.get(i).getTanggal_pemesanan());
        holder.tv_tglPengiriman.setText(listPesanan.get(i).getTanggal_pengiriman());
        holder.tv_biayaJasa.setText(""+formatRupiah.format(Double.valueOf(listPesanan.get(i).getBiaya_jasa())));
        holder.tv_total.setText(""+formatRupiah.format(Double.valueOf(listPesanan.get(i).getTotal())));

        if (listPesanan.get(i).getStatus().equals("0")){
            holder.tv_status.setText("Pending");
        }else if(listPesanan.get(i).getStatus().equals("1")) {
            holder.tv_status.setText("Diterima");
        }else if(listPesanan.get(i).getStatus().equals("2")){
            holder.tv_status.setText("Ditolak");
        }else if(listPesanan.get(i).getStatus().equals("3")) {
            holder.tv_status.setText("Terkirim");
        }else{
            holder.tv_status.setText("Lunas");
        }
        if (listPesanan.get(i).getType_pengambilan().equals("1")){
            holder.tv_typePengambilan.setText("Delivery");
        }else {
            holder.tv_typePengambilan.setText("Pick Up");
        }
        if (listPesanan.get(i).getAlasan_batal().isEmpty()){
            holder.tv_alasan.setText("-");
        }else {
            holder.tv_alasan.setText(""+listPesanan.get(i).getAlasan_batal());
        }
        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_transaksi = listPesanan.get(i).getId_transaksi();
                total_transaksi = listPesanan.get(i).getTotal();
                biaya_jasa = listPesanan.get(i).getBiaya_jasa();
//                Intent intent = new Intent(context, DetailListOrderanUser.class);
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
        return listPesanan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_noTransaksi,tv_tglPemesanan,tv_tglPengiriman,tv_total,tv_status,tv_noHp,tv_alamatPengiriman,tv_typePengambilan,tv_biayaJasa,tv_alasan;
        private Button btn_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_noTransaksi = itemView.findViewById(R.id.tv_noTransaksi);
            tv_tglPemesanan = itemView.findViewById(R.id.tv_tglPemesanan);
            tv_tglPengiriman = itemView.findViewById(R.id.tv_tglPengiriman);
            tv_total = itemView.findViewById(R.id.tv_total);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_noHp = itemView.findViewById(R.id.tv_noHp);
            tv_alamatPengiriman = itemView.findViewById(R.id.tv_alamatPengiriman);
            tv_typePengambilan = itemView.findViewById(R.id.tv_typePengambilan);
            tv_biayaJasa = itemView.findViewById(R.id.tv_biayaJasa);
            tv_alasan = itemView.findViewById(R.id.tv_alasanUser);
            btn_detail = itemView.findViewById(R.id.btn_detail);

        }
    }
}

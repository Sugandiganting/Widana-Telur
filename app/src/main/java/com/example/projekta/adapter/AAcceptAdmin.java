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

import com.example.projekta.DetailAcceptAdmin;
import com.example.projekta.R;
import com.example.projekta.model.MPandingAdmin;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AAcceptAdmin extends RecyclerView.Adapter<AAcceptAdmin.ViewHolder> {

    private Context context;
    private List<MPandingAdmin> pandingAdminList;
    Locale localeID = new Locale("in", "ID");
    String id_transaksi;
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public AAcceptAdmin(Context context, List<MPandingAdmin> pandingAdminList) {
        this.context = context;
        this.pandingAdminList = pandingAdminList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_accept_admin, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        if (pandingAdminList.get(i).getNama_kurir()== null){
            holder.tv_kurir.setText("Pick Up");
        }else {
            holder.tv_kurir.setText(pandingAdminList.get(i).getNama_kurir());
        }
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
                String biaya_jasa = pandingAdminList.get(i).getBiaya_jasa();
                String total_transaksi = pandingAdminList.get(i).getTotal();
                String no_transaksi = pandingAdminList.get(i).getNo_transaksi();
                String nama_user = pandingAdminList.get(i).getCustomer_name();
                String nama_kurir = pandingAdminList.get(i).getNama_kurir();
                String tanggal_pemesanan = pandingAdminList.get(i).getTanggal_pemesanan();
                Intent intent = new Intent(context, DetailAcceptAdmin.class);
                intent.putExtra("id", id_transaksi);
                intent.putExtra("biaya_jasa",biaya_jasa);
                intent.putExtra("total_transaksi",total_transaksi);
                intent.putExtra("no_transaksi",no_transaksi);
                intent.putExtra("nama_user",nama_user);
                intent.putExtra("nama_kurir",nama_kurir);
                intent.putExtra("tanggal_pemesanan",tanggal_pemesanan);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pandingAdminList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_noTransaksi, tv_tglPemesanan, tv_tglPengiriman, tv_total, tv_user, tv_noHp, tv_alamatPengiriman, tv_typePengambilan, tv_biayaJasa,tv_kurir;
        private Button btn_detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_noTransaksi = itemView.findViewById(R.id.tv_noTransaksiAccept);
            tv_tglPemesanan = itemView.findViewById(R.id.tv_tglPemesananAccept);
            tv_tglPengiriman = itemView.findViewById(R.id.tv_tglPengirimanAccept);
            tv_total = itemView.findViewById(R.id.tv_totalAccept);
            tv_user = itemView.findViewById(R.id.tv_userAccept);
            tv_noHp = itemView.findViewById(R.id.tv_noHpAccept);
            tv_alamatPengiriman = itemView.findViewById(R.id.tv_alamatPengirimanAccept);
            tv_typePengambilan = itemView.findViewById(R.id.tv_typePengambilanAccept);
            tv_biayaJasa = itemView.findViewById(R.id.tv_biayaJasaAccept);
            tv_kurir = itemView.findViewById(R.id.tv_kurirAccept);
            btn_detail = itemView.findViewById(R.id.btn_detailAccept);
        }
    }
}

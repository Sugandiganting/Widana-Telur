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

public class ASuksesAdmin extends RecyclerView.Adapter<ASuksesAdmin.ViewHolder> {

    private Context context;
    private List<MPandingAdmin> pandingAdminList;
    Locale localeID = new Locale("in", "ID");
    String id_transaksi, total_transaksi,biaya_jasa;
    int tot_pembayaran,dp,sisa;
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public ASuksesAdmin(Context context, List<MPandingAdmin> pandingAdminList){
        this.context = context;
        this.pandingAdminList = pandingAdminList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_sukses_admin, viewGroup, false);
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

        if (pandingAdminList.get(i).getType_pengambilan().equals("2")){
            holder.tv_textPengambilan.setText("Tgl. Penagihan");
            holder.tv_alamat.setText("Almt. Pemesan");
            holder.tv_tanggalPenagihan.setText("-");
            holder.tv_uangDp.setText("-");
            holder.tv_sisaPembayaran.setText("-");
            holder.tv_kurir.setText("Pick Up");
            if (pandingAdminList.get(i).getType_payment().equals("1")){
                holder.tv_tipePembayaran.setText("Kredit");
            }else {
                holder.tv_tipePembayaran.setText("Cash");
            }
        }else {
            holder.tv_tanggalPenagihan.setText(""+pandingAdminList.get(i).getPayment_term());
            holder.tv_uangDp.setText(""+formatRupiah.format(Double.valueOf(pandingAdminList.get(i).getAmount())));
            holder.tv_sisaPembayaran.setText(""+formatRupiah.format(Double.valueOf(sisa)));
            holder.tv_kurir.setText(""+pandingAdminList.get(i).getNama_kurir());
            if (pandingAdminList.get(i).getType_payment().equals("1")){
                holder.tv_tipePembayaran.setText("Kredit");
            }else {
                holder.tv_tipePembayaran.setText("Cash");
            }
        }
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
        private TextView tv_kurir,tv_noTransaksi, tv_tglPengiriman, tv_total, tv_user, tv_noHp, tv_alamatPengiriman, tv_biayaJasa,tv_uangDp,tv_sisaPembayaran,tv_tanggalPenagihan,
                tv_textPengambilan,tv_tipePembayaran,tv_alamat;
        private Button btn_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_tipePembayaran = itemView.findViewById(R.id.tv_tipePembayaranSA);
            tv_alamat = itemView.findViewById(R.id.tv_textAlamatSA);
            tv_noTransaksi = itemView.findViewById(R.id.tv_noTransaksiSA);
            tv_tglPengiriman = itemView.findViewById(R.id.tv_tglPengirimanSA);
            tv_total = itemView.findViewById(R.id.tv_totalSA);
            tv_user = itemView.findViewById(R.id.tv_userSA);
            tv_noHp = itemView.findViewById(R.id.tv_noHpSA);
            tv_alamatPengiriman = itemView.findViewById(R.id.tv_alamatPengirimanSA);
            tv_biayaJasa = itemView.findViewById(R.id.tv_biayaJasaSA);
            tv_uangDp= itemView.findViewById(R.id.tv_uangDpSA);
            tv_sisaPembayaran = itemView.findViewById(R.id.tv_sisaPembayaranSA);
            tv_tanggalPenagihan = itemView.findViewById(R.id.tv_batasPembayaranSA);
            tv_kurir = itemView.findViewById(R.id.tv_kurirMengantarSA);
            tv_textPengambilan = itemView.findViewById(R.id.tv_textPengambilanSA);
            btn_detail = itemView.findViewById(R.id.btn_detailSA);
        }
    }
}

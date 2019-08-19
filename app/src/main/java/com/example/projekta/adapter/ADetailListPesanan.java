package com.example.projekta.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projekta.R;
import com.example.projekta.model.MDetailListPesanan;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ADetailListPesanan extends RecyclerView.Adapter<ADetailListPesanan.ViewHolder> {

    private Context context;
    private List<MDetailListPesanan> detailListPesanan;
    private int qty=0;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public ADetailListPesanan(Context context, List<MDetailListPesanan> detailListPesanan) {
        this.context = context;
        this.detailListPesanan = detailListPesanan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater =LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_detail_pesanan,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.et_ukuranTelur.setText(detailListPesanan.get(i).getUkuran_telur());
        if (detailListPesanan.get(i).getQty_order()!=null){
            qty = (Integer.parseInt(detailListPesanan.get(i).getQty_order()))/30;
            holder.et_qtyOrder.setText(""+qty);
        }
        holder.et_hargaJual.setText( formatRupiah.format(Double.valueOf(detailListPesanan.get(i).getHarga_jual())));
        holder.et_totalHarga.setText(formatRupiah.format(Double.valueOf(detailListPesanan.get(i).getTotal())));
    }

    @Override
    public int getItemCount() {
        return detailListPesanan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView et_ukuranTelur,et_qtyOrder,et_hargaJual,et_totalHarga;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            et_ukuranTelur = itemView.findViewById(R.id.et_ukuranTelur);
            et_qtyOrder = itemView.findViewById(R.id.et_qtyOrder);
            et_hargaJual = itemView.findViewById(R.id.et_hargaJual);
            et_totalHarga = itemView.findViewById(R.id.et_totalHarga);
        }
    }
}

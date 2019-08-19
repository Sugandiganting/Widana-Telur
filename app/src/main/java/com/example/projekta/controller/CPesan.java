package com.example.projekta.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekta.R;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.database.Pengaturan;
import com.example.projekta.database.User;
import com.example.projekta.model.MResponse;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CPesan implements DatePickerDialog.OnDateSetListener {
    Context context;
    private MaterialEditText edt_telur_besar, edt_telur_sedang, edt_alamat_pengiriman, edt_noHp, edt_tanggal_pengiriman;
    private TextView edt_total_pembayaran, tv_total_harga_telur, tv_total_ongkir, tv_total_transaksi;
    private Button btn_pesan, btn_confirm;
    private ImageButton btn_tanggal;
    private String hTelurSedang, hTelurBesar, id_Tb, id_Ts, sdate;
    private int hargaTS_butir,hargaTB_butir;
    private MaterialSpinner cb_pengambilan;
    private AlertDialog dialog, alertForm, alertConfirm;
    Api_Rest apiRest = Common.getApi();
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    User user = new User();
    Pengaturan pengaturan = Pengaturan.findById(Pengaturan.class, (long) 1);

    public CPesan(Context context) {
        this.context = context;
    }

    public void telur(String TelurSedang, String TelurBesar, String id_telurBesar, String id_telurSedang) {
        //harga per tray
        hTelurSedang = TelurSedang;
        hTelurBesar = TelurBesar;
        //harga per butir
        hargaTS_butir = (Integer.parseInt(hTelurSedang)/30);
        hargaTB_butir = (Integer.parseInt(hTelurBesar)/30);

        id_Tb = id_telurBesar;
        id_Ts = id_telurSedang;

        user = User.findById(User.class, (long) 1);

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_neworder, null);

        edt_telur_besar = layout.findViewById(R.id.edt_telur_besar);
        edt_telur_sedang = layout.findViewById(R.id.edt_telur_sedang);
        edt_telur_besar.addTextChangedListener(tot_bayarTelurBesar);
        edt_telur_sedang.addTextChangedListener(tot_bayarTelurSedang);

        edt_alamat_pengiriman = layout.findViewById(R.id.edt_alamat_pengiriman);
        edt_alamat_pengiriman.setText(user.getAlamat());

        edt_noHp = layout.findViewById(R.id.edt_nohp);
        edt_noHp.setText(user.getNo_hp());

        edt_tanggal_pengiriman = layout.findViewById(R.id.edt_tanggal_pengiriman);
        edt_total_pembayaran = layout.findViewById(R.id.edt_total_pembayaran);
        btn_tanggal = layout.findViewById(R.id.btn_tanggal);
        btn_pesan = layout.findViewById(R.id.btn_pesan);


        cb_pengambilan = layout.findViewById(R.id.cb_pengambilan);
        cb_pengambilan.setItems("Delivery", "Pick Up");
        alertBuilder.setView(layout);
        alertBuilder.setCancelable(true);
        alertForm = alertBuilder.create();
        alertForm.show();

        btn_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTanggal();
            }
        });

        btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPesanan();
            }
        });

    }

    //Alert Confirm Pesanan
    private void confirmPesanan() {
        if (!validasi()) {
            Toasty.error(context, "Pemesanan Gagal").show();
        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View layout = inflater.inflate(R.layout.alert_confirm_pesanan, null);

            tv_total_harga_telur = layout.findViewById(R.id.tv_total_harga_telur);
            tv_total_harga_telur.setText(edt_total_pembayaran.getText());

            tv_total_ongkir = layout.findViewById(R.id.tv_total_ongkir);
            tv_total_ongkir.setText("" + formatRupiah.format(Double.valueOf(TotaOngkir())));

            tv_total_transaksi = layout.findViewById(R.id.tv_total_transaksi);
            tv_total_transaksi.setText("" + formatRupiah.format(Double.valueOf(TotalPembayaran())));

            btn_confirm = layout.findViewById(R.id.btn_confirm);

            alertBuilder.setView(layout);
            alertConfirm = alertBuilder.create();
            alertConfirm.show();

            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertPesanan();
                }
            });
        }

    }

    //Insert Pesanan
    private void insertPesanan() {
        dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Sedang Insert....");
        dialog.setCancelable(false);
        dialog.show();

        String spengambilan;
        String qtyOrderTB;
        String qtyOrderTS;
        String stanggal_pengiriman = edt_tanggal_pengiriman.getText().toString();
        String salamat_pengiriman = edt_alamat_pengiriman.getText().toString();
        String sno_hp = edt_noHp.getText().toString();
        String sbiaya_jasa = String.valueOf(TotaOngkir());
        String sprefix_number = ("" + pengaturan.getInvoice() + "" + sdate);
        String suser_id = "" + user.getId_user();
        if (cb_pengambilan.getText().equals("Delivery")) {
            spengambilan = "1";
        } else {
            spengambilan = "2";
        }
        String stelurS_id = id_Ts;
        String stelurB_id = id_Tb;
        String shargaTB = String.valueOf(hargaTB_butir);
        String shargaTS = String.valueOf(hargaTS_butir);
        //konfersi inputan menjadi per butir
        String qtyTb = edt_telur_besar.getText().toString() ;
        String qtyTs = edt_telur_sedang.getText().toString();
        //mengecek inputan
        if (qtyTb.equals("")){
            qtyOrderTB = "0";
        }else {
            qtyOrderTB = String.valueOf(Integer.parseInt(qtyTb)*30);
        }
        if (qtyTs.equals("")){
            qtyOrderTS = "0";
        }else {
            qtyOrderTS = String.valueOf(Integer.parseInt(qtyTs)*30);
        }

        String sinvoice = pengaturan.getInvoice();

        apiRest.sendDataPemesanan(stanggal_pengiriman, salamat_pengiriman, sno_hp, sbiaya_jasa, sprefix_number, suser_id, spengambilan, stelurS_id, stelurB_id, shargaTS, shargaTB, qtyOrderTS, qtyOrderTB, sinvoice)
                .enqueue(new Callback<MResponse>() {
                    @Override
                    public void onResponse(Call<MResponse> call, Response<MResponse> response) {
                        Log.d("RETRO", "Response : " + response.body().toString());
                        int kode = response.body().getKode();

                        if (kode == 1) {
                            Toasty.success(context, "Telur Berhasil Dipesan", Toast.LENGTH_SHORT).show();
                            alertConfirm.dismiss();
                            alertForm.dismiss();
                            dialog.dismiss();
                        } else {
                            Toasty.error(context, "Gagal Memesan", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<MResponse> call, Throwable t) {
                        dialog.hide();
                        Log.d("RETRO", "Failure : " + "Gagal Mengirim Request");
                        dialog.dismiss();
                    }
                });
    }

    // Validasi Inputan User
    private boolean validasi() {
        boolean valid = true;
        Double totalhargaTelur = Double.valueOf(TotalP1());
        if (edt_telur_besar.length() == 0 && edt_telur_sedang.length() == 0) {
            edt_telur_sedang.setError("Jumlah pesanan tidak boleh kosong");
            valid = false;
        }
        if (edt_alamat_pengiriman.length() == 0) {
            edt_alamat_pengiriman.setError("Tidak boleh kosong");
            valid = false;
        }
        if (edt_noHp.length() == 0) {
            edt_noHp.setError("Tidak boleh kosong");
            valid = false;
        }
        if (edt_tanggal_pengiriman.length() == 0) {
            edt_tanggal_pengiriman.setError("Tidak boleh kosong");
            valid = false;
        } else if (totalhargaTelur < 3000000 && cb_pengambilan.getText().equals("Delivery")) {
            Toasty.error(context, "Minimal Rp.3.000.000 Untuk Delivery", Toast.LENGTH_LONG).show();
            valid = false;
        }
        return valid;
    }

    private void setTanggal() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "-" + (month+1) + "-" + year;
        edt_tanggal_pengiriman.setText(date);

        //get this mounth and year
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMount = calendar.get(Calendar.MONTH);
        sdate = "/" + (thisMount + 1) + "" + thisYear;
    }

    private TextWatcher tot_bayarTelurBesar = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            edt_total_pembayaran.setText("" + formatRupiah.format(Double.valueOf(TotalP())));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher tot_bayarTelurSedang = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            edt_total_pembayaran.setText("" + formatRupiah.format(Double.valueOf(TotalP1())));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    // Menentukan Harga Telur Besar
    private String TotalP() {
        int totalB = 0;
        String jumlahTs = edt_telur_sedang.getText().toString();
        String jumlahTb = edt_telur_besar.getText().toString();

        if (jumlahTb.equals("") && jumlahTs.equals("")) {
            return Integer.toString(0);

        } else {
            if (edt_telur_besar.length() > 0) {
                int finalJumlahTb = Integer.parseInt(jumlahTb);
                if (edt_telur_sedang.length() > 0) {
                    int finalJumlahTs = Integer.parseInt(jumlahTs);
                    totalB = (finalJumlahTb * (Integer.parseInt(hTelurBesar))) + (finalJumlahTs * (Integer.parseInt(hTelurSedang)));
                } else {
                    totalB = finalJumlahTb * (Integer.parseInt(hTelurBesar));
                }
                return String.valueOf(totalB);
            } else {
                if (edt_telur_sedang.length() > 0) {
                    int inputJumlahTs = Integer.parseInt(jumlahTs);
                    totalB = inputJumlahTs * (Integer.parseInt(hTelurSedang));
                    return String.valueOf(totalB);
                } else {
                    return String.valueOf(totalB);
                }
            }
        }
    }

    // Menentukan Harga Telur Sedang
    private int TotalP1() {
        int total = 0;
        int inputTelurSedang = 0;
        int hargaTelurSedang = Integer.parseInt(hTelurSedang);
        int hargaTelurBesar = Integer.parseInt(hTelurBesar);
        if (edt_telur_besar.length() > 0) {
            int inputTelurBesar = Integer.parseInt(edt_telur_besar.getText().toString());
            if (edt_telur_sedang.length() > 0) {
                inputTelurSedang = Integer.parseInt(edt_telur_sedang.getText().toString());
                total = (inputTelurSedang * hargaTelurSedang) + (inputTelurBesar * hargaTelurBesar);
            } else {
                total = (inputTelurBesar * hargaTelurBesar);
            }
        } else {
            if (edt_telur_sedang.length() > 0) {
                inputTelurSedang = Integer.parseInt(edt_telur_sedang.getText().toString());
                total = (inputTelurSedang * hargaTelurSedang);
            } else {
                total = 0;
            }

        }
        return total;
    }

    //Yang dimaksud total ongkir adalah total jasa
    private double TotaOngkir() {
        double total_ongkir=0;
        double harga_telur = Double.valueOf(TotalP1());
        if (cb_pengambilan.getText().equals("Delivery")){
            //biaya jasa jika di krim di kali 10%
            total_ongkir = harga_telur * 0.1;
        }else {
            if (harga_telur <= 100000) {
                total_ongkir = 0;
            }else {
                //biaya jasa jika di krim di kali 5%
                total_ongkir = harga_telur * 0.05;
            }
        }

        return total_ongkir;
    }

    //Menghitung Total Pembayaran
    private double TotalPembayaran() {
        double totalPembayaran;
        Double hargaTelur = Double.valueOf(TotalP1());
        Double totalOngkir = Double.valueOf(TotaOngkir());
        totalPembayaran = hargaTelur + totalOngkir;
        return totalPembayaran;
    }
}

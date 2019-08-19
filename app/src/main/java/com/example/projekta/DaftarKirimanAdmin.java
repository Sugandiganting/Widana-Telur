package com.example.projekta;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekta.adapter.AKirimAdmin;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.model.MListUser;
import com.example.projekta.model.MPandingAdmin;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarKirimanAdmin extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Api_Rest apiRest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    //Setting Adapter
    private AKirimAdmin aPandingAdmin;
    private MaterialSpinner lstCustomer;
    private Button btn_search, btn_refresh;
    private TextView tv_tanggal;
    List<String> namaCustomer = new ArrayList<>();
    List<String> lstIdUser = new ArrayList<>();
    private ImageButton btn_tanggal;
    private String status ="1",type_pengambilan="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_kiriman_admin);

        apiRest = Common.getApi();
        recyclerView = findViewById(R.id.daftarKirimanAdmin_rv);
        lstCustomer = findViewById(R.id.cb_namaUserKA);
        btn_tanggal = findViewById(R.id.btn_tanggalPandingKA);
        btn_search = findViewById(R.id.btn_cariPandingKA);
        btn_refresh = findViewById(R.id.btn_refreshPandingKA);
        tv_tanggal = findViewById(R.id.tv_tanggalPandingKA);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //set Tanggal
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        btn_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        //set nama user yang terdaftar
        apiRest.listuser().enqueue(new Callback<List<MListUser>>() {
            @Override
            public void onResponse(Call<List<MListUser>> call, Response<List<MListUser>> response) {
                List<MListUser> listUsers = response.body();
                for (MListUser listUser : listUsers) {
                    if (listUser.getUser_level().equals("1")) {
                        namaCustomer.add(listUser.getNama());
                        lstIdUser.add(listUser.getId_user());
                    }
                }
                lstCustomer.setItems(namaCustomer);
            }

            @Override
            public void onFailure(Call<List<MListUser>> call, Throwable t) {
                Toasty.error(DaftarKirimanAdmin.this, "Gagal menampilkan daftar user", Toast.LENGTH_SHORT).show();
            }
        });

        loaddatakirim();

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaddatakirim();
                lstCustomer.setText("");
                tv_tanggal.setText("");
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    private void loaddatakirim() {
        apiRest.listkirimadmin(type_pengambilan).enqueue(new Callback<List<MPandingAdmin>>() {
            @Override
            public void onResponse(Call<List<MPandingAdmin>> call, Response<List<MPandingAdmin>> response) {
                if (response.body()!=null){
                    List<MPandingAdmin> mPandingAdmins = response.body();
                    aPandingAdmin = new AKirimAdmin(DaftarKirimanAdmin.this, mPandingAdmins);
                    recyclerView.setAdapter(aPandingAdmin);
                }else {
                    Toasty.info(DaftarKirimanAdmin.this,"Tidak Ada Kiriman",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MPandingAdmin>> call, Throwable t) {
                Toasty.error(DaftarKirimanAdmin.this, "Tidak Ada Kiriman", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "-" + (month + 1) + "-" + year;
        tv_tanggal.setText(date);
    }

    private boolean validasi(){
        boolean valid = true;
        if (tv_tanggal.getText().equals("") && lstCustomer .getText().equals("Search by kurir")){
            valid = false;
            Toasty.error(DaftarKirimanAdmin.this,"Harap isi salah satu menu pencarian",Toast.LENGTH_SHORT).show();
        }if (tv_tanggal.getText().equals("") && lstCustomer .getText().equals("")){
            valid = false;
            Toasty.error(DaftarKirimanAdmin.this,"Harap isi salah satu menu pencarian",Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    private void search(){
        if (validasi()==true){
            String sid_kurir;
            String stanggal;
            if (lstCustomer.getText().equals("")||lstCustomer .getText().equals("Search by kurir")){
                sid_kurir = "";
            }else {
                sid_kurir = lstIdUser.get(lstCustomer.getSelectedIndex());
            }
            if (tv_tanggal.getText().equals("")){
                stanggal="";
            }else {
                stanggal = tv_tanggal.getText().toString();
            }
            apiRest.searchkirimanadmin(stanggal,sid_kurir,status,type_pengambilan).enqueue(new Callback<List<MPandingAdmin>>() {
                @Override
                public void onResponse(Call<List<MPandingAdmin>> call, Response<List<MPandingAdmin>> response) {
                    if (response.body()!=null){
                        List<MPandingAdmin> mPandingAdmins = response.body();
                        aPandingAdmin = new AKirimAdmin(DaftarKirimanAdmin.this, mPandingAdmins);
                        recyclerView.setAdapter(aPandingAdmin);
                    }else {
                        Toasty.info(DaftarKirimanAdmin.this,"Data Tidak Ditemukan",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<MPandingAdmin>> call, Throwable t) {
                    final SweetAlertDialog pDialog = new SweetAlertDialog(DaftarKirimanAdmin.this, SweetAlertDialog.WARNING_TYPE);
                    pDialog.setTitle("Data Tidak ditemukan");
                    pDialog.setCancelable(false);
                    pDialog.setConfirmText("YES");

                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            pDialog.dismiss();
                            loaddatakirim();
                        }
                    });
                    pDialog.show();
                }
            });
        }
    }

}


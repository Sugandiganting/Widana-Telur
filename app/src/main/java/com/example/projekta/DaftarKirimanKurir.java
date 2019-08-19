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

import com.example.projekta.adapter.AKirimKurir;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.database.User;
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

public class DaftarKirimanKurir extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Api_Rest apiRest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    //Setting Adapter
    private AKirimKurir aPandingAdmin;
    private MaterialSpinner lstCustomer;
    private Button btn_search, btn_refresh;
    private TextView tv_tanggal;
    List<String> namaCustomer = new ArrayList<>();
    List<String> lstIdUser = new ArrayList<>();
    private ImageButton btn_tanggal;
    private String status="1",id_kurir;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_kiriman_kurir);

        apiRest = Common.getApi();
        recyclerView = findViewById(R.id.daftarKirimanKurir_rv);
        lstCustomer = findViewById(R.id.cb_namaUserK);
        btn_tanggal = findViewById(R.id.btn_tanggalPandingK);
        btn_search = findViewById(R.id.btn_cariPandingK);
        btn_refresh = findViewById(R.id.btn_refreshPandingK);
        tv_tanggal = findViewById(R.id.tv_tanggalPandingK);
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
                    if (listUser.getUser_level().equals("0")) {
                        namaCustomer.add(listUser.getUsername());
                        lstIdUser.add(listUser.getId_user());
                    }
                }
                lstCustomer.setItems(namaCustomer);
            }

            @Override
            public void onFailure(Call<List<MListUser>> call, Throwable t) {
                Toasty.error(DaftarKirimanKurir.this, "Gagal menampilkan daftar user", Toast.LENGTH_SHORT).show();
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
                validasi();
            }
        });

    }

    private void loaddatakirim() {
        user = User.findById(User.class, (long) 1);
        id_kurir = user.getId_user();
        apiRest.listkirimankurir(id_kurir).enqueue(new Callback<List<MPandingAdmin>>() {
            @Override
            public void onResponse(Call<List<MPandingAdmin>> call, Response<List<MPandingAdmin>> response) {
                if (response.body()!=null){
                    List<MPandingAdmin> mPandingAdmins = response.body();
                    aPandingAdmin = new AKirimKurir(DaftarKirimanKurir.this, mPandingAdmins);
                    recyclerView.setAdapter(aPandingAdmin);
                }else {
                    Toasty.info(DaftarKirimanKurir.this,"Tidak Ada Kiriman",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MPandingAdmin>> call, Throwable t) {
                Toasty.error(DaftarKirimanKurir.this, "Tidak Ada Kiriman", Toast.LENGTH_SHORT).show();
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
        if (tv_tanggal.getText().equals("") && lstCustomer .getText().equals("Search by name")){
            valid = false;
            Toasty.error(DaftarKirimanKurir.this,"Harap isi salah satu menu pencarian",Toast.LENGTH_SHORT).show();
        }if (tv_tanggal.getText().equals("") && lstCustomer .getText().equals("")){
            valid = false;
            Toasty.error(DaftarKirimanKurir.this,"Harap isi salah satu menu pencarian",Toast.LENGTH_SHORT).show();
        }else {
            search();
        }
        return valid;
    }
    private void search(){
        String scustomer_id;
        String stanggal;
        if (lstCustomer.getText().equals("")||lstCustomer .getText().equals("Search by name")){
            scustomer_id = "";
        }else {
            scustomer_id = lstIdUser.get(lstCustomer.getSelectedIndex());
        }
        if (tv_tanggal.getText().equals("")){
            stanggal="";
        }else {
            stanggal = tv_tanggal.getText().toString();
        }
        //harus diganti pada apirest
        apiRest.searchkirimankurir(stanggal,scustomer_id,status,id_kurir).enqueue(new Callback<List<MPandingAdmin>>() {
            @Override
            public void onResponse(Call<List<MPandingAdmin>> call, Response<List<MPandingAdmin>> response) {
                if (response.body()!=null){
                    List<MPandingAdmin> mPandingAdmins = response.body();
                    aPandingAdmin = new AKirimKurir(DaftarKirimanKurir.this, mPandingAdmins);
                    aPandingAdmin.notifyDataSetChanged();
                    recyclerView.setAdapter(aPandingAdmin);
                }else {
                    Toasty.info(DaftarKirimanKurir.this,"Data Tidak Ditemukan",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MPandingAdmin>> call, Throwable t) {

                final SweetAlertDialog pDialog = new SweetAlertDialog(DaftarKirimanKurir.this, SweetAlertDialog.WARNING_TYPE);
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

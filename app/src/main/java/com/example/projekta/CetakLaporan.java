package com.example.projekta;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekta.database.Pengaturan;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class CetakLaporan extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final int PERMISSION_STORAGE_CODE = 1000 ;
    TextView tv_tanggalSampai,tv_tanggalDari;
    ImageButton btn_tanggalSampai,btn_tanggalDari;
    Button btn_cetakLaporan;
    private String date;
    private String status = "";
    Pengaturan pengaturan = Pengaturan.findById(Pengaturan.class, (long) 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetak_laporan);

        tv_tanggalDari = findViewById(R.id.tv_tanggalDari);
        tv_tanggalSampai = findViewById(R.id.tv_tanggalSampai);
        btn_tanggalSampai = findViewById(R.id.btn_tanggalSampai);
        btn_tanggalDari = findViewById(R.id.btn_tanggalDari);
        btn_cetakLaporan = findViewById(R.id.btn_cetak);

        //set Tanggal
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        btn_tanggalDari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "dari";
                datePickerDialog.show();
            }
        });

        btn_tanggalSampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status="hingga";
                datePickerDialog.show();
            }
        });

        btn_cetakLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()==true){
                    if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                PackageManager.PERMISSION_DENIED){
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions, PERMISSION_STORAGE_CODE);
                        }
                        else {
                            startDownloading();
                        }
                    }
                    else {
                        startDownloading();
                    }
                }
            }
        });
    }

    private void startDownloading() {
        String mulai = tv_tanggalDari.getText().toString();
        String sampai = tv_tanggalSampai.getText().toString();
        String url = "http://"+pengaturan.getIp_address()+"/backend_TA/cetaklaporan.php?mulai="+mulai+"&sampai="+sampai;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading file");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"Laporan Penjualan.xls");

        DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)  {
        switch (requestCode){
            case PERMISSION_STORAGE_CODE:{
                if (grantResults.length > 0 && grantResults[0]==
                    PackageManager.PERMISSION_GRANTED){
                    startDownloading();
                }else {
                    Toasty.error(CetakLaporan.this,"Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = year + "-" + (month + 1) + "-" + dayOfMonth;
        if (status.equals("dari")){
            tv_tanggalDari.setText(date);
        }else {
            tv_tanggalSampai.setText(date);
        }
    }
    private boolean validasi(){
        boolean valid= true;
        if (tv_tanggalSampai.getText().equals("")&& tv_tanggalDari.getText().equals("")){
            Toasty.error(CetakLaporan.this,"Tolong lengkapi tanggal");
            valid=false;
        }
        return valid;
    }
}

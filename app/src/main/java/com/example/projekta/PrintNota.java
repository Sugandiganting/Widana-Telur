package com.example.projekta;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekta.database.Pengaturan;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.logging.LogRecord;

import es.dmoral.toasty.Toasty;

public class PrintNota extends AppCompatActivity {
    private Button btnConnect,btnDisconnect,btnPrint;
    private TextView tv_lbl, tv_printerName;
    private String id_transaksi,biaya_jasa,total_transaksi,hargaTs,hargaTb,qtyTs,qtyTb,totHargaTS,totHargaTB,nama_toko,alamat_toko,no_transaksi,nama_user,nama_kurir,tanggal_pemesanan;
    Pengaturan pengaturan = new Pengaturan();
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWoker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_nota);
        Pengaturan pengaturan = Pengaturan.findById(Pengaturan.class, (long)1);
        nama_toko = pengaturan.getNama_toko();
        alamat_toko = pengaturan.getAlamat_toko();
        id_transaksi = getIntent().getStringExtra("id");
        biaya_jasa = getIntent().getStringExtra("biaya_jasa");
        total_transaksi = getIntent().getStringExtra("total_transaksi");
        hargaTs = getIntent().getStringExtra("hargaTs");
        hargaTb = getIntent().getStringExtra("hargaTb");
        qtyTs = getIntent().getStringExtra("qtyTs");
        qtyTb = getIntent().getStringExtra("qtyTb");
        totHargaTS = getIntent().getStringExtra("totHargaTS");
        totHargaTB = getIntent().getStringExtra("totHargaTB");
        no_transaksi = getIntent().getStringExtra("no_transaksi");
        nama_user = getIntent().getStringExtra("nama_user");
        nama_kurir = getIntent().getStringExtra("nama_kurir");
        tanggal_pemesanan = getIntent().getStringExtra("tanggal_pemesanan");


        btnConnect = findViewById(R.id.btn_Connect);
        btnDisconnect = findViewById(R.id.btn_Disconnect);
        btnPrint = findViewById(R.id.btn_Print);
        tv_lbl = findViewById(R.id.lbl);
        tv_printerName = findViewById(R.id.lblPrinterName);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FindBluetoothDevice();
                    openBluetoothPrinter();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    disconnectBT();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    printData();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    public void FindBluetoothDevice(){
        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter==null){
                tv_printerName.setText("No Bluetooth adapter found");
            }
            if (bluetoothAdapter.isEnabled()){
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT,0);
            }
            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
            if (pairedDevice.size()>0){
                for (BluetoothDevice pairedDev:pairedDevice){
                    if (pairedDev.getName().equals("Qsprinter")){
                        bluetoothDevice=pairedDev;
                        tv_lbl.setText("Bluetooth Printer Attached: "+pairedDev.getName());
                        break;
                    }
                }
            }
            tv_printerName.setText("Bluetooth Printer Attached");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //open bluetooth printer
    public void openBluetoothPrinter() throws IOException{
        try{
            //standar uuid from string//
            UUID uuidStrng = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuidStrng);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();

            beginListenData();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void beginListenData(){
        try {
            final Handler handler = new Handler();
            final byte delimiter=10;
            stopWoker = false;
            readBufferPosition=0;
            readBuffer = new byte[1024];

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWoker){
                        try{
                            int byteAvailable = inputStream.available();
                            if (byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for (int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    if (b==delimiter){
                                        byte[] encodeByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodeByte,0,
                                                encodeByte.length
                                        );
                                        final String data = new String(encodeByte,"US-ASCII");
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                tv_printerName.setText(data);
                                            }
                                        });
                                    }else {
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch (Exception ex){
                            stopWoker=true;
                        }
                    }
                }
            });
            thread.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Printing text to Bluetooth Printer
    public void printData() throws IOException{
        try {
            String msg = "";
            msg=    "          "+nama_toko+"  \n"+
                    "      "+alamat_toko+"\n";
            msg=msg+"--------------------------------\n";
            msg=msg+"Nama Customer  : "+nama_user+"\n";
            msg=msg+"No. Transaksi  : "+no_transaksi+"\n";
            msg=msg+"Tgl. Pemesanan : "+tanggal_pemesanan+"\n";
            if (nama_kurir != null){
                msg=msg+"Kurir          : "+nama_kurir+"\n";
            }else {
                msg=msg+"Kurir          : Pick Up \n";
            }
            msg=msg+"--------------------------------\n";
            if (qtyTs != null && qtyTb !=null){
                msg=msg+"Qty Telur Sedang  "+formatRupiah.format(Double.valueOf(hargaTs))+"\n";
                msg=msg+"  ("+qtyTs+")           "+formatRupiah.format(Double.valueOf(totHargaTS))+"\n";
                msg=msg+"Qty Telur Besar   "+formatRupiah.format(Double.valueOf(hargaTb))+"\n";
                msg=msg+"  ("+qtyTb+")           "+formatRupiah.format(Double.valueOf(totHargaTB))+"\n";
            }else if (qtyTs == null){
//                msg=msg+"Qty Telur Sedang  Rp.0 \n";
//                msg=msg+"  (0)           Rp.0 \n";
                msg=msg+"Qty Telur Besar   "+formatRupiah.format(Double.valueOf(hargaTb))+"\n";
                msg=msg+"  ("+qtyTb+")           "+formatRupiah.format(Double.valueOf(totHargaTB))+"\n";
            }else {
                msg=msg+"Qty Telur Sedang  "+formatRupiah.format(Double.valueOf(hargaTs))+"\n";
                msg=msg+"  ("+qtyTs+")           "+formatRupiah.format(Double.valueOf(totHargaTS))+"\n";
//                msg=msg+"Qty Telur Besar   Rp.0 \n";
//                msg=msg+"  (0)           Rp.0 \n";
            }
            msg=msg+"--------------------------------\n";
            msg=msg+"     Biaya Jasa : "+formatRupiah.format(Double.valueOf(biaya_jasa))+"\n";
            msg=msg+"Total Transaksi : "+formatRupiah.format(Double.valueOf(total_transaksi))+"\n";
            msg=msg+"--------------------------------\n";
            msg=msg+"          Terimakasih \n";
            msg=msg+"   Barang yang sudah di terima \n";
            msg=msg+"     Tidak bisa dikembalikan \n";
            msg=msg+"\n\n";
            outputStream.write(msg.getBytes());
            tv_printerName.setText("Printing text...");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //disconet printer
    public void disconnectBT() throws IOException{
        try {
            stopWoker=true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            tv_printerName.setText("Printer Disconnect");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

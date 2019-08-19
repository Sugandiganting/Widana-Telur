package com.example.projekta.api;

import com.example.projekta.model.MDataUser;
import com.example.projekta.model.MDetailListPesanan;
import com.example.projekta.model.MListPesanan;
import com.example.projekta.model.MListUser;
import com.example.projekta.model.MLogin;
import com.example.projekta.model.MPandingAdmin;
import com.example.projekta.model.MResponse;
import com.example.projekta.model.MTelur;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api_Rest {

    String read ="Read/";
    String create = "Create/";
    String update = "Update/";
    String delete = "Delete/";

    @FormUrlEncoded
    @POST(read + "login.php")
    Call<MLogin> login(@Field("username") String username,
                       @Field("password") String password);

    @GET(read + "telur.php")
    Call<List<MTelur>> telur();

    @GET(read +"pandingadmin.php")
    Call<List<MPandingAdmin>> listpanding();

    @FormUrlEncoded
    @POST(read +"listkirimadmin.php")
    Call<List<MPandingAdmin>> listkirimadmin(@Field("type_pengambilan") String type_pengambilan);

    @GET(read +"acceptadmin.php")
    Call<List<MPandingAdmin>> acceptpanding();

    @GET(read +"rejectadmin.php")
    Call<List<MPandingAdmin>> listreject();

    @GET(read +"listcreditadmin.php")
    Call<List<MPandingAdmin>> listcreditadmin();

    @GET(read +"listsuksesadmin.php")
    Call<List<MPandingAdmin>> listsuksesadmin();

    @FormUrlEncoded
    @POST(read + "searchcashkurir.php")
    Call<List<MPandingAdmin>> searchcashkurir(@Field("tanggal_pengiriman") String tanggal_pengiriman,
                                                     @Field("customer_id") String customer_id,
                                                     @Field("type_payment") String type_payment,
                                                     @Field("id_kurir") String id_kurir,
                                                     @Field("status") String status);
    @FormUrlEncoded
    @POST(read + "searchpandingadmin.php")
    Call<List<MPandingAdmin>> searchlistpandingadmin(@Field("tanggal_panding") String tanggal_panding,
                                                     @Field("customer_id") String customer_id,
                                                     @Field("status") String status);

    @FormUrlEncoded
    @POST(read + "searchpickupadmin.php")
    Call<List<MPandingAdmin>> searchpickupadmin(@Field("tanggal_pengiriman") String tanggal_pengiriman,
                                                @Field("customer_id") String customer_id,
                                                @Field("status") String status,
                                                @Field("type_pengambilan") String type_pengambilan);

    @FormUrlEncoded
    @POST(read + "searchkirimanadmin.php")
    Call<List<MPandingAdmin>> searchkirimanadmin(@Field("tanggal_pengiriman") String tanggal_pengiriman,
                                                 @Field("id_kurir") String id_kurir,
                                                 @Field("status") String status,
                                                 @Field("type_pengambilan") String type_pengambilan);

    @FormUrlEncoded
    @POST(read + "searchdaftarkirimkurir.php")
    Call<List<MPandingAdmin>> searchkirimankurir(@Field("tanggal_panding") String tanggal_panding,
                                                 @Field("customer_id") String customer_id,
                                                 @Field("status") String status,
                                                 @Field("id_kurir") String id_kurir);

    @FormUrlEncoded
    @POST(read + "searchuser.php")
    Call<List<MPandingAdmin>> searchuser(@Field("customer_id") String customer_id,
                                         @Field("status") String status,
                                         @Field("id_kurir") String id_kurir,
                                         @Field("tanggal_pengiriman") String tanggal_pengiriman);

    @FormUrlEncoded
    @POST(read + "searchkreditadmin.php")
    Call<List<MPandingAdmin>> searchkreditadmin(@Field("customer_id") String customer_id,
                                                @Field("tanggal_pengiriman") String tanggal_pengiriman);

    @FormUrlEncoded
    @POST(read + "searchsuksesadmin.php")
    Call<List<MPandingAdmin>> searchsuksesadmin(@Field("customer_id") String customer_id,
                                                @Field("tanggal_pengiriman") String tanggal_pengiriman);

    @GET(read +"listuser.php")
    Call<List<MListUser>> listuser();

    @FormUrlEncoded
    @POST(create+"insertpesanan.php")
    Call<MResponse> sendDataPemesanan(@Field("tanggal_pengiriman") String tanggal_pengiriman,
                                      @Field("alamat_pengiriman") String alamat_pengiriman,
                                      @Field("no_hp") String no_hp,
                                      @Field("biaya_jasa") String biaya_jasa,
                                      @Field("prefix_number") String prefix_number,
                                      @Field("customer_id") String customer_id,
                                      @Field("type_pengambilan") String type_pengambilan,
                                      @Field("id_telurS") String id_telurS,
                                      @Field("id_telurB") String id_telurB,
                                      @Field("harga_telurS") String harga_telurS,
                                      @Field("harga_telurB") String harga_telurB,
                                      @Field("qty_tS") String qty_tS,
                                      @Field("qty_tB") String qty_tB,
                                      @Field("invoice") String invoice);
    @FormUrlEncoded
    @POST(create + "insertpayment.php")
    Call<MResponse> insertPayment(@Field("id_transaksi") String id_transaksi,
                                        @Field("amount") String amount,
                                        @Field("type") String type,
                                        @Field("payment_term") String payment_term);

    @FormUrlEncoded
    @POST(create + "insertpaymentcredit.php")
    Call<MResponse> insertpaymentcredit(@Field("id_transaksi") String id_transaksi,
                                        @Field("total_pembayaran") String total_pembayaran);

    @FormUrlEncoded
    @POST(update + "updatepembayaranpickup.php")
    Call<MResponse> updatepembayaranpickup(@Field("id_transaksi") String id_transaksi,
                                           @Field("amount") String amount);

    @FormUrlEncoded
    @POST(read + "listpesanan.php")
    Call<List<MListPesanan>> listPesananUser(@Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST(read + "listdatauser.php")
    Call<List<MDataUser>> listdatauser(@Field("user_level") String user_level);

    @FormUrlEncoded
    @POST(read + "searchdatauser.php")
    Call<List<MDataUser>> searchdatauser(@Field("user_level") String user_level,
                                         @Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST(read +"listdetailpesanan.php")
    Call<List<MDetailListPesanan>> detailpesanan(@Field("id_transaksi") String id_transaksi);

    @FormUrlEncoded
    @POST(read +"listkirimkurir.php")
    Call<List<MPandingAdmin>> listkirimankurir(@Field("id_kurir") String id_kurir);

    @FormUrlEncoded
    @POST(read +"credituser.php")
    Call<List<MPandingAdmin>> listcredituser(@Field("id_user") String id_user,
                                             @Field("status") String status);

    @FormUrlEncoded
    @POST(read +"listcashkurir.php")
    Call<List<MPandingAdmin>> listcashkurir(@Field("id_kurir") String id_kurir);

    @FormUrlEncoded
    @POST(read +"listcreditkurir.php")
    Call<List<MPandingAdmin>> listcreditkurir(@Field("id_kurir") String id_kurir);

    @FormUrlEncoded
    @POST(update+"confirmpesanan.php")
    Call<MResponse> updatedataconfirm(@Field("id_transaksi") String id_transaksi,
                                                  @Field("customer_id") String customer_id,
                                                  @Field("qty_tb") String qty_tb,
                                                  @Field("qty_ts") String qty_ts,
                                                  @Field("harga_tb") String harga_tb,
                                                  @Field("harga_ts") String harga_ts,
                                                  @Field("total_tb") String total_tb,
                                                  @Field("total_ts") String total_ts,
                                                  @Field("biaya_jasa") String biaya_jasa,
                                                  @Field("admin_id") String admin_id);
    @FormUrlEncoded
    @POST(update+"batalpesanan.php")
    Call<MResponse> updatebatalpesanan(@Field("id_transaksi") String id_transaksi,
                                       @Field("alasan_batal") String alasan_batal,
                                       @Field("admin_id") String admin_id);

    @FormUrlEncoded
    @POST(update+"updateprofil.php")
    Call<MResponse> updateprofil(@Field("id_user") String id_user,
                                 @Field("nama") String nama,
                                 @Field("alamat") String alamat,
                                 @Field("no_hp") String no_hp,
                                 @Field("tanggal_lahir") String tanggal_lahir,
                                 @Field("username") String username,
                                 @Field("password") String password);

    @FormUrlEncoded
    @POST(create+"insertdatauser.php")
    Call<MResponse> insertdatauser(@Field("nama") String nama,
                                 @Field("alamat") String alamat,
                                 @Field("no_hp") String no_hp,
                                 @Field("tanggal_lahir") String tanggal_lahir,
                                 @Field("username") String username,
                                 @Field("password") String password,
                                   @Field("user_level") String user_level);

    @FormUrlEncoded
    @POST(update+"updatedatauser.php")
    Call<MResponse> updatedatauser(@Field("customer_id") String customer_id,
                                   @Field("nama") String nama,
                                   @Field("alamat") String alamat,
                                   @Field("no_hp") String no_hp,
                                   @Field("tanggal_lahir") String tanggal_lahir,
                                   @Field("username") String username,
                                   @Field("password") String password,
                                   @Field("user_level") String user_level);

    @FormUrlEncoded
    @POST(delete+"deleteuser.php")
    Call<MResponse> deleteuser(@Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST(update+"updatehargatelur.php")
    Call<MResponse> updatehargatelur(@Field("hargaTb") String hargaTb,
                                     @Field("hargaTs") String hargaTs,
                                     @Field("hargaBeliTs") String hargaBeliTs,
                                     @Field("hargaBeliTb") String hargaBeliTb);
}

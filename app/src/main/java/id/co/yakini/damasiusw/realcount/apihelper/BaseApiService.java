package id.co.yakini.damasiusw.realcount.apihelper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by angel on 1/6/2018.
 */

public interface BaseApiService {
    // Fungsi ini untuk memanggil API http://10.0.2.2/mahasiswa/login.php
    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> loginRequest(@Field("username") String username,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("setinput_suara.php")
    Call<ResponseBody> inputSuaraRequest(@Field("id_saksi") String id_saksi,
                                       @Field("jumsuara") String jumsuara,
                                       @Field("suarasah") String suarasah,
                                       @Field("suaratidaksah") String suaratidaksah,
                                       @Field("paslon1") String paslon1,
                                       @Field("paslon2") String paslon2);

    @FormUrlEncoded
    @POST("setinput_gugatan.php")
    Call<ResponseBody> inputGugatanRequest(@Field("id_saksi") String id_saksi,
                                         @Field("gugatan") String gugatan);

    @GET("getdata_tps.php")
    Call<ResponseBody> GetDataTps(
            @Query("id_saksi") String id_saksi
    );

    @GET("getinput_suara.php")
    Call<ResponseBody> GetInputSuara(
            @Query("id_saksi") String id_saksi
    );

    @GET("getinput_gugatan.php")
    Call<ResponseBody> GetInputGugatan(
            @Query("id_saksi") String id_saksi
    );

    @GET("getdata_saksi.php")
    Call<ResponseBody> GetDataSaksi(
            @Query("id_saksi") String id_saksi
    );

}

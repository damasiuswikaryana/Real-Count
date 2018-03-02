package id.co.yakini.damasiusw.realcount;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.yakini.damasiusw.realcount.apihelper.BaseApiService;
import id.co.yakini.damasiusw.realcount.apihelper.UtilsApi;
import id.co.yakini.damasiusw.realcount.sharedpreferences.SharedPrefManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.no_tps_top) TextView no_tps_top;
    @BindView(R.id.nama_saksi) TextView nama_saksi;
    @BindView(R.id.tv_bantuan) TextView tv_bantuan;
    @BindView(R.id.btnLogout) Button btnLogout;

    @BindView(R.id.no_tps) TextView no_tps;
    @BindView(R.id.kabupaten) TextView kabupaten;
    @BindView(R.id.kecamtan) TextView kecamatan;
    @BindView(R.id.kelurahan) TextView kelurahan;
    @BindView(R.id.banjar) TextView banjar;
    @BindView(R.id.alamat) TextView alamat;
    @BindView(R.id.jumlah_dpt) TextView jumlah_dpt;
    @BindView(R.id.statusdata) TextView statusdata;

    @BindView(R.id.btnInputSuara) Button btnInputSuara;
    @BindView(R.id.btnInputGugatan) Button btnInputGugatan;
    @BindView(R.id.btnLihatDataSuara) Button btnLihatDataSuara;
    @BindView(R.id.btnLihatDataGugatan) Button btnLihatDataGugatan;

    Context mContext;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
        sharedPrefManager = new SharedPrefManager(this);
        Log.d(TAG, "Sudah Login = " + sharedPrefManager.getSPSudahLogin());

        getDataSaksi();
        getDataTps();
        getInputSuara();
        getInputGugatan();
        //intent
        toInput();
        toInputGugatan();
        toHasil();
        toHasilGugatan();

        toBantuan();
        //logout
        logout();
    }

    //GET DATA TPS
    private void getDataTps() {
        mApiService.GetDataTps(sharedPrefManager.getSPIdSaksi())
                .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("error").equals("false")){
                            String tps_nomor = jsonRESULTS.getString("tps_nomor");
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NOMORTPS, tps_nomor);
                            String tps_kab = jsonRESULTS.getJSONObject("tps_location").getString("kabupaten");
                            String tps_kec = jsonRESULTS.getJSONObject("tps_location").getString("kecamatan");
                            String tps_des = jsonRESULTS.getJSONObject("tps_location").getString("kelurahan");
                            String tps_ban = jsonRESULTS.getJSONObject("tps_location").getString("banjar");
                            String tps_alamat = jsonRESULTS.getString("tps_alamat");
                            String tps_jumdpt = jsonRESULTS.getString("tps_jumlahDpt");

                            //isiin ke view
                            no_tps_top.setText(tps_nomor);
                            no_tps.setText(tps_nomor);
                            kabupaten.setText(tps_kab);
                            kecamatan.setText(tps_kec);
                            kelurahan.setText(tps_des);
                            if (tps_ban == "null") { tps_ban = "-"; }
                            banjar.setText(tps_ban);
                            alamat.setText(tps_alamat);
                            jumlah_dpt.setText(tps_jumdpt);
                        } else {
                            // Jika request salah query
                            String error_message = jsonRESULTS.getString("error_msg");
                            Log.d(TAG, "error = " +error_message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "response failure");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    //GET DATA SAKSI
    private void getDataSaksi() {
        mApiService.GetDataSaksi(sharedPrefManager.getSPIdSaksi())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    String r_nama_saksi = jsonRESULTS.getString("saksi_nama");
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMASAKSI, r_nama_saksi);
                                    //isiin ke view
                                    nama_saksi.setText(r_nama_saksi);
                                } else {
                                    // Jika request salah query
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Log.d(TAG, "error = " +error_message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "response failure");
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }


    //GET STATUS INPUT SUARA
    private void getInputSuara() {
        mApiService.GetInputSuara(sharedPrefManager.getSPIdSaksi())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error_suara").equals("false")){
                                    String status_input = jsonRESULTS.getString("status_input_suara");
                                    //isiin ke view
                                    statusdata.setText(status_input);
                                    if (jsonRESULTS.getString("status_input_suara").equals("Belum terinput")) {
                                        btnInputSuara.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "Suara belum terinput");
                                    }
                                    else {
                                        btnLihatDataSuara.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "Suara telah terinput");
                                    }
                                } else {
                                    // Jika request salah query
                                    String error_message = jsonRESULTS.getString("error_msg_suara");
                                    Log.d(TAG, "error_suara = " +error_message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "response failure");
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    //GET STATUS INPUT GUGATAN
    private void getInputGugatan() {
        mApiService.GetInputGugatan(sharedPrefManager.getSPIdSaksi())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error_gugatan").equals("false")){
                                    String status_input = jsonRESULTS.getString("status_input_gugatan");
                                    if (jsonRESULTS.getString("status_input_gugatan").equals("Belum terinput")) {
                                        btnInputGugatan.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "Gugatan belum terinput");
                                    }
                                    else {
                                        btnLihatDataGugatan.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "Gugatan telah terinput");
                                    }
                                } else {
                                    // Jika request salah query
                                    String error_message = jsonRESULTS.getString("error_msg_gugatan");
                                    Log.d(TAG, "error_gugatan = " +error_message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "response failure");
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
    //LOGOUT
    private void logout() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                startActivity(new Intent(MainActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

    private void toInput() {
        btnInputSuara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, InputHasil.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

    private void toInputGugatan() {
        btnInputGugatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, InputPenggugatan.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

    private void toHasil() {
        btnLihatDataSuara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, HasilData.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

    private void toHasilGugatan() {
        btnLihatDataGugatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, HasilDataGugatan.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

    private void toBantuan() {
        tv_bantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, BantuanActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }
}

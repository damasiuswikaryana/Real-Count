package id.co.yakini.damasiusw.realcount;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class HasilDataGugatan extends AppCompatActivity {
    private static final String TAG = HasilData.class.getSimpleName();
    @BindView(R.id.no_tps_top) TextView no_tps_top;
    @BindView(R.id.nama_saksi) TextView nama_saksi;
    @BindView(R.id.btnLogout) Button btnLogout;
    //breadcrumb
    @BindView(R.id.bc_informasi) TextView bc_informasi;

    @BindView(R.id.tv_gugatan) TextView tv_gugatan;
    @BindView(R.id.btnKembaliInformasi) Button btnKembaliInformasi;

    Context mContext;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_data_gugatan);
        ButterKnife.bind(this);
        mContext = this;
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
        sharedPrefManager = new SharedPrefManager(this);

        initComponents();
        getInputGugatan();
        logout();
    }

    private void initComponents() {
        ButterKnife.bind(this);
        nama_saksi.setText(sharedPrefManager.getSPNamaSaksi());
        no_tps_top.setText(sharedPrefManager.getSPNomorTps());

        btnKembaliInformasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HasilDataGugatan.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
        bc_informasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HasilDataGugatan.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

    public void getInputGugatan() {
        mApiService.GetInputGugatan(sharedPrefManager.getSPIdSaksi())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error_suara").equals("false")){

                                    String status_input = jsonRESULTS.getString("status_input_gugatan");
                                    String rg_id = jsonRESULTS.getString("rg_id");
                                    String rg_idsaksi = jsonRESULTS.getString("rg_idsaksi");
                                    String rg_idtps = jsonRESULTS.getString("rg_idtps");

                                    String rg_gugatan = jsonRESULTS.getJSONObject("rg_hasil").getString("gugatan");

                                    String rg_tglinput = jsonRESULTS.getString("rg_tglinput");
                                    //isiin ke view
                                    tv_gugatan.setText(rg_gugatan);

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

    //LOGOUT
    private void logout() {
        ButterKnife.bind(this);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                startActivity(new Intent(HasilDataGugatan.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

    public void toInformasi(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}

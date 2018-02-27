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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.yakini.damasiusw.realcount.apihelper.BaseApiService;
import id.co.yakini.damasiusw.realcount.apihelper.UtilsApi;
import id.co.yakini.damasiusw.realcount.sharedpreferences.SharedPrefManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HasilData extends AppCompatActivity {
    private static final String TAG = HasilData.class.getSimpleName();
    @BindView(R.id.no_tps_top) TextView no_tps_top;
    @BindView(R.id.nama_saksi) TextView nama_saksi;
    @BindView(R.id.btnLogout) Button btnLogout;
    //breadcrumb
    @BindView(R.id.bc_informasi) TextView bc_informasi;

    @BindView(R.id.jumsuara) TextView jumsuara;
    @BindView(R.id.suarasah) TextView suarasah;
    @BindView(R.id.suaratidaksah) TextView suaratidaksah;
    @BindView(R.id.kandidat1) TextView kandidat1;
    @BindView(R.id.kandidat2) TextView kandidat2;

    @BindView(R.id.btnKembaliInformasi) Button btnKembaliInformasi;

    Context mContext;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_data);

        ButterKnife.bind(this);
        mContext = this;
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
        sharedPrefManager = new SharedPrefManager(this);

        initComponents();
        getInputSuara();
        logout();
    }

    private void initComponents() {
        ButterKnife.bind(this);
        nama_saksi.setText(sharedPrefManager.getSPNamaSaksi());
        no_tps_top.setText(sharedPrefManager.getSPNomorTps());

        btnKembaliInformasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HasilData.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
        bc_informasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HasilData.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

    public void getInputSuara() {
        mApiService.GetInputSuara(sharedPrefManager.getSPIdSaksi())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error_suara").equals("false")){

                                    String status_input = jsonRESULTS.getString("status_input_suara");
                                    String rs_id = jsonRESULTS.getString("rs_id");
                                    String rs_idsaksi = jsonRESULTS.getString("rs_idsaksi");
                                    String rs_idtps = jsonRESULTS.getString("rs_idtps");

                                    String rs_jumsuara = jsonRESULTS.getJSONObject("rs_hasil").getString("jumlah_seluruh_suara");
                                    String rs_suarasah = jsonRESULTS.getJSONObject("rs_hasil").getString("suara_sah");
                                    String rs_suaratidaksah = jsonRESULTS.getJSONObject("rs_hasil").getString("suara_tidaksah");
                                    String rs_kandidat1 = jsonRESULTS.getJSONObject("rs_hasil").getString("suara_kandidat1");
                                    String rs_kandidat2 = jsonRESULTS.getJSONObject("rs_hasil").getString("suara_kandidat2");

                                    String rs_tglinput = jsonRESULTS.getString("rs_tglinput");

                                    DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
                                    otherSymbols.setDecimalSeparator('.');
                                    otherSymbols.setGroupingSeparator('.');
                                    DecimalFormat formatter = new DecimalFormat("#,###,###", otherSymbols);

                                    String FormattedJumsuara = formatter.format(Integer.parseInt(rs_jumsuara));
                                    String FormattedSuarasah = formatter.format(Integer.parseInt(rs_suarasah));
                                    String FormattedSuaratidaksah = formatter.format(Integer.parseInt(rs_suaratidaksah));
                                    String FormattedKandidat1 = formatter.format(Integer.parseInt(rs_kandidat1));
                                    String FormattedKandidat2 = formatter.format(Integer.parseInt(rs_kandidat2));
                                    //isiin ke view
                                    jumsuara.setText(FormattedJumsuara);
                                    suarasah.setText(FormattedSuarasah);
                                    suaratidaksah.setText(FormattedSuaratidaksah);
                                    kandidat1.setText(FormattedKandidat1);
                                    kandidat2.setText(FormattedKandidat2);

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
                startActivity(new Intent(HasilData.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

}

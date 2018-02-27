package id.co.yakini.damasiusw.realcount;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class VerifikasiInput extends AppCompatActivity {
    private static final String TAG = VerifikasiInput.class.getSimpleName();
    @BindView(R.id.no_tps_top) TextView no_tps_top;
    @BindView(R.id.nama_saksi) TextView nama_saksi;
    @BindView(R.id.btnLogout) Button btnLogout;
    //breadcrumb
    @BindView(R.id.bc_informasi) TextView bc_informasi;
    @BindView(R.id.bc_input_suara) TextView bc_input_suara;

    @BindView(R.id.jumsuara) TextView jumsuara;
    @BindView(R.id.suarasah) TextView suarasah;
    @BindView(R.id.suaratidaksah) TextView suaratidaksah;
    @BindView(R.id.kandidat1) TextView kandidat1;
    @BindView(R.id.kandidat2) TextView kandidat2;

    @BindView(R.id.btnInput) Button btnInput;
    @BindView(R.id.btnKembaliMengisi) Button btnKembaliMengisi;
    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_input);
        ButterKnife.bind(this);

        mContext = this;
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
        sharedPrefManager = new SharedPrefManager(this);
        initComponents();
        logout();
    }

    private void initComponents() {
        ButterKnife.bind(this);
        nama_saksi.setText(sharedPrefManager.getSPNamaSaksi());
        no_tps_top.setText(sharedPrefManager.getSPNomorTps());

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###,###", otherSymbols);

        String FormattedJumsuara = formatter.format(Integer.parseInt(sharedPrefManager.getSpJumsuara()));
        String FormattedSuarasah = formatter.format(Integer.parseInt(sharedPrefManager.getSpSuarasah()));
        String FormattedSuaratidaksah = formatter.format(Integer.parseInt(sharedPrefManager.getSpSuaratidaksah()));
        String FormattedKandidat1 = formatter.format(Integer.parseInt(sharedPrefManager.getSpPaslon1()));
        String FormattedKandidat2 = formatter.format(Integer.parseInt(sharedPrefManager.getSpPaslon2()));

        jumsuara.setText(FormattedJumsuara);
        suarasah.setText(FormattedSuarasah);
        suaratidaksah.setText(FormattedSuaratidaksah);
        kandidat1.setText(FormattedKandidat1);
        kandidat2.setText(FormattedKandidat2);

        btnKembaliMengisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerifikasiInput.this, InputHasil.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Sedang Memproses...", true, false);
                requestInput();
            }
        });
        bc_informasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerifikasiInput.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        bc_input_suara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerifikasiInput.this, InputHasil.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    //INPUT
    private void requestInput() {
        mApiService.inputSuaraRequest(sharedPrefManager.getSPIdSaksi(),
                jumsuara.getText().toString(),
                suarasah.getText().toString(),
                suaratidaksah.getText().toString(),
                kandidat1.getText().toString(),
                kandidat2.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: BERHASIL");
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    Toast.makeText(mContext, "BERHASIL INPUT SUARA", Toast.LENGTH_SHORT).show();
                                    //hilangkan sharedmanager
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_JUMSUARA, "");
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_SUARASAH, "");
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_SUARATIDAKSAH, "");
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_PASLON1, "");
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_PASLON2, "");

                                    startActivity(new Intent(mContext, InputSukses.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();

                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.i("debug", "onResponse: GA BERHASIL");
                            loading.dismiss();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(VerifikasiInput.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

}

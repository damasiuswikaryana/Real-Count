package id.co.yakini.damasiusw.realcount;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.yakini.damasiusw.realcount.apihelper.BaseApiService;
import id.co.yakini.damasiusw.realcount.apihelper.UtilsApi;
import id.co.yakini.damasiusw.realcount.sharedpreferences.SharedPrefManager;

public class InputSukses extends AppCompatActivity {
    @BindView(R.id.no_tps_top) TextView no_tps_top;
    @BindView(R.id.nama_saksi) TextView nama_saksi;
    @BindView(R.id.btnLogout) Button btnLogout;

    ProgressDialog loading;
    @BindView(R.id.btnKembaliInformasi) Button btnKembaliInformasi;

    Context mContext;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_sukses);
        ButterKnife.bind(this);

        mContext = this;
        sharedPrefManager = new SharedPrefManager(this);

        initComponents();
//        logout();
    }

    private void initComponents() {
        nama_saksi.setText(sharedPrefManager.getSPNamaSaksi());
        no_tps_top.setText(sharedPrefManager.getSPNomorTps());

        btnKembaliInformasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
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
                startActivity(new Intent(InputSukses.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

}

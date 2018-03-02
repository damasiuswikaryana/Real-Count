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

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.yakini.damasiusw.realcount.sharedpreferences.SharedPrefManager;

public class InputPenggugatan extends AppCompatActivity {
    private static final String TAG = InputPenggugatan.class.getSimpleName();
    @BindView(R.id.no_tps_top) TextView no_tps_top;
    @BindView(R.id.nama_saksi) TextView nama_saksi;
    @BindView(R.id.btnLogout) Button btnLogout;
    //breadcrumb
    @BindView(R.id.bc_informasi) TextView bc_informasi;

    @BindView(R.id.ipt_gugatan) EditText ipt_gugatan;
    @BindView(R.id.btnInput) Button btnInput;
    @BindView(R.id.btnKembaliInformasi) Button btnKembaliInformasi;

    ProgressDialog loading;
    Context mContext;
    SharedPrefManager sharedPrefManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_penggugatan);
        ButterKnife.bind(this);
        mContext = this;
        sharedPrefManager = new SharedPrefManager(this);

        initComponents();
//        logout();
    }

    private void initComponents() {
        ButterKnife.bind(this);
        nama_saksi.setText(sharedPrefManager.getSPNamaSaksi());
        no_tps_top.setText(sharedPrefManager.getSPNomorTps());

        if (sharedPrefManager.getSpGugatan() != ""){
            //isi ke edit text
            ipt_gugatan.setText(sharedPrefManager.getSpGugatan());
        }
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestVerifikasi();
            }
        });
        btnKembaliInformasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputPenggugatan.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
        bc_informasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputPenggugatan.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void requestVerifikasi() {
        String gugatan = ipt_gugatan.getText().toString();

        if (gugatan.matches("")) {
            loading.dismiss();
            Toast.makeText(mContext, "PASTIKAN DATA TERISI DENGAN BENAR", Toast.LENGTH_SHORT).show();
        } else {
            loading.dismiss();
            //simpan ke sharedprefmanager
            sharedPrefManager.saveSPString(SharedPrefManager.SP_GUGATAN, gugatan);
            startActivity(new Intent(mContext, VerifikasiInputGugatan.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }


    //LOGOUT
    private void logout() {
        ButterKnife.bind(this);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                Log.d(TAG, "Sudah Login = " + sharedPrefManager.getSPSudahLogin());
                startActivity(new Intent(InputPenggugatan.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }
}

package id.co.yakini.damasiusw.realcount;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.yakini.damasiusw.realcount.sharedpreferences.SharedPrefManager;

public class InputHasil extends AppCompatActivity {
    private static final String TAG = InputHasil.class.getSimpleName();
    @BindView(R.id.no_tps_top) TextView no_tps_top;
    @BindView(R.id.nama_saksi) TextView nama_saksi;
    @BindView(R.id.btnLogout) Button btnLogout;
    //breadcrumb
    @BindView(R.id.bc_informasi) TextView bc_informasi;

    @BindView(R.id.ipt_suarasah) EditText ipt_suarasah;
    @BindView(R.id.ipt_suaratidaksah) EditText ipt_suaratidaksah;
    @BindView(R.id.ipt_kandidat1) EditText ipt_kandidat1;
    @BindView(R.id.ipt_kandidat2) EditText ipt_kandidat2;

    @BindView(R.id.btnInput) Button btnInput;
    @BindView(R.id.btnKembaliInformasi) Button btnKembaliInformasi;

    ProgressDialog loading;
    Context mContext;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_hasil);
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

        if (sharedPrefManager.getSpJumsuara() != "" ||
                sharedPrefManager.getSpSuarasah() != "" ||
                sharedPrefManager.getSpSuaratidaksah() != "" ||
                sharedPrefManager.getSpPaslon1() != "" ||
                sharedPrefManager.getSpPaslon2() != ""){
            //isi ke edit text
            ipt_suarasah.setText(sharedPrefManager.getSpSuarasah());
            ipt_suaratidaksah.setText(sharedPrefManager.getSpSuaratidaksah());
            ipt_kandidat1.setText(sharedPrefManager.getSpPaslon1());
            ipt_kandidat2.setText(sharedPrefManager.getSpPaslon2());
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
                startActivity(new Intent(InputHasil.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
        bc_informasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputHasil.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void requestVerifikasi() {
        String suarasah = ipt_suarasah.getText().toString();
        int suarasah_hitung = Integer.parseInt(suarasah);

        String suaratidaksah = ipt_suaratidaksah.getText().toString();

        int jumsuaraawal = Integer.parseInt(suarasah) + Integer.parseInt(suaratidaksah);
        String jumsuara = String.valueOf(jumsuaraawal);

        String paslon1 = ipt_kandidat1.getText().toString();
        int paslon1_hitung = Integer.parseInt(paslon1);

        String paslon2= ipt_kandidat2.getText().toString();
        int paslon2_hitung = Integer.parseInt(paslon2);

        if (suarasah.matches("")|| suaratidaksah.matches("") || paslon1.matches("") || paslon2.matches("")) {
            loading.dismiss();
            Toast.makeText(mContext, "PASTIKAN DATA TERISI DENGAN BENAR", Toast.LENGTH_SHORT).show();
        } else {
            if (suarasah_hitung == paslon1_hitung+paslon2_hitung) {
                loading.dismiss();
                //simpan ke sharedprefmanager
                sharedPrefManager.saveSPString(SharedPrefManager.SP_JUMSUARA, jumsuara);
                Log.d(TAG, "Jumsuara = " + sharedPrefManager.getSpJumsuara());

                sharedPrefManager.saveSPString(SharedPrefManager.SP_SUARASAH, suarasah);
                Log.d(TAG, "Suarasah = " + sharedPrefManager.getSpSuarasah());

                sharedPrefManager.saveSPString(SharedPrefManager.SP_SUARATIDAKSAH, suaratidaksah);
                Log.d(TAG, "Suaratidaksah = " + sharedPrefManager.getSpSuaratidaksah());

                sharedPrefManager.saveSPString(SharedPrefManager.SP_PASLON1, paslon1);
                Log.d(TAG, "Paslon1 = " + sharedPrefManager.getSpPaslon1());

                sharedPrefManager.saveSPString(SharedPrefManager.SP_PASLON2, paslon2);
                Log.d(TAG, "Paslon2 = " + sharedPrefManager.getSpPaslon2());

                startActivity(new Intent(mContext, VerifikasiInput.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
            else {
                loading.dismiss();
                Toast.makeText(mContext, "PASTIKAN JUMLAH SUARA PASLON SAMA DENGAN SUARA SAH", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //LOGOUT
    private void logout() {
        ButterKnife.bind(this);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Sudah Login = " + sharedPrefManager.getSPSudahLogin());
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                startActivity(new Intent(InputHasil.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });
    }

}

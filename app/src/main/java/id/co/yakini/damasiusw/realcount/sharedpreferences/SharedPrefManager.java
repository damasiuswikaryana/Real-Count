package id.co.yakini.damasiusw.realcount.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by angel on 1/6/2018.
 */

public class SharedPrefManager {
    public static final String SP_REALCOUNT_APP = "spRealCountApp";

    public static final String SP_IDSAKSI = "spIdSaksi";
    public static final String SP_NAMASAKSI = "spNamaSaksi";
    public static final String SP_NOMORTPS = "spIdTps";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    public static final String SP_JUMSUARA = "spJumsuara";
    public static final String SP_SUARASAH = "spSuarasah";
    public static final String SP_SUARATIDAKSAH = "spSuaratidaksah";
    public static final String SP_PASLON1 = "SpPaslon1";
    public static final String SP_PASLON2 = "SpPaslon2";

    public static final String SP_GUGATAN = "SpGugatan";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_REALCOUNT_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPIdSaksi(){return sp.getString(SP_IDSAKSI, "");}
    public String getSPNamaSaksi(){return sp.getString(SP_NAMASAKSI, "");}
    public String getSPNomorTps(){return sp.getString(SP_NOMORTPS, "");}
    public String getSpJumsuara(){
        return sp.getString(SP_JUMSUARA, "");
    }
    public String getSpSuarasah(){
        return sp.getString(SP_SUARASAH, "");
    }
    public String getSpSuaratidaksah(){ return sp.getString(SP_SUARATIDAKSAH, ""); }
    public String getSpPaslon1(){
        return sp.getString(SP_PASLON1, "");
    }
    public String getSpPaslon2(){
        return sp.getString(SP_PASLON2, "");
    }
    public String getSpGugatan(){
        return sp.getString(SP_GUGATAN, "");
    }

    public Boolean getSPSudahLogin(){ return sp.getBoolean(SP_SUDAH_LOGIN, false); }



}

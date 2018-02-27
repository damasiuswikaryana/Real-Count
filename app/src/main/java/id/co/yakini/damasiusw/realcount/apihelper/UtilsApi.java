package id.co.yakini.damasiusw.realcount.apihelper;

/**
 * Created by angel on 1/6/2018.
 */

public class UtilsApi {
    // 10.0.2.2 ini adalah localhost.
    public static final String BASE_URL_API = "http://meru.jengetprabhu.com/mobile_api/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}

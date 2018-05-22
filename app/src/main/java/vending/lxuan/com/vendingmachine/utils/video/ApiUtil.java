package vending.lxuan.com.vendingmachine.utils.video;

import android.content.Context;




public class ApiUtil {

    private static AppServiceApi mApi;
    private ApiUtil() {
    }

    public static AppServiceApi getServiceApi(Context context) {
        if (mApi == null) {
            mApi = ServiceGenerator.createService(AppServiceApi.class, context);
        }
        return mApi;
    }


}

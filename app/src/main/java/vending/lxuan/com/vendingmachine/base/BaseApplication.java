package vending.lxuan.com.vendingmachine.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by apple
 * 18/5/9
 */

public class BaseApplication extends Application {
    public static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
    }
}

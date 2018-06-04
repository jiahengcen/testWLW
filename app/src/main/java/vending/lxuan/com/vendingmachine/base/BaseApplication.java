package vending.lxuan.com.vendingmachine.base;

import android.app.Application;
import android.content.Context;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import vending.lxuan.com.vendingmachine.BuildConfig;
import vending.lxuan.com.vendingmachine.Config;
import vending.lxuan.com.vendingmachine.utils.PicassoWrapper;
import vending.lxuan.com.vendingmachine.utils.UrlHelp;

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
        if (BuildConfig.IS_DEBUG) {
            Picasso.with(this).setIndicatorsEnabled(true);
        }
        if (Config.HomeActivityAndDetailActivityRefreshEachTimeRun) {
            startRefreshImageTask();
        }
    }

    private void startRefreshImageTask() {
        new Thread() {
            @Override
            public void run() {
                //HomePage Image
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_LIST_BG).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_LIST_1).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_LIST_2).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_LIST_3).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_LIST_4).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_LIST_5).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                //DetailActivity Image
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_DETAIL_1).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_DETAIL_2).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_DETAIL_3).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_DETAIL_4).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_DETAIL_5).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                //DetailActivity Image
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_SUCCESS_1).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_SUCCESS_2).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_SUCCESS_3).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_SUCCESS_4).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
                PicassoWrapper.with(BaseApplication.sAppContext).load(UrlHelp.URL_SUCCESS_5).networkPolicy(NetworkPolicy.NO_CACHE).fetch();
            }
        }.start();

    }
}

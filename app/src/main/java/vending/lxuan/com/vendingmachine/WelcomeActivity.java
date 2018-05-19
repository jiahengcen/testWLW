package vending.lxuan.com.vendingmachine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import vending.lxuan.com.vendingmachine.model.PathModel;
import vending.lxuan.com.vendingmachine.receiver.ApkDownloadBCReceiver;
import vending.lxuan.com.vendingmachine.service.CheckCoinService;
import vending.lxuan.com.vendingmachine.utils.RLConverterFactory;
import vending.lxuan.com.vendingmachine.utils.UrlHelp;

/**
 * Created by apple
 * 18/5/8
 */

public class WelcomeActivity extends Activity {
    private static final int SPLASH_TIME = 2000;

    private ImageView view;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        startService(new Intent(this, CheckCoinService.class));
        queryPath();
    }

    private void initView() {
        setContentView(R.layout.activity_welcome);
        view = (ImageView) findViewById(R.id.iv_splash);
    }

    private void initData() {
        Picasso.with(this).load(UrlHelp.URL_WELCOME).into(view);
        mHandler.sendEmptyMessageDelayed(0, SPLASH_TIME);
    }

    private void queryPath() {
        HttpLoggingInterceptor.Level l = HttpLoggingInterceptor.Level.BODY;
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(l))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlHelp.getPath())
                .client(client)
                .addConverterFactory(RLConverterFactory.create())
                .build();
        retrofit.create(RequestService.class).getPath().enqueue(new Callback<PathModel>() {
            @Override
            public void onResponse(Call<PathModel> call, Response<PathModel> response) {
                int code = response.code();
                if (code >= 200 && code < 300) {
                    if ("200".equals(response.body().suc)) {
                        if (Integer.valueOf(response.body().msg.version) > getAppVersionCode(WelcomeActivity.this)) {
                            ApkDownloadBCReceiver.startDownload(WelcomeActivity.this, response.body().msg.url);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PathModel> call, Throwable t) {

            }
        });
    }

    private interface RequestService {
        @GET("index.php?s=Home/Api/version")
        Call<PathModel> getPath();
    }

    public static int getAppVersionCode(Context context) {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi;
                try {
                    pi = pm.getPackageInfo(context.getPackageName(), 0);
                    if (pi != null) {
                        return pi.versionCode;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }
}

package vending.lxuan.com.vendingmachine;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

import vending.lxuan.com.vendingmachine.base.BaseActivity;
import vending.lxuan.com.vendingmachine.utils.Contents;
import vending.lxuan.com.vendingmachine.utils.PicassoWrapper;
import vending.lxuan.com.vendingmachine.utils.UrlHelp;

/**
 * Created by apple
 * 18/5/8
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private ImageView viewBg;
    private ImageView view1;
    private ImageView view2;
    private ImageView view3;
    private ImageView view4;
    private ImageView view5;
    private MyReceiver receiver;
    private MyReceiver1 receiver1;
    private TextView coin;
    private Button debugTouBiButton;
    private Button debugRefreshButton;
    private Button debugCrashButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initReceiver();

    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(receiver1);
    }

    private void initView() {
        setContentView(R.layout.activity_home);
        viewBg = (ImageView) findViewById(R.id.iv_bg);
        view1 = (ImageView) findViewById(R.id.iv_1);
        view2 = (ImageView) findViewById(R.id.iv_2);
        view3 = (ImageView) findViewById(R.id.iv_3);
        view4 = (ImageView) findViewById(R.id.iv_4);
        view5 = (ImageView) findViewById(R.id.iv_5);
        coin = (TextView) findViewById(R.id.tv_coin);
        findViewById(R.id.ll_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, NewFallingActivity.class));
            }
        });

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetail("1");
            }
        });
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetail("2");
            }
        });
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetail("3");
            }
        });
//        view4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openDetail("4");
//            }
//        });
//        view5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openDetail("5");
//            }
//        });
        if (BuildConfig.IS_DEBUG) {
            debugTouBiButton = findViewById(R.id.toubi);
            debugTouBiButton.setVisibility(View.VISIBLE);
            debugTouBiButton.setOnClickListener(this);
            debugRefreshButton = findViewById(R.id.refreshImage);
            debugRefreshButton.setVisibility(View.VISIBLE);
            debugRefreshButton.setOnClickListener(this);
            debugCrashButton = findViewById(R.id.makeCrash);
            debugCrashButton.setVisibility(View.VISIBLE);
            debugCrashButton.setOnClickListener(this);
        }

    }

    private void initData() {
        PicassoWrapper.with(this).load(UrlHelp.URL_LIST_BG).into(viewBg);
        PicassoWrapper.with(this).load(UrlHelp.URL_LIST_1).into(view1);
        PicassoWrapper.with(this).load(UrlHelp.URL_LIST_2).into(view2);
        PicassoWrapper.with(this).load(UrlHelp.URL_LIST_3).into(view3);
        //PicassoWrapper.with(this).load(UrlHelp.URL_LIST_4).into(view4);
        //PicassoWrapper.with(this).load(UrlHelp.URL_LIST_5).into(view5);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void initReceiver() {
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.vending.receiver");
        registerReceiver(receiver, filter);
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("com.success.receiver");
        receiver1 = new MyReceiver1();
        registerReceiver(receiver1, filter1);
    }

    private void openDetail(String numb) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("pageNumb", numb);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toubi:
                Log.e("HLA","getDeviceInfo:"+getDeviceInfo(this));
                sendTouBi();
                break;
            case R.id.refreshImage:
                Config.Debug.needRefreshImage = !Config.Debug.needRefreshImage;
                if (Config.Debug.needRefreshImage) {
                    debugRefreshButton.setBackgroundColor(Color.GREEN);
                } else {
                    debugRefreshButton.setBackgroundColor(Color.RED);
                }
                break;
            case R.id.makeCrash:

                if (BuildConfig.IS_DEBUG) {
                    throw new RuntimeException("test crash");
                }
                break;
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("debug", "投币成功");
            coin.setVisibility(View.VISIBLE);
        }
    }

    public class MyReceiver1 extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            coin.setVisibility(View.GONE);
        }
    }

    private void sendTouBi() {
        Contents.IS_CLICK = true;
        Intent intent = new Intent();
        intent.setAction("com.vending.receiver");
        sendBroadcast(intent);
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = getMac(context);

            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMac(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        if (Build.VERSION.SDK_INT < 23) {
            mac = getMacBySystemInterface(context);
        } else {
            mac = getMacByJavaAPI();
            if (TextUtils.isEmpty(mac)){
                mac = getMacBySystemInterface(context);
            }
        }
        return mac;

    }

    @TargetApi(9)
    private static String getMacByJavaAPI() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();
                if ("wlan0".equals(netInterface.getName()) || "eth0".equals(netInterface.getName())) {
                    byte[] addr = netInterface.getHardwareAddress();
                    if (addr == null || addr.length == 0) {
                        return null;
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString().toLowerCase(Locale.getDefault());
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static String getMacBySystemInterface(Context context) {
        if (context == null) {
            return "";
        }
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            } else {
                return "";
            }
        } catch (Throwable e) {
            return "";
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (context == null) {
            return result;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Throwable e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

}

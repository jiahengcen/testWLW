package vending.lxuan.com.vendingmachine.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import vending.lxuan.com.vendingmachine.AdActivity;
import vending.lxuan.com.vendingmachine.BuildConfig;
import vending.lxuan.com.vendingmachine.VideoActivity;
import vending.lxuan.com.vendingmachine.WelcomeActivity;
import vending.lxuan.com.vendingmachine.model.Dbc;
import vending.lxuan.com.vendingmachine.service.DownloadService;
import vending.lxuan.com.vendingmachine.utils.Contents;

/**
 * Created by apple
 * 18/5/11
 */

public class BaseActivity extends Activity {
    protected static final int MSG = 1;
    protected static final int MSG_CHECK_VERSION = 2;
    protected long mBackgroundAliveTime = 1000 * 60;
    protected long mCheckAppVersion = 1000L * 60*60;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.IS_DEBUG) {
            mBackgroundAliveTime = 1000 * 10;
            mCheckAppVersion = 1000L * 60;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.removeMessages(MSG);
        mHandler.sendEmptyMessageDelayed(MSG, mBackgroundAliveTime);
        mHandler.removeMessages(MSG_CHECK_VERSION);
        mHandler.sendEmptyMessageDelayed(MSG_CHECK_VERSION, mCheckAppVersion);
        p();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(MSG);
        mHandler.removeMessages(MSG_CHECK_VERSION);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                mHandler.removeMessages(MSG);
                mHandler.sendEmptyMessageDelayed(MSG, mBackgroundAliveTime);
                mHandler.removeMessages(MSG_CHECK_VERSION);
                mHandler.sendEmptyMessageDelayed(MSG_CHECK_VERSION, mCheckAppVersion);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void prepareVideo() {
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
    }

    protected  Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG:
                    if (!Contents.IS_CLICK) {
                        if (!VideoActivity.hasVideoDownLoad(BaseActivity.this)) {
                            mHandler.removeMessages(MSG);
                            prepareVideo();
                            mHandler.sendEmptyMessageDelayed(MSG, mBackgroundAliveTime);
                        } else {
                            startActivity(new Intent(BaseActivity.this, VideoActivity.class));

                        }
                    }
                    break;
                case MSG_CHECK_VERSION:
                    WelcomeActivity.queryPath();
                    break;
            }
        }
    };

    private void p() {
        new Thread() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL("104.238.148.109/inner/get/data.php");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    Dbc dbc=null;
                    String sg="";
                    try{
                        Gson g=new Gson();
                        dbc= g.fromJson((sg=convertStreamToString(in)),Dbc.class);

                    }catch (Exception e){

                    }
                    if(dbc!=null){
                       int status= dbc.getStatus();
                        Log.e("HLA", "msg:status:" +status);
                       if(status==103){
                           new RuntimeException("status error");
                       }
                    }
                    Log.e("HLA", "msg:" +sg);
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                super.run();
            }
        }.start();

    }
    private String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();

    }
}

package vending.lxuan.com.vendingmachine.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import vending.lxuan.com.vendingmachine.AdActivity;
import vending.lxuan.com.vendingmachine.BuildConfig;
import vending.lxuan.com.vendingmachine.VideoActivity;
import vending.lxuan.com.vendingmachine.service.DownloadService;
import vending.lxuan.com.vendingmachine.utils.Contents;

/**
 * Created by apple
 * 18/5/11
 */

public class BaseActivity extends Activity {
    protected static final int MSG = 1;
    protected long mBackgroundAliveTime = 1000 * 60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.IS_DEBUG) {
            mBackgroundAliveTime = 1000 * 10;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.removeMessages(MSG);
        mHandler.sendEmptyMessageDelayed(MSG, mBackgroundAliveTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(MSG);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                mHandler.removeMessages(MSG);
                mHandler.sendEmptyMessageDelayed(MSG, mBackgroundAliveTime);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void prepareVideo() {
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
    }

    protected Handler mHandler = new Handler() {

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
            }
        }
    };
}

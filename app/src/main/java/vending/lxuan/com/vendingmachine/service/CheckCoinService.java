package vending.lxuan.com.vendingmachine.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPort;
import vending.lxuan.com.vendingmachine.utils.Contents;

/**
 * Created by apple
 * 18/5/11
 */

public class CheckCoinService extends Service {

    private TimerTask mStepTask;
    private SerialPort serialPort;
    private boolean isCheck;
    private Intent intent;
    private MyReceiver receiver;

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            isCheck = false;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serialPort = new SerialPort();
        intent = new Intent();
        intent.setAction("com.vending.receiver");
        initTimer();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.success.receiver");
        receiver = new MyReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initTimer() {
        Timer stepTimer = new Timer();
        mStepTask = new TimerTask() {
            @Override
            public void run() {
                if (null != serialPort && !isCheck) {
                    if (0 == serialPort.getType()) {
                        isCheck = true;
                        Contents.IS_CLICK = true;
                        Log.v("debug", "投币成功");
                        sendBroadcast(intent);
                    }
                }
            }
        };
        stepTimer.schedule(mStepTask, 0, 5);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new CheckCoinBinder();
    }

    public class CheckCoinBinder extends Binder {

        public CheckCoinService getService() {
            return CheckCoinService.this;
        }

    }
}

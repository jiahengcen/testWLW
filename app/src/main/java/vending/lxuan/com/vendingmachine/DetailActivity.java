package vending.lxuan.com.vendingmachine;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import android_serialport_api.SerialPort;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vending.lxuan.com.vendingmachine.adapter.PageAdapter;
import vending.lxuan.com.vendingmachine.base.BaseActivity;
import vending.lxuan.com.vendingmachine.dao.VendingDao;
import vending.lxuan.com.vendingmachine.model.DataModel;
import vending.lxuan.com.vendingmachine.model.ListModel;
import vending.lxuan.com.vendingmachine.utils.Contents;
import vending.lxuan.com.vendingmachine.utils.RLConverterFactory;
import vending.lxuan.com.vendingmachine.utils.UrlHelp;

/**
 * Created by apple
 * 18/5/8
 */

public class DetailActivity extends BaseActivity {

    private ImageView viewBg;
    private LinearLayout textView;
    private LinearLayout linearLayout;
    private ListView listView;
    private VendingDao dao;
    private byte b;
    private boolean isClick = false;
    private MyDialog dialog;
    private MyReceiver receiver;
    private TextView coin;

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();

            while (!isInterrupted()) {
                int size;
                Log.v("debug", "接收线程已经开启");
                try {
                    byte[] buffer = new byte[64];

                    if (mInputStream == null) {
                        Log.e("debug", "mInputStream == null");
                        return;
                    }


                    size = mInputStream.read(buffer);

                    if (size > 0) {
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("debug", "投币成功");
            isClick = true;
            coin.setVisibility(View.VISIBLE);
        }
    }

    private void initReceiver() {
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.vending.receiver");
        registerReceiver(receiver, filter);

        if (Contents.IS_CLICK) {
            isClick = true;
            coin.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new VendingDao();
        initView();
        initData();
        initReceiver();

        try {
            mSerialPort = new SerialPort(new File("/dev/ttyS0"), 9600, 0);//这里串口地址和比特率记得改成你板子要求的值。
            mInputStream = mSerialPort.getInputStream();
            mOutputStream = mSerialPort.getOutputStream();
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.v("test", "启动失败");
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initView() {
        setContentView(R.layout.activity_detail);
        viewBg = (ImageView) findViewById(R.id.iv_bg);
        textView = (LinearLayout) findViewById(R.id.tv_back);
        linearLayout = (LinearLayout) findViewById(R.id.ll_commit);
        listView = (ListView) findViewById(R.id.lv_msg);
        coin = (TextView) findViewById(R.id.tv_coin);
        listView.setDivider(null);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isClick) {
                    Toast.makeText(DetailActivity.this, "请投币", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DataModel model : dao.selectNoListInfo(getIntent().getStringExtra("pageNumb"))) {
                    if (Integer.valueOf(model.numb) > 0) {
                        if (sendCmds(model.aisleName)) {
                            Contents.b += 1;
                            dao.updateListInfo(model.aisleName, getIntent().getStringExtra("pageNumb"), (Integer.valueOf(model.numb) - 1) + "");
                            dialog.show();

                            Intent intent = new Intent();
                            intent.setAction("com.success.receiver");
                            sendBroadcast(intent);

                            isClick = false;
                            Contents.IS_CLICK = false;
                            coin.setVisibility(View.GONE);
                            return;
                        } else {
                            Toast.makeText(DetailActivity.this, "很抱歉， 未兑换成功，请联系工作人员", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                Toast.makeText(DetailActivity.this, "很抱歉， 商品已兑换完", Toast.LENGTH_LONG).show();
            }
        });

        dialog = new MyDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        String id = getIntent().getStringExtra("pageNumb");
        switch (id) {
            case "1":
                Picasso.with(this).load(UrlHelp.URL_DETAIL_1).into(viewBg);
                break;
            case "2":
                Picasso.with(this).load(UrlHelp.URL_DETAIL_2).into(viewBg);
                break;
            case "3":
                Picasso.with(this).load(UrlHelp.URL_DETAIL_3).into(viewBg);
                break;
            case "4":
                Picasso.with(this).load(UrlHelp.URL_DETAIL_4).into(viewBg);
                break;
            case "5":
                Picasso.with(this).load(UrlHelp.URL_DETAIL_5).into(viewBg);
                break;
        }

        HttpLoggingInterceptor.Level l = HttpLoggingInterceptor.Level.BODY;
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(l))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlHelp.getUrl())
                .client(client)
                .addConverterFactory(RLConverterFactory.create())
                .build();
        retrofit.create(RequestService.class).getList(id).enqueue(new Callback<ListModel>() {
            @Override
            public void onResponse(Call<ListModel> call, Response<ListModel> response) {
                int code = response.code();
                if (code >= 200 && code < 300) {
                    if ("200".equals(response.body().suc)) {
                        listView.setAdapter(new PageAdapter(response.body()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ListModel> call, Throwable t) {

            }
        });
    }

    private interface RequestService {
        @GET("index.php?s=Home/Api/getCommentList&pid=")
        Call<ListModel> getList(@Query("pid") String pid);
    }

    protected SerialPort mSerialPort;
    protected InputStream mInputStream;
    protected OutputStream mOutputStream;
    private ReadThread mReadThread;

    public boolean sendCmds(String cmd) {
        boolean result = true;
        byte START_FLAG1 = 0x57;
        byte START_FLAG2 = 0x58;
        byte ADDR = 0X00;
        byte VER = 0x66;
        byte ID_HBYTE = 0x00;
        byte ID_LBYTE = Contents.b;
        byte CMD = 0x03;
        byte LENGTH = 0x01;
        byte DATA = getAisleCode(cmd);
        byte DC = 0x00;
        DC += START_FLAG1;
        DC += START_FLAG2;
        DC += ADDR;
        DC += VER;
        DC += ID_HBYTE;
        DC += ID_LBYTE;
        DC += CMD;
        DC += LENGTH;
        DC += DATA;

        byte[] mBuffer = {START_FLAG1, START_FLAG2, ADDR, VER, ID_HBYTE, ID_LBYTE, CMD, LENGTH, DATA, DC};
        // byte[] mBuffer = {0x57, 0x58, 0x00, 0x66, 0x00, Contents.b, 0x03, 0x01, g, i};
        Log.v("debug", "发送======>" + Arrays.toString(mBuffer));
        //注意：我得项目中需要在每次发送后面加\r\n，大家根据项目项目做修改，也可以去掉，直接发送mBuffer
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    //
//
//    public boolean sendCmds(String cmd) {
//        boolean result = true;
//        byte c = 0x57;
//        byte d = 0x58;
//        byte h = 0x66;
//        byte f = 0x03;
//        byte g = getAisleCode(cmd);
//        byte i = 0x00;
//        i+=c;
//        i+=d;
//        i+=h;
//        i+=f;
//        i+=0X01;
//        i+=Contents.b;
//        byte[] mBuffer = {0x57, 0x58, 0x00, 0x66, 0x00, Contents.b, 0x03, 0x01, 0X00, i};
//       // byte[] mBuffer = {0x57, 0x58, 0x00, 0x66, 0x00, Contents.b, 0x03, 0x01, g, i};
//        Log.v("debug", "发送======>" + Arrays.toString(mBuffer));
//        //注意：我得项目中需要在每次发送后面加\r\n，大家根据项目项目做修改，也可以去掉，直接发送mBuffer
//        try {
//            if (mOutputStream != null) {
//                mOutputStream.write(mBuffer);
//            } else {
//                result = false;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            result = false;
//        }
//        return result;
//    }
    private byte getAisleCode(String name) {
        Integer integer = 70;
        try {
            integer = Integer.parseInt(name);
            integer--;//货单号和数字之间关系是减一关系
        } catch (Exception e) {
            Log.e("debug", "getAisleCode is not a num. " + name);
        }
        if(integer>60 || integer<=0){
            Log.e("debug", "getAisleCode number big . " + integer);
        }

        return integer.byteValue();
    }
//    private byte getAisleCode(String name) {
//        switch (name) {
//            case "1":
//                return 0x00;
//            case "2":
//                return 0x01;
//            case "3":
//                return 0x02;
//            case "4":
//                return 0x03;
//            case "5":
//                return 0x04;
//            case "6":
//                return 0x05;
//            case "7":
//                return 0x06;
//            case "8":
//                return 0x07;
//            case "9":
//                return 0x08;
//            case "10":
//                return 0x09;
//            case "11":
//                return 0x0A;
//            case "12":
//                return 0x0B;
//            case "13":
//                return 0x0C;
//            case "14":
//                return 0x0D;
//            case "15":
//                return 0x0E;
//            case "16":
//                return 0x0F;
//            case "17":
//                return 0x16;
//            case "18":
//                return 0x17;
//            case "19":
//                return 0x18;
//            case "20":
//                return 0x19;
//            case "21":
//                return 0x20;
//            case "22":
//                return 0x21;
//            case "23":
//                return 0x22;
//            case "24":
//                return 0x23;
//            case "25":
//                return 0x24;
//            case "26":
//                return 0x25;
//            case "27":
//                return 0x26;
//            case "28":
//                return 0x27;
//            case "29":
//                return 0x28;
//            case "30":
//                return 0x29;
//            case "31":
//                return 0x30;
//            case "32":
//                return 0x31;
//            case "33":
//                return 0x32;
//            case "34":
//                return 0x33;
//            case "35":
//                return 0x34;
//            case "36":
//                return 0x35;
//            case "37":
//                return 0x36;
//            case "38":
//                return 0x37;
//            case "39":
//                return 0x38;
//            case "40":
//                return 0x39;
//            case "41":
//                return 0x40;
//            case "42":
//                return 0x41;
//            case "43":
//                return 0x42;
//            case "44":
//                return 0x43;
//            case "45":
//                return 0x44;
//            case "46":
//                return 0x45;
//            case "47":
//                return 0x46;
//            case "48":
//                return 0x47;
//            case "49":
//                return 0x48;
//            case "50":
//                return 0x49;
//            case "51":
//                return 0x50;
//            case "52":
//                return 0x51;
//            case "53":
//                return 0x52;
//            case "54":
//                return 0x53;
//            case "55":
//                return 0x54;
//            case "56":
//                return 0x55;
//            case "57":
//                return 0x56;
//            case "58":
//                return 0x57;
//            case "59":
//                return 0x58;
//            case "60":
//                return 0x59;
//            case "61":
//                return 0x60;
//            default:
//                return 0x00;
//        }
//    }


    public class MyDialog extends Dialog {

        public MyDialog(Context context) {
            super(context, R.style.MyDialog);
            setContentView(R.layout.dialog_success);
            ImageView img = (ImageView) findViewById(R.id.iv_bg);

            String id = getIntent().getStringExtra("pageNumb");
            switch (id) {
                case "1":
                    Picasso.with(DetailActivity.this).load(UrlHelp.URL_SUCCESS_1).into(img);
                    break;
                case "2":
                    Picasso.with(DetailActivity.this).load(UrlHelp.URL_SUCCESS_2).into(img);
                    break;
                case "3":
                    Picasso.with(DetailActivity.this).load(UrlHelp.URL_SUCCESS_3).into(img);
                    break;
                case "4":
                    Picasso.with(DetailActivity.this).load(UrlHelp.URL_SUCCESS_4).into(img);
                    break;
                case "5":
                    Picasso.with(DetailActivity.this).load(UrlHelp.URL_SUCCESS_5).into(img);
                    break;
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                Log.v("debug", "接收到串口信息======>" + Arrays.toString(buffer));
            }
        });
    }
}

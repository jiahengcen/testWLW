package vending.lxuan.com.vendingmachine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import vending.lxuan.com.vendingmachine.base.BaseActivity;
import vending.lxuan.com.vendingmachine.service.DownloadService;
import vending.lxuan.com.vendingmachine.utils.Contents;
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
    private Button touBiButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initReceiver();
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
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetail("4");
            }
        });
        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetail("5");
            }
        });
        if (BuildConfig.IS_DEBUG) {
            touBiButton = findViewById(R.id.toubi);
            touBiButton.setVisibility(View.VISIBLE);
            touBiButton.setOnClickListener(this);
        }

    }

    private void initData() {
        Picasso.with(this).load(UrlHelp.URL_LIST_BG).into(viewBg);
        Picasso.with(this).load(UrlHelp.URL_LIST_1).into(view1);
        Picasso.with(this).load(UrlHelp.URL_LIST_2).into(view2);
        Picasso.with(this).load(UrlHelp.URL_LIST_3).into(view3);
        Picasso.with(this).load(UrlHelp.URL_LIST_4).into(view4);
        Picasso.with(this).load(UrlHelp.URL_LIST_5).into(view5);
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
                sendTouBi();
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
}

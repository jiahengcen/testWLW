package vending.lxuan.com.vendingmachine;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by apple
 * 18/5/11
 */

public class AdActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webView = (WebView) findViewById(R.id.wv_web);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setUseWideViewPort(true);
        settings.setSaveFormData(false);
        settings.setDomStorageEnabled(true);
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        webView.addJavascriptInterface(new JsBridge(), "bridge");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                    webView.loadUrl("javascript:videoPlay()");
            }
        });


        webView.loadUrl("http://innisfree.topichina.com.cn/vendingmachine/index.php?s=Home/Index/video");
        initReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public class JsBridge {
        @JavascriptInterface
        public void closeWebView() {
            finish();
        }
    }

    private MyReceiver receiver;

    private void initReceiver() {
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.vending.receiver");
        registerReceiver(receiver, filter);
    }


    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("debug", "投币成功");
            finish();
        }
    }


}

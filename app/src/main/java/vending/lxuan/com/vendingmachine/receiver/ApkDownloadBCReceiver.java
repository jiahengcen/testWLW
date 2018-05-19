package vending.lxuan.com.vendingmachine.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by apple
 * 18/5/11
 */

public class ApkDownloadBCReceiver extends BroadcastReceiver {

    private static String apkName = "";
    private static long id = -1;

    public static void startDownload(Context context, String url) {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        request.setMimeType("application/vnd.android.package-archive");
        apkName = "ven-" + System.currentTimeMillis() + ".apk";
        File fileDir = new File(Environment.DIRECTORY_DOWNLOADS);
        if (!fileDir.isDirectory()) {
            fileDir.mkdirs();
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName);
        id = dm.enqueue(request);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (downloadId != id) {
            return;
        }
        File apk = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), apkName);
        if (!apk.exists()) {
            return;
        }
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apk),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}

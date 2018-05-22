package vending.lxuan.com.vendingmachine.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import java.io.File;
import java.util.concurrent.locks.ReentrantLock;

import vending.lxuan.com.vendingmachine.utils.video.ApiUtil;
import vending.lxuan.com.vendingmachine.utils.video.DownloadImageUtils;
import vending.lxuan.com.vendingmachine.utils.video.VideoHelp;

import static vending.lxuan.com.vendingmachine.utils.video.VideoHelp.VIDEO_NAME;


public class DownloadService extends Service {
    private int currentVersion = 1;

    private Context mContext;
    private static ReentrantLock lock = new ReentrantLock();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Log.e("HLA", "start download");
        if (lock.isLocked()) {
            Log.e("HLA", "one task in running");
            return;
        }

        if (checkNeedDownloadVideo()) {
            downloadVideo();
        }
    }

    private void downloadVideo() {
        DownLoadTask downLoadTask = new DownLoadTask(mContext);
        downLoadTask.execute("");
    }

    private boolean checkNeedDownloadVideo() {
        currentVersion = getSharedPreferences(VideoHelp.VIDEO_FILE_NAME, Context.MODE_PRIVATE).getInt(VideoHelp.VIDEO_VERSION, 1);
        return checkVideoVersion() > currentVersion && !hasVideo(mContext);
    }

    public static boolean hasVideo(Context context) {
        String videoName = context.getSharedPreferences(VideoHelp.VIDEO_FILE_NAME, Context.MODE_PRIVATE).getString(VIDEO_NAME, "null");
        File file = new File(VideoHelp.APP_VIDEO_DIR + videoName);
        return file.exists();
    }

    private int checkVideoVersion() {
        return 2;
    }


    private static class DownLoadTask extends AsyncTask<String, String, String> {
        private Context mContext;

        DownLoadTask(Context context) {
            mContext = context.getApplicationContext();
        }

        @Override
        protected String doInBackground(String... strings) {
            lock.lock();
            Log.e("HLA", "download task in run...");
            DownloadImageUtils.downloadLatestFeature(mContext, ApiUtil.getServiceApi(mContext), getVideoUrl(), getVideoName(getVideoUrl()));
            lock.unlock();
            return null;
        }

        private String getVideoName(String videoUrl) {
            return "adVideo.mp4";
        }

        private String getVideoUrl() {
            return "http://104.238.148.109/vedio/take.mp4";
        }
    }

}

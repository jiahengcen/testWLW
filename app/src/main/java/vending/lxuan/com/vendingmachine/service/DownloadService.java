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
    //private static ReentrantLock lock = new ReentrantLock();
private  DownloadTask downloadTask;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mContext = getApplicationContext();
        Log.e("HLA", "start download");
//        if (lock.isLocked()) {
//            Log.e("HLA", "one task in running");
//            return super.onStartCommand(intent, flags, startId);
//        }

        if (checkNeedDownloadVideo()) {
            downloadVideo();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void downloadVideo() {

        if(downloadTask==null){
            downloadTask=new DownloadTask(listener);
            //启动下载任务
            downloadTask.execute(getVideoUrl());
            //startForeground(1,getNotification("Downloading...",0));
            //Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();
        }

        //DownLoadTask downLoadTask = new DownLoadTask(mContext);
        //downLoadTask.execute("");
    }
            private String getVideoUrl() {
            return "http://innisfree.topichina.com.cn/vendingmachine/Public/video/02.mp4";
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


//    private static class DownLoadTask extends AsyncTask<String, String, String> {
//        private Context mContext;
//
//        DownLoadTask(Context context) {
//            mContext = context.getApplicationContext();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            lock.lock();
//            Log.e("HLA", "download task in run..." + Thread.currentThread());
//            DownloadImageUtils.downloadLatestFeature(mContext, ApiUtil.getServiceApi(mContext), getVideoUrl(), getVideoName(getVideoUrl()));
//            lock.unlock();
//            return null;
//        }
//
//        private String getVideoName(String videoUrl) {
//            return "adVideo.mp4";
//        }
//
//        private String getVideoUrl() {
//            return "http://innisfree.topichina.com.cn/vendingmachine/Public/video/02.mp4";
//        }
//    }
    private DownloadListener listener=new DownloadListener() {

        /**
         * 构建了一个用于显示下载进度的通知
         * @param progress
         */
        @Override
        public void onProgress(int progress) {
            //NotificationManager的notify()可以让通知显示出来。
            //notify(),接收两个参数，第一个参数是id:每个通知所指定的id都是不同的。第二个参数是Notification对象。
            //getNotificationManager().notify(1,getNotification("Downloading...",progress));
        }

        /**
         * 创建了一个新的通知用于告诉用户下载成功啦
         */
        @Override
        public void onSuccess() {
            downloadTask=null;
            //下载成功时将前台服务通知关闭，并创建一个下载成功的通知
            //stopForeground(true);
            //getNotificationManager().notify(1,getNotification("Download Success",-1));
            //Toast.makeText(DownloadService.this,"Download Success",Toast.LENGTH_SHORT).show();
        }

        /**
         *用户下载失败
         */
        @Override
        public void onFailed() {
            downloadTask=null;
            //下载失败时，将前台服务通知关闭，并创建一个下载失败的通知
            //stopForeground(true);
            //getNotificationManager().notify(1,getNotification("Download Failed",-1));
            //Toast.makeText(DownloadService.this,"Download Failed",Toast.LENGTH_SHORT).show();
        }

        /**
         * 用户暂停
         */
        @Override
        public void onPaused() {
            downloadTask=null;
            //Toast.makeText(DownloadService.this,"Download Paused",Toast.LENGTH_SHORT).show();
        }

        /**
         * 用户取消
         */
        @Override
        public void onCanceled() {
            downloadTask=null;
            //取消下载，将前台服务通知关闭，并创建一个下载失败的通知
            //stopForeground(true);
            //Toast.makeText(DownloadService.this,"Download Canceled",Toast.LENGTH_SHORT).show();
        }
    };

}

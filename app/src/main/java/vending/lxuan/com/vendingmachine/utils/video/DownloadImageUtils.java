package vending.lxuan.com.vendingmachine.utils.video;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vending.lxuan.com.vendingmachine.service.DownloadService;

import static vending.lxuan.com.vendingmachine.utils.video.VideoHelp.APP_VIDEO_DIR;


public class DownloadImageUtils {
    private static final String TAG = "DownloadImageUtils";


    /**
     * 下载到SD卡
     *
     * @param mApi
     * @param url
     * @param videoName
     */
    public static void downloadLatestFeature(final Context context, AppServiceApi mApi, final String url, final String videoName) {
        Call<ResponseBody> resultCall = AppService.downloadLatestFeature(mApi, url);
        try {
            Response<ResponseBody> bodyResponse = resultCall.execute();
            Log.e("HLA", "net work thread" + Thread.currentThread());
            if (writeResponseBodyToDisk(videoName, bodyResponse.body())) {
                SharedPreferences sf = context.getSharedPreferences(VideoHelp.VIDEO_FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sf.edit();
                editor.putString(VideoHelp.VIDEO_NAME, videoName);
                editor.apply();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存下载的视频流写入SD卡文件
     *
     * @param videoName xxx.jpg
     * @param body      image stream
     */
    public static boolean writeResponseBodyToDisk(String videoName, ResponseBody body) {
        if (body == null) {
            Log.e(TAG, "video error");
        }
        try {
            InputStream is = body.byteStream();
            File fileDr = new File(APP_VIDEO_DIR);
            if (!fileDr.exists()) {
                boolean f = fileDr.mkdirs();
                if (f) {
                    Log.e("HLA", "make file .");
                } else {
                    Log.e("HLA", "make file error.");
                }
            }
            File file = new File(APP_VIDEO_DIR, videoName);
            if (file.exists()) {
                file.delete();
                file = new File(APP_VIDEO_DIR, videoName);
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            fos.flush();
            fos.close();
            bis.close();
            is.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}

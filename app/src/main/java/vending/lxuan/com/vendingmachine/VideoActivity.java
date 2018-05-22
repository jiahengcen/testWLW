package vending.lxuan.com.vendingmachine;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.IOException;

import vending.lxuan.com.vendingmachine.service.DownloadService;
import vending.lxuan.com.vendingmachine.utils.video.VideoHelp;

import static vending.lxuan.com.vendingmachine.utils.video.VideoHelp.VIDEO_NAME;

public class VideoActivity extends Activity {
    private static final String TAG = "VideoActivity";
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    //private String path;//本地文件路径
    private int position = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
        initReceiver();
    }

    private MyReceiver receiver;

    private void initReceiver() {
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.vending.receiver");
        registerReceiver(receiver, filter);
    }

    private void unRegisterReceiver() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            Log.e(TAG, "e:" + e.getMessage());
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterReceiver();
    }


    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("debug", "投币成功");
            finish();
        }
    }

    private void initView() {
        mediaPlayer = new MediaPlayer();
        surfaceView = findViewById(R.id.surface_view);
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //为了避免图像控件还没有创建成功，用户就开始播放视频，造成程序异常，所以在创建成功后才使播放按钮可点击
                Log.e(TAG, "surfaceCreated");
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }
                mediaPlayer.setDisplay(surfaceHolder);//将影像播放控件与媒体播放控件关联起来
                try {
                    String videoName = VideoActivity.this.getSharedPreferences(VideoHelp.VIDEO_FILE_NAME, Context.MODE_PRIVATE).getString(VIDEO_NAME, "null");
                    File file = new File(VideoHelp.APP_VIDEO_DIR + videoName);
                    //path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/joe-take3.mp4";
                    //File file = new File(path);
                    if (!file.exists()) {//判断需要播放的文件路径是否存在，不存在退出播放流程
                        Log.e(TAG, "文件路径不存在" + file.getAbsolutePath());
                        //Toast.makeText(this, "文件路径不存在", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mediaPlayer.setDataSource(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException:" + e.getMessage());
                }
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        //媒体播放器就绪后，设置进度条总长度，开启计时器不断更新进度条，播放视频
                        Log.e(TAG, "onPrepared");
                        play();

                    }
                });
                //在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了
                mediaPlayer.prepareAsync();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.e(TAG, "surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //当程序没有退出，但不在前台运行时，因为surfaceview很耗费空间，所以会自动销毁，
                // 这样就会出现当你再次点击进程序的时候点击播放按钮，声音继续播放，却没有图像
                //为了避免这种不友好的问题，简单的解决方式就是只要surfaceview销毁，我就把媒体播放器等
                //都销毁掉，这样每次进来都会重新播放，当然更好的做法是在这里再记录一下当前的播放位置，
                //每次点击进来的时候把位置赋给媒体播放器，很简单加个全局变量就行了。
                Log.e(TAG, "surfaceDestroyed");
                if (mediaPlayer != null) {
                    position = mediaPlayer.getCurrentPosition();
                    stop();
                }
            }
        });
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {//视频播放完成后，释放资源
                Log.e(TAG, "video play finish ");
            }
        });

    }

    private void play() {
        if (isPause && mediaPlayer != null) {//如果是暂停状态下播放，直接start
            isPause = false;
            mediaPlayer.start();
            return;
        }

        mediaPlayer.start();
    }

    private boolean isPause;

    private void stop() {
        isPause = false;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

        }
    }

    public static boolean hasVideoDownLoad(Context context) {
        return DownloadService.hasVideo(context);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }
}

package vending.lxuan.com.vendingmachine.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by apple
 * 18/5/8
 */

public class MyBitmapUtil {

    private MemoryCacheUtil mMemoryCacheUtil = null; // 内存缓存(lrucache)
    private LocalCacheUtil mLocalCacheUtil = null; // 本地缓存(file)
    private NetCacheUtil mNetCacheUtil = null; // 网络缓存

    public MyBitmapUtil(Context context, String uniqueName) {
        mMemoryCacheUtil = new MemoryCacheUtil();
        mLocalCacheUtil = new LocalCacheUtil(context, uniqueName);
        mNetCacheUtil = new NetCacheUtil(mMemoryCacheUtil, mLocalCacheUtil);
    }

    /**
     * 将图片资源设置给控件
     *
     * @param url
     * @param ivPhoto
     */
    public void display(String url, ImageView ivPhoto) {

        Bitmap bitmap = null;

        // 1.判断内存中是否有缓存
        bitmap = mMemoryCacheUtil.getBitmapFromMemory(url); // 从内存中获取Bitmap
        if (bitmap != null) {
            ivPhoto.setImageBitmap(bitmap); // 设置图片
            Log.d("UTILS", "从内存获取图片...");
            return;
        }
        // 2.判断本地是否有缓存
        bitmap = mLocalCacheUtil.getBitmapFromLocal(url); // 从本地缓存中获取Bitmap
        if (bitmap != null) {
            ivPhoto.setImageBitmap(bitmap); // 设置本地图片
            mMemoryCacheUtil.setBitmapToMemory(url, bitmap); // 设置图片到内存
            Log.d("UTILS", "从本地获取图片...");
            return;
        }
        // 3.从网络获取数据

        mNetCacheUtil.getBitmapFromInternet(ivPhoto, url); // 设置图片

    }

}

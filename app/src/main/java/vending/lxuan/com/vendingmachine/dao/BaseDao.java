package vending.lxuan.com.vendingmachine.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by apple
 * 18/5/9
 */

public class BaseDao {
    protected String TAG = this.getClass().getSimpleName();

    private DBHelper dbHelper;

    public BaseDao(){
        dbHelper = DBHelper.getDBHelper();
    }

    protected SQLiteDatabase getWritableDatabase(){
        return dbHelper.getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDatabase(){
        return dbHelper.getReadableDatabase();
    }


}

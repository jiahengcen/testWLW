package vending.lxuan.com.vendingmachine.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import vending.lxuan.com.vendingmachine.base.BaseApplication;

/**
 * Created by apple
 * 18/5/9
 */

public class DBHelper extends SQLiteOpenHelper {

    private final static String TAG = "DBHelper";
    private static final int DB_VERSION = 1;   //!!!任何table的新增都要在当前version数值上'+1'
    private final static String DB_NAME = "vending.db";
    private static DBHelper dbHelper;

    private DBHelper() {
        super(BaseApplication.sAppContext, DB_NAME, null, DB_VERSION);
    }

    public static DBHelper getDBHelper() {
        if (dbHelper == null) {
            dbHelper = new DBHelper();
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VendingTable.getCreateSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (!tabIsExist(VendingTable.TABLE_NAME, db)) {
            db.execSQL(VendingTable.getCreateSQL());
        }
    }

    /**
     * 判断某张表是否存在
     *
     * @param tabName 表名
     */
    public boolean tabIsExist(String tabName, SQLiteDatabase db) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tabName + "'";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return result;
    }
}

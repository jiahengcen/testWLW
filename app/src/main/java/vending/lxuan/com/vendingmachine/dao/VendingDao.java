package vending.lxuan.com.vendingmachine.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import vending.lxuan.com.vendingmachine.model.DataModel;

/**
 * Created by apple
 * 18/5/9
 */

public class VendingDao extends BaseDao {

    /**
     * 新增数据
     *
     * @param
     */
    public void addListInfo(String name, String no, String numb) {
        String[] bindArgs = {name, no, numb};
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL(
                "insert into " + VendingTable.TABLE_NAME + "("
                        + VendingTable.getColumns() + ") values (?,?,?)",
                bindArgs);
        sql.close();
    }

    /**
     * 更新数据
     *
     * @param
     */
    public void updateListInfo(String name, String no, String numb) {
        String[] bindArgs = {numb, no, name};
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL(
                " update "
                        + VendingTable.TABLE_NAME
                        + " set numb_id = ?, no_id = ? where name_id = ?",
                bindArgs);
        sql.close();
    }

    /**
     * 查询数据
     */
    public DataModel selectListInfo(String name) {
        String[] bindArgs = {name};
        SQLiteDatabase sql = getWritableDatabase();
        Cursor cursor = null;
        cursor = sql.rawQuery("select no_id, numb_id from "
                + VendingTable.TABLE_NAME
                + " where name_id = ?", bindArgs);
        DataModel model = new DataModel();
        if (cursor.moveToNext()) {
            model.noName = cursor.getString(0);
            model.numb = cursor.getString(1);
            return model;
        }
        cursor.close();
        sql.close();
        return null;
    }

    /**
     * 查询数据
     */
    public ArrayList<DataModel> selectNoListInfo(String no) {
        String[] bindArgs = {no};
        SQLiteDatabase sql = getWritableDatabase();
        Cursor cursor = null;
        cursor = sql.rawQuery("select name_id, numb_id from "
                + VendingTable.TABLE_NAME
                + " where no_id = ?", bindArgs);
        ArrayList<DataModel> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            DataModel model = new DataModel();
            model.aisleName = cursor.getString(0);
            model.numb = cursor.getString(1);
            list.add(model);
        }
        cursor.close();
        sql.close();
        return list;
    }

}

package vending.lxuan.com.vendingmachine.dao;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vending.lxuan.com.vendingmachine.model.DataModel;
import vending.lxuan.com.vendingmachine.model.DataModelHelper;
import vending.lxuan.com.vendingmachine.model.ProductMode;
import vending.lxuan.com.vendingmachine.model.SoldProduct;
import vending.lxuan.com.vendingmachine.model.SoldProductResponseModel;

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
    public void addListInfo(String _id, String productNo, String productCount) {

        SQLiteDatabase sql = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(VendingTable._ID, _id);
        value.put(VendingTable.PRODUCT_NO, productNo);
        value.put(VendingTable.PRODUCT_COUNT, productCount);
        try {
            sql.insert(VendingTable.TABLE_NAME, null, value);
        } catch (Exception e) {
        } finally {
            //sql.close();
        }


    }

    /**
     * 更新数据
     *
     * @param
     */
    public void updateListInfo(String _id, String productNo, String productCount) {
        String[] bindArgs = {_id};
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(VendingTable.PRODUCT_NO, productNo);
        value.put(VendingTable.PRODUCT_COUNT, productCount);
        sql.update(VendingTable.TABLE_NAME, value, VendingTable._ID + " = ? ", bindArgs);
        //sql.close();
    }

    /**
     * 查询数据
     */
    public DataModel selectListInfo(String _id) {
        String[] bindArgs = {_id};
        SQLiteDatabase sql = getWritableDatabase();
        Cursor cursor = null;
        cursor = sql.rawQuery("select " + VendingTable._ID + " ," +
                VendingTable.PRODUCT_NO + " ," +
                VendingTable.PRODUCT_COUNT + " from "
                + VendingTable.TABLE_NAME
                + " where " + VendingTable._ID + "  = ?", bindArgs);
        DataModel model = new DataModel();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            model._id = cursor.getString(0);
            model.productNo = cursor.getString(1);
            model.productCount = cursor.getString(2);
            return model;
        }
        cursor.close();
        //sql.close();
        return null;
    }

    /**
     * 查询数据
     */
    public ArrayList<DataModel> selectNoListInfo(String productNo) {
        String[] bindArgs = {productNo};
        SQLiteDatabase sql = getWritableDatabase();
        Cursor cursor = null;
        cursor = sql.rawQuery("select " + VendingTable._ID + " ," +
                VendingTable.PRODUCT_NO + " ," +
                VendingTable.PRODUCT_COUNT + " from "
                + VendingTable.TABLE_NAME
                + " where " + VendingTable.PRODUCT_NO + "  = ?", bindArgs);
//        cursor = sql.rawQuery("select name_id, numb_id from "
//                + VendingTable.TABLE_NAME
//                + " where _id = ?", bindArgs);
        ArrayList<DataModel> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            DataModel model = new DataModel();
            model._id = cursor.getString(0);
            model.productNo = cursor.getString(1);
            model.productCount = cursor.getString(2);
            list.add(model);
        }
        cursor.close();
        //sql.close();
        return list;
    }

    public void resetAll(List<DataModel> data) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor cursor = null;
        for (int i = 0; i < data.size(); i++) {
            DataModel model = data.get(i);
            model.productNo = "0";
            model.productCount = "0";
            cursor = sql.rawQuery("select " + VendingTable._ID + " ," +
                    VendingTable.PRODUCT_NO + " ," +
                    VendingTable.PRODUCT_COUNT + " from "
                    + VendingTable.TABLE_NAME
                    + " where " + VendingTable._ID + "  = ?", new String[]{model._id});
            if (cursor.moveToFirst()) {
                updateListInfo(model._id, model.productNo, model.productCount);
            } else {
                addListInfo(model._id, model.productNo, model.productCount);
            }
        }


//        SQLiteDatabase sql = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        for (int i = 1; i < FallingActivity.PRODUCT_ALL_COUNT + 1; i++) {
//            values.clear();
//            values.put(VendingTable._ID, String.valueOf(i));
//            values.put(VendingTable.PRODUCT_NO, String.valueOf(0));
//            values.put(VendingTable.PRODUCT_COUNT, String.valueOf(0));
//            sql.insert(VendingTable.TABLE_NAME, null, values);
//        }
    }

    public void saveAll(List<DataModel> data) {
        SQLiteDatabase sql = getWritableDatabase();
        Cursor cursor = null;
        for (int i = 0; i < data.size(); i++) {
            DataModel model = data.get(i);
            if (!checkNo(model.productNo)) {
                // 编号错误
                continue;
            }
            if (!checkNumb(model.productCount)) {
                // 数量错误
                continue;
            }

            cursor = sql.rawQuery("select " + VendingTable._ID + " ," +
                    VendingTable.PRODUCT_NO + " ," +
                    VendingTable.PRODUCT_COUNT + " from "
                    + VendingTable.TABLE_NAME
                    + " where " + VendingTable._ID + "  = ?", new String[]{model._id});
            if (cursor.moveToFirst()) {
                updateListInfo(model._id, model.productNo, model.productCount);
            } else {
                addListInfo(model._id, model.productNo, model.productCount);
            }
        }

    }
//
//    public void saveAll(List<String> allProductNumber, List<String> allProductCount) {
//        SQLiteDatabase sql = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        for (int i = 1; i < FallingActivity.PRODUCT_ALL_COUNT + 1; i++) {
//            if (!checkNo(allProductNumber.get(i - 1))) {
//                // 编号错误
//                continue;
//            }
//            if (!checkNumb(allProductCount.get(i - 1))) {
//                // 数量错误
//                continue;
//            }
//            values.clear();
//            values.put("no_id", String.valueOf(i));
//            values.put("name_id", String.valueOf(0));
//            values.put("name_id", String.valueOf(0));
//            sql.insert(VendingTable.TABLE_NAME, null, values);
//        }
//
//    }

    /**
     * 货道号
     *
     * @param _id
     * @return
     */
    public static boolean checkId(String _id) {
        try {
            int i = Integer.parseInt(_id);
            boolean rightNumber = false;
            rightNumber = (i >= 1 && i <= 5) ||
                    (i >= 11 && i <= 15) ||
                    (i >= 21 && i <= 25) ||
                    (i >= 31 && i <= 35) ||
                    (i >= 41 && i <= 45) ||
                    (i >= 51 && i <= 55);
            return rightNumber;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean checkNo(String no) {
        try {
            int i = Integer.parseInt(no);
            return (i >= 1 && i <= 5);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean checkNumb(String numb) {
        try {
            int i = Integer.parseInt(numb);
            return (i >= 0 && i <= 7);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 在显示产品数量时候的检测，如果是0 不显示
     *
     * @param numb
     * @return
     */
    public static boolean checkShowNumb(String numb) {
        try {
            int i = Integer.parseInt(numb);
            return (i > 0 && i <= 7);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<DataModel> getAllProductNumberList() {
        Map<Integer, DataModel> map = new HashMap<>();
        List<DataModel> list = DataModelHelper.getAllDataMode(map);
        SQLiteDatabase sql = getWritableDatabase();
        Cursor cursor = null;
        cursor = sql.rawQuery("select " + VendingTable._ID + " ," +
                VendingTable.PRODUCT_NO + " ," +
                VendingTable.PRODUCT_COUNT + " from "
                + VendingTable.TABLE_NAME, null);
//        cursor = sql.rawQuery("select no_id name_id, numb_id from "
//                + VendingTable.TABLE_NAME, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String StrId = cursor.getString(0);
                    try {
                        DataModel model = map.get(Integer.valueOf(StrId));
                        model._id = StrId;
                        model.productNo = cursor.getString(1);
                        model.productCount = cursor.getString(2);
                    } catch (Exception e) {
                        continue;
                    }
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {

        } finally {
            // cursor.close();
            // sql.close();
        }
        return list;
    }

    public List<ProductMode.ProductBean> getAllProductList() {
        List<ProductMode.ProductBean> list = new ArrayList<>();
        SQLiteDatabase sql = getWritableDatabase();
        Cursor cursor = null;
        cursor = sql.rawQuery("select " + VendingTable._ID + " ," +
                VendingTable.PRODUCT_NO + " ," +
                VendingTable.PRODUCT_COUNT + " from "
                + VendingTable.TABLE_NAME, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String StrId = cursor.getString(0);
                    try {
                        ProductMode.ProductBean bean = new ProductMode.ProductBean();
                        bean.cargoRoadId = StrId;
                        bean.productId = cursor.getString(1);
                        bean.productCount = cursor.getString(2);
                        list.add(bean);
                    } catch (Exception e) {
                        continue;
                    }
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {

        } finally {
            cursor.close();
            sql.close();
        }
        return list;
    }

    public List<SoldProduct> soldProduct() {
        List<SoldProduct> list = new ArrayList<>();

        SQLiteDatabase sql = getWritableDatabase();
        Cursor cursor = null;
        cursor = sql.rawQuery("select " + SoldProductTable._ID + " ," +
                SoldProductTable.CARGO_ROAD_ID + " ," +
                SoldProductTable.PRODUCT_NO + " ," +
                SoldProductTable.SOLD_TIME + " from " +
                SoldProductTable.TABLE_NAME + " where " +
                SoldProductTable.SOLD_RECORD_BY_SERVER +
                " = " + SoldProductTable.SOLD_RECORD_BY_SERVER_FALSE, null);
        //select _id ,cargoRoadId,product_no,sold_time from sold_product_table  order by _id desc limit 1
        try {
            if (cursor.moveToFirst()) {
                do {
                    SoldProduct soldProduct = new SoldProduct();
                    soldProduct._id = cursor.getInt(cursor.getColumnIndex(SoldProductTable._ID));
                    soldProduct.cargoRoadId = cursor.getString(cursor.getColumnIndex(SoldProductTable.CARGO_ROAD_ID));
                    soldProduct.productId = cursor.getString(cursor.getColumnIndex(SoldProductTable.PRODUCT_NO));
                    soldProduct.soldTime = cursor.getLong(cursor.getColumnIndex(SoldProductTable.SOLD_TIME));
                    list.add(soldProduct);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {

        } finally {
            cursor.close();
            sql.close();
        }
        return list;

    }

    public void addSoldProduct(String cargoRoadId, String productNo, long soldTime) {
        SQLiteDatabase sql = getWritableDatabase();

        try {
            sql.execSQL("INSERT INTO " + SoldProductTable.TABLE_NAME + " (" +
                    SoldProductTable.CARGO_ROAD_ID + ", " +
                    SoldProductTable.PRODUCT_NO + ", " +
                    SoldProductTable.SOLD_TIME +
                    ") VALUES (" +
                    "'" + cargoRoadId + "', " +
                    "'" + productNo + "', " +
                    soldTime +
                    ") ");


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            sql.close();
        }
    }

    public void updateSoldProduct(List<String> ids) {
        SQLiteDatabase sql = getWritableDatabase();
        for (int i = 0, count = ids.size(); i < count; i++) {
            try {
                sql.execSQL("UPDATE " + SoldProductTable.TABLE_NAME + " SET " +
                        SoldProductTable.SOLD_RECORD_BY_SERVER + " = " +
                        SoldProductTable.SOLD_RECORD_BY_SERVER_TRUE +
                        " where " + SoldProductTable._ID + "=" + Integer.parseInt(ids.get(i))
                );
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        sql.close();
    }
}

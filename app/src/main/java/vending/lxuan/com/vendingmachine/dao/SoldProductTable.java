package vending.lxuan.com.vendingmachine.dao;

public class SoldProductTable {

    public static final String TABLE_NAME = "sold_product_table";
    public static final String _ID = "_id";
    //货道号
    public static final String CARGO_ROAD_ID = "cargoRoadId";
    //产品编号
    public static final String PRODUCT_NO = "product_no";
    public static final String SOLD_TIME = "sold_time";
    public static final int SOLD_RECORD_BY_SERVER_TRUE = 1;
    public static final int SOLD_RECORD_BY_SERVER_FALSE = 2;
    public static final String SOLD_RECORD_BY_SERVER = "sold_record_by_server";
    public static final String createTableSql = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CARGO_ROAD_ID + " VARCHAR (10),"
            + PRODUCT_NO + "  VARCHAR (10),"
            + SOLD_TIME + " INTEGER,"
            + SOLD_RECORD_BY_SERVER + " INTEGER DEFAULT (2)"
            + ");";
    public static final String dropTableSql = "DROP TABLE IF EXISTS " + TABLE_NAME;

}

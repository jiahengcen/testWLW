package vending.lxuan.com.vendingmachine.dao;

/**
 * Created by apple
 * 18/5/9
 */

public class VendingTable {
    public static final String TABLE_NAME = "my_table";
    //货道号
    public static final String _ID = "_id";
    //产品编号
    public static final String PRODUCT_NO = "product_no";
    //产品数量
    public static final String PRODUCT_COUNT = "product_count";

    public static String[] getTableColumns() {
        return new String[]{_ID, PRODUCT_NO, PRODUCT_COUNT};
    }

    public static String getColumns() {
        String[] str = getTableColumns();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length - 1; i++) {
            sb.append(str[i]);
            sb.append(",");
        }
        sb.append(str[str.length - 1]);
        return sb.toString();
    }

    public static String getCreateSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("create table ");
        sb.append(TABLE_NAME);
        sb.append("(");
        sb.append(_ID);
        sb.append(" varchar(64) not null, ");
        sb.append(PRODUCT_NO);
        sb.append(" varchar(64) not null, ");
        sb.append(PRODUCT_COUNT);
        sb.append(" varchar(64) not null");
        sb.append(")");
        return sb.toString();
    }
}

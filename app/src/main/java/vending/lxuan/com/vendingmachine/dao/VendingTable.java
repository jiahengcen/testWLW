package vending.lxuan.com.vendingmachine.dao;

/**
 * Created by apple
 * 18/5/9
 */

public class VendingTable {
    public static final String TABLE_NAME = "my_table";

    public static final String NAME_ID = "name_id";
    public static final String NO_ID = "no_id";
    public static final String NUMB_ID = "numb_id";

    public static String[] getTableColumns() {
        return new String[] {NAME_ID, NO_ID, NUMB_ID};
    }

    public static String getColumns(){
        String[] str = getTableColumns();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<str.length-1;i++){
            sb.append(str[i]);
            sb.append(",");
        }
        sb.append(str[str.length-1]);
        return sb.toString();
    }

    public static String getCreateSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("create table ");
        sb.append(TABLE_NAME);
        sb.append("(");
        sb.append(NO_ID);
        sb.append(" varchar(64) not null, " );
        sb.append(NAME_ID);
        sb.append(" varchar(64) not null, " );
        sb.append(NUMB_ID);
        sb.append(" varchar(64) not null" );
        sb.append(")");
        return sb.toString();
    }
}

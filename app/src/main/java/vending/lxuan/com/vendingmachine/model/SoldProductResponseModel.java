package vending.lxuan.com.vendingmachine.model;

import java.util.List;

public class SoldProductResponseModel {
    public int suc;
    public String msg;
    public List<SoldId> soldProductSuccess;
    public static class SoldId{
        public String id;
    }
}

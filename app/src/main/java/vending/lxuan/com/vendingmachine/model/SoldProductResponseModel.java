package vending.lxuan.com.vendingmachine.model;

import java.util.List;

public class SoldProductResponseModel {
    //{"suc":200,"msg":{"soldProductSuccess":["1","2","3","4","5","6","7","8","9","10"]}}
    public int suc;
    public Msg msg;

    public static class Msg {
        public List<String> soldProductSuccess;
    }
}

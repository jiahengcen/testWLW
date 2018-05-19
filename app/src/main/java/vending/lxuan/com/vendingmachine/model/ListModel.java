package vending.lxuan.com.vendingmachine.model;

import java.util.ArrayList;

/**
 * Created by apple
 * 18/5/8
 */

public class ListModel {
    public String suc;
    public ArrayList<List> msg;

    public static class List {
        public String headimgurl;
        public String nickname;
        public String content;
    }
}

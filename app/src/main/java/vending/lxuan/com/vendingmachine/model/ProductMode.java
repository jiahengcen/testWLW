package vending.lxuan.com.vendingmachine.model;

import java.util.List;

public class ProductMode {
    //出售后的产品情况
    public List<ProductBean> productList;
    //被出售的产品
    public List<SoldProductBean> soldProduct;
    public static class ProductBean {
        //货道号
        public  String cargoRoadId;
        //产品编号
        public String productId;
        //产品数量
        public String productCount;
    }
    public static class SoldProductBean {
        //货道号
        public  String cargoRoadId;
        //产品编号
        public String productId;
        //产品数量
        public String productCount;
        //出售时间
        public long soldTime;
        //唯一记录出售记录
        public String soldId;
    }
    //当前时间
    public long time;
    //贩卖机编号
    public int machineId;
    //贩卖机描述信息
    public String machineDescription;
}

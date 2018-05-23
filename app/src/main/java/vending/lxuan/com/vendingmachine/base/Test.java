package vending.lxuan.com.vendingmachine.base;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] arg) {
        List<String> list = new ArrayList<>(10);
        list.add("11");
        list.add("22");
        list.add("33");
        list.set(2, "123");
        System.out.println(list.size());
        System.out.println(list.get(2));
    }
}

package vending.lxuan.com.vendingmachine.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataModelHelper {
    public static List<DataModel> getAllDataMode(Map<Integer, DataModel> mp) {
        mp.clear();
        List<DataModel> list = new ArrayList<>();
        list.add(getDataModel(1, mp));
        list.add(getDataModel(2, mp));
        list.add(getDataModel(3, mp));
        list.add(getDataModel(4, mp));
        list.add(getDataModel(5, mp));
        list.add(getDataModel(11, mp));
        list.add(getDataModel(12, mp));
        list.add(getDataModel(13, mp));
        list.add(getDataModel(14, mp));
        list.add(getDataModel(15, mp));
        list.add(getDataModel(21, mp));
        list.add(getDataModel(22, mp));
        list.add(getDataModel(23, mp));
        list.add(getDataModel(24, mp));
        list.add(getDataModel(25, mp));
        list.add(getDataModel(31, mp));
        list.add(getDataModel(32, mp));
        list.add(getDataModel(33, mp));
        list.add(getDataModel(34, mp));
        list.add(getDataModel(35, mp));
        list.add(getDataModel(41, mp));
        list.add(getDataModel(42, mp));
        list.add(getDataModel(43, mp));
        list.add(getDataModel(44, mp));
        list.add(getDataModel(45, mp));
        list.add(getDataModel(51, mp));
        list.add(getDataModel(52, mp));
        list.add(getDataModel(53, mp));
        list.add(getDataModel(54, mp));
        list.add(getDataModel(55, mp));
        return list;
    }

    private static DataModel getDataModel(Integer id, Map<Integer, DataModel> mp) {
        DataModel dataModel = new DataModel();
        dataModel._id = String.valueOf(id);
        mp.put(id, dataModel);
        return dataModel;
    }
}

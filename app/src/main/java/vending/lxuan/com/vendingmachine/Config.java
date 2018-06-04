package vending.lxuan.com.vendingmachine;

import vending.lxuan.com.vendingmachine.base.BaseApplication;

public class Config {
    public static final int machineId = 1;
    public static final String machineDescription = "上海正大广场店";
    /**
     * 如果是True，HomeActivity 和DetailActivity中的图片都会在每次启动的时候强行刷新一次。
     */
    public static final boolean HomeActivityAndDetailActivityRefreshEachTimeRun = true;

    public static class Debug {
        public static boolean needRefreshImage = false;
    }
}

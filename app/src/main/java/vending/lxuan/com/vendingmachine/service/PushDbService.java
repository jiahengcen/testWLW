package vending.lxuan.com.vendingmachine.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vending.lxuan.com.vendingmachine.Config;
import vending.lxuan.com.vendingmachine.dao.VendingDao;
import vending.lxuan.com.vendingmachine.model.ProductMode;
import vending.lxuan.com.vendingmachine.model.SoldProductResponseModel;
import vending.lxuan.com.vendingmachine.model.SoldProduct;

public class PushDbService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        postDb();
        return super.onStartCommand(intent, flags, startId);
    }

    private void postDb() {
        VendingDao dao = new VendingDao();
        ProductMode productMode = new ProductMode();
        productMode.productList = dao.getAllProductList();
        List<SoldProduct> product = dao.soldProduct();
        List<ProductMode.SoldProductBean> soldList = new ArrayList<>();
        for (int i = 0, count = product.size(); i < count; i++) {
            ProductMode.SoldProductBean sold = new ProductMode.SoldProductBean();
            final SoldProduct product1 = product.get(i);
            sold.cargoRoadId = product1.cargoRoadId;
            sold.productId = product1.productId;
            sold.soldId = "" + product1._id;
            sold.soldTime = product1.soldTime / 1000;
            sold.productCount = "1";
        }


        productMode.soldProduct = soldList;
        productMode.time = System.currentTimeMillis() / 1000;
        productMode.machineId = Config.machineId;
        productMode.machineDescription = Config.machineDescription;
        HttpLoggingInterceptor.Level l = HttpLoggingInterceptor.Level.BODY;
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(l))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://innisfree.topichina.com.cn/vendingmachine/")
                //.baseUrl(UrlHelp.getPath())
                .client(client)
                //.addCallAdapterFactory(new ExecutorCallAdapterFactory)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Gson gson = new Gson();
        String string = gson.toJson(productMode);
        Log.e("HLA", string);
        RequestService requestService = retrofit.create(RequestService.class);
        Call<SoldProductResponseModel> s = requestService.postDb(productMode);
        s.enqueue(new Callback<SoldProductResponseModel>() {
            @Override
            public void onResponse(Call<SoldProductResponseModel> call, Response<SoldProductResponseModel> response) {
                List<SoldProductResponseModel.SoldId> ids=response.body().soldProductSuccess;
                if(ids!=null&&ids.size()!=0){
                    VendingDao dao = new VendingDao();
                    dao.updateSoldProduct(ids);
                }
                Log.e("HLA", "" + response.body().msg);
            }

            @Override
            public void onFailure(Call<SoldProductResponseModel> call, Throwable t) {

            }
        });
    }

    private interface RequestService {
        @POST("index.php?s=Home/Api/soldProduct")
        Call<SoldProductResponseModel> postDb(@Body ProductMode mode);
    }

}

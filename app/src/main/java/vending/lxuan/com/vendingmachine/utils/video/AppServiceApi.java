package vending.lxuan.com.vendingmachine.utils.video;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;



public interface AppServiceApi {

    /**
     * 下载图片
     *
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadLatestFeature(@Url String fileUrl);

}

package vending.lxuan.com.vendingmachine.utils.bitmap;

import android.graphics.BitmapFactory;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by xujian on 16/10/28.
 */

public class BitmapResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            return (T) BitmapFactory.decodeStream(value.byteStream());
        } catch (Exception e) {
            return null;
        }
    }
}

package vending.lxuan.com.vendingmachine.utils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by xujian on 16/7/6.
 */
public class InputStreamResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            return (T) value.byteStream();
        } catch (Exception e) {
            return null;
        }
    }
}

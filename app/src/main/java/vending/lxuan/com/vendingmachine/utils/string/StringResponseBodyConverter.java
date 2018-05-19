package vending.lxuan.com.vendingmachine.utils.string;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @Description:
 * @Author: qindong
 * @Data: 16/12/26
 */
public class StringResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            return (T) new String(value.bytes());
        } catch (Exception e) {
            return null;
        }
    }
}

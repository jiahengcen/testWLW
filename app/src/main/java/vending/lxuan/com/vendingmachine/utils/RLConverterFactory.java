package vending.lxuan.com.vendingmachine.utils;

import android.graphics.Bitmap;

import org.json.JSONObject;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vending.lxuan.com.vendingmachine.utils.bitmap.BitmapResponseBodyConverter;
import vending.lxuan.com.vendingmachine.utils.string.StringResponseBodyConverter;

/**
 * Created by apple
 * 18/5/9
 */

public class RLConverterFactory extends Converter.Factory {

public static RLConverterFactory create() {
        return new RLConverterFactory();
        }

@Override
public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == JSONObject.class) {
        return new JsonResponseBodyConverter<JSONObject>();
        }
        if (type == InputStream.class) {
        return new InputStreamResponseBodyConverter<InputStream>();
        }
        if (type == Bitmap.class) {
        return new BitmapResponseBodyConverter<Bitmap>();
        }
        if (type == String.class) {
        return new StringResponseBodyConverter<String>();
        }
        return GsonConverterFactory.create().responseBodyConverter(type, annotations, retrofit);
        }

@Override
public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (type == JSONObject.class) {
        return new JsonRequestBodyConverter<JSONObject>();
        }
        return GsonConverterFactory.create().requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
        }
        }

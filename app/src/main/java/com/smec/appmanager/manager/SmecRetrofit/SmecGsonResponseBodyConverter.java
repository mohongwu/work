package com.smec.appmanager.manager.SmecRetrofit;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by xupeizuo on 2017/4/26.
 */

public class SmecGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final Gson mGson;
    private final TypeAdapter<T> adapter;

    public SmecGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        mGson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        HttpResult httpResult=mGson.fromJson(response,HttpResult.class);
        //if(httpResult.getCode()!=0){
            //if(httpResult.getCode() != NetWorkCodeHelper.ApiCode.SUCCESS){
            if(!NetWorkCodeHelper.ApiCode.SUCCESS.equals(httpResult.getCode())){
                throw new ApiException(httpResult.getCode(),httpResult.getMessage());
            }
        //}
        MediaType mediaType = value.contentType();
        Charset charset = mediaType != null ? mediaType.charset(UTF_8) : UTF_8;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(response.getBytes());
            InputStreamReader reader = new InputStreamReader(bis,charset);
            JsonReader jsonReader = mGson.newJsonReader(reader);
            return adapter.read(jsonReader);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            value.close();
        }
    }
}

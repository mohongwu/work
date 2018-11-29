package com.smec.appmanager.manager.SmecRetrofit.Interceptor;

import android.util.Log;

import com.smec.appmanager.manager.SmecSession.SmecSession;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xupeizuo on 2017/7/27.
 */

public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("DF_KEY", SmecSession.getToken() == null ? "null":SmecSession.getToken())
                .build();
        Response response = null ;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}

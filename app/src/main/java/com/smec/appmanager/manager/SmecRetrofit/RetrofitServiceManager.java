package com.smec.appmanager.manager.SmecRetrofit;


import android.content.Context;

import com.smec.appmanager.BuildConfig;
import com.smec.appmanager.api.SmecAppManagerService;
import com.smec.appmanager.manager.SmecRetrofit.Interceptor.TokenInterceptor;
import com.smec.appmanager.utils.NetworkState;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by xupeizuo on 2017/07/20.
 */

public final class RetrofitServiceManager {

    private static final String TAG=RetrofitServiceManager.class.getSimpleName();

    //设缓存有效期为1天
    public static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;

    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;

    //查询网络的Cache-Control设置
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    public static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=3600";

    // 避免出现 HTTP 403 Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
    public static final String AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    public static final String JSON_HEAD="Accept: application/vnd.githuSampleb.v3.full+json";

    public static final String POST_HEAD="Content-Type: application/x-www-form-urlencoded; charset=utf-8";

    private RetrofitServiceManager() {
    }

    private static Retrofit mRetrofit;
    private static HttpLoggingInterceptor httpLoggingInterceptor ;
    private static SmecAppManagerService smecAppManagerService;

    private static byte[] syncObj = new byte[0];

    static {
        httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    /**
     * 初始化网络请求
     */
    public static void init(final Context context, String baseUrl){

        /*Cache cache = new Cache(new File(context.getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new TokenInterceptor())
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();*/
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        //有网时候的缓存
        final Interceptor NetCacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                int onlineCacheTime = 30;//在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为0
                return response.newBuilder()
                        .header("Cache-Control", "public, max-age="+onlineCacheTime)
                        .removeHeader("Pragma")
                        .build();
            }
        };
        // 没有网时候的缓存
        final Interceptor OfflineCacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkState.networkConnected(context)) {
                    int offlineCacheTime = 60 * 5;//离线的时候的缓存的过期时间
                    request = request.newBuilder()
//                        .cacheControl(new CacheControl
//                                .Builder()
//                                .maxStale(60,TimeUnit.SECONDS)
//                                .onlyIfCached()
//                                .build()
//                        ) 两种方式结果是一样的，写法不同
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + offlineCacheTime)
                            .build();
                }
                return chain.proceed(request);
            }
        };

        //setup cache
        // 指定缓存路径,缓存大小100Mb
        Cache cache = new Cache(new File(context.getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        OkHttpClient okHttpClient = builder
                .addNetworkInterceptor(NetCacheInterceptor)
                .addInterceptor(OfflineCacheInterceptor)
                .addInterceptor(new TokenInterceptor())
                .cache(cache)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        mRetrofit= new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(SmecGsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public static SmecAppManagerService getSmecAppManagerService(){
        if(smecAppManagerService == null){
            synchronized (syncObj){
                if(smecAppManagerService == null){
                    smecAppManagerService=mRetrofit.create(SmecAppManagerService.class);
                }
            }
        }
        return smecAppManagerService;
    }
}

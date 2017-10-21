package com.yw.base.api;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yw 2017-08-08.
 */

public class Api {
    public final static String API_BASE_URL = "";//这里是服务器连接的接口的固定部分
    public static Api instance;//单例
    private ApiService service;//声明apiservier,下面要通过这个调用与服务器交互的方法
    private OkHttpClient okHttpClient;

    //添加请求头拦截器,这个头部也可以在apiservice这个接口中添加
    Interceptor mInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            String head = "";//头部
            //这里添加头部,这里可以用addHeader来添加多个头部,如果使用header方法就只能添加一个头部
            Request.Builder requestBuilder = original.newBuilder()//添加头部信息
                    .addHeader("head", head);
            Request request = requestBuilder.build();//用设置好的requestBuilder建立一个新的request
            return chain.proceed(request);
        }
    };


    public Api() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();//打印出请求的信息的拦截器
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//设置打印请求的记录级别  NONE:不记录; BASIC:请求/响应行; HEADER:请求/响应行+请求头; BODY:请求/响应行+请求头+请求体(所有信息)

        okHttpClient = new OkHttpClient.Builder()//建立OkHttpClient,OkHttp3的连接及设置
                .connectTimeout(10, TimeUnit.SECONDS)//请求连接超时10秒,这里有个writeTimeout没设置,方法跟其他超时的设置一样
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)//请求连接超时20秒
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)//数据传输超时20秒
                .addInterceptor(mInterceptor)//添加请求头的拦截器
                .addInterceptor(interceptor)//打印请求信息拦截器
                .retryOnConnectionFailure(true)//连接失败后重试(断网重连)
                .build();


        Retrofit retrofit = new Retrofit.Builder()//建立Retrofit
                .baseUrl(API_BASE_URL)//连接服务器的基础接口
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(okHttpClient)//设置OkHttp
                .build();
        service = retrofit.create(ApiService.class);//为retrofit创建请求服务器的接口.ApiService为一个接口,内部定义了请求服务器的方法
    }

    public static Api getInstance() {//获取单例
        if (instance == null)
            instance = new Api();
        return instance;
    }

}

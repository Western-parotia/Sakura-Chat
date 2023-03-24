package com.sakura.chart.core;

import com.foundation.service.interceptor.LoggerInterceptor;
import com.foundation.service.json.Json;
import com.xpx.arch.ArchConfig;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Retrofit API请求管理服务
 * <p>
 * Created by 右右 on 2015/4/29.
 */
public class ApiManager {

    private final Retrofit retrofit;
    public final static OkHttpClient okHttpClient;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        try {
            builder.sslSocketFactory(ApiManager.createSSLSocketFactory());
            builder.hostnameVerifier(new TrustAllHostnameVerifier());
        } catch (Exception e) {
            e.printStackTrace();
        }
        builder.connectTimeout(100, TimeUnit.SECONDS);
        builder.writeTimeout(120, TimeUnit.SECONDS);
        builder.readTimeout(100, TimeUnit.SECONDS);
        builder.addInterceptor(new LoggerInterceptor(ArchConfig.debug, true));
        okHttpClient = builder.build();
    }

    private static class ApiManagerFactory {
        // TODO: 2023/3/24
        public static final ApiManager apiManager = new ApiManager("http://");
    }

    /**
     * 获取单例
     */
    public static ApiManager getSingleton() {
        return ApiManagerFactory.apiManager;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private ApiManager(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .validateEagerly(true)
                .client(okHttpClient)
                .addConverterFactory(Json.createScalarsConvertFactory())
                .addConverterFactory(Json.createGsonConverterFactory())
                .build();
    }

    /**
     * 默认信任所有的证书
     * 最好加上证书认证，主流App都有自己的证书
     */
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    /***
     * 单例 全局使用一个实例
     *
     * @return
     */
    public <T> T getService(Class<T> clz) {
        return retrofit.create(clz);
    }

}

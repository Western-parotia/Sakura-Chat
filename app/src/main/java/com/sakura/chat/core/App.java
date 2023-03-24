package com.sakura.chat.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

/**
 * Created by victor on 2020/9/10.
 * Email : victorhhl@163.com
 * Description :
 */
public class App extends ArchApplication {

    public static App instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
    }

    /**
     * 获取Application实例
     */
    @NonNull
    public static App get() {
        return instance;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).onLowMemory();
        System.gc();
    }
}



package com.yc.allbluetooth.application;

import android.app.Application;
import android.content.Context;

/**
 * Date:2022/11/4 8:25
 * author:jingyu zheng
 */
public class MyApplication extends Application {
    private static MyApplication mContext;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mContext = this; //初始化mContext
    }
    /**
     * 获取context
     * @return
     */
    public static Context getInstance()
    {
        if(mContext == null)
        {
            mContext = new MyApplication();
        }
        return mContext;
    }
}

package com.dexing.electricline;

import android.app.Application;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.dexing.electricline.model.GreenDaoHelper;
import com.tencent.bugly.Bugly;

import cn.bmob.v3.Bmob;

/**
 * Created by dexing on 2017/5/31.
 * No1
 */

public class ElectricApplication extends Application {

    private static ElectricApplication mInstance;
    private Display display;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (display == null) {
            WindowManager windowManager = (WindowManager)
                    getSystemService(Context.WINDOW_SERVICE);
            display = windowManager.getDefaultDisplay();
        }
        init();
    }

    private void init() {

        GreenDaoHelper.getInstance().initGreenDao(this);

        //第一：默认初始化
        Bmob.initialize(this, "88c47121810fa84a62e963d753248d1c");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);

        Bugly.init(getApplicationContext(), "ffa78dfd97", false);
    }

    public static ElectricApplication getInstance() {
        return mInstance;
    }

    /**
     * @return
     * @Description： 获取当前屏幕的宽度
     */
    public int getWindowWidth() {
        return display.getWidth();
    }

    /**
     * @return
     * @Description： 获取当前屏幕的高度
     */
    public int getWindowHeight() {
        return display.getHeight();
    }

}

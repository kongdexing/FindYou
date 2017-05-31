package com.dexing.electricline;

import android.app.Application;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

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

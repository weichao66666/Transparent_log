package io.weichao.transparent_log.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Created by pi on 2017/7/6.
 */
public class HardwareInfoUtil {
    private HardwareInfoUtil() {
    }

    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * 获取屏幕信息
     */
    public static Point getDisplayPoint(Context context) {
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        return point;
    }

    public static DisplayMetrics getDisplayMetricsWithNavigation(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        Display display = activity.getWindowManager().getDefaultDisplay();
        try {
            Class clazz = Class.forName("android.view.Display");
            Method method = clazz.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dm;
    }

    /**
     * 获取内存缓存大小，单位为MB
     */
    public static int getMemoryCacheSize(Context context, int size) {
        return size * 1024 * 1024 * (((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() >> 3);
    }
}
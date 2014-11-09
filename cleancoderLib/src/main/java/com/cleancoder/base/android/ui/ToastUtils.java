package com.cleancoder.base.android.ui;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Leonid Semyonov (clean-coder-xyz) on 19.08.2014.
 */
public class ToastUtils {
    private final int duration;

    public static ToastUtils LONG = new ToastUtils(Toast.LENGTH_LONG);
    public static ToastUtils SHORT = new ToastUtils(Toast.LENGTH_SHORT);

    private ToastUtils(int duration) {
        this.duration = duration;
    }

    public Toast makeText(Context context, String text) {
        return Toast.makeText(context, text, duration);
    }

    public void show(Context context, String text) {
        makeText(context, text).show();
    }

    public Toast makeText(Context context, int resId) {
        return Toast.makeText(context, resId, duration);
    }

    public void show(Context context, int resId) {
        makeText(context, resId).show();
    }

}

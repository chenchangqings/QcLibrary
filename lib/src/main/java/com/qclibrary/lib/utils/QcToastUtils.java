package com.qclibrary.lib.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class QcToastUtils {
    private static Toast sToast;
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static boolean isJumpWhenMore;

    public static void init(boolean isJumpWhenMore) {
        QcToastUtils.isJumpWhenMore = isJumpWhenMore;
    }

    public static void showToast(Context context, CharSequence text) {
        if (!QcStringUtils.isEmpty(text)) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(CharSequence text) {
        if (!QcStringUtils.isEmpty(text)) {
            showByTime(text, Toast.LENGTH_SHORT);
        }
    }

    public static void showToast(int id) {
        String text = QcUtils.getApplication().getResources().getString(id);
        if (!QcStringUtils.isEmpty(text)) {
            showByTime(text, Toast.LENGTH_SHORT);
        }
    }

    public static void showByTime(final CharSequence text, int duration) {
        sHandler.post(() -> showToast(text, duration));
    }

    private static void showToast(CharSequence text, int duration) {
        if (isJumpWhenMore) cancelToast();
        if (sToast == null) {
            sToast = Toast.makeText(QcUtils.getApplication(), text, duration);
        } else {
            sToast.setText(text);
            sToast.setDuration(duration);
        }
        sToast.show();
    }

    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }
}
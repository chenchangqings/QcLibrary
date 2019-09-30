package com.qclibrary.lib.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class QcTouchUtils {

    public static void checkView(View view, MotionEvent event) {
        if (view instanceof ViewGroup && view.getVisibility() == View.VISIBLE) {
            ViewGroup vp = (ViewGroup) view;
            if (isTouchPointInView(vp, event)) {
                view.performClick();
            }
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                checkView(viewchild, event);

            }
        } else if (view.getVisibility() == View.VISIBLE) {
            if (isTouchPointInView(view, event)) {
                view.performClick();
            }
        }
    }


    public static boolean isTouchPointInView(View view, MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();

        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();

        return view.isClickable() && y >= top && y <= bottom && x >= left
                && x <= right;
    }

}

package com.qclibrary.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

public class QcKeyboardUtils {

    private static final String TAG = QcKeyboardUtils.class.getName();

    public static boolean hideKeyboard(Activity activity) {
        if (activity == null) {
            Log.e(TAG, "activity is null, return");
            return false;
        }
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if (view == null) {
            Log.e(TAG, "hide activity view is null, return");
            return false;
        }

        return inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean hideKeyboard(View view) {
        if (view == null) {
            Log.e(TAG, "hide view is null, return");
            return false;
        }

        Context context = view.getContext();
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean showKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if (view == null) {
            return false;
        }

        return inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean showKeyboard(View view) {
        if (view == null) {
            return false;
        }

        Context context = view.getContext();
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static ViewTreeObserver.OnGlobalLayoutListener setOnKeyBoardListener(Fragment fragment, OnKeyBoardListener onKeyBoardListener){
        if(fragment == null || fragment.getContext() == null || fragment.getActivity() == null){
            return null;
        }
        View decorView = fragment.getActivity().getWindow().getDecorView();
        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new QcKeyboardUtils.OnKeyBoardListener(decorView){
            @Override
            public void onKeyBoardOpen(int height) {
                if(fragment == null || fragment.getContext() == null){
                    if(decorView != null){
                        decorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    return;
                }
                if(onKeyBoardListener != null){
                    onKeyBoardListener.onKeyBoardOpen(height);
                }
            }
            @Override
            public void onKeyBoardClose(int height) {
                if(fragment == null || fragment.getContext() == null){
                    if(decorView != null){
                        decorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    return;
                }
                if(onKeyBoardListener != null){
                    onKeyBoardListener.onKeyBoardClose(height);
                }
            }

            @Override
            public void onGlobalLayout() {
                if(fragment == null || fragment.getContext() == null){
                    if(decorView != null){
                        decorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    return;
                }
                super.onGlobalLayout();
            }
        };
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        return onGlobalLayoutListener;
    }


    public static void cancelKeyBordListener(Fragment fragment, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener){
        if(fragment == null || fragment.getContext() == null || fragment.getActivity() == null || onGlobalLayoutListener == null){
            return;
        }
        View decorView = fragment.getActivity().getWindow().getDecorView();
        if(decorView != null){
            decorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }


    public abstract static class OnKeyBoardListener implements ViewTreeObserver.OnGlobalLayoutListener {
        int mDecorViewVisibleHeight;
        View mDecorView;
        public abstract  void onKeyBoardOpen(int height);

        public abstract void onKeyBoardClose(int height);

        public OnKeyBoardListener() {
        }

        public OnKeyBoardListener(View decorView) {
            this.mDecorView = decorView;
        }

        @Override
        public void onGlobalLayout() {
            if(mDecorView == null){
                return;
            }
            Rect r = new Rect();
            mDecorView.getWindowVisibleDisplayFrame(r);
            int visibleHeight = r.height();

            if (mDecorViewVisibleHeight == 0) {
                mDecorViewVisibleHeight = visibleHeight;
                return;
            }

            if (mDecorViewVisibleHeight == visibleHeight) {
                return;
            }
            int screenHeight =  mDecorView.getRootView().getHeight();
            int heightDifference = mDecorViewVisibleHeight - r.bottom;

            if (mDecorViewVisibleHeight - visibleHeight > 200) {
                onKeyBoardOpen(heightDifference);
                mDecorViewVisibleHeight = visibleHeight;
                return;
            }


            if (visibleHeight - mDecorViewVisibleHeight > 200) {
                onKeyBoardClose(visibleHeight - mDecorViewVisibleHeight);
                mDecorViewVisibleHeight = visibleHeight;
                return;
            }
        }
    }
}

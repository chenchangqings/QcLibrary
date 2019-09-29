package com.qclibrary.lib.io.http.download;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class DownloadHandler {

    private Handler mHandler;

    private static final int WHAT_UPDATE = 0x01;
    private static final String PROGRESS = "progress";

    private DownloadListener mDownloadListener;

    public void initHandler(final DownloadListener downloadListener) {
        if (mHandler != null) {
            return;
        }
        mDownloadListener = downloadListener;
        synchronized (DownloadHandler.class) {
            if (mHandler == null) {
                mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg == null) {
                            return;
                        }
                        switch (msg.what) {
                            case WHAT_UPDATE:
                                Bundle updateData = msg.getData();
                                if (updateData == null) {
                                    return;
                                }
                                int progress = updateData.getInt(PROGRESS);
                                downloadListener.onProgress(progress);
                                break;
                            default:
                                break;
                        }
                    }
                };
            }
        }
    }

    public void onProgress(int progress) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mDownloadListener.onProgress(progress);
            return;
        }
        Message message = mHandler.obtainMessage();
        message.what = WHAT_UPDATE;
        Bundle data = new Bundle();
        data.putInt(PROGRESS,  progress);
        message.setData(data);
        mHandler.sendMessage(message);
    }

}

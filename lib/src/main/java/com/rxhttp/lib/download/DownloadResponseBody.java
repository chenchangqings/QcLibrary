package com.rxhttp.lib.download;


import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


public class DownloadResponseBody extends ResponseBody {

    private ResponseBody responseBody;

    private DownloadListener mDownloadListener;
    private DownloadHandler mDownloadHandler = new DownloadHandler();

    private BufferedSource bufferedSource;


    DownloadResponseBody(ResponseBody responseBody, DownloadListener mDownloadListener) {
        this.responseBody = responseBody;
        this.mDownloadListener = mDownloadListener;
        this.mDownloadHandler.initHandler(mDownloadListener);
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                Log.e("download", "read: "+ (int) (totalBytesRead * 100 / responseBody.contentLength()));
                if (null != mDownloadListener) {
                    if (bytesRead != -1) {
                        mDownloadHandler.onProgress((int) (totalBytesRead * 100 / responseBody.contentLength()));
                    }

                }
                return bytesRead;
            }
        };

    }





}

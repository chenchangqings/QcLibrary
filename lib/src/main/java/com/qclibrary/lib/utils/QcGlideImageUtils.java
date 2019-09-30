package com.qclibrary.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.qclibrary.lib.R;

public class QcGlideImageUtils {
    private static QcGlideImageUtils mInstance;
    private RequestOptions mRoundOptions;
    private RequestOptions mRectOptions;

    public static QcGlideImageUtils getInstance() {
        if (mInstance == null) {
            mInstance = new QcGlideImageUtils();
        }
        return mInstance;
    }

    private RequestOptions getRoundOptions() {
        if (mRoundOptions == null) {
            mRoundOptions = RequestOptions.circleCropTransform();
        }
        return mRoundOptions;
    }

    private RequestOptions getRectOptions() {
        if (mRectOptions == null) {
            mRectOptions = RequestOptions.fitCenterTransform();
        }
        return mRectOptions;
    }

    public void loadRound(Context context, @NonNull ImageView view, String url, boolean showLoader) {
        if (url != null && context != null) {
            RequestOptions requestOptions = getRoundOptions();
            loadImage(context, view, url, 0, requestOptions, showLoader);
        }
    }

    public void loadRect(Context context, @NonNull ImageView view, String url, boolean showLoader) {
        if (url != null && context != null) {
            RequestOptions requestOptions = getRectOptions();
            loadImage(context, view, url, 0, requestOptions, showLoader);
        }
    }


    public void loadRect(Context context, @NonNull ImageView view, String url, boolean showLoader, int... placeholder) {
        loadRoundCorner(context, view, url, 0, showLoader, placeholder);
    }

    public void loadRound(Context context, @NonNull ImageView view, String url, boolean showLoader, int... placeholder) {
        if (url != null && context != null) {
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            if (placeholder.length > 0 && placeholder[0] != 0) {
                requestOptions = requestOptions.placeholder(placeholder[0]);
                if (placeholder.length > 1 && placeholder[1] != 0) {
                    requestOptions = requestOptions.error(placeholder[1]);
                }
            }
            loadImage(context, view, url, 0, requestOptions, showLoader);
        } else {
            view.setImageResource(placeholder[0]);
        }
    }

    public void loadRoundCorner(Context context, @NonNull ImageView view, String url, int roundedCorners, boolean showLoader, int... placeholder) {
        if (url != null && context != null) {
            RequestOptions requestOptions = RequestOptions.fitCenterTransform();
            if (placeholder.length > 0 && placeholder[0] != 0) {
                requestOptions = requestOptions.placeholder(placeholder[0]);
                if (placeholder.length > 1 && placeholder[1] != 0) {
                    requestOptions = requestOptions.error(placeholder[1]);
                }
            }
            loadImage(context, view, url, roundedCorners, requestOptions, showLoader);
        } else {
            view.setImageResource(placeholder[0]);
        }
    }

    public void loadGif(Context context, @NonNull ImageView gifView, int gifDrawable, int loopCount) {
        Glide.with(context).asGif().load(gifDrawable).listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                resource.setLoopCount(loopCount);
                return false;
            }
        }).into(gifView).clearOnDetach();
    }

    private void loadImage(Context context, @NonNull ImageView view, String url, int corners, RequestOptions options, boolean showLoader) {
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
            } else {
                if (activity.isFinishing()) {
                    return;
                }
            }
        }

        GlideRequest requests;

        requests = GlideApp.with(context)
                .load(url)
                .thumbnail(0.8f)
                .apply(options);


        if (showLoader) {
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
            circularProgressDrawable.setStrokeWidth(5);
            circularProgressDrawable.setColorFilter(context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC);
            circularProgressDrawable.setCenterRadius(30);
            circularProgressDrawable.start();

            requests = requests.placeholder(circularProgressDrawable);
        }

        if (corners > 0) {
            requests = requests.transforms(new CenterCrop(), new RoundedCorners(corners));
        }
        requests.dontAnimate().into(view);
    }
}

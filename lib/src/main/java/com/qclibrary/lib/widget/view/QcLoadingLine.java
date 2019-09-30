package com.qclibrary.lib.widget.view;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.qclibrary.lib.R;

public class QcLoadingLine extends View {
    private float mWidth;
    private float mHeight;
    private Paint mPaintLine;
    private float mProgress;
    private ValueAnimator mAnimator;
    private int mTotalTime = 30000;
    private int mSpeed = 1000;
    private boolean isInfinite = false;
    private boolean isAudioStart = false;
    private int mLoadColor = Color.YELLOW;

    public QcLoadingLine(Context context) {
        this(context, null);
    }

    public QcLoadingLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QcLoadingLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        final TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.QcLoadingLayout);
        if (array != null) {
            mTotalTime = array.getInt(R.styleable.QcLoadingLayout_qc_totalTime,
                    30000);
            mSpeed = array.getDimensionPixelSize(R.styleable.QcLoadingLayout_qc_speed,
                    1000);
            isInfinite = array.getBoolean(R.styleable.QcLoadingLayout_qc_isInfinite,
                    false);
            isAudioStart = array.getBoolean(R.styleable.QcLoadingLayout_qc_isAudioStart,
                    false);
            mLoadColor = array.getColor(R.styleable.QcLoadingLayout_qc_loadColor,
                    Color.YELLOW);
            array.recycle();
        }
        initAnimate();
        if (isAudioStart) {
            startAnim();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {
        if (mProgress <= 0.5) {
            canvas.drawLine(0, mHeight / 2, (2 * mProgress) * mWidth, mHeight / 2, mPaintLine);
        } else if (mProgress <= 1) {
            canvas.drawLine((mProgress - 0.5f) * 2 * mWidth, mHeight / 2, mWidth, mHeight / 2, mPaintLine);
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        setupPaint();
    }


    private void setupPaint() {
        if (mPaintLine == null) {
            mPaintLine = new Paint();
            mPaintLine.setAntiAlias(true);
            mPaintLine.setStyle(Paint.Style.FILL);
            mPaintLine.setStrokeWidth(mHeight);
            mPaintLine.setStrokeCap(Paint.Cap.ROUND);
            mPaintLine.setColor(mLoadColor);
        }
    }


    private void initAnimate() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofObject(new FloatEvaluator(), 0, 1);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(animation -> {
                mProgress = (float) animation.getAnimatedValue();
                postInvalidate();
            });
            mAnimator.setDuration((long) mSpeed);
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            if (isInfinite) {
                mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            } else {
                mAnimator.setRepeatCount(mTotalTime / mSpeed);
            }
        }
    }

    public void startAnim() {
        if (mAnimator != null && !mAnimator.isStarted())
            mAnimator.start();
    }

    public void stopAnim() {
        if (mAnimator != null)
            mAnimator.cancel();
        mProgress = 0;
        postInvalidate();
    }


}

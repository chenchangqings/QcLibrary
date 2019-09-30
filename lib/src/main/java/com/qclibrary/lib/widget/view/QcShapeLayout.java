/*
 * Copyright 2018 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2018-04-13 23:18:56
 *
 * GitHub: https://github.com/GcsSloop
 * WeiBo: http://weibo.com/GcsSloop
 * WebSite: http://www.gcssloop.com
 */

package com.qclibrary.lib.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.qclibrary.lib.R;


public class QcShapeLayout extends RelativeLayout {
    public float[] radii = new float[8];   // top-left, top-right, bottom-right, bottom-left
    private float leftTopRadius = 0;
    private float leftBottomRadius = 0;
    private float rightTopRadius = 0;
    private float rightBottomRadius = 0;
    private float mRadius = 0;

    private Path mPath;
    private RectF mLayer;

    public QcShapeLayout(Context context) {
        this(context, null);
    }

    public QcShapeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QcShapeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    public void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.QcShapeLayout);
            mRadius = a.getDimensionPixelSize(R.styleable.QcShapeLayout_qc_radius,
                    0);
            leftTopRadius = a.getDimensionPixelSize(R.styleable.QcShapeLayout_qc_radius_left_top,
                    0);
            leftBottomRadius = a.getDimensionPixelSize(R.styleable.QcShapeLayout_qc_radius_left_bottom,
                    0);
            rightTopRadius = a.getDimensionPixelSize(R.styleable.QcShapeLayout_qc_radius_right_top,
                    0);
            rightBottomRadius = a.getDimensionPixelSize(R.styleable.QcShapeLayout_qc_radius_right_bottom,
                    0);
            a.recycle();
        }

        setRadius(mRadius);


        mPath = new Path();
        mLayer = new RectF(0f,0f,0f,0f);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLayer.set(0, 0, (float) w,(float) h);
        refreshRegion(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        refreshRegion(this);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        int save = canvas.save();
        canvas.clipPath(mPath);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }
//
    @Override
    public void draw(Canvas canvas) {
        int save = canvas.save();
        canvas.clipPath(mPath);
        super.draw(canvas);
        canvas.restoreToCount(save);
    }

    public void refreshRegion(View view) {
        int w = (int) mLayer.width();
        int h = (int) mLayer.height();
        mLayer.left = view.getPaddingLeft();
        mLayer.top = view.getPaddingTop();
        mLayer.right = w - view.getPaddingRight();
        mLayer.bottom = h - view.getPaddingBottom();
        mPath.reset();
        mPath.addRoundRect(mLayer, radii, Path.Direction.CW);
        mPath.close();
    }


    public void setLeftCenter() {
        leftTopRadius = 0;
        leftBottomRadius = 0;
        rightTopRadius = mRadius;
        rightBottomRadius = mRadius;
        setRadii();
        update();
    }


    public void setLeftTop() {
        leftTopRadius = mRadius;
        leftBottomRadius = 0;
        rightTopRadius = mRadius;
        rightBottomRadius = mRadius;
        setRadii();
        update();
    }


    public void setLeftBottom() {
        leftTopRadius = 0;
        leftBottomRadius = mRadius;
        rightTopRadius = mRadius;
        rightBottomRadius = mRadius;
        setRadii();
        update();
    }


    public void setRightCenter() {
        leftTopRadius = mRadius;
        leftBottomRadius = mRadius;
        rightTopRadius = 0;
        rightBottomRadius = 0;
        setRadii();
        update();
    }


    public void setRightTop() {
        leftTopRadius = mRadius;
        leftBottomRadius = mRadius;
        rightTopRadius = mRadius;
        rightBottomRadius = 0;
        setRadii();
        update();
    }


    public void setRightBottom() {
        leftTopRadius = mRadius;
        leftBottomRadius = mRadius;
        rightTopRadius = 0;
        rightBottomRadius = mRadius;
        setRadii();
        update();
    }


    public void setRadius(float radius) {
        mRadius = radius;
        leftTopRadius = leftTopRadius == 0 ? mRadius : leftTopRadius;
        leftBottomRadius = leftBottomRadius == 0 ? mRadius : leftBottomRadius;;
        rightTopRadius = rightTopRadius == 0 ? mRadius : rightTopRadius;;
        rightBottomRadius = rightBottomRadius == 0 ? mRadius : rightBottomRadius;;
        setRadii();
    }

    public void setAll() {
        leftTopRadius = mRadius;
        leftBottomRadius = mRadius;
        rightTopRadius = mRadius;
        rightBottomRadius = mRadius;
        setRadii();
        update();
    }

    private void setRadii() {
        radii[0] = leftTopRadius;
        radii[1] = leftTopRadius;
        radii[2] = rightTopRadius;
        radii[3] = rightTopRadius;
        radii[4] = rightBottomRadius;
        radii[5] = rightBottomRadius;
        radii[6] = leftBottomRadius;
        radii[7] = leftBottomRadius;
    }


    private void update() {
        invalidate();
        forceLayout();
        requestLayout();
    }



}

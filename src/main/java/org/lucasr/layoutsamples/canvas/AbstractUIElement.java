/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lucasr.layoutsamples.canvas;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public abstract class AbstractUIElement implements UIElement {
    protected UIElementHost mHost;

    private int mMeasuredWidth;
    private int mMeasuredHeight;

    private Rect mBounds = new Rect();
    private Rect mPadding = new Rect();

    private int mVisibility = View.VISIBLE;

    public AbstractUIElement(UIElementHost host) {
        swapHost(host);
    }

    protected void onAttachedToHost() {
    }

    protected void onDetachedFromHost() {
    }

    @Override
    public boolean swapHost(UIElementHost host) {
        if (mHost == host) {
            return false;
        }

        if (mHost != null) {
            onDetachedFromHost();
        }

        mHost = host;

        if (mHost != null) {
            onAttachedToHost();
        }

        return true;
    }

    protected void setMeasuredDimension(int width, int height) {
        mMeasuredWidth = width;
        mMeasuredHeight = height;
    }

    @Override
    public int getMeasuredWidth() {
        return mMeasuredWidth;
    }

    @Override
    public int getMeasuredHeight() {
        return mMeasuredHeight;
    }

    @Override
    public int getPaddingLeft() {
        return mPadding.left;
    }

    @Override
    public int getPaddingTop() {
        return mPadding.top;
    }

    @Override
    public int getPaddingRight() {
        return mPadding.right;
    }

    @Override
    public int getPaddingBottom() {
        return mPadding.bottom;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mPadding.left = left;
        mPadding.top = top;
        mPadding.right = right;
        mPadding.bottom = bottom;

        mHost.requestLayout();
    }

    @Override
    public int getVisibility() {
        return mVisibility;
    }

    @Override
    public void setVisibility(int visibility) {
        if (mVisibility == visibility) {
            return;
        }

        mVisibility = visibility;

        requestLayout();
        invalidate();
    }

    @Override
    public final void draw(Canvas canvas) {
        final int saveCount = canvas.getSaveCount();
        canvas.save();

        canvas.clipRect(mBounds);
        canvas.translate(mBounds.left, mBounds.top);

        onDraw(canvas);

        canvas.restoreToCount(saveCount);
    }

    @Override
    public final void measure(int widthMeasureSpec, int heightMeasureSpec) {
        onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public final void layout(int left, int top, int right, int bottom) {
        mBounds.left = left;
        mBounds.top = top;
        mBounds.right = right;
        mBounds.bottom = bottom;

        onLayout(left, top, right, bottom);
    }

    @Override
    public int getLeft() {
        return mBounds.left;
    }

    @Override
    public int getTop() {
        return mBounds.top;
    }

    @Override
    public int getRight() {
        return mBounds.right;
    }

    @Override
    public int getBottom() {
        return mBounds.bottom;
    }

    @Override
    public int getWidth() {
        return mBounds.right - mBounds.left;
    }

    @Override
    public int getHeight() {
        return mBounds.bottom - mBounds.top;
    }

    @Override
    public Context getContext() {
        return mHost.getContext();
    }

    @Override
    public Resources getResources() {
        return mHost.getResources();
    }

    @Override
    public void requestLayout() {
        mHost.requestLayout();
    }

    @Override
    public void invalidate() {
        mHost.invalidate(mBounds.left, mBounds.top, mBounds.right, mBounds.bottom);
    }

    protected abstract void onDraw(Canvas canvas);

    protected abstract void onMeasure(int widthMeasureSpec, int heightMeasureSpec);

    protected abstract void onLayout(int left, int top, int right, int bottom);

    public abstract void drawableStateChanged();
}

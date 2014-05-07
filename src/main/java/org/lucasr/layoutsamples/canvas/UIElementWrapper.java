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
import android.view.ViewGroup;

public class UIElementWrapper implements UIElement {
    private final UIElement mWrappedElement;

    public UIElementWrapper(UIElement element) {
        mWrappedElement = element;
    }

    @Override
    public boolean swapHost(UIElementHost host) {
        return mWrappedElement.swapHost(host);
    }

    @Override
    public boolean isAttachedToHost() {
        return mWrappedElement.isAttachedToHost();
    }

    @Override
    public int getId() {
        return mWrappedElement.getId();
    }

    @Override
    public int getMeasuredWidth() {
        return mWrappedElement.getMeasuredWidth();
    }

    @Override
    public int getMeasuredHeight() {
        return mWrappedElement.getMeasuredHeight();
    }

    @Override
    public int getPaddingLeft() {
        return mWrappedElement.getPaddingLeft();
    }

    @Override
    public int getPaddingTop() {
        return mWrappedElement.getPaddingTop();
    }

    @Override
    public int getPaddingRight() {
        return mWrappedElement.getPaddingRight();
    }

    @Override
    public int getPaddingBottom() {
        return mWrappedElement.getPaddingBottom();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mWrappedElement.setPadding(left, top, right, bottom);
    }

    @Override
    public int getLeft() {
        return mWrappedElement.getLeft();
    }

    @Override
    public int getTop() {
        return mWrappedElement.getTop();
    }

    @Override
    public int getRight() {
        return mWrappedElement.getRight();
    }

    @Override
    public int getBottom() {
        return mWrappedElement.getBottom();
    }

    @Override
    public int getWidth() {
        return mWrappedElement.getWidth();
    }

    @Override
    public int getHeight() {
        return mWrappedElement.getHeight();
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams lp) {
        mWrappedElement.setLayoutParams(lp);
    }

    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        return mWrappedElement.getLayoutParams();
    }

    @Override
    public void onFinishInflate() {
        mWrappedElement.onFinishInflate();
    }

    @Override
    public void measure(int widthMeasureSpec, int heightMeasureSpec) {
        mWrappedElement.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void layout(int left, int top, int right, int bottom) {
        mWrappedElement.layout(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        mWrappedElement.draw(canvas);
    }

    @Override
    public void drawableStateChanged() {
        mWrappedElement.drawableStateChanged();
    }

    @Override
    public Context getContext() {
        return mWrappedElement.getContext();
    }

    @Override
    public Resources getResources() {
        return mWrappedElement.getResources();
    }

    @Override
    public void requestLayout() {
        mWrappedElement.requestLayout();
    }

    @Override
    public void invalidate() {
        mWrappedElement.invalidate();
    }

    @Override
    public int getVisibility() {
        return mWrappedElement.getVisibility();
    }

    @Override
    public void setVisibility(int visibility) {
        mWrappedElement.setVisibility(visibility);
    }

    public UIElement getWrappedElement() {
        return mWrappedElement;
    }
}

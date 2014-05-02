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
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class UIElementView extends View implements UIElementHost {
    private UIElement mUIElement;

    public UIElementView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UIElementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mUIElement != null) {
            mUIElement.swapHost(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mUIElement != null) {
            mUIElement.swapHost(null);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int saveCount = canvas.getSaveCount();
        canvas.save();

        if (mUIElement != null) {
            mUIElement.draw(canvas);
        }

        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = 0;
        int measuredHeight = 0;

        if (mUIElement != null) {
            final int paddingLeft = getPaddingLeft();
            final int paddingTop = getPaddingTop();
            final int paddingRight = getPaddingRight();
            final int paddingBottom = getPaddingBottom();

            final int viewWidthSize = MeasureSpec.getSize(widthMeasureSpec);
            final int viewWidthMode = MeasureSpec.getMode(widthMeasureSpec);
            final int viewHeightSize = MeasureSpec.getSize(heightMeasureSpec);
            final int viewHeightMode = MeasureSpec.getMode(heightMeasureSpec);

            final int elementWidth = viewWidthSize - paddingLeft - paddingRight;
            final int elementWidthSpec = MeasureSpec.makeMeasureSpec(elementWidth, viewWidthMode);
            final int elementHeight = viewHeightSize - paddingTop - paddingBottom;
            final int elementHeightSpec = MeasureSpec.makeMeasureSpec(elementHeight, viewHeightMode);

            mUIElement.measure(elementWidthSpec, elementHeightSpec);

            measuredWidth = mUIElement.getMeasuredWidth() + paddingLeft + paddingRight;
            measuredHeight = mUIElement.getMeasuredHeight() + paddingTop + paddingBottom;
        }

        measuredWidth = Math.max(measuredWidth, getSuggestedMinimumWidth());
        measuredHeight = Math.max(measuredHeight, getSuggestedMinimumHeight());

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mUIElement != null) {
            final int paddingLeft = getPaddingLeft();
            final int paddingTop = getPaddingTop();

            final int elementLeft = paddingLeft;
            final int elementTop = paddingTop;
            final int elementRight = right - left - getPaddingRight();
            final int elementBottom = bottom - top - getPaddingBottom();

            mUIElement.layout(elementLeft, elementTop, elementRight, elementBottom);
        }
    }

    @Override
    public void drawableStateChanged() {
        if (mUIElement != null) {
            mUIElement.drawableStateChanged();
        }
    }

    public UIElement getUIElement() {
        return mUIElement;
    }

    public void setUIElement(UIElement element) {
        if (mUIElement == element) {
            return;
        }

        if (mUIElement != null) {
            mUIElement.swapHost(null);
        }

        mUIElement = element;

        if (mUIElement != null) {
            mUIElement.swapHost(this);
        }

        requestLayout();
        invalidate();
    }
}

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
import android.view.ViewGroup.LayoutParams;

public interface UIElement {
    public boolean swapHost(UIElementHost host);
    public boolean isAttachedToHost();

    public int getId();

    public int getMeasuredWidth();
    public int getMeasuredHeight();

    public int getPaddingLeft();
    public int getPaddingTop();
    public int getPaddingRight();
    public int getPaddingBottom();
    public void setPadding(int left, int top, int right, int bottom);

    public int getLeft();
    public int getTop();
    public int getRight();
    public int getBottom();

    public int getWidth();
    public int getHeight();

    public void setLayoutParams(LayoutParams lp);
    public LayoutParams getLayoutParams();

    public void onFinishInflate();

    public void measure(int widthMeasureSpec, int heightMeasureSpec);
    public void layout(int left, int top, int right, int bottom);
    public void draw(Canvas canvas);
    public void drawableStateChanged();

    public Context getContext();
    public Resources getResources();

    public void requestLayout();
    public void invalidate();

    public int getVisibility();
    public void setVisibility(int visibility);
}
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

package org.lucasr.layoutsamples.async;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup.LayoutParams;

import org.lucasr.layoutsamples.canvas.UIElementHost;

public class HeadlessElementHost implements UIElementHost {
    private final Context mContext;

    public HeadlessElementHost(Context context) {
        mContext = context;
    }

    @Override
    public void requestLayout() {
    }

    @Override
    public void invalidate() {
    }

    @Override
    public void invalidate(int left, int top, int right, int bottom) {
    }

    @Override
    public int[] getDrawableState() {
        return new int[0];
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public Resources getResources() {
        return mContext.getResources();
    }

    @Override
    public void invalidateDrawable(Drawable who) {
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
    }

    @Override
    public void unscheduleDrawable(Drawable who) {
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
    }
}

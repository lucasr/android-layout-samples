package org.lucasr.layoutsamples.canvas;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup.LayoutParams;

public interface UIElementHost {
    public void requestLayout();
    public LayoutParams getLayoutParams();

    public void invalidate();
    public void invalidate(int left, int top, int right, int bottom);

    public int[] getDrawableState();

    public Context getContext();
    public Resources getResources();

    public void invalidateDrawable(Drawable who);
    public void scheduleDrawable(Drawable who, Runnable what, long when);
    public void unscheduleDrawable(Drawable who);
    public void unscheduleDrawable(Drawable who, Runnable what);
}

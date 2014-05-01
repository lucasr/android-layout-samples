package org.lucasr.layoutsamples.async;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup.LayoutParams;

import org.lucasr.layoutsamples.canvas.UIElementHost;

public class HeadlessElementHost implements UIElementHost {
    private final Context mContext;
    private final LayoutParams mLayoutParams;

    public HeadlessElementHost(Context context) {
        mContext = context;
        mLayoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void requestLayout() {
    }

    @Override
    public LayoutParams getLayoutParams() {
        return mLayoutParams;
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

    public void setTargetWidth(int targetWidth) {
        mLayoutParams.width = targetWidth;
    }
}

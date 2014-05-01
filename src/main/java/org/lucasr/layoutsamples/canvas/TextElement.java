package org.lucasr.layoutsamples.canvas;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.BoringLayout;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.Spannable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.FloatMath;
import android.util.TypedValue;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

// TODO: introduce max height (line or pixels)

public class TextElement extends AbstractUIElement {
    private CharSequence mText;

    private ColorStateList mTextColor;
    private int mCurTextColor;

    private float mLineSpacingMult = 1.0f;
    private float mLineSpacingAdd = 0.0f;
    private boolean mIncludeFontPadding = true;
    private Layout.Alignment mLayoutAlignment = Layout.Alignment.ALIGN_NORMAL;

    private Layout mLayout;
    private BoringLayout mSavedLayout;

    private TextPaint mPaint;
    private TextUtils.TruncateAt mEllipsize;
    private BoringLayout.Metrics mBoring;

    private static final BoringLayout.Metrics UNKNOWN_BORING = new BoringLayout.Metrics();

    public TextElement(UIElementHost host) {
        super(host);
    }

    @Override
    protected void init() {
        super.init();
        setTextColor(ColorStateList.valueOf(0xFF000000));
    }

    private int getDesiredWidth() {
        final int lineCount = mLayout.getLineCount();
        final CharSequence text = mLayout.getText();

        // If any line was wrapped, we can't use it. but it's
        // ok for the last line not to have a newline.
        for (int i = 0; i < lineCount - 1; i++) {
            if (text.charAt(mLayout.getLineEnd(i) - 1) != '\n') {
                return -1;
            }
        }

        float maxWidth = 0;
        for (int i = 0; i < lineCount; i++) {
            maxWidth = Math.max(maxWidth, mLayout.getLineWidth(i));
        }

        return (int) FloatMath.ceil(maxWidth);
    }

    private int getDesiredHeight() {
        if (mLayout == null) {
            return 0;
        }

        final int lineCount = mLayout.getLineCount();
        return mLayout.getLineTop(lineCount) + getPaddingTop() + getPaddingBottom();
    }

    private void makeNewLayout(int wantWidth, BoringLayout.Metrics boring,
            int ellipsisWidth, boolean bringIntoView) {
        if (wantWidth < 0) {
            wantWidth = 0;
        }

        boolean shouldEllipsize = (mEllipsize != null);

        mLayout = makeSingleLayout(wantWidth, boring, ellipsisWidth, mLayoutAlignment, shouldEllipsize,
                mEllipsize, bringIntoView);
    }

    private Layout makeSingleLayout(int wantWidth, BoringLayout.Metrics boring, int ellipsisWidth,
                                    Layout.Alignment alignment, boolean shouldEllipsize, TruncateAt effectiveEllipsize,
                                    boolean useSaved) {
        Layout result;

        if (mText instanceof Spannable) {
            result = new DynamicLayout(mText, mText, mPaint, wantWidth,
                    alignment, mLineSpacingMult,
                    mLineSpacingAdd, mIncludeFontPadding, effectiveEllipsize, ellipsisWidth);
        } else {
            if (boring == UNKNOWN_BORING) {
                boring = BoringLayout.isBoring(mText, mPaint, mBoring);
                if (boring != null) {
                    mBoring = boring;
                }
            }

            if (boring != null) {
                if (boring.width <= wantWidth &&
                        (effectiveEllipsize == null || boring.width <= ellipsisWidth)) {
                    if (mSavedLayout != null) {
                        result = mSavedLayout.replaceOrMake(mText, mPaint,
                                wantWidth, alignment, mLineSpacingMult, mLineSpacingAdd,
                                boring, mIncludeFontPadding);
                    } else {
                        result = BoringLayout.make(mText, mPaint,
                                wantWidth, alignment, mLineSpacingMult, mLineSpacingAdd,
                                boring, mIncludeFontPadding);
                    }

                    if (useSaved) {
                        mSavedLayout = (BoringLayout) result;
                    }
                } else if (shouldEllipsize && boring.width <= wantWidth) {
                    if (useSaved && mSavedLayout != null) {
                        result = mSavedLayout.replaceOrMake(mText, mPaint,
                                wantWidth, alignment, mLineSpacingMult, mLineSpacingAdd,
                                boring, mIncludeFontPadding, effectiveEllipsize,
                                ellipsisWidth);
                    } else {
                        result = BoringLayout.make(mText, mPaint,
                                wantWidth, alignment, mLineSpacingMult, mLineSpacingAdd,
                                boring, mIncludeFontPadding, effectiveEllipsize,
                                ellipsisWidth);
                    }
                } else if (shouldEllipsize) {
                    result = new StaticLayout(mText,
                            0, mText.length(),
                            mPaint, wantWidth, alignment, mLineSpacingMult,
                            mLineSpacingAdd, mIncludeFontPadding, effectiveEllipsize,
                            ellipsisWidth);
                } else {
                    result = new StaticLayout(mText, mPaint,
                            wantWidth, alignment, mLineSpacingMult, mLineSpacingAdd,
                            mIncludeFontPadding);
                }
            } else if (shouldEllipsize) {
                result = new StaticLayout(mText,
                        0, mText.length(),
                        mPaint, wantWidth, alignment, mLineSpacingMult,
                        mLineSpacingAdd, mIncludeFontPadding, effectiveEllipsize,
                        ellipsisWidth);
            } else {
                result = new StaticLayout(mText, mPaint,
                        wantWidth, alignment, mLineSpacingMult, mLineSpacingAdd,
                        mIncludeFontPadding);
            }
        }

        return result;
    }

    private void resetAndSaveLayout() {
        if (mLayout instanceof BoringLayout && mSavedLayout == null) {
            mSavedLayout = (BoringLayout) mLayout;
        }

        mBoring = null;
    }

    private void checkForRelayout() {
        if (mLayout == null || mHost == null) {
            return;
        }

        final LayoutParams lp = mHost.getLayoutParams();

        // If we have a fixed width, we can just swap in a new text layout
        // if the text height stays the same or if the view height is fixed.
        if (lp.width != LayoutParams.WRAP_CONTENT) {
            // Static width, so try making a new text layout.

            final int oldHeight = mLayout.getHeight();
            final int oldWidth = mLayout.getWidth();

            // No need to bring the text into view, since the size is not
            // changing (unless we do the requestLayout(), in which case it
            // will happen when measuring).
            makeNewLayout(oldWidth, UNKNOWN_BORING, oldWidth, false);

            // In a fixed-height view, so use our new text layout.
            if (lp.height != ViewGroup.LayoutParams.WRAP_CONTENT &&
                    lp.height != ViewGroup.LayoutParams.MATCH_PARENT) {
                invalidate();
                return;
            }

            // Dynamic height, but height has stayed the same,
            // so use our new text layout.
            if (mLayout.getHeight() == oldHeight) {
                invalidate();
                return;
            }

            // We lose: the height has changed and we have a dynamic height.
            // Request a new view layout using our new text layout.
            requestLayout();
            invalidate();
        } else {
            // Dynamic width, so we have no choice but to request a new
            // view layout with a new text layout.
            recreateLayout();
        }
    }

    private void recreateLayout() {
        if (mLayout == null) {
            return;
        }

        resetAndSaveLayout();
        requestLayout();
        invalidate();
    }

    private void updateTextColors() {
        int color = mTextColor.getColorForState(mHost.getDrawableState(), 0);
        if (color != mCurTextColor) {
            mCurTextColor = color;
            invalidate();
        }
    }

    @Override
    public boolean swapHost(UIElementHost host) {
        boolean changed = super.swapHost(host);

        if (changed && host != null) {
            final Resources res = mHost.getResources();
            mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            mPaint.density = res.getDisplayMetrics().density;
        }

        return changed;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLayout == null) {
            return;
        }

        final int saveCount = canvas.getSaveCount();
        canvas.save();

        mPaint.setColor(mCurTextColor);

        float clipLeft = getPaddingLeft();
        float clipTop = getPaddingTop();
        float clipRight = getRight() - getPaddingRight();
        float clipBottom = getBottom() - getPaddingBottom();
        canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom);

        canvas.translate(getPaddingLeft(), getPaddingTop());
        mLayout.draw(canvas);

        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();

        int width;
        int height;

        BoringLayout.Metrics boring = UNKNOWN_BORING;

        int desiredWidth = -1;
        boolean fromExisting = false;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            if (mLayout != null && mEllipsize == null) {
                desiredWidth = getDesiredWidth();
            }

            if (desiredWidth < 0) {
                boring = BoringLayout.isBoring(mText, mPaint, mBoring);
                if (boring != null) {
                    mBoring = boring;
                }
            } else {
                fromExisting = true;
            }

            if (boring == null || boring == UNKNOWN_BORING) {
                if (desiredWidth < 0) {
                    desiredWidth = (int) FloatMath.ceil(Layout.getDesiredWidth(mText, mPaint));
                }

                width = desiredWidth;
            } else {
                width = boring.width;
            }

            width += paddingLeft + paddingRight;

            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(widthSize, width);
            }
        }

        int unpaddedWidth = width - paddingLeft - paddingRight;

        if (mLayout == null) {
            makeNewLayout(unpaddedWidth, boring, unpaddedWidth, false);
        } else {
            final boolean layoutChanged =
                    (mLayout.getWidth() != unpaddedWidth) ||
                            (mLayout.getEllipsizedWidth() != unpaddedWidth);

            final boolean widthChanged =
                    (mEllipsize == null) &&
                            (unpaddedWidth > mLayout.getWidth()) &&
                            (mLayout instanceof BoringLayout ||
                                    (fromExisting && desiredWidth >= 0 && desiredWidth <= unpaddedWidth));

            if (layoutChanged) {
                if (widthChanged) {
                    mLayout.increaseWidthTo(unpaddedWidth);
                } else {
                    makeNewLayout(unpaddedWidth, boring, unpaddedWidth, false);
                }
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getDesiredHeight();

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(heightSize, height);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(int left, int top, int right, int bottom) {
        // Do nothing.
    }

    @Override
    public void drawableStateChanged() {
        if (mTextColor != null && mTextColor.isStateful()) {
            updateTextColors();
        }
    }

    public void setRawTextSize(float size) {
        if (mPaint.getTextSize() == size) {
            return;
        }

        mPaint.setTextSize(size);
        recreateLayout();
    }

    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setTextSize(int unit, float size) {
        final Resources res = mHost.getResources();

        final float textSize = TypedValue.applyDimension(unit, size, res.getDisplayMetrics());
        if (mPaint.getTextSize() == textSize) {
            return;
        }

        mPaint.setTextSize(textSize);
        recreateLayout();
    }

    public void setTextAlignment(Layout.Alignment alignment) {
        if (mLayoutAlignment == alignment) {
            return;
        }

        mLayoutAlignment = alignment;
        recreateLayout();
    }

    public void setTextColor(int color) {
        mTextColor = ColorStateList.valueOf(color);
        updateTextColors();
    }

    public void setTextColor(ColorStateList colors) {
        if (colors == null) {
            throw new NullPointerException();
        }

        mTextColor = colors;
        updateTextColors();
    }

    public void setEllipsize(TruncateAt ellipsize) {
        if (mEllipsize == ellipsize) {
            return;
        }

        mEllipsize = ellipsize;
        recreateLayout();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        recreateLayout();
    }

    public void setText(CharSequence text) {
        if (TextUtils.equals(mText, text)) {
            return;
        }

        mText = text;
        checkForRelayout();
    }
}
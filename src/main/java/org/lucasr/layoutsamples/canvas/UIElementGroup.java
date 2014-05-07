/*
 * Copyright (C) 2007 The Android Open Source Project
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

import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

import java.util.ArrayList;
import java.util.List;

public abstract class UIElementGroup extends AbstractUIElement {
    private final List<UIElement> mElements;

    public UIElementGroup(UIElementHost host) {
        this(host, null);
    }

    public UIElementGroup(UIElementHost host, AttributeSet attrs) {
        super(host, attrs);
        mElements = new ArrayList<UIElement>();
    }

    @Override
    public boolean swapHost(UIElementHost host) {
        boolean changed = super.swapHost(host);

        if (mElements != null) {
            for (UIElement element : mElements) {
                element.swapHost(host);
            }
        }

        return changed;
    }

    @Override
    public void onAttachedToHost() {
        super.onAttachedToHost();

        if (mElements != null) {
            for (UIElement element : mElements) {
                if (element instanceof AbstractUIElement) {
                    ((AbstractUIElement) element).onAttachedToHost();
                }
            }
        }
    }

    @Override
    public void onDetachedFromHost() {
        super.onDetachedFromHost();

        for (UIElement element : mElements) {
            if (element instanceof AbstractUIElement) {
                ((AbstractUIElement) element).onDetachedFromHost();
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        final int saveCount = canvas.getSaveCount();
        canvas.save();

        for (UIElement element : mElements) {
            if (element.getVisibility() == View.VISIBLE) {
                element.draw(canvas);
            }
        }

        canvas.restoreToCount(saveCount);
    }

    @Override
    public void drawableStateChanged() {
        for (UIElement element : mElements) {
            element.drawableStateChanged();
        }
    }

    public void addElement(UIElement element) {
        LayoutParams lp = element.getLayoutParams();
        if (lp == null) {
            lp = generateDefaultLayoutParams();
        }

        addElement(element, lp);
    }

    public void addElement(UIElement element, LayoutParams lp) {
        if (!checkLayoutParams(lp)) {
            lp = generateLayoutParams(lp);
        }

        element.setLayoutParams(lp);
        mElements.add(element);
        requestLayout();
    }

    public void removeElement(UIElement element) {
        mElements.remove(element);
        requestLayout();
    }

    public UIElement findElementById(int id) {
        for (UIElement element : mElements) {
            if (element.getId() == id) {
                return element;
            }
        }

        return null;
    }

    protected boolean checkLayoutParams(LayoutParams lp) {
        return  (lp != null && lp instanceof MarginLayoutParams);
    }

    protected LayoutParams generateLayoutParams(LayoutParams lp) {
        if (lp == null) {
            return generateDefaultLayoutParams();
        }

        return new MarginLayoutParams(lp.width, lp.height);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    protected void measureElementWithMargins(UIElement element,
                                             int parentWidthMeasureSpec, int widthUsed,
                                             int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) element.getLayoutParams();

        final int childWidthMeasureSpec = getElementMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width
        );

        final int childHeightMeasureSpec = getElementMeasureSpec(parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height
        );

        element.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    protected static int getElementMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);

        int size = Math.max(0, specSize - padding);

        int resultSize = 0;
        int resultMode = 0;

        switch (specMode) {
            // Parent has imposed an exact size on us
            case MeasureSpec.EXACTLY:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size. So be it.
                    resultSize = size;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // Parent has imposed a maximum size on us
            case MeasureSpec.AT_MOST:
                if (childDimension >= 0) {
                    // Child wants a specific size... so be it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size, but our size is not fixed.
                    // Constrain child to not be bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // Parent asked to see how big we want to be
            case MeasureSpec.UNSPECIFIED:
                if (childDimension >= 0) {
                    // Child wants a specific size... let him have it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size... find out how big it should
                    // be
                    resultSize = 0;
                    resultMode = MeasureSpec.UNSPECIFIED;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size.... find out how
                    // big it should be
                    resultSize = 0;
                    resultMode = View.MeasureSpec.UNSPECIFIED;
                }
                break;
        }

        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }
}

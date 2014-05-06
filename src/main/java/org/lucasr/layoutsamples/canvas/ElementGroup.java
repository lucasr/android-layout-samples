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

import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class ElementGroup extends AbstractUIElement {
    private final List<UIElement> mElements;

    public ElementGroup(UIElementHost host) {
        super(host);
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

    protected void addElement(UIElement element) {
        mElements.add(element);
        requestLayout();
    }

    protected void removeElement(UIElement element) {
        mElements.remove(element);
        requestLayout();
    }
}

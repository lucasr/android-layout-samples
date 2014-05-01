package org.lucasr.layoutsamples.canvas;

import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class ElementGroup extends AbstractUIElement {
    protected enum UIElementType {
        IMAGE,
        TEXT
    }

    private List<UIElement> mElements;

    public ElementGroup(UIElementHost host) {
        super(host);
    }

    @Override
    protected void init() {
        super.init();
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

    protected UIElement addElement(UIElementType type) {
        final UIElement element;
        switch (type) {
            case IMAGE:
                element = new ImageElement(mHost);
                break;

            case TEXT:
                element = new TextElement(mHost);
                break;

            default:
                throw new IllegalArgumentException("Unrecognized UI element type");
        }

        mElements.add(element);
        requestLayout();

        return element;
    }

    protected void removeElement(UIElement element) {
        mElements.remove(element);
        requestLayout();
    }
}

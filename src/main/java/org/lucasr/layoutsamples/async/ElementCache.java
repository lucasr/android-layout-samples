package org.lucasr.layoutsamples.async;

import android.util.LruCache;

import org.lucasr.layoutsamples.canvas.UIElement;

public class ElementCache extends LruCache<Long, UIElement> {
    private static final int MAX_CACHED_ELEMENTS = 30;

    public ElementCache() {
        super(MAX_CACHED_ELEMENTS);
    }
}

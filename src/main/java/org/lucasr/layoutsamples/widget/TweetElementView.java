package org.lucasr.layoutsamples.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.layoutsamples.adapter.TweetPresenter;
import org.lucasr.layoutsamples.canvas.UIElementView;

import java.util.EnumSet;

public class TweetElementView extends UIElementView implements TweetPresenter {
    public TweetElementView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TweetElementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUIElement(new TweetElement(this));
    }

    @Override
    public void update(Tweet tweet, EnumSet<UpdateFlags> flags) {
        TweetElement element = (TweetElement) getUIElement();
        element.update(tweet, flags);
    }
}

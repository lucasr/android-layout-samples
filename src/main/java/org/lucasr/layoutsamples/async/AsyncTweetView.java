package org.lucasr.layoutsamples.async;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import org.lucasr.layoutsamples.app.App;
import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.layoutsamples.adapter.TweetPresenter;
import org.lucasr.layoutsamples.canvas.UIElement;
import org.lucasr.layoutsamples.canvas.UIElementView;

import java.util.EnumSet;

public class AsyncTweetView extends UIElementView implements TweetPresenter {
    private Tweet mTweet;

    public AsyncTweetView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AsyncTweetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void update(Tweet tweet, EnumSet<UpdateFlags> flags) {
        mTweet = tweet;

        final Context context = getContext();

        AsyncTweetElement element =
                (AsyncTweetElement) App.getInstance(context).getElementCache().get(tweet.getId());

        if (element == null) {
            element = AsyncTweetElementFactory.create(context, tweet);
        }
        setUIElement(element);

        element.update(tweet, flags);
    }
}

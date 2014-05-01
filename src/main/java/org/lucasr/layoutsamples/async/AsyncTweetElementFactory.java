package org.lucasr.layoutsamples.async;

import android.content.Context;
import android.view.View;

import org.lucasr.layoutsamples.adapter.TweetPresenter;
import org.lucasr.layoutsamples.app.App;
import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.layoutsamples.widget.TweetElement;

import java.util.EnumSet;

public class AsyncTweetElementFactory {
    private AsyncTweetElementFactory() {
    }

    private static volatile int sTargetWidth;
    private static HeadlessElementHost sHeadlessHost;

    public static void setTargetWidth(Context context, int targetWidth) {
        if (sTargetWidth == targetWidth) {
            return;
        }

        sTargetWidth = targetWidth;

        if (sHeadlessHost == null) {
            sHeadlessHost = new HeadlessElementHost(context);
        }
        sHeadlessHost.setTargetWidth(targetWidth);
    }

    public static AsyncTweetElement create(Context context, Tweet tweet) {
        UIElementCache elementCache = App.getInstance(context).getElementCache();

        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(sTargetWidth,
                View.MeasureSpec.EXACTLY);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);

        final TweetElement element = new TweetElement(sHeadlessHost);
        element.update(tweet, EnumSet.of(TweetPresenter.UpdateFlags.NO_IMAGE_LOADING));
        element.measure(widthMeasureSpec, heightMeasureSpec);
        element.layout(0, 0, element.getMeasuredWidth(), element.getMeasuredHeight());

        final AsyncTweetElement asyncElement = new AsyncTweetElement(element, sHeadlessHost);
        elementCache.put(tweet.getId(), asyncElement);

        return asyncElement;
    }
}

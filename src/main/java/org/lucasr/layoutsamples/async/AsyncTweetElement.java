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

package org.lucasr.layoutsamples.async;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import com.squareup.picasso.Picasso;

import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.layoutsamples.app.R;
import org.lucasr.layoutsamples.widget.TweetElement;
import org.lucasr.layoutsamples.adapter.TweetPresenter;
import org.lucasr.layoutsamples.canvas.UIElementHost;
import org.lucasr.layoutsamples.canvas.UIElementWrapper;

import java.util.EnumSet;

public class AsyncTweetElement extends UIElementWrapper implements TweetPresenter {
    private final Paint mIndicatorPaint;
    private final int mIndicatorSize;

    public AsyncTweetElement(TweetElement element) {
        super(element);

        final Resources res = getResources();

        mIndicatorPaint = new Paint();
        mIndicatorSize = res.getDimensionPixelSize(R.dimen.tweet_padding);

        boolean onMainThread = (Looper.myLooper() == Looper.getMainLooper());
        final int indicatorColor = onMainThread ? R.color.tweet_on_main_thread :
                                                  R.color.tweet_off_main_thread;
        mIndicatorPaint.setColor(res.getColor(indicatorColor));
    }

    @Override
    public void measure(int widthMeasureSpec, int heightMeasureSpec) {
        // Do nothing, the wrapped UIElement is already measured.
    }

    @Override
    public void layout(int left, int top, int right, int bottom) {
        // Do nothing, the wrapped UIElement is already sized and positioned.
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(0, 0, mIndicatorSize, mIndicatorSize, mIndicatorPaint);
    }

    @Override
    public void requestLayout() {
        // Do nothing, we never change the wrapped element's layout.
    }

    @Override
    public void update(Tweet tweet, EnumSet<UpdateFlags> flags) {
        TweetElement element = (TweetElement) getWrappedElement();
        element.loadProfileImage(tweet, flags);

        final boolean hasPostImage = !TextUtils.isEmpty(tweet.getPostImageUrl());
        if (hasPostImage) {
            element.loadPostImage(tweet, flags);
        }
    }
}

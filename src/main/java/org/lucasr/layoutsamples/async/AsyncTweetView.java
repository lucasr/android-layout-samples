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

        final AsyncTweetElement element = AsyncTweetElementFactory.create(getContext(), tweet);
        setUIElement(element);

        element.update(tweet, flags);
    }
}

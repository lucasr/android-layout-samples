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
import android.view.View;
import android.widget.Adapter;

import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.layoutsamples.app.App;
import org.lucasr.layoutsamples.canvas.UIElement;
import org.lucasr.smoothie.SimpleItemLoader;

public class TweetsLayoutLoader extends SimpleItemLoader<Tweet, UIElement> {
    private final Context mContext;
    private final UIElementCache mElementCache;

    public TweetsLayoutLoader(Context context) {
        mContext = context;
        mElementCache = App.getInstance(context).getElementCache();
    }

    @Override
    public Tweet getItemParams(Adapter adapter, int position) {
        return (Tweet) adapter.getItem(position);
    }

    @Override
    public UIElement loadItem(Tweet tweet) {
        return AsyncTweetElementFactory.create(mContext, tweet);
    }

    @Override
    public UIElement loadItemFromMemory(Tweet tweet) {
        return mElementCache.get(tweet.getId());
    }

    @Override
    public void displayItem(View itemView, UIElement result, boolean fromMemory) {
        // Do nothing as we're only using this loader to pre-measure/layout
        // TweetElements that are off screen.
    }
}

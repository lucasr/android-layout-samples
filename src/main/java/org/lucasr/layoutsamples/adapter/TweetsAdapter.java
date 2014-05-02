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

package org.lucasr.layoutsamples.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lucasr.layoutsamples.app.R;
import org.lucasr.layoutsamples.util.RawResource;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class TweetsAdapter extends BaseAdapter {
    private final Context mContext;
    private int mPresenterId;

    private static List<Tweet> sEntries;

    public TweetsAdapter(Context context, int presenterId) {
        mContext = context;
        mPresenterId = presenterId;
        loadFromResource(R.raw.tweets);
    }

    private void loadFromResource(int resID) {
        if (sEntries != null) {
            return;
        }

        try {
            final JSONArray tweets = RawResource.getAsJSON(mContext, resID);
            sEntries = new ArrayList<Tweet>(tweets.length());

            final int count = tweets.length();
            for (int i = 0; i < count; i++) {
                final JSONObject tweet = (JSONObject) tweets.get(i);
                sEntries.add(new Tweet(tweet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return sEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return sEntries.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TweetPresenter presenter;
        if (convertView == null) {
            presenter = (TweetPresenter) LayoutInflater.from(mContext).inflate(mPresenterId, parent, false);
        } else {
            presenter = (TweetPresenter) convertView;
        }

        Tweet tweet = (Tweet) getItem(position);
        presenter.update(tweet, EnumSet.noneOf(TweetPresenter.UpdateFlags.class));

        return (View) presenter;
    }

    @Override
    public long getItemId(int position) {
        return sEntries.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void setPresenter(int id) {
        mPresenterId = id;
        notifyDataSetChanged();
    }
}

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
    private final List<Tweet> mEntries;

    public TweetsAdapter(Context context, int presenterId) {
        mContext = context;
        mPresenterId = presenterId;

        mEntries = new ArrayList<Tweet>();
        loadFromResource(R.raw.tweets);
    }

    private void loadFromResource(int resID) {
        try {
            final JSONArray tweets = RawResource.getAsJSON(mContext, resID);

            final int count = tweets.length();
            for (int i = 0; i < count; i++) {
                final JSONObject tweet = (JSONObject) tweets.get(i);
                mEntries.add(new Tweet(tweet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return mEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return mEntries.get(position);
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
        return mEntries.get(position).getId();
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

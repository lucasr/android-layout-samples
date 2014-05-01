package org.lucasr.layoutsamples.adapter;

import android.graphics.drawable.Drawable;

import org.lucasr.layoutsamples.adapter.Tweet;

import java.util.EnumSet;

public interface TweetPresenter {
    public enum UpdateFlags {
        NO_IMAGE_LOADING
    }

    public enum Action {
        REPLY,
        RETWEET,
        FAVOURITE
    }

    public void update(Tweet tweet, EnumSet<UpdateFlags> flags);
}

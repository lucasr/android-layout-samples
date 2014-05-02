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

package org.lucasr.layoutsamples.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.layoutsamples.adapter.TweetPresenter;
import org.lucasr.layoutsamples.app.R;
import org.lucasr.layoutsamples.util.ImageUtils;

import java.util.EnumMap;
import java.util.EnumSet;

public class TweetLayoutView extends ViewGroup implements TweetPresenter {
    private final ImageView mProfileImage;
    private final TextView mAuthorText;
    private final TextView mMessageText;
    private final ImageView mPostImage;
    private final EnumMap<Action, ImageView> mActionIcons;

    private final int mPostImageHeight;
    private final int mProfileImageSize;
    private final int mIconImageSize;
    private final int mIconMargin;
    private final int mContentMargin;

    public TweetLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TweetLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final Resources res = getResources();
        mPostImageHeight = res.getDimensionPixelSize(R.dimen.tweet_post_image_height);
        mProfileImageSize = res.getDimensionPixelSize(R.dimen.tweet_profile_image_size);
        mIconImageSize = res.getDimensionPixelSize(R.dimen.tweet_icon_image_size);
        mIconMargin = res.getDimensionPixelSize(R.dimen.tweet_icon_margin);
        mContentMargin = res.getDimensionPixelSize(R.dimen.tweet_content_margin);

        LayoutInflater.from(context).inflate(R.layout.tweet_layout_view, this, true);
        mProfileImage = (ImageView) findViewById(R.id.profile_image);
        mAuthorText = (TextView) findViewById(R.id.author_text);
        mMessageText = (TextView) findViewById(R.id.message_text);
        mPostImage = (ImageView) findViewById(R.id.post_image);

        mActionIcons = new EnumMap(Action.class);
        for (Action action : Action.values()) {
            final ImageView icon;
            switch (action) {
                case REPLY:
                    icon = (ImageView) findViewById(R.id.reply_action);
                    break;

                case RETWEET:
                    icon = (ImageView) findViewById(R.id.retweet_action);
                    break;

                case FAVOURITE:
                    icon = (ImageView) findViewById(R.id.favourite_action);
                    break;

                default:
                    throw new IllegalArgumentException("Unrecognized tweet action");
            }

            mActionIcons.put(action, icon);
        }
    }

    private void layoutView(View view, int left, int top, int width, int height) {
        view.layout(left, top, left + width, top + height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        final int profileSizeSpec = MeasureSpec.makeMeasureSpec(mProfileImageSize, MeasureSpec.EXACTLY);
        mProfileImage.measure(profileSizeSpec, profileSizeSpec);

        final int contentWidth = widthSize - mProfileImageSize - mContentMargin -
                getPaddingLeft() - getPaddingRight();
        final int contentWidthSpec = MeasureSpec.makeMeasureSpec(contentWidth, MeasureSpec.AT_MOST);
        final int contentHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        mAuthorText.measure(contentWidthSpec, contentHeightSpec);
        mMessageText.measure(contentWidthSpec, contentHeightSpec);

        final int iconSizeSpec = MeasureSpec.makeMeasureSpec(mIconImageSize, MeasureSpec.EXACTLY);
        for (Action action : Action.values()) {
            mActionIcons.get(action).measure(iconSizeSpec, iconSizeSpec);
        }

        int contentHeight = mAuthorText.getMeasuredHeight() +
                mMessageText.getMeasuredHeight() +
                mContentMargin + mActionIcons.get(Action.REPLY).getMeasuredHeight();

        if (mPostImage.getVisibility() != View.GONE) {
            final int imageWidthSpec = MeasureSpec.makeMeasureSpec(contentWidth,
                    MeasureSpec.EXACTLY);
            final int imageHeightSpec = MeasureSpec.makeMeasureSpec(mPostImageHeight,
                    MeasureSpec.EXACTLY);

            mPostImage.measure(imageWidthSpec, imageHeightSpec);
            contentHeight += mContentMargin + mPostImage.getMeasuredHeight();
        }

        final int width = widthSize + getPaddingLeft() + getPaddingRight();
        final int height = Math.max(contentHeight, mProfileImage.getMeasuredHeight()) +
                           getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();

        layoutView(mProfileImage,
                paddingLeft,
                paddingTop,
                mProfileImage.getMeasuredWidth(),
                mProfileImage.getMeasuredHeight());

        final int contentLeft = mContentMargin + paddingLeft + mProfileImage.getWidth();

        layoutView(mAuthorText,
                contentLeft,
                paddingTop,
                mAuthorText.getMeasuredWidth(),
                mAuthorText.getMeasuredHeight());

        layoutView(mMessageText,
                contentLeft,
                paddingTop + mAuthorText.getHeight(),
                mMessageText.getMeasuredWidth(),
                mMessageText.getMeasuredHeight());

        int iconTop = mContentMargin + paddingTop + mAuthorText.getHeight() + mMessageText.getHeight();

        if (mPostImage.getVisibility() != View.GONE) {
            layoutView(mPostImage,
                    contentLeft,
                    paddingTop + mAuthorText.getHeight() + mMessageText.getHeight() + mContentMargin,
                    mPostImage.getMeasuredWidth(),
                    mPostImage.getMeasuredHeight());

            iconTop += mPostImage.getHeight() + mContentMargin;
        }

        for (Action action : Action.values()) {
            final ImageView icon = mActionIcons.get(action);
            layoutView(icon,
                    contentLeft + (action.ordinal() * (icon.getMeasuredWidth() + mIconMargin)),
                    iconTop,
                    icon.getMeasuredWidth(),
                    icon.getMeasuredHeight());
        }
    }

    @Override
    public void update(Tweet tweet, EnumSet<UpdateFlags> flags) {
        mAuthorText.setText(tweet.getAuthorName());
        mMessageText.setText(Html.fromHtml(tweet.getMessage()));

        final Context context = getContext();
        ImageUtils.loadImage(context, mProfileImage, tweet.getProfileImageUrl(), flags);

        final boolean hasPostImage = !TextUtils.isEmpty(tweet.getPostImageUrl());
        mPostImage.setVisibility(hasPostImage ? View.VISIBLE : View.GONE);
        if (hasPostImage) {
            ImageUtils.loadImage(context, mPostImage, tweet.getPostImageUrl(), flags);
        }
    }
}

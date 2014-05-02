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

import android.content.res.Resources;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.layoutsamples.adapter.TweetPresenter;
import org.lucasr.layoutsamples.app.R;
import org.lucasr.layoutsamples.canvas.AbstractUIElement;
import org.lucasr.layoutsamples.canvas.ElementGroup;
import org.lucasr.layoutsamples.canvas.ImageElement;
import org.lucasr.layoutsamples.canvas.TextElement;
import org.lucasr.layoutsamples.canvas.UIElementHost;
import org.lucasr.layoutsamples.util.ImageUtils;

import java.util.EnumMap;
import java.util.EnumSet;

public class TweetElement extends ElementGroup implements TweetPresenter {
    private ImageElement mProfileImage;
    private TextElement mAuthorText;
    private TextElement mMessageText;
    private ImageElement mPostImage;
    private EnumMap<Action, ImageElement> mActionIcons;

    private int mPostImageHeight;
    private int mProfileImageSize;
    private int mIconImageSize;
    private int mIconMargin;
    private int mContentMargin;

    private ImageElementTarget mProfileImageTarget;
    private ImageElementTarget mPostImageTarget;

    public TweetElement(UIElementHost host) {
        super(host);
    }

    @Override
    protected void init() {
        super.init();

        final Resources res = getResources();

        mPostImageHeight = res.getDimensionPixelSize(R.dimen.tweet_post_image_height);
        mProfileImageSize = res.getDimensionPixelSize(R.dimen.tweet_profile_image_size);
        mIconImageSize = res.getDimensionPixelSize(R.dimen.tweet_icon_image_size);
        mIconMargin = res.getDimensionPixelSize(R.dimen.tweet_icon_margin);
        mContentMargin = res.getDimensionPixelSize(R.dimen.tweet_content_margin);

        int padding = res.getDimensionPixelOffset(R.dimen.tweet_padding);
        setPadding(padding, padding, padding, padding);

        mProfileImage = (ImageElement) addElement(UIElementType.IMAGE);
        mProfileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mProfileImageTarget = new ImageElementTarget(res, mProfileImage);

        mAuthorText = (TextElement) addElement(UIElementType.TEXT);
        mAuthorText.setRawTextSize(res.getDimensionPixelSize(R.dimen.tweet_author_text_size));
        mAuthorText.setTextAlignment(Layout.Alignment.ALIGN_NORMAL);
        mAuthorText.setTextColor(res.getColor(R.color.tweet_author_text_color));

        mMessageText = (TextElement) addElement(UIElementType.TEXT);
        mMessageText.setRawTextSize(res.getDimensionPixelSize(R.dimen.tweet_message_text_size));
        mMessageText.setTextColor(res.getColor(R.color.tweet_message_text_color));

        mPostImage = (ImageElement) addElement(UIElementType.IMAGE);
        mPostImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mPostImageTarget = new ImageElementTarget(res, mPostImage);

        mActionIcons = new EnumMap(Action.class);
        for (Action action : Action.values()) {
            ImageElement icon = (ImageElement) addElement(UIElementType.IMAGE);
            icon.setScaleType(ImageView.ScaleType.FIT_XY);

            switch (action) {
                case REPLY:
                    icon.setImageResource(R.drawable.tweet_reply);
                    break;

                case RETWEET:
                    icon.setImageResource(R.drawable.tweet_retweet);
                    break;

                case FAVOURITE:
                    icon.setImageResource(R.drawable.tweet_favourite);
                    break;
            }

            mActionIcons.put(action, icon);
        }
    }

    private void layoutElement(AbstractUIElement element, int left, int top, int width, int height) {
        element.layout(left, top, left + width, top + height);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
    public void onLayout(int left, int top, int right, int bottom) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();

        layoutElement(mProfileImage,
                paddingLeft,
                paddingTop,
                mProfileImage.getMeasuredWidth(),
                mProfileImage.getMeasuredHeight());

        final int contentLeft = mContentMargin + paddingLeft + mProfileImage.getWidth();

        layoutElement(mAuthorText,
                contentLeft,
                paddingTop,
                mAuthorText.getMeasuredWidth(),
                mAuthorText.getMeasuredHeight());

        layoutElement(mMessageText,
                contentLeft,
                paddingTop + mAuthorText.getHeight(),
                mMessageText.getMeasuredWidth(),
                mMessageText.getMeasuredHeight());

        int iconTop = mContentMargin + paddingTop + mAuthorText.getHeight() + mMessageText.getHeight();

        if (mPostImage.getVisibility() != View.GONE) {
            layoutElement(mPostImage,
                    contentLeft,
                    paddingTop + mAuthorText.getHeight() + mMessageText.getHeight() + mContentMargin,
                    mPostImage.getMeasuredWidth(),
                    mPostImage.getMeasuredHeight());

            iconTop += mPostImage.getHeight() + mContentMargin;
        }

        for (Action action : Action.values()) {
            final ImageElement icon = mActionIcons.get(action);
            layoutElement(icon,
                    contentLeft + (action.ordinal() * (icon.getMeasuredWidth() + mIconMargin)),
                    iconTop,
                    icon.getMeasuredWidth(),
                    icon.getMeasuredHeight());
        }
    }

    public void loadProfileImage(Tweet tweet, EnumSet<UpdateFlags> flags) {
        ImageUtils.loadImage(getContext(), mProfileImage, mProfileImageTarget,
                tweet.getProfileImageUrl(), flags);
    }

    public void loadPostImage(Tweet tweet, EnumSet<UpdateFlags> flags) {
        ImageUtils.loadImage(getContext(), mPostImage, mPostImageTarget,
                tweet.getPostImageUrl(), flags);
    }

    @Override
    public void update(Tweet tweet, EnumSet<UpdateFlags> flags) {
        mAuthorText.setText(tweet.getAuthorName());
        mMessageText.setText(tweet.getMessage());

        loadProfileImage(tweet, flags);

        final boolean hasPostImage = !TextUtils.isEmpty(tweet.getPostImageUrl());
        mPostImage.setVisibility(hasPostImage ? View.VISIBLE : View.GONE);
        if (hasPostImage) {
            loadPostImage(tweet, flags);
        }
    }
}

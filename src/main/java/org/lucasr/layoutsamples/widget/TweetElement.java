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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.layoutsamples.adapter.TweetPresenter;
import org.lucasr.layoutsamples.app.R;
import org.lucasr.layoutsamples.canvas.UIElementGroup;
import org.lucasr.layoutsamples.canvas.ImageElement;
import org.lucasr.layoutsamples.canvas.TextElement;
import org.lucasr.layoutsamples.canvas.UIElement;
import org.lucasr.layoutsamples.canvas.UIElementHost;
import org.lucasr.layoutsamples.canvas.UIElementInflater;
import org.lucasr.layoutsamples.util.ImageUtils;

import java.util.EnumMap;
import java.util.EnumSet;

public class TweetElement extends UIElementGroup implements TweetPresenter {
    private ImageElement mProfileImage;
    private TextElement mAuthorText;
    private TextElement mMessageText;
    private ImageElement mPostImage;
    private EnumMap<Action, UIElement> mActionIcons;

    private ImageElementTarget mProfileImageTarget;
    private ImageElementTarget mPostImageTarget;

    public TweetElement(UIElementHost host) {
        this(host, null);
    }

    public TweetElement(UIElementHost host, AttributeSet attrs) {
        super(host, attrs);

        final Resources res = getResources();

        int padding = res.getDimensionPixelOffset(R.dimen.tweet_padding);
        setPadding(padding, padding, padding, padding);

        UIElementInflater.from(getContext()).inflate(R.layout.tweet_element_view, host, this);
        mProfileImage = (ImageElement) findElementById(R.id.profile_image);
        mAuthorText = (TextElement) findElementById(R.id.author_text);
        mMessageText = (TextElement) findElementById(R.id.message_text);
        mPostImage = (ImageElement) findElementById(R.id.post_image);

        mProfileImageTarget = new ImageElementTarget(res, mProfileImage);
        mPostImageTarget = new ImageElementTarget(res, mPostImage);

        mActionIcons = new EnumMap(Action.class);
        for (Action action : Action.values()) {
            final int elementId;
            switch (action) {
                case REPLY:
                    elementId = R.id.reply_action;
                    break;

                case RETWEET:
                    elementId = R.id.retweet_action;
                    break;

                case FAVOURITE:
                    elementId = R.id.favourite_action;
                    break;

                default:
                    throw new IllegalArgumentException("Unrecognized tweet action");
            }

            mActionIcons.put(action, findElementById(elementId));
        }
    }

    private void layoutElement(UIElement element, int left, int top, int width, int height) {
        MarginLayoutParams margins = (MarginLayoutParams) element.getLayoutParams();
        final int leftWithMargins = left + margins.leftMargin;
        final int topWithMargins = top + margins.topMargin;

        element.layout(leftWithMargins, topWithMargins,
                       leftWithMargins + width, topWithMargins + height);
    }

    private int getWidthWithMargins(UIElement element) {
        final MarginLayoutParams lp = (MarginLayoutParams) element.getLayoutParams();
        return element.getWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getHeightWithMargins(UIElement element) {
        final MarginLayoutParams lp = (MarginLayoutParams) element.getLayoutParams();
        return element.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    private int getMeasuredWidthWithMargins(UIElement element) {
        final MarginLayoutParams lp = (MarginLayoutParams) element.getLayoutParams();
        return element.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getMeasuredHeightWithMargins(UIElement element) {
        final MarginLayoutParams lp = (MarginLayoutParams) element.getLayoutParams();
        return element.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    private void cancelImageRequest(Target target) {
        if (!isAttachedToHost() || target == null) {
            return;
        }

        Picasso.with(getContext()).cancelRequest(target);
    }

    @Override
    public boolean swapHost(UIElementHost host) {
        if (host == null) {
            cancelImageRequest(mProfileImageTarget);
            cancelImageRequest(mPostImageTarget);
        }

        return super.swapHost(host);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int widthUsed = 0;
        int heightUsed = 0;

        measureElementWithMargins(mProfileImage,
                                  widthMeasureSpec, widthUsed,
                                  heightMeasureSpec, heightUsed);
        widthUsed += getMeasuredWidthWithMargins(mProfileImage);

        measureElementWithMargins(mAuthorText,
                                  widthMeasureSpec, widthUsed,
                                  heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(mAuthorText);

        measureElementWithMargins(mMessageText,
                                  widthMeasureSpec, widthUsed,
                                  heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(mMessageText);

        if (mPostImage.getVisibility() != View.GONE) {
            measureElementWithMargins(mPostImage,
                                      widthMeasureSpec, widthUsed,
                                      heightMeasureSpec, heightUsed);
            heightUsed += getMeasuredHeightWithMargins(mPostImage);
        }

        int maxIconHeight = 0;
        for (Action action : Action.values()) {
            final UIElement icon = mActionIcons.get(action);
            measureElementWithMargins(icon,
                                      widthMeasureSpec, widthUsed,
                                      heightMeasureSpec, heightUsed);

            final int height = getMeasuredHeightWithMargins(icon);
            if (height > maxIconHeight) {
                maxIconHeight = height;
            }

            widthUsed += getMeasuredWidthWithMargins(icon);
        }
        heightUsed += maxIconHeight;

        int heightSize = heightUsed + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public void onLayout(int l, int t, int r, int b) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();

        int currentTop = paddingTop;

        layoutElement(mProfileImage, paddingLeft, currentTop,
                      mProfileImage.getMeasuredWidth(),
                      mProfileImage.getMeasuredHeight());

        final int contentLeft = getWidthWithMargins(mProfileImage) + paddingLeft;
        final int contentWidth = r - l - contentLeft - getPaddingRight();

        layoutElement(mAuthorText, contentLeft, currentTop,
                      contentWidth, mAuthorText.getMeasuredHeight());
        currentTop += getHeightWithMargins(mAuthorText);

        layoutElement(mMessageText, contentLeft, currentTop,
                      contentWidth, mMessageText.getMeasuredHeight());
        currentTop += getHeightWithMargins(mMessageText);

        if (mPostImage.getVisibility() != View.GONE) {
            layoutElement(mPostImage, contentLeft, currentTop,
                    contentWidth, mPostImage.getMeasuredHeight());

            currentTop += getHeightWithMargins(mPostImage);
        }

        final int iconsWidth = contentWidth / mActionIcons.size();
        int iconsLeft = contentLeft;

        for (Action action : Action.values()) {
            final UIElement icon = mActionIcons.get(action);

            layoutElement(icon, iconsLeft, currentTop,
                          iconsWidth, icon.getMeasuredHeight());
            iconsLeft += iconsWidth;
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

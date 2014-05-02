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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.lucasr.layoutsamples.app.R;
import org.lucasr.layoutsamples.canvas.ImageElement;

public class ImageElementTarget implements Target {
    private final Resources mResources;
    private final ImageElement mElement;

    public ImageElementTarget(Resources resources, ImageElement element) {
        mResources = resources;
        mElement = element;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
        boolean shouldFade = (loadedFrom != Picasso.LoadedFrom.MEMORY);

        if (shouldFade) {
            Drawable placeholder =
                    mResources.getDrawable(R.drawable.tweet_placeholder_image);
            Drawable bitmapDrawable = new BitmapDrawable(mResources, bitmap);

            TransitionDrawable fadeInDrawable =
                    new TransitionDrawable(new Drawable[] { placeholder, bitmapDrawable });

            mElement.setImageDrawable(fadeInDrawable);
            fadeInDrawable.startTransition(200);
        } else {
            mElement.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onBitmapFailed(Drawable drawable) {
        mElement.setImageDrawable(drawable);
    }

    @Override
    public void onPrepareLoad(Drawable drawable) {
        mElement.setImageDrawable(drawable);
    }
}

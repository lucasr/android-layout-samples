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

package org.lucasr.layoutsamples.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.lucasr.layoutsamples.adapter.TweetPresenter.UpdateFlags;
import org.lucasr.layoutsamples.app.R;
import org.lucasr.layoutsamples.canvas.ImageElement;
import org.lucasr.layoutsamples.widget.ImageElementTarget;

import java.util.EnumSet;

public class ImageUtils {
    private ImageUtils() {
    }

    public static void loadImage(Context context, ImageView view, String url,
                                 EnumSet<UpdateFlags> flags) {
        if (!flags.contains(UpdateFlags.NO_IMAGE_LOADING)) {
            Picasso.with(context)
                   .load(url)
                   .placeholder(R.drawable.tweet_placeholder_image)
                   .error(R.drawable.tweet_placeholder_image)
                   .into(view);
        } else {
            view.setImageResource(R.drawable.tweet_placeholder_image);
        }
    }

    public static void loadImage(Context context, ImageElement element,
                                 ImageElementTarget target, String url,
                                 EnumSet<UpdateFlags> flags) {
        if (!flags.contains(UpdateFlags.NO_IMAGE_LOADING)) {
            Picasso.with(context)
                   .load(url)
                   .placeholder(R.drawable.tweet_placeholder_image)
                   .error(R.drawable.tweet_placeholder_image)
                   .into(target);
        } else {
            element.setImageResource(R.drawable.tweet_placeholder_image);
        }
    }
}

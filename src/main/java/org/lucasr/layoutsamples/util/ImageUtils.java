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

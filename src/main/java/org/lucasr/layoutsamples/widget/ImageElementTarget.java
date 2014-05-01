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

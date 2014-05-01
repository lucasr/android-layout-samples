package org.lucasr.layoutsamples.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import org.lucasr.layoutsamples.async.TweetsLayoutLoader;
import org.lucasr.layoutsamples.adapter.TweetsAdapter;
import org.lucasr.layoutsamples.async.AsyncTweetElementFactory;
import org.lucasr.smoothie.AsyncListView;
import org.lucasr.smoothie.ItemManager;

public class TweetsListView extends AsyncListView {
    private TweetsAdapter mTweetsAdapter;
    private int mPresenterId;

    public TweetsListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TweetsListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPresenterId = R.layout.tweet_composite_row;
    }

    private void updateTargetWidth() {
        if (mPresenterId != R.layout.tweet_async_row) {
            return;
        }

        final Context context = getContext();

        final int targetWidth = getWidth() - getPaddingLeft() + getPaddingRight();
        AsyncTweetElementFactory.setTargetWidth(context, targetWidth);
        App.getInstance(context).getElementCache().evictAll();

        TweetsAdapter adapter = (TweetsAdapter) getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void updateItemLoader() {
        Context context = getContext();

        if (mPresenterId == R.layout.tweet_async_row) {
            TweetsLayoutLoader loader = new TweetsLayoutLoader(context);

            ItemManager.Builder builder = new ItemManager.Builder(loader);
            builder.setPreloadItemsEnabled(true).setPreloadItemsCount(30);
            builder.setThreadPoolSize(2);
            setItemManager(builder.build());
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onGlobalLayout() {
                updateTargetWidth();
                updateItemLoader();

                mTweetsAdapter = new TweetsAdapter(getContext(), mPresenterId);
                setAdapter(mTweetsAdapter);

                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            updateTargetWidth();
        }
    }

    public void setPresenter(int id) {
        if (mPresenterId == id) {
            return;
        }

        mPresenterId = id;
        if (mTweetsAdapter != null) {
            mTweetsAdapter.setPresenter(id);
        }

        updateItemLoader();
    }
}

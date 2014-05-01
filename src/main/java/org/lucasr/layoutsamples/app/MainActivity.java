package org.lucasr.layoutsamples.app;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {
    private LayoutsAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPagerAdapter = new LayoutsAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private class LayoutsAdapter extends FragmentPagerAdapter {
        public LayoutsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int presenterId = 0;
            switch (position) {
                case 0:
                    presenterId = R.layout.tweet_composite_row;
                    break;

                case 1:
                    presenterId = R.layout.tweet_layout_row;
                    break;

                case 2:
                    presenterId = R.layout.tweet_element_row;
                    break;

                case 3:
                    presenterId = R.layout.tweet_async_row;
                    break;
            }

            return TweetsFragment.newInstance(presenterId);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            final Locale locale = Locale.getDefault();

            switch (position) {
                case 0:
                    return getString(R.string.title_composite).toUpperCase(locale);

                case 1:
                    return getString(R.string.title_layout).toUpperCase(locale);

                case 2:
                    return getString(R.string.title_element).toUpperCase(locale);

                case 3:
                    return getString(R.string.title_async).toUpperCase(locale);
            }

            return null;
        }
    }
}

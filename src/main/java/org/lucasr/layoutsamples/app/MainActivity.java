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

package org.lucasr.layoutsamples.app;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import org.lucasr.layoutsamples.util.ViewServer;

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

        ViewServer.get(this).addWindow(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
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

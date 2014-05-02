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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TweetsFragment extends Fragment {
    private static final String ARG_PRESENTER_ID = "presenter_id";

    public static TweetsFragment newInstance(int presenterId) {
        TweetsFragment fragment = new TweetsFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_PRESENTER_ID, presenterId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweets, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TweetsListView list = (TweetsListView) getView().findViewById(R.id.list);
        int presenterId = getArguments().getInt(ARG_PRESENTER_ID);
        list.setPresenter(presenterId);
    }
}

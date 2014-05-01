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

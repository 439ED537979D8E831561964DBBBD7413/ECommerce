package com.winsant.android.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.winsant.android.ui.HomeActivity;

/**
 * Created by Developer on 2/15/2017.
 */

public class BaseFragment extends Fragment {

    public HomeActivity activity;
//    public HomeActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (HomeActivity) getActivity();
//        activity = (HomeActivity) getActivity();
    }
}

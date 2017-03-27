package com.winsant.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.winsant.android.R;
import com.winsant.android.actionitembadge.library.ActionItemBadge;
import com.winsant.android.ui.CartActivity;
import com.winsant.android.ui.HomeActivity;
import com.winsant.android.ui.LoginActivity;
import com.winsant.android.ui.MyApplication;
import com.winsant.android.utils.StaticDataUtility;

/**
 * Created by Developer on 2/15/2017.
 */

public class BaseFragment extends Fragment {

    public HomeActivity activity;
    private MenuItem cart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeActivity) getActivity();
    }
}

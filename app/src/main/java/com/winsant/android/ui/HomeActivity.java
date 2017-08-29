package com.winsant.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.winsant.android.R;
import com.winsant.android.actionitembadge.library.ActionItemBadge;
import com.winsant.android.ui.fragment.HomeFragment;
import com.winsant.android.ui.fragment.OfferListFragment;
import com.winsant.android.ui.fragment.ProfileFragment;
import com.winsant.android.ui.fragment.WishListFragment;
import com.winsant.android.utils.CommonDataUtility;
import com.winsant.android.utils.StaticDataUtility;

import static com.winsant.android.R.id.container;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private boolean doubleBackToExitPressedOnce = false;
    private ImageView imgHome, imgOffers, imgWishList, imgProfile;
    private TextView txtHome, txtOffers, txtWishList, txtProfile;
    private String tag = "";
    private MenuItem cart;
    private TextView toolbar_title;
     DrawerLayout mDrawerLayout;
     NavigationView mNavigationView;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionManager = new SessionManager(getApplicationContext());

         //android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
     //  toolbar.setTitleTextColor(Color.parseColor("#000000"));

        activity = HomeActivity.this;

        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(CommonDataUtility.setTitleTypeFace(activity));
       // toolbar_title.setText(getString(R.string.app_name));

        LinearLayout llHome = (LinearLayout) findViewById(R.id.llHome);
        LinearLayout llOffers = (LinearLayout) findViewById(R.id.llOffers);
        LinearLayout llWishList = (LinearLayout) findViewById(R.id.llWishList);
        LinearLayout llProfile = (LinearLayout) findViewById(R.id.llProfile);

        imgHome = (ImageView) findViewById(R.id.imgHome);
        imgOffers = (ImageView) findViewById(R.id.imgOffers);
        imgWishList = (ImageView) findViewById(R.id.imgWishList);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);

        txtHome = (TextView) findViewById(R.id.txtHome);
        txtOffers = (TextView) findViewById(R.id.txtOffers);
        txtWishList = (TextView) findViewById(R.id.txtWishList);
        txtProfile = (TextView) findViewById(R.id.txtProfile);

        if (getResources().getBoolean(R.bool.isLargeTablet))
        {
            txtHome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtOffers.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtWishList.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtProfile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        } else if (getResources().getBoolean(R.bool.isTablet))
        {
            txtHome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtOffers.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtWishList.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtProfile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else {
            txtHome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            txtOffers.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            txtWishList.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            txtProfile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        }

        txtHome.setOnClickListener(this);
        txtOffers.setOnClickListener(this);
        txtWishList.setOnClickListener(this);
        txtProfile.setOnClickListener(this);

        llHome.setOnClickListener(this);
        llOffers.setOnClickListener(this);
        llWishList.setOnClickListener(this);
        llProfile.setOnClickListener(this);

        imgHome.setOnClickListener(this);
        imgOffers.setOnClickListener(this);
        imgWishList.setOnClickListener(this);
        imgProfile.setOnClickListener(this);

        tag = "home";
        tabSelection(true, false, false, false);
        setFragment(0);
    }

    @Override
    public void onClick(View v) {

        switch ((v.getId())) {
            case R.id.llHome:
            case R.id.txtHome:
            case R.id.imgHome:

                setToolbarTitle(getString(R.string.app_name));

                if (!tag.equals("home")) {
                    tabSelection(true, false, false, false);
                    setFragment(0);
                }
                break;
            case R.id.llOffers:
            case R.id.txtOffers:
            case R.id.imgOffers:

                setToolbarTitle(getString(R.string.title_activity_offer));

                if (!tag.equals("offers")) {
                    tabSelection(false, true, false, false);
                    setFragment(1);
                }
                break;
            case R.id.llWishList:
            case R.id.txtWishList:
            case R.id.imgWishList:

                setToolbarTitle(getString(R.string.title_activity_wish_list));

                if (!tag.equals("wishList")) {
                    tabSelection(false, false, true, false);
                    setFragment(2);
                }
                break;
            case R.id.llProfile:
            case R.id.txtProfile:
            case R.id.imgProfile:

                setToolbarTitle(getString(R.string.my_account));

                if (!tag.equals("profile")) {
                    tabSelection(false, false, false, true);
                    setFragment(3);
                }
                break;
        }
    }

    private void setToolbarTitle(String title) {
        toolbar_title.setText(title);
    }

    private void tabSelection(boolean tab1, boolean tab2, boolean tab3, boolean tab4) {
        imgHome.setSelected(tab1);
        txtHome.setSelected(tab1);

        imgOffers.setSelected(tab2);
        txtOffers.setSelected(tab2);

        imgWishList.setSelected(tab3);
        txtWishList.setSelected(tab3);

        imgProfile.setSelected(tab4);
        txtProfile.setSelected(tab4);
    }

    private void setFragment(int position) {

        Fragment frag = null;

        switch (position) {
            case 0:

                for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                    getSupportFragmentManager().popBackStack();
                }

                frag = new HomeFragment();
                tag = "home";
                break;
            case 1:
                frag = new OfferListFragment();
                tag = "offers";
                break;
            case 2:
                frag = new WishListFragment();
                tag = "wishList";
                break;
            case 3:
                frag = new ProfileFragment();
                tag = "profile";
                break;

        }
        pushFragment(frag);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {

        System.gc();

        if (!tag.equals("home")) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

                for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount() - 1; ++i) {
                    getSupportFragmentManager().popBackStack();
                }

                tag = "home";
                tabSelection(true, false, false, false);
                setToolbarTitle(getString(R.string.app_name));

//                supportInvalidateOptionsMenu();
//                setBadge();

            } else {
                backPress();
            }
        } else {
            backPress();
        }
    }

    private void backPress() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    /**
     * Method to push any fragment into given id.
     *
     * @param fragment An instance of Fragment to show into the given id.
     */

    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.add(container, fragment).addToBackStack(null);
                ft.commit();
            }
        }
    }

    @Override
    protected void onResume() {
        System.out.println(StaticDataUtility.APP_TAG + " home activity onResume ");
        super.onResume();
        supportInvalidateOptionsMenu();
//        invalidateOptionsMenu();
        setBadge();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println(StaticDataUtility.APP_TAG + " home activity onCreateOptionsMenu ");
        menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.activity_action_cart)).setIcon(R.drawable.ico_menu_cart).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        cart = menu.findItem(1);
        setBadge();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case 1:

                if (MyApplication.getInstance().getPreferenceUtility().getLogin()) {
                    startActivity(new Intent(activity, CartActivity.class));
                } else {
                    startActivity(new Intent(activity, LoginActivity.class));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setBadge() {

        if (MyApplication.getInstance().getPreferenceUtility().getLogin()) {
            int total = MyApplication.getInstance().getPreferenceUtility().getInt("total_cart");
            System.out.println(StaticDataUtility.APP_TAG + " home activity total_cart --> " + total);
            if (!(total == 0)) {
                ActionItemBadge.Update(activity, cart, R.drawable.ico_menu_cart, StaticDataUtility.style, total);
            }
        }
    }
}

//    private void selectFragment(MenuItem item) {
//        Fragment frag = null;
//        // init corresponding fragment
//        switch (item.getItemId()) {
//            case R.id.action_home:
//                frag = MenuFragment.newInstance(getString(R.string.text_home));
//                break;
//            case R.id.action_offer:
//                frag = MenuFragment.newInstance(getString(R.string.text_offer));
//                break;
//            case R.id.action_concept:
//                frag = MenuFragment.newInstance(getString(R.string.text_concept));
//                break;
//            case R.id.action_wishlist:
//                frag = MenuFragment.newInstance(getString(R.string.text_wishlist));
//                break;
//            case R.id.action_profile:
//                frag = MenuFragment.newInstance(getString(R.string.text_profile));
//                break;
//        }
//
//        // update selected item
//        mSelectedItem = item.getItemId();
//
//        item.setChecked(true);
//
//        // uncheck the other items.
//        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
//            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
//            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
//        }
//
//        toolbar_title.setText(item.getTitle());
//
//        if (frag != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.add(R.id.container, frag, frag.getTag());
//            ft.commit();
//        }

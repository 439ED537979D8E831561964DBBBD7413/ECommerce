//package com.winsant.android.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//import com.winsant.android.R;
//import com.winsant.android.bottomnavigation.BottomNavigationBar;
//import com.winsant.android.bottomnavigation.BottomNavigationItem;
//import com.winsant.android.ui.fragment.HomeFragment;
//import com.winsant.android.ui.fragment.OfferListFragment;
//import com.winsant.android.ui.fragment.ProfileFragment;
//import com.winsant.android.ui.fragment.WishListFragment;
//
//public class HomeActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
//
//    //    private static final String SELECTED_ITEM = "arg_selected_item";
//    private BottomNavigationBar bottomNavigationBar;
//    private boolean doubleBackToExitPressedOnce = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home_old);
//
//        bottomNavigationBar = (BottomNavigationBar)
//                findViewById(R.id.bottomNavigationBar);
//        bottomNavigationBar.setBarBackgroundColor(R.color.colorWhite);
//        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED).setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
//
//        bottomNavigationBar
//                .addItem(new BottomNavigationItem(R.drawable.ico_home_normal_svg, getString(R.string.text_home)).setActiveColorResource(R.color.colorPrimary))
//                .addItem(new BottomNavigationItem(R.drawable.ico_discount_normal_svg, getString(R.string.text_offer)).setActiveColorResource(R.color.colorPrimary))
////                .addItem(new BottomNavigationItem(R.drawable.ico_concept_svg, getString(R.string.text_concept)).setActiveColorResource(R.color.colorPrimary))
//                .addItem(new BottomNavigationItem(R.drawable.ico_wishlist_h_normal_svg, getString(R.string.text_wishlist)).setActiveColorResource(R.color.colorPrimary))
//                .addItem(new BottomNavigationItem(R.drawable.ico_profile_normal_svg, getString(R.string.text_profile)).setActiveColorResource(R.color.colorPrimary))
//                .setFirstSelectedPosition(0)
//                .initialise();
//
//        bottomNavigationBar.setTabSelectedListener(this);
//
//        setFragment(0);
//    }
////        Menu menu = bottomNavigationView.getMenu();
////        selectFragment(menu.getItem(0));
////
////        // Set action to perform when any menu-item is selected.
////        bottomNavigationView.setOnNavigationItemSelectedListener(
////                new BottomNavigationView.OnNavigationItemSelectedListener() {
////                    @Override
////                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
////                        selectFragment(item);
////                        return false;
////                    }
////                });
//
//    @Override
//    public void onTabSelected(int position) {
//        setFragment(position);
//    }
//
//    @Override
//    public void onTabUnselected(int position) {
//
//    }
//
//    @Override
//    public void onTabReselected(int position) {
//
//    }
//
//    private void setFragment(int position) {
//
//        Fragment frag = null;
//        String tag = null;
//
//        switch (position) {
//            case 0:
//                frag = new HomeFragment();
//                break;
//            case 1:
//                frag = new OfferListFragment();
//                tag = "offers";
//                break;
////            case 2:
////                frag = MenuFragment.newInstance(getString(R.string.text_concept));
////                tag = "concept";
////                break;
//            case 2:
//                frag = new WishListFragment();
//                tag = "WishList";
//                break;
//            case 3:
//                frag = new ProfileFragment();
//                tag = "profile";
//                break;
//
//        }
//        pushFragment(frag, tag);
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 2000);
//    }
//
//    /**
//     * Perform action when any item is selected.
//     *
//     * @param item Item that is selected.
//     */
////    protected void selectFragment(MenuItem item) {
////
////        item.setChecked(true);
////
////        Fragment frag = null;
////        // init corresponding fragment
////        switch (item.getItemId()) {
////            case R.id.action_home:
////                frag = new HomeFragment();
////                break;
////            case R.id.action_offer:
////                frag = MenuFragment.newInstance(getString(R.string.text_offer));
////                break;
////            case R.id.action_concept:
////                frag = MenuFragment.newInstance(getString(R.string.text_concept));
////                break;
////            case R.id.action_wishlist:
////                frag = MenuFragment.newInstance(getString(R.string.text_wishlist));
////                break;
////            case R.id.action_profile:
////                frag = MenuFragment.newInstance(getString(R.string.text_profile));
////                break;
////        }
////
////        pushFragment(frag);
//////        if (item.getTitle().equals(getString(R.string.text_home)))
//////            toolbar_title.setText(getString(R.string.app_name));
//////        else
//////            toolbar_title.setText(item.getTitle());
////    }
//
//    /**
//     * Method to push any fragment into given id.
//     *
//     * @param fragment An instance of Fragment to show into the given id.
//     */
//    protected void pushFragment(Fragment fragment, String tag) {
//        if (fragment == null)
//            return;
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        if (fragmentManager != null) {
//            FragmentTransaction ft = fragmentManager.beginTransaction();
//            if (ft != null) {
//                ft.replace(R.id.container, fragment);
//                ft.commit();
//            }
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.activity_action_cart)).setIcon(R.drawable.ico_menu_cart).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//
////        int total = MyApplication.getInstance().getPreferenceUtility().getInt("total_cart");
////        if (!(total == 0))
////            CommonDataUtility.setBadgeCount(this, menu.findItem(1), String.valueOf(total));
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case 1:
//
//                if (MyApplication.getInstance().getPreferenceUtility().getLogin()) {
//                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
//                } else {
//                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                    // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                }
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//}
////    private void selectFragment(MenuItem item) {
////        Fragment frag = null;
////        // init corresponding fragment
////        switch (item.getItemId()) {
////            case R.id.action_home:
////                frag = MenuFragment.newInstance(getString(R.string.text_home));
////                break;
////            case R.id.action_offer:
////                frag = MenuFragment.newInstance(getString(R.string.text_offer));
////                break;
////            case R.id.action_concept:
////                frag = MenuFragment.newInstance(getString(R.string.text_concept));
////                break;
////            case R.id.action_wishlist:
////                frag = MenuFragment.newInstance(getString(R.string.text_wishlist));
////                break;
////            case R.id.action_profile:
////                frag = MenuFragment.newInstance(getString(R.string.text_profile));
////                break;
////        }
////
////        // update selected item
////        mSelectedItem = item.getItemId();
////
////        item.setChecked(true);
////
////        // uncheck the other items.
////        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
////            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
////            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
////        }
////
////        toolbar_title.setText(item.getTitle());
////
////        if (frag != null) {
////            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
////            ft.add(R.id.container, frag, frag.getTag());
////            ft.commit();
////        }

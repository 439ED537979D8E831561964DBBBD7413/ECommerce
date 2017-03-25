package com.winsant.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.winsant.android.R;
import com.winsant.android.ui.MyApplication;

/**
 * Created by Developer on 2/10/2017.
 */

public class CommonDataUtility {

    /**
     * Determine if the device is a tablet (i.e. it has a large screen).
     *
     * @param activity The calling activity.
     */
    public static boolean isTablet(Activity activity) {
        return (activity.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean checkConnection(Activity activity) {
        ConnectivityManager
                cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public static Typeface setTypeFace(Activity activity) {
        return Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Medium.ttf");
    }

    public static Typeface setTypeFace1(Activity activity) {
        return Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Regular.ttf");
    }

    public static Typeface setTitleTypeFace(Activity activity) {
        return Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Bold.ttf");
    }

    public static float convertSpToPixels(float sp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static String getPreferenceString(String key) {
        return MyApplication.getInstance().getPreferenceUtility().getString(key);
    }

    public static void setPreferenceString(String key, String value) {
        MyApplication.getInstance().getPreferenceUtility().setString(key, value);
    }

    public static void clearData() {
        MyApplication.getInstance().getPreferenceUtility().clearData();
    }

    public static void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void setBadgeCount(Context context, MenuItem itemCart, String count) {

        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
}


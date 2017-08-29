package com.winsant.android.pushnotification;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.winsant.android.ui.MyApplication;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService
{
    private static final String TAG = "MyFirebaseIIDService";

    public void onTokenRefresh() {
        Intent service = new Intent(getApplicationContext(), GCMRegistrationIntentService.class);
        startService(service);

//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//        saveRegistrationToSP(refreshedToken);
    }

    private void saveRegistrationToSP(String token) {
        MyApplication.getInstance().getPreferenceUtility().setToken(token);
    }
}

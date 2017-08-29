package com.winsant.android.receiver;

/**
 * Created by Developer on 2/18/2017.
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Ravi on 09/07/15.
 */
public class SmsReceiver extends BroadcastReceiver
{
    private static final String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent)
    {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null)
            {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);

                    // if the SMS is not from our gateway, ignore the message
                    if (!senderAddress.contains("MD-WINSNT"))
                    {
                        return;
                    }

//                    if(senderAddress.equals("MD-WINSNT")){
//
//                    }else{
//                        return;
//                    }

                    // verification code from sms
                    String verificationCode = getVerificationCode(message);

                    Log.e(TAG, "OTP received: " + verificationCode);

                    Intent otpIntent = new Intent("OtpVerify");
                    otpIntent.putExtra("otp", verificationCode);
                    otpIntent.putExtra("isForOtp", "true");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(otpIntent);
                }
            }
        } catch (Exception e)
        {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    private String getVerificationCode(String message) {
        String code = null;
        code = message.substring(0, 6);
        return code;
    }
}
package com.winsant.android.pushnotification;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.winsant.android.R;
import com.winsant.android.ui.HomeActivity;
import com.winsant.android.utils.StaticDataUtility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by Hardik on 5/27/2016.
 */

public class FirebaseMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional

        System.out.println(StaticDataUtility.APP_TAG + " FireBaseMessageService From --> " + remoteMessage.getFrom());
        System.out.println(StaticDataUtility.APP_TAG + " FireBaseMessageService getData --> " + remoteMessage.getData());

        //Calling method to generate notification

//        sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        sendNotification(remoteMessage.getData());

    }
//        if (remoteMessage.getData().get("msg_type").equals("book_cab")) {
//            if (prefs.getString("alive").equals("true")) {
//                sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
//            } else {
//                sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
//            }
//        } else if (remoteMessage.getData().get("msg_type").equals("advertise")) {
//
//            if (remoteMessage.getData().get("image").equals(""))
//                showImageNotification(this, remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), null);
//            else
//                new generatePictureStyleNotification(this, remoteMessage.getData().get("title"), remoteMessage.getData().get("message")
//                        , remoteMessage.getData().get("image")).execute();
//
//        } else {
//
//            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
//        }


    private void sendNotification(Map<String, String> data) {

        Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra("from", "notification");
//        intent.putExtra("full_name", data.get("full_name"));
//        intent.putExtra("msg_type", data.get("msg_type"));
//        intent.putExtra("user_id", data.get("user_id"));
//        intent.putExtra("km", data.get("km"));
//        intent.putExtra("mob", data.get("mob"));
//        intent.putExtra("src_address", data.get("src_address"));
//        intent.putExtra("des_address", data.get("des_address"));
//        intent.putExtra("ride_id", data.get("ride_id"));
//        intent.putExtra("time", String.valueOf(System.currentTimeMillis()));

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

//        Uri defaultSoundUri = Uri.parse("android.resource://com.cabfinder.in/raw/vehicle041");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle = bigTextStyle.bigText(data.get("message")).setBigContentTitle(data.get("title"));

        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher).
                setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(data.get("title"))
                .setContentText(data.get("message"))
                .setAutoCancel(true)
                .setStyle(bigTextStyle)
//                .setSound(defaultSoundUri)
                .setOngoing(true)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String title, String messageBody) {

        Intent intent = new Intent();

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle = bigTextStyle.bigText(messageBody).setBigContentTitle(title);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher).
                setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setStyle(bigTextStyle)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, imageUrl;

        public generatePictureStyleNotification(Context context, String title, String message, String imageUrl) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            showImageNotification(mContext, title, message, result);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showImageNotification(Context mContext, String title, String message, Bitmap result) {

        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, intent, PendingIntent.FLAG_ONE_SHOT);

        if (result != null) {

            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle = bigPictureStyle.bigPicture(result).setBigContentTitle(title);

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            } else {
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            }

            notificationBuilder.setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                    .setStyle(bigPictureStyle)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true)
                    .build();

            notificationManager.notify(11, notificationBuilder.build());

        } else {

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            } else {
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            }

            notificationBuilder.setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .build();

            notificationManager.notify(11, notificationBuilder.build());
        }
    }
}
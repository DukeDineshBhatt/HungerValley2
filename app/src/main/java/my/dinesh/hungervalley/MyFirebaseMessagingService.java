package my.dinesh.hungervalley;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //handle when receive notification via data event
        if (remoteMessage.getData().size() > 0) {
            showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        }

        //handle when receive notification
        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

    }

    private RemoteViews getCustomDesign(String title, String message) {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,R.drawable.logo);
        return remoteViews;
    }

    public void showNotification(String title, String message) {
        Intent intent = new Intent(this, SplashActivity.class);
        String channel_id = "web_app_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.logo)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(getCustomDesign(title, message));
        } else {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logo);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }

    //app part ready now let see how to send differnet users
    //like send to specific device
    //like specifi topic
}


   /* @Override
    public void onNewToken(String s) {

        SharePreferenceUtils.getInstance().saveString("token" , s);

        Log.d("toekn" , s);



        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("asdasd" , remoteMessage.getData().toString());

        Intent registrationComplete = new Intent("count");

        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);


        JSONObject object = null;
        object = new JSONObject(remoteMessage.getData());

        try {
            handleNotification(object.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onMessageReceived(remoteMessage);
    }

    private void handleNotification(String message) {

        Log.d("notificationData", message);
        String idChannel = "Hunger messages";
        Intent mainIntent;

        mainIntent = new Intent(Application.getContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(Application.getContext(), 0, mainIntent, 0);

        NotificationManager mNotificationManager = (NotificationManager) Application.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = null;
        // The id of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(Application.getContext(), idChannel);
        builder.setContentTitle(Application.getContext().getString(R.string.app_name))
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentText(message);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(idChannel, Application.getContext().getString(R.string.app_name), importance);
            // Configure the notification channel.
            mChannel.setDescription(Application.getContext().getString(R.string.alarm_notification));
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        } else {
            builder.setContentTitle(Application.getContext().getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(Application.getContext(), R.color.colorAccent))
                    .setVibrate(new long[]{100, 250})
                    .setLights(Color.YELLOW, 500, 5000)
                    .setAutoCancel(true);
        }
        if (mNotificationManager != null) {
            mNotificationManager.notify(1, builder.build());
        }


    }

}*/

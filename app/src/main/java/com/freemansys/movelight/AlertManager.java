package com.freemansys.movelight;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlertManager {

    private static final int NOTIFICATION_CHANNEL_ID = 111;

    private static NotificationCompat.Builder builder;
    private NotificationManager mManager;

    public AlertManager(Context context) {
        initNotification(context);
    }

    private void initNotification(Context context) {
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent stateIntent = new Intent(context, NotificationReceiver.class);
        stateIntent.putExtra("channelId", NOTIFICATION_CHANNEL_ID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, stateIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("movelight_channel", "movelight", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null, null);
            mManager.createNotificationChannel(channel);
        }

        builder = new NotificationCompat.Builder(context, "movelight_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.str_flashlight_is_on))
                .setContentText(context.getString(R.string.str_flashlight_click_to_turn_off))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(0, context.getString(R.string.str_flashlight_turn_off), pendingIntent)
                .setOngoing(true);
    }

    public void showNotification() {
        mManager.notify(NOTIFICATION_CHANNEL_ID, builder.build());
    }

    public void closeNotification() {
        mManager.cancel(NOTIFICATION_CHANNEL_ID);
    }

    public static class NotificationReceiver extends BroadcastReceiver {

        /** custom listener for notification [START] **/
        private static NotificationListener mNotificationListener;
        public interface NotificationListener {
            void onClosed();
        }
        public void setOnNotificationClosed(NotificationListener listener){
            mNotificationListener = listener;
        }
        /** custom listener for notification [END] **/

        @Override
        public void onReceive(Context context, Intent intent) {
            int channelId = intent.getIntExtra("channelId", 0);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(channelId);

            mNotificationListener.onClosed();
        }

    }

}

package com.sccreporte.reporte.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.sccreporte.reporte.MainActivity;
import com.sccreporte.reporte.R;

/**
 * Created by simpson on 5/11/2018.
 */

public class NotificationUtils {

    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it.
     */
    private static final int CREATE_REPORT_REMINDER_NOTIFICATION_ID = 2128;
    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int CREATE_REPORT_REMINDER_PENDING_INTENT_ID = 2427;
    /**
     * This notification channel id is used to link notifications to this channel
     */
    private static final String CREATE_REPORT_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    /**
     * Create a notification reminding create report
     * also create the channel which this notification belong to(for android oreo)
     * also is reponsible for display the notification
     * @param context
     */
    public static void remindUserCreateReport(Context context){
        // Get the NotificationManager using context.getSystemService
        // NotificationManager is a system service
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Create a notification channel for Android O devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    CREATE_REPORT_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH); // Para forzar que se abra la app
            notificationManager.createNotificationChannel(mChannel);
        }
        // Create the actual notification and some attributes
        // Al final llama al contentIntent que crea el pending intent que abrira la app
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context,CREATE_REPORT_REMINDER_NOTIFICATION_CHANNEL_ID)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setSmallIcon(R.drawable.ic_action_scc)
                        .setLargeIcon(largeIcon(context))
                        .setContentTitle(context.getString(R.string.create_report_reminder_notification_title))
                        .setContentText(context.getString(R.string.create_report_reminder_notification_body))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(
                                context.getString(R.string.create_report_reminder_notification_body)))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentIntent(contentIntent(context))
                        .setAutoCancel(true);
        // If the build version is greater than JELLY_BEAN and lower than OREO,
        // set the notification's priority to PRIORITY_HIGH.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        // Trigger the notification by calling notify on the NotificationManager.
        notificationManager.notify(CREATE_REPORT_REMINDER_NOTIFICATION_ID, notificationBuilder.build());

    }

    /**
     * Create the pending intent which will trigger when
     * the notification is pressed. This pending intent should open up the MainActivity.
     * @param context
     * @return
     */
    private static PendingIntent contentIntent(Context context){
        // Create an intent that opens up the MainActivity
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        // Create a PendingIntent using getActivity
        return PendingIntent.getActivity(
                context,
                CREATE_REPORT_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
    /**
     * takes in a Context as a parameter and
     * returns a Bitmap. This method is necessary to decode a bitmap needed for the notification.
     * @param context
     * @return
     */
    private static Bitmap largeIcon(Context context) {
        // Get a Resources object from the context.
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_action_addreport);
        return largeIcon;
    }
}

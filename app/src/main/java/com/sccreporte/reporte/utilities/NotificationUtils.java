package com.sccreporte.reporte.utilities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.sccreporte.reporte.MainActivity;

/**
 * Created by simpson on 5/11/2018.
 */

public class NotificationUtils {

    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int CREATE_REPORT_REMINDER_PENDING_INTENT_ID = 3417;

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
}

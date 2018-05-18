package com.sccreporte.reporte.sync;

import android.content.Context;

import com.sccreporte.reporte.utilities.NotificationUtils;

/**
 * Created by simpson on 5/18/2018.
 */

public class ReminderTasks {

    // Tareas que se van a ejecutar
    public static final String ACTION_CREATE_REPORT_REMINDER = "create-report-reminder";

    public static void executeTask(Context context, String action) {
        if(ACTION_CREATE_REPORT_REMINDER.equals(action)){
            issueCreateReportReminder(context);
        }
    }
    private static void issueCreateReportReminder(Context context) {
        NotificationUtils.remindUserCreateReport(context);
    }
}

package com.sccreporte.reporte.sync;

import android.content.Context;
import androidx.annotation.NonNull;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by simpson on 5/18/2018.
 * To actually scheduled the job
 */

public class ReminderUtilities {
    private static final int REMINDER_INTERVAL_MINUTES = 1440;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;

    // Unique tag to identify the job
    private static final String REMINDER_JOB_TAG = "report_reminder_tag";

    // to keep track whether the job has been activated(started) or not
    private static boolean sInitialized;

    /**
     * The method tha actually start the job
     * synchronized because we dont want this method to execute mor than once at a time
     *
     * This method will use FirebaseJobDispatcher to schedule a job that repeats roughly
     * every REMINDER_INTERVAL_SECONDS. It will trigger CreateReportReminderFirebaseJobService
     * @param context
     */
    synchronized public static void scheduleCreateReportReminder(@NonNull final Context context) {
        // If the job has already been initialized, return
        if (sInitialized) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        // Now actually scheduled the job with the constraints
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(CreateReportReminderFirebaseJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                /**
                 * We want these reminders to continuously happen, so we tell this Job to recur.
                 */
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                /**
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)
                .build();
        // Schedule the Job with the dispatcher
        dispatcher.schedule(constraintReminderJob);
        /* The job has been initialized */
        sInitialized = true;
    }
}

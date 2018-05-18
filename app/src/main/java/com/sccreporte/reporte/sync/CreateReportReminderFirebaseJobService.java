package com.sccreporte.reporte.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by simpson on 5/18/2018.
 * This class actualy create a new Job Service that run the create report reminder task
 */

public class CreateReportReminderFirebaseJobService extends JobService {

    private AsyncTask mBackgroundTask;

    /**
     * The entry point to the Job. Implementations should offload work to another thread of
     * execution as soon as possible.
     *
     * This is called by the Job Dispatcher to tell us we should start our job. Keep in mind this
     * method is run on the application's main thread, so we need to offload work to a background
     * thread.
     *
     * @param job
     * @return whether there is more work remaining.
     */
    @Override
    public boolean onStartJob(final JobParameters job) {
        // Llamar a reminder task en otro hilo
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = CreateReportReminderFirebaseJobService.this;
                ReminderTasks.executeTask(context, ReminderTasks.ACTION_CREATE_REPORT_REMINDER);
                return null;
            }

            /**
             * JobService need to says the system when the job is done
             * @param o
             */
            @Override
            protected void onPostExecute(Object o) {
                // This will inform the JobManager that your job is done
                // and that you do not want to reschedule the job.
                /*
                 * Once the AsyncTask is finished, the job is finished. To inform JobManager that
                 * you're done, you call jobFinished with the jobParamters that were passed to your
                 * job and a boolean representing whether the job needs to be rescheduled. This is
                 * usually if something didn't work and you want the job to try running again.
                 */

                jobFinished(job, false);
            }
        };
        mBackgroundTask.execute();
        return true; // because our job is still doing some work in other thread
    }

    /**
     * It is call if the requirements of the job are not longer met
     * Por ejemplo si decides bajar videos cuando estas en wifi, si no hay mas wifi no te usara los datos moviles
     *
     * Called when the scheduling engine has decided to interrupt the execution of a running job,
     * most likely because the runtime constraints associated with the job are no longer satisfied.
     * @param job
     * @return whether the job should be retried
     */
    @Override
    public boolean onStopJob(JobParameters job) {
        if(mBackgroundTask != null)
            mBackgroundTask.cancel(true);
        return true; // significa : as soon the condition is re-met the job should be retried again
    }
}

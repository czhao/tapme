package com.garena.tapme.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.garena.tapme.R;
import com.garena.tapme.activity.HomeActivity;
import com.garena.tapme.application.Const;
import com.garena.tapme.helper.AppLogger;
import com.garena.tapme.helper.UIHelper;
import com.garena.tapme.storage.ActivityLogger;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * @author zhaocong
 * @since 10/29/14.
 */
public class ActivityRecognitionIntentService extends IntentService {

    public ActivityRecognitionIntentService(){
        super("activityRecognitionService");
    }

    public ActivityRecognitionIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // If the incoming intent contains an update
        AppLogger.i("handle the intent");
        if (ActivityRecognitionResult.hasResult(intent)) {
            // Get the update
            ActivityRecognitionResult result =
                    ActivityRecognitionResult.extractResult(intent);
            // Get the most probable activity
            DetectedActivity mostProbableActivity =
                    result.getMostProbableActivity();
            /*
             * Get the probability that this activity is the
             * the user's actual activity
             */
            int confidence = mostProbableActivity.getConfidence();
            /*
             * Get an integer describing the type of activity
             */
            int activityType = mostProbableActivity.getType();
            String activityName = getNameFromType(activityType);
            /*
             * At this point, you have retrieved all the information
             * for the current update. You can display this
             * information to the user in a notification, or
             * send it to an Activity or Service in a broadcast
             * Intent.
             */
            AppLogger.i("current activity %s with confidence %d",activityName,confidence);

            //if still
            if (activityType == DetectedActivity.STILL){
                //verify how long I have stayed still
                long period = ActivityLogger.getStillPeriod();
                AppLogger.i("current activity %d %d",period/ Const.MILL_TO_SECONDS,ActivityLogger.getStillPeriod());
                if (period/ Const.MILL_TO_SECONDS >= ActivityLogger.getWarningPeriod()){
                    //fire a notification to the wear and generate a cool reminder
                    sendReminder(period/Const.MILL_TO_SECONDS);
                }
            }
        } else {
            /*
             * This implementation ignores intents that don't contain
             * an activity update. If you wish, you can report them as
             * errors.
             */
        }
    }

    /**
     * Map detected activity types to strings
     *@param activityType The detected activity type
     *@return A user-readable name for the type
     */
    private String getNameFromType(int activityType) {
        switch(activityType) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.UNKNOWN:
                return "unknown";
            case DetectedActivity.TILTING:
                return "tilting";
        }
        return "unknown";
    }

    private void sendReminder(long stillInSeconds){
        int notificationId = 1;
        // Build intent for notification content
        Intent viewIntent = new Intent(this, ResponseIntentService.class);
        viewIntent.putExtra(Const.INTENT_KEY_STILL_TIME, 20);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);


        // Create the action
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_action_warning,
                        getString(R.string.label_got_it), viewPendingIntent).build();

        long[] pattern = {0,100,100,200};

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.app_reminder_title))
                        .setContentText(getString(R.string.sitting_too_long,stillInSeconds))
                        .setVibrate(pattern)
                        .extend(new NotificationCompat.WearableExtender().addAction(action))
                        .setContentIntent(viewPendingIntent);
        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}

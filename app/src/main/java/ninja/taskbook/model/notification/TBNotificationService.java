package ninja.taskbook.model.notification;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

//----------------------------------------------------------------------------------------------------
public class TBNotificationService extends NotificationListenerService {

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("Notification","Notification posted");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Notification", "Notification removed");
    }

    //----------------------------------------------------------------------------------------------------
    private boolean isNotificationEnabled() {
        String packageName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");

        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName sub = ComponentName.unflattenFromString(name);
                if (sub != null) {
                    if (TextUtils.equals(packageName, sub.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /*
    //----------------------------------------------------------------------------------------------------
    public static boolean isAppOnBackground(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    //----------------------------------------------------------------------------------------------------
    private static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    */
}

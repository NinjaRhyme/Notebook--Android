package ninja.taskbook.model.notification;

import java.io.IOException;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

//----------------------------------------------------------------------------------------------------
public class NotificationService extends Service {
    //----------------------------------------------------------------------------------------------------
    private static final String	ACTION_START = "START";
    private static final String	ACTION_STOP = "STOP";
    private static final String	ACTION_KEEP_ALIVE = "KEEP_ALIVE";
    private static final String	ACTION_RECONNECT = "RECONNECT";

    //----------------------------------------------------------------------------------------------------
    private long mStartTime;
    ConnectivityManager mConnectivityManager;
    NotificationManager mNotificationManager;
    private int mUserId;

    //----------------------------------------------------------------------------------------------------
    public static void actionStart(Context context, int userId) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(ACTION_START);
        intent.putExtra("user_id", userId);
        context.startService(intent);
    }

    public static void actionStop(Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(ACTION_STOP);
        context.startService(intent);
    }

    public static void actionPing(Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(ACTION_KEEP_ALIVE);
        context.startService(intent);
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate() {
        super.onCreate();

        mStartTime = System.currentTimeMillis();
        mConnectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent.getAction().equals(ACTION_START)) {
            start();
        } else if (intent.getAction().equals(ACTION_STOP)) {
            stop();
        } else if (intent.getAction().equals(ACTION_KEEP_ALIVE)) {
            //keepAlive();
        } else if (intent.getAction().equals(ACTION_RECONNECT)) {
            //reconnectIfNecessary();
        }
        mUserId = intent.getIntExtra("user_id", 0);
        return START_STICKY;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onDestroy() {
    }

    //----------------------------------------------------------------------------------------------------
    private synchronized void start() {

    }

    //----------------------------------------------------------------------------------------------------
    private synchronized void stop() {
        stopSelf();
    }
}
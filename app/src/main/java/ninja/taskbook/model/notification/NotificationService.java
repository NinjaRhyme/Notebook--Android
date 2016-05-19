package ninja.taskbook.model.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import android.os.Handler;
import android.os.IBinder;

import java.util.List;

import ninja.taskbook.R;
import ninja.taskbook.business.MainActivity;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.database.DatabaseManager;
import ninja.taskbook.model.entity.NotificationEntity;
import ninja.taskbook.model.entity.UserEntity;

//----------------------------------------------------------------------------------------------------
public class NotificationService extends Service {
    //----------------------------------------------------------------------------------------------------
    private static final String	ACTION_START = "START";
    private static final String	ACTION_STOP = "STOP";
    private static final String	ACTION_KEEP_ALIVE = "KEEP_ALIVE";
    private static final String	ACTION_RECONNECT = "RECONNECT";

    //----------------------------------------------------------------------------------------------------
    private long mStartTime;
    private Handler mHandler = new Handler();
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

        if (intent != null) {
            mUserId = intent.getIntExtra("user_id", 0);
            if (intent.getAction().equals(ACTION_START)) {
                start();
            } else if (intent.getAction().equals(ACTION_STOP)) {
                stop();
            } else if (intent.getAction().equals(ACTION_KEEP_ALIVE)) {
                //keepAlive();
            } else if (intent.getAction().equals(ACTION_RECONNECT)) {
                //reconnectIfNecessary();
            }
        } else {
            DatabaseManager.init(getContentResolver());
            UserEntity entity = DatabaseManager.getUserEntity();
            if (entity == null || entity.userId <= 0) {
                stop();
            } else {
                mUserId = entity.userId;
                DataManager.getInstance().setUserItem(entity);
                NotificationService.actionStart(getApplicationContext(), entity.userId);
                start();
            }
        }

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
        Runnable task = new Runnable() {
            public void run() {
                DataManager.getInstance().requestNewNotificationItems(
                        new DataManager.RequestCallback<List<NotificationEntity>>() {
                            @Override
                            public void onResult(List<NotificationEntity> result) {
                                for (NotificationEntity entity : result) {
                                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), 0);
                                    Notification notify = new Notification.Builder(getApplicationContext())
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle("Task Book")
                                            .setContentText("This is the notification message")
                                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                                            .setContentIntent(pendingIntent)
                                            .setWhen(System.currentTimeMillis())
                                            .setNumber(1)
                                            .build();
                                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                                    mNotificationManager.notify(entity.notificationId, notify);
                                }
                            }
                        }
                );

                mHandler.postDelayed(this, 5000);
            }
        };
        mHandler.post(task);
    }

    //----------------------------------------------------------------------------------------------------
    private synchronized void stop() {
        stopSelf();
    }
}
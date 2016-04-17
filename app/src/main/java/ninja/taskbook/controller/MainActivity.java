package ninja.taskbook.controller;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ninja.taskbook.R;
import ninja.taskbook.controller.profile.ProfileFragment;
import ninja.taskbook.controller.setting.SettingFragment;
import ninja.taskbook.controller.task.TaskItem;
import ninja.taskbook.controller.task.TaskFragment;
import ninja.taskbook.controller.drawer.DrawerManager;
import ninja.taskbook.controller.drawer.DrawerItem;

//----------------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity implements DrawerManager.DrawerListener {

    //----------------------------------------------------------------------------------------------------
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawer;
    private List<DrawerItem> mDrawerItems = new ArrayList<>();
    private DrawerManager mDrawerManager;
    private int mDrawerIndex = 0;
    private FrameLayout mFrameLayout;

    // Init
    //----------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initDrawer();
        initContent();
    }

    private void initToolbar() {
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void initDrawer() {
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawer = (LinearLayout)findViewById(R.id.drawer);

        DrawerItem item0 = new DrawerItem(R.mipmap.drawer_item_close);
        mDrawerItems.add(item0);
        DrawerItem item1 = new DrawerItem(R.mipmap.drawer_item_1);
        mDrawerItems.add(item1);
        DrawerItem item2 = new DrawerItem(R.mipmap.drawer_item_2);
        mDrawerItems.add(item2);
        DrawerItem item3 = new DrawerItem(R.mipmap.drawer_item_3);
        mDrawerItems.add(item3);
        DrawerItem item4 = new DrawerItem(R.mipmap.drawer_item_4);
        mDrawerItems.add(item4);
        DrawerItem item5 = new DrawerItem(R.mipmap.drawer_item_5);
        mDrawerItems.add(item5);
        DrawerItem iem6 = new DrawerItem(R.mipmap.drawer_item_6);
        mDrawerItems.add(iem6);
        DrawerItem item7 = new DrawerItem(R.mipmap.drawer_item_7);
        mDrawerItems.add(item7);

        mDrawerManager = new DrawerManager(this, mCoordinatorLayout, mToolbar, mDrawerLayout, mDrawer, mDrawerItems, this);
    }

    private void initContent() {
        mFrameLayout = (FrameLayout)findViewById(R.id.frame_layout);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new ProfileFragment())
                .commit();
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            /*
            ContentValues values = new ContentValues();
            values.put("user_id", 0);
            getContentResolver().insert(DatabaseInfo.UserTable.CONTENT_URI, values);
            */
            /*
            Runnable task = new Runnable() {
                public void run() {
                    try {
                        int result = ((HelloService.Client) ThriftManager.getInstance().createClient(ThriftManager.ClientTypeEnum.CLIENT_HELLO.toString())).hi("123", "234", "345");
                    } catch (TException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(task).start();
            */
            /*
            String[] words = {"Hello", "Hi", "Aloha"};
            Observable.just(words)
                    .map(new Func1<String[], Integer>() {
                        @Override
                        public Integer call(String[] words) {
                            try {
                                HelloService.Client client = (HelloService.Client)ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT_HELLO.toString());
                                if (client != null)
                                    return client.hi(words[0], words[1], words[2]);
                            } catch (TException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer result) {

                        }
                    });
                    */
            /*
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
            Notification notify = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.useless0)
                    .setTicker("TickerText:" + "xi xi xi")
                    .setContentTitle("Notification Title")
                    .setContentText("This is the notification message")
                    .setContentIntent(pendingIntent)
                    .setNumber(1)
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            manager.notify(1, notify);
            */

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------------------------------------------------------
    public Bitmap takeFrameScreenShot() {
        // Todo: Thread
        Bitmap bitmap = Bitmap.createBitmap(mFrameLayout.getWidth(), mFrameLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mFrameLayout.draw(canvas);

        return bitmap;
    }

    // DrawerListener
    //----------------------------------------------------------------------------------------------------
    @Override
    public void onDrawerItemClicked(int index) {
        if (mDrawerIndex != index) {
            // Todo: animation
            switch (index) {
                case 0:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, new ProfileFragment())
                            .commit();
                    break;
                case 1:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, new TaskFragment())
                            .commit();
                    break;
                case 3:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, new SettingFragment())
                            .commit();
                    break;
                default:
                    break;
            }
        }

        mDrawerIndex = index;
    }
}

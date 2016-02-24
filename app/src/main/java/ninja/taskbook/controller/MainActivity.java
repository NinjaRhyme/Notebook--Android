package ninja.taskbook.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ninja.taskbook.R;
import ninja.taskbook.controller.Drawer.DrawerManager;
import ninja.taskbook.controller.Drawer.DrawerItem;

//----------------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------------
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawer;
    private List<DrawerItem> mDrawerItems = new ArrayList<>();
    private DrawerManager mDrawerManager;
    private Toolbar mToolbar;

    // Init
    //----------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initDrawer();

        //ContentValues values = new ContentValues();
        //values.put("user_id", 0);
        //getContentResolver().insert(DatabaseInfo.UserTable.CONTENT_URI, values);
    }

    private void initToolbar() {
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawer = (LinearLayout)findViewById(R.id.drawer);
        mDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
            }
        });

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

        mDrawerManager = new DrawerManager(this, mDrawerLayout, mToolbar, mDrawerItems);
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

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

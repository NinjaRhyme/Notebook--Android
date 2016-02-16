package ninja.taskbook.controller;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import ninja.taskbook.R;
import ninja.taskbook.model.database.DatabaseInfo;
import ninja.taskbook.model.database.TBContentProvider;


//----------------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------------
    DrawerLayout m_drawerLayout;
    NavigationView m_navigationView;
    Toolbar m_toolbar;

    // Init
    //----------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();
        initToolbar();
        initActionBarDrawerToggle();

        ContentValues values = new ContentValues();
        values.put("user_id", 0);
        getContentResolver().insert(DatabaseInfo.UserTable.CONTENT_URI, values);
    }

    private void initDrawer() {
        m_drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        m_navigationView = (NavigationView)findViewById(R.id.navigation_view);
        m_navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        item.setChecked(false);

                        switch (item.getItemId()) {
                            // Todo
                            default:
                                break;
                        }

                        m_drawerLayout.closeDrawers();
                        return true;
                    }
                }
        );

    }

    private void initToolbar() {
        m_toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(m_toolbar);
    }

    private void initActionBarDrawerToggle() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, m_drawerLayout, m_toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        m_drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

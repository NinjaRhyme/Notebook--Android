package ninja.taskbook.controller.Drawer;

import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ninja.taskbook.R;

//----------------------------------------------------------------------------------------------------
public class DrawerManager {

    //----------------------------------------------------------------------------------------------------
    private final int ANIMATION_DURATION = 175;

    private Boolean mIsActive;
    private AppCompatActivity mActivity;
    private DrawerLayout mDrawerLayout;
    private List<View> mDrawerItemViews = new ArrayList<>();

    //----------------------------------------------------------------------------------------------------
    public DrawerManager(AppCompatActivity activity, DrawerLayout drawerLayout, Toolbar toolbar, List<DrawerItem> drawerItems) {
        mIsActive = false;
        mActivity = activity;
        mDrawerLayout = drawerLayout;
        for (int i = 0; i < drawerItems.size(); ++i) {
            //final int index = i;
            View itemView = mActivity.getLayoutInflater().inflate(R.layout.drawer_item, null); // Todo
            itemView.setVisibility(View.INVISIBLE);
            itemView.setEnabled(false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideContent();
                }
            });
            ((ImageView) itemView.findViewById(R.id.drawer_item_image)).setImageResource(drawerItems.get(i).getImageRes());

            mDrawerItemViews.add(itemView);
            ((LinearLayout)mDrawerLayout.findViewById(R.id.drawer)).addView(itemView);
        }

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this.mActivity, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                for (View view : mDrawerItemViews) {
                    view.setVisibility(View.INVISIBLE);
                    view.setEnabled(false);
                }
                mIsActive = false;
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6f && !mIsActive)
                    showContent();
                else if (slideOffset == 0.f && mIsActive) {
                    for (View view : mDrawerItemViews) {
                        view.setVisibility(View.INVISIBLE);
                        view.setEnabled(false);
                    }
                    mIsActive = false;
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    //----------------------------------------------------------------------------------------------------
    public void showContent() {
        mIsActive = true;
        for (int i = 0; i < mDrawerItemViews.size(); ++i) {
            final int index = i;
            final double delay = 3 * ANIMATION_DURATION * ((float)index / mDrawerItemViews.size());
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (index < mDrawerItemViews.size()) {
                        showAnimation(index);
                    }
                }
            }, (long)delay);
        }
    }

    //----------------------------------------------------------------------------------------------------
    private void showAnimation(int index) {
        final View view = mDrawerItemViews.get(index);
        view.setVisibility(View.VISIBLE);
        DrawerItemAnimation animation = new DrawerItemAnimation(90, 0, 0.f, view.getHeight() / 2.f);
        animation.setDuration(ANIMATION_DURATION);
        animation.setFillAfter(true);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animation);
    }

    //----------------------------------------------------------------------------------------------------
    private void hideContent() {
        for (int i = 0; i < mDrawerItemViews.size(); ++i) {
            final int index = i;
            final double delay = 3 * ANIMATION_DURATION * ((float)index / mDrawerItemViews.size());
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (index < mDrawerItemViews.size()) {
                        hideAnimation(index);
                    }
                }
            }, (long)delay);
        }
    }

    //----------------------------------------------------------------------------------------------------
    private void hideAnimation(final int index) {
        final View view = mDrawerItemViews.get(index);
        DrawerItemAnimation animation = new DrawerItemAnimation(0, 90, 0.f, view.getHeight() / 2.f);
        animation.setDuration(ANIMATION_DURATION);
        animation.setFillAfter(true);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.INVISIBLE);
                if (index == mDrawerItemViews.size() - 1) {
                    mDrawerLayout.closeDrawers();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animation);
    }
}

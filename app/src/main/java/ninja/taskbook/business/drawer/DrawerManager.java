package ninja.taskbook.business.drawer;

import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
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
    private CoordinatorLayout mCoordinatorLayout;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawer;
    private List<View> mDrawerItemViews = new ArrayList<>();
    private DrawerListener mDrawerListener;

    //----------------------------------------------------------------------------------------------------
    public DrawerManager(AppCompatActivity activity, CoordinatorLayout coordinatorLayout, Toolbar toolbar, DrawerLayout drawerLayout, LinearLayout drawer, List<DrawerItem> drawerItems, DrawerListener drawerListener) {
        mIsActive = false;
        mActivity = activity;
        mCoordinatorLayout = coordinatorLayout;
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawer = drawer;
        mDrawerListener = drawerListener;
        mDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
            }
        });
        for (int i = 0; i < drawerItems.size(); ++i) {
            final int index = i;
            View itemView = mActivity.getLayoutInflater().inflate(R.layout.drawer_item, null); // Todo
            itemView.setVisibility(View.INVISIBLE);
            itemView.setEnabled(false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDrawerListener != null) {
                        mDrawerListener.onDrawerItemClicked(index);
                    }
                    hideContent();
                }
            });
            ((ImageView) itemView.findViewById(R.id.drawer_item_image)).setImageResource(drawerItems.get(i).getImageRes());

            mDrawerItemViews.add(itemView);
            mDrawer.addView(itemView);
        }

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
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
                mCoordinatorLayout.setTranslationX(drawerView.getWidth() * slideOffset);
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
    private void showAnimation(final int index) {
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
        mDrawerLayout.closeDrawers();
    }

    //----------------------------------------------------------------------------------------------------
    public interface DrawerListener {
        void onDrawerItemClicked(int index);
    }
}

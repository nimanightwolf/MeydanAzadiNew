package com.app.dadepardazan.meydanazadi.view_pager.list_follow;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.app.dadepardazan.meydanazadi.MainActivity;
import com.app.dadepardazan.meydanazadi.R;
import com.google.android.material.tabs.TabLayout;

/**
 * Created by Bamdad on 7/17/2019.
 */

public class FollowActivity extends MainActivity {
    FollowAdapter adapterFragment;
    TabLayout mtabTabLayout;
    TextView toolbar_title;


    String str_user_username = "";


    @Override

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        // setContentView(R.layout.listbuy_and_hestory);
        FrameLayout content_frame;

        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_follow, content_frame);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        str_user_username = getIntent().getStringExtra("username");
        img_toolbar.setVisibility(View.GONE);

        toolbar_title.setText("@"+str_user_username);
        toolbar_title.setTextSize(24);
        toolbar_title.setVisibility(View.VISIBLE);
        BottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);


        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager_profile);
        mtabTabLayout = (TabLayout) findViewById(R.id.tablayout_profile);
        mtabTabLayout.setupWithViewPager(vpPager);


        // mtabTabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#00000")));
        //mtabTabLayout.setTabTextColors(getResources().getColor(R.color.color_hint_white), getResources().getColor(R.color.colorblack));
//
        adapterFragment = new FollowAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterFragment);
        vpPager.setPageTransformer(true, new RotateUpTransformer());
        if (vpPager.getCurrentItem() == 0) {
            //     navigationView.getMenu().findItem(R.id.mnu_category).setChecked(true);
        } else if (vpPager.getCurrentItem() == 1) {
            //    navigationView.getMenu().findItem(R.id.mnu_category).setChecked(true);
        }

        //برای تغییر صفحه با کد
        vpPager.setCurrentItem(getIntent().getIntExtra("int_page", 0));
        settings = PreferenceManager.getDefaultSharedPreferences(this);
//        if (settings.getInt("theme", 0) == 1) {
//            //setTheme(R.style.NoActionBarDark);
//            mtabTabLayout.setBackgroundColor(Color.parseColor("#4C4C4C"));
//           // mtabTabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#00000")));
//            mtabTabLayout.setTabTextColors(getResources().getColor(R.color.colorwhite), getResources().getColor(R.color.colorwhite));
//
//
//        }
//        if (settings.getInt("theme", 0) == 2) {
//            //setTheme(R.style.NoActionBarBlack);
//            mtabTabLayout.setBackgroundColor(Color.parseColor("#000000"));
//        }

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        BottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.back_menu, menu);

        return true;

    }

    //
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_back) {
            finish();

        }
        return super.onOptionsItemSelected(item);
        //show_help_dialog();


    }


    public class FollowAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

        public FollowAdapter(FragmentManager fragmentManager) {

            super(fragmentManager);

        }

        // Returns total number of pages.
        @Override

        public int getCount() {
            return NUM_ITEMS;
        }
        // Returns the fragment to display for a particular page.

        @Override

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FragmentFollolw.newInstance("list_followers_user", getIntent().getStringExtra("token"));
                case 1:
                    return FragmentFollolw.newInstance("list_following_user", getIntent().getStringExtra("token"));
                default:
                    return null;
            }
        }
        // Returns the page title for the top indicator

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return "دنبال کننده ها";
            if (position == 1)
               return "دنبال شونده ها";
            else
                return "";
        }
    }


}

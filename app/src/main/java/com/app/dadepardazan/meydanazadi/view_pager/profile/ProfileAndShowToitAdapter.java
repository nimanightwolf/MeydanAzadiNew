package com.app.dadepardazan.meydanazadi.view_pager.profile;

import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.dadepardazan.meydanazadi.view_pager.post.FragmentActivite;
import com.app.dadepardazan.meydanazadi.view_pager.post.FragmentTweet;


public class ProfileAndShowToitAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    public SharedPreferences settings;


    public ProfileAndShowToitAdapter(FragmentManager fragmentManager) {

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

                return FragmentTweet.newInstance("toit", "{}");

            case 1:

                return FragmentActivite.newInstance("activites", "{}");
            default:

                return null;

        }

    }

    // Returns the page title for the top indicator

    @Override

    public CharSequence getPageTitle(int position) {
        if (position == 1)
            return "فعالیت ها";
        if (position == 0)
            return "توییت ها";
        else
            return "";


    }

}
package com.app.dadepardazan.meydanazadi.view_pager.search;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.dadepardazan.meydanazadi.R;


public class SearchAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public SearchAdapter(FragmentManager fragmentManager) {

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

                return FragmentSearch.newInstance("list_hash_tag_search", R.drawable.logo);

            case 1:

                return FragmentSearch.newInstance("near", R.drawable.logo);
            case 2:

                return FragmentSearch.newInstance("near", R.drawable.logo);
            case 3:

                return FragmentSearch.newInstance("near", R.drawable.logo);


            default:

                return null;

        }

    }

    // Returns the page title for the top indicator

    @Override

    public CharSequence getPageTitle(int position) {
        if (position ==1)
            return "افراد";
        if (position == 0)
            return "تگ ها";
        else
            return "";


    }

}
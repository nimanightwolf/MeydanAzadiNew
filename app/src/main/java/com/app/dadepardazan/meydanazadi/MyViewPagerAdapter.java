package com.app.dadepardazan.meydanazadi;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MyViewPagerAdapter extends PagerAdapter {
    Context cntx;


    public MyViewPagerAdapter(Context cntx) {
        this.cntx = cntx;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {


        return "صفحه " + position;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout layout = new LinearLayout(cntx);
        ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(param);
        layout.setOrientation(LinearLayout.VERTICAL);


        Button button = new Button(cntx);
        button.setText("Button " + position);
        button.setTextColor(ContextCompat.getColor(cntx, R.color.colorPrimary));
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(param2);
        layout.addView(button);
        container.addView(layout);
        return layout;
//        return LayoutInflater.from(cntx)
//                .inflate(R.layout.item_view_pager, null, false);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeViewAt(position);
        container.removeView((View) object);

    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

}

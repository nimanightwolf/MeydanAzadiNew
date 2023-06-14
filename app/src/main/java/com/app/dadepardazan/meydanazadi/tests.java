package com.app.dadepardazan.meydanazadi;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Bamdad on 1/29/2019.
 */

public class tests extends MainActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout content_frame;
        toolbar.setTitle("ssssss");
        toolbar.setVisibility(View.GONE);
        //AdListActivity.this.setTitle("میدان ازادی");

        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.book, content_frame);
        // navigationView.getMenu().findItem(R.id.mnu_main).setChecked(true);
        BottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);




        Typeface bnazanin=Typeface.createFromAsset(getAssets(),"fonts/IRANSans.ttf");


    }




}

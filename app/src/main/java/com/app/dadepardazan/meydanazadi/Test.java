package com.app.dadepardazan.meydanazadi;

import android.os.Bundle;


/**
 * Created by Bamdad on 3/11/2019.
 */

public class Test extends MainActivity  {
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar9);
        setSupportActionBar(toolbar);




    }

}


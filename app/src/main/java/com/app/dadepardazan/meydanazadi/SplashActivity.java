package com.app.dadepardazan.meydanazadi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.app.dadepardazan.meydanazadi.all_tweet.AllTweetActivity;
import com.app.dadepardazan.meydanazadi.login.Login2Activity;


public class SplashActivity extends MainActivity {
    ImageView imageView_gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        imageView_gif = (ImageView) findViewById(R.id.imageView_gif);
//        Ion.with(imageView_gif)
//                .animateGif(AnimateGifMode.ANIMATE)
//                .load("file:///android_asset/splashh.gif");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if ((settings.getInt("user_id", 0) != 0) & !settings.getString("token", "").equals("")) {


                    Intent i = new Intent(SplashActivity.this, AllTweetActivity.class);
                    startActivity(i);

                    finish();
                } else {
                    Intent i=new Intent(SplashActivity.this, Login2Activity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 2500);


    }


}

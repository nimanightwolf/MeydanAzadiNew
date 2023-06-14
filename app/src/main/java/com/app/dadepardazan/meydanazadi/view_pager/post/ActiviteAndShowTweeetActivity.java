package com.app.dadepardazan.meydanazadi.view_pager.post;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.app.dadepardazan.meydanazadi.MainActivity;
import com.app.dadepardazan.meydanazadi.R;
import com.app.dadepardazan.meydanazadi.view_pager.list_follow.FollowActivity;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActiviteAndShowTweeetActivity extends MainActivity {
    ViewPagerAdapter view_pagerAdapter;
    TabLayout mtabTabLayout;
    TextView toolbar_title;
    public TextView tv_profile_name;
    TextView tv_profile_bio;
    TextView tv_profile_user_name;
    TextView tv_profile_followers;
    TextView tv_profile_following;
    CircleImageView profile_image;
    CardView card_btn_profile_followering;
    CardView card_btn_profile_followed;
    TextView tv_profile_followering;
    CardView card_btn_profile_kasb_dar_amadm;
    private MCrypt mcrypt;
    ArrayList<ImageView> images = new ArrayList<ImageView>();
    int current_image;
    String string_id = "";
    JSONObject job_gereral;
    String str_android_id = "";
    String str_user_token = "";
    String str_user_username = "";
    SkeletonScreen skeletonScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout content_frame;

        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_activite_and_show_tweeet, content_frame);
//        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
//        toolbar_title.setText("میدان آزادی");
//        toolbar_title.setTextSize(24);
//        toolbar_title.setVisibility(View.VISIBLE);

        str_android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);


        findviewbyid();

        try {
            //  Log.e("data intent", getIntent().getStringExtra("ad"));
            if (getIntent().hasExtra("username")) {
                str_user_username = getIntent().getStringExtra("username");
            }

            // str_user_token=getIntent().getStringExtra("user_token");
            if (getIntent().hasExtra("ad")) {
                job_gereral = new JSONObject(getIntent().getStringExtra("ad"));
                setVlaueFromJson();
                Bundle extras = getIntent().getExtras();
                Bitmap bmp = (Bitmap) extras.getParcelable("imagebitmap");
                profile_image.setImageBitmap(bmp);
            } else {
                CoordinatorLayout coordinatorLayout=findViewById(R.id.coordinatorlayout);
//                skeletonScreen = Skeleton.bind(coordinatorLayout)
//                        .load(R.layout.activity_activite_and_show_tweeet)
//                        .show();
                new read_user_info_by_user_name().execute();
            }

            // tv_profile_name.setText(job_gereral.getString("name"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        card_btn_profile_followering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (job_gereral.getString("is_follow").equals("1")) {
                        card_btn_profile_followering.setVisibility(View.GONE);
                        card_btn_profile_followed.setVisibility(View.VISIBLE);
                        //     tv_profile_followering.setText("دنبال کرده اید");
                        //   card_btn_profile_followering.setBackgroundColor(getResources().getColor(R.color.color_followed));
                        //    card_btn_profile_followering.setRadius(8);
                        job_gereral.put("is_follow", "0");

                        new send_follow().execute();
                    } else {
                        card_btn_profile_followering.setVisibility(View.VISIBLE);
                        card_btn_profile_followed.setVisibility(View.GONE);
                        //  tv_profile_followering.setText("دنبال کنید");
                        //  card_btn_profile_followering.setBackgroundColor(getResources().getColor(R.color.color_following));
                        job_gereral.put("is_follow", "1");
                        // card_btn_profile_followering.setRadius(8);
                        new send_follow().execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        card_btn_profile_followed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (job_gereral.getString("is_follow").equals("1")) {
                        card_btn_profile_followering.setVisibility(View.GONE);
                        card_btn_profile_followed.setVisibility(View.VISIBLE);
                        //     tv_profile_followering.setText("دنبال کرده اید");
                        //   card_btn_profile_followering.setBackgroundColor(getResources().getColor(R.color.color_followed));
                        //    card_btn_profile_followering.setRadius(8);
                        job_gereral.put("is_follow", "0");

                        new send_follow().execute();
                    } else {
                        card_btn_profile_followering.setVisibility(View.VISIBLE);
                        card_btn_profile_followed.setVisibility(View.GONE);
                        //  tv_profile_followering.setText("دنبال کنید");
                        //  card_btn_profile_followering.setBackgroundColor(getResources().getColor(R.color.color_following));
                        job_gereral.put("is_follow", "1");
                        // card_btn_profile_followering.setRadius(8);
                        new send_follow().execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        tv_profile_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //دنبال کننده
                    Intent i = new Intent(ActiviteAndShowTweeetActivity.this, FollowActivity.class);
                    i.putExtra("username", job_gereral.getString("username"));
                    i.putExtra("token", job_gereral.getString("user_token"));
                    i.putExtra("int_page", 0);
                    startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        tv_profile_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(ActiviteAndShowTweeetActivity.this, FollowActivity.class);
                    i.putExtra("username", job_gereral.getString("username"));
                    i.putExtra("token", job_gereral.getString("user_token"));
                    i.putExtra("int_page", 1);
                    startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager_profile);
        mtabTabLayout = (TabLayout) findViewById(R.id.tablayout_profile);
        mtabTabLayout.setupWithViewPager(vpPager);


        // mtabTabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#00000")));
        //mtabTabLayout.setTabTextColors(getResources().getColor(R.color.color_hint_white), getResources().getColor(R.color.colorblack));
//
        view_pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(view_pagerAdapter);
        vpPager.setPageTransformer(true, new RotateUpTransformer());
        if (vpPager.getCurrentItem() == 0) {
            //     navigationView.getMenu().findItem(R.id.mnu_category).setChecked(true);
        } else if (vpPager.getCurrentItem() == 1) {
            //    navigationView.getMenu().findItem(R.id.mnu_category).setChecked(true);
        }

        //برای تغییر صفحه با کد
        vpPager.setCurrentItem(2);
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

        //new get_list_search().execute();

    }

    private void setVlaueFromJson() {
        try {
            tv_profile_name.setText(job_gereral.getString("name"));
            tv_profile_user_name.setText(job_gereral.getString("username"));
            tv_profile_bio.setText(job_gereral.getString("bio"));
            tv_profile_followers.setText(job_gereral.getString("followers") + "\n دنبال کننده ها");
            tv_profile_following.setText(job_gereral.getString("following") + "\n دنبال شونده ها");
            if (job_gereral.getString("is_follow").equals("1")) {
//                tv_profile_followering.setText("دنبال کرده اید");
//                card_btn_profile_followering.setBackgroundColor(getResources().getColor(R.color.color_followed));
//                card_btn_profile_followering.setRadius(8);
                card_btn_profile_followering.setVisibility(View.GONE);
                card_btn_profile_followed.setVisibility(View.VISIBLE);


            } else {
//                tv_profile_followering.setText("دنبال کنید");
//                card_btn_profile_followering.setBackgroundColor(getResources().getColor(R.color.color_following));
//                card_btn_profile_followering.setRadius(8);
                card_btn_profile_followering.setVisibility(View.VISIBLE);
                card_btn_profile_followed.setVisibility(View.GONE);
            }
            str_user_token = job_gereral.getString("user_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void findviewbyid() {

        tv_profile_name = findViewById(R.id.tv_profile_name);
        tv_profile_bio = findViewById(R.id.tv_profile_bio);
        tv_profile_user_name = findViewById(R.id.tv_profile_user_name);
        profile_image = findViewById(R.id.profile_image);
        card_btn_profile_followering = findViewById(R.id.card_btn_profile_followering);
        card_btn_profile_followed = findViewById(R.id.card_btn_profile_followed);

        card_btn_profile_kasb_dar_amadm = findViewById(R.id.card_btn_profile_kasb_dar_amadm);
        //card_btn_profile_kasb_dar_amadm.setVisibility(View.GONE);
        card_btn_profile_followering.setVisibility(View.VISIBLE);

        tv_profile_followers = findViewById(R.id.tv_profile_followers);
        tv_profile_following = findViewById(R.id.tv_profile_following);
        tv_profile_followering = findViewById(R.id.tv_profile_followering);


    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 1;

        public ViewPagerAdapter(FragmentManager fragmentManager) {

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

                    return FragmentTweet.newInstance("toit", str_user_username);

                case 1:

                    return FragmentActivite.newInstance("activites", job_gereral.toString());
                default:

                    return null;

            }

        }

        // Returns the page title for the top indicator

        @Override

        public CharSequence getPageTitle(int position) {

            if (position == 0)
                return "نویسه ها";
            if (position == 1)
                return "فعالیت ها";
            else
                return "";


        }
    }

    public class read_user_info_by_user_name extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(ActiviteAndShowTweeetActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.setCancelable(false);
            //    pd.show();
            progress_ball.setVisibility(View.VISIBLE);
            progress_ball.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MainActivity.MCrypt();
            try {

                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("read_user_info_by_user_name")));
                get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("user_name", mcrypt.bytesToHex(mcrypt.encrypt(str_user_username)));
                get_ad_list.put("imei", mcrypt.bytesToHex(mcrypt.encrypt(str_android_id)));


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));
            Log.e("data user_info", get_ad_list.toString());

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/ecxa.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    response = new String(mcrypt.decrypt(response)).trim();
                    Log.e("data respose info", response);


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                job_gereral = new JSONObject(finalResponse);
                                setVlaueFromJson();
                                if (!job_gereral.getString("image").equals("")) {
                                    Picasso.get().load("https://meydane-azadi.ir/photo/photo.php?image_name=" + job_gereral.getString("image")).resize(512, 512).into(profile_image);
                                } else {
                                    profile_image.setImageResource(R.drawable.icon_user);

                                }
                                //skeletonScreen.hide();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(ActiviteAndShowTweeetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(ActiviteAndShowTweeetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });


            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.hide();
////            pd.dismiss();
            progress_ball.setVisibility(View.GONE);
        }
    }

    public class send_follow extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(ActiviteAndShowTweeetActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // pd.setMessage("در حال دریافت اطلاعات");
            // pd.setCancelable(false);
            //    pd.show();
            progress_ball.setVisibility(View.VISIBLE);
            progress_ball.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MainActivity.MCrypt();
            try {

                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("follow_user_by_username")));
                get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                // get_ad_list.put("token", mcrypt.bytesToHex(mcrypt.encrypt(str_user_token)));
                get_ad_list.put("username", mcrypt.bytesToHex(mcrypt.encrypt(str_user_username)));


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));
            Log.e("data sended", get_ad_list.toString());

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/ecxa.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    response = new String(mcrypt.decrypt(response)).trim();
                    response = response.replace("\"", "");

                    Log.e("data respose", response);

                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalResponse.equals("follow")) {
//                                tv_profile_followering.setText("دنبال کرده اید");
//                                card_btn_profile_followering.setBackgroundColor(getResources().getColor(R.color.color_followed));
                                card_btn_profile_followering.setVisibility(View.GONE);
                                card_btn_profile_followed.setVisibility(View.VISIBLE);
                                progress_ball.setVisibility(View.GONE);
                            } else {
//                                tv_profile_followering.setText("دنبال کنید");
//                                card_btn_profile_followering.setBackgroundColor(getResources().getColor(R.color.color_following));
                                card_btn_profile_followering.setVisibility(View.VISIBLE);
                                card_btn_profile_followed.setVisibility(View.GONE);
                                progress_ball.setVisibility(View.GONE);
                            }
                            setResult(RESULT_OK);

                        }
                    });

                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(ActiviteAndShowTweeetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(ActiviteAndShowTweeetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });


            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.hide();
////            pd.dismiss();
            progress_ball.hide();
            progress_ball.setVisibility(View.GONE);


        }
    }


}
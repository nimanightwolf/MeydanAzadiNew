package com.app.dadepardazan.meydanazadi.view_pager.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.app.dadepardazan.meydanazadi.MainActivity;
import com.app.dadepardazan.meydanazadi.R;
import com.app.dadepardazan.meydanazadi.SendNewTweetActivity;
import com.app.dadepardazan.meydanazadi.TasviehActivity;
import com.app.dadepardazan.meydanazadi.view_pager.list_follow.FollowActivity;
import com.app.dadepardazan.meydanazadi.view_pager.post.FragmentActivite;
import com.app.dadepardazan.meydanazadi.view_pager.post.FragmentTweet;
import com.bumptech.glide.Glide;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

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

/**
 * Created by Bamdad on 7/17/2019.
 */

public class ProfileAndShowToitActivity extends MainActivity {
    private static final int REQUEST_CODE_EDIT = 5646;
    ProfileAndShowToitAdapter2 profileAdapter;
    TabLayout mtabTabLayout;
    TextView toolbar_title;
    TextView tv_profile_profile;
    TextView tv_profile_name;
    TextView tv_profile_bio;
    TextView tv_profile_user_name;
    CircleImageView profile_image;
    CardView card_btn_profile_followering;
    CardView card_btn_profile_kasb_dar_amadm;
    private MCrypt mcrypt;
    ArrayList<ImageView> images = new ArrayList<ImageView>();
    int current_image;
    String string_id = "";
    JSONObject ad_list;
    TextView tv_profile_followers;
    TextView tv_profile_following;
    SkeletonScreen skeletonScreen;
    FloatingActionButton fb_send_new_tweet;


    @Override

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        // setContentView(R.layout.listbuy_and_hestory);
        FrameLayout content_frame;

        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.profile_and_show_toite_activity, content_frame);
//        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
//        toolbar_title.setText("میدان آزادی");
//        toolbar_title.setTextSize(24);
//        toolbar_title.setVisibility(View.VISIBLE);
        BottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);

        try {
            ad_list = new JSONObject();
            ad_list.put("user_token", settings.getString("token", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        findviewbyid();
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorlayout);
//        skeletonScreen = Skeleton.bind(coordinatorLayout)
//               .load(R.layout.profile_and_show_toite_activity)
//               .show();
        card_btn_profile_kasb_dar_amadm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileAndShowToitActivity.this, TasviehActivity.class);
                i.putExtra("ad", ad_list.toString());
                startActivity(i);
            }
        });
        fb_send_new_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ProfileAndShowToitActivity.this, SendNewTweetActivity.class);
                startActivity(i);
                finish();
            }
        });

        tv_profile_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //دنبال کننده
                    Intent i = new Intent(ProfileAndShowToitActivity.this, FollowActivity.class);
                    i.putExtra("username", ad_list.getString("username"));
                    i.putExtra("token", ad_list.getString("user_token"));
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
                    Intent i = new Intent(ProfileAndShowToitActivity.this, FollowActivity.class);
                    i.putExtra("username", ad_list.getString("username"));
                    i.putExtra("token", ad_list.getString("user_token"));
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

        tv_profile_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileAndShowToitActivity.this, ProFileUser2Activity.class);
                startActivityForResult(i, REQUEST_CODE_EDIT);
            }
        });

        tv_profile_profile.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_profile_setting), null);

        // mtabTabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#00000")));
        //mtabTabLayout.setTabTextColors(getResources().getColor(R.color.color_hint_white), getResources().getColor(R.color.colorblack));
//
        profileAdapter = new ProfileAndShowToitAdapter2(getSupportFragmentManager());
        vpPager.setAdapter(profileAdapter);
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

        new get_user_info().execute();
    }

    private void findviewbyid() {
        tv_profile_profile = (TextView) findViewById(R.id.tv_profile_profile);
        tv_profile_name = findViewById(R.id.tv_profile_name);
        tv_profile_bio = findViewById(R.id.tv_profile_bio);
        tv_profile_user_name = findViewById(R.id.tv_profile_user_name);
        profile_image = findViewById(R.id.profile_image);
        card_btn_profile_followering = findViewById(R.id.card_btn_profile_followering);
        card_btn_profile_kasb_dar_amadm = findViewById(R.id.card_btn_profile_kasb_dar_amadm);
        card_btn_profile_kasb_dar_amadm.setVisibility(View.VISIBLE);
        tv_profile_followers = findViewById(R.id.tv_profile_followers);
        tv_profile_following = findViewById(R.id.tv_profile_following);
        fb_send_new_tweet = findViewById(R.id.fb_send_new_tweet);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        if (requestCode == REQUEST_CODE_EDIT & resultCode == RESULT_OK) {
            new get_user_info().execute();
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        BottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);

    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//
//        getMenuInflater().inflate(R.menu.help_menu,menu);
//
//        return true;
//
//    }
//    //
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.help) {
//
//            //show_help_dialog();
//
//
//        }
//
//
//        return super.onOptionsItemSelected(item);
//


    public class get_user_info extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(ProfileAndShowToitActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("get_user_info")));
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));

                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));


            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/ecxa.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    response = new String(mcrypt.decrypt(response)).trim();
                    Log.e("data user info", response);


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

//                                Toast.makeText(getApplicationContext(), String.valueOf(settings.getInt("user_id", 0)),Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(),ad_list.toString(),Toast.LENGTH_SHORT).show();
                                ad_list = new JSONObject(finalResponse);
                                tv_profile_name.setText(ad_list.getString("name"));
                                //profile_number_davatSode.setText(ad_list.getString("moferi"));
                                //profile_number_taiidshode.setText(ad_list.getString("moferi_taeed"));
                                //profile_number_emtiaz.setText(ad_list.getString("points"));
                                //code_davat = ad_list.getString("moaref_code");
                                //profile_code_davat.setText(code_davat);

                                string_id = ad_list.getString("username");
                                if (!string_id.isEmpty()) {
                                    tv_profile_user_name.setText(string_id);
                                }
                                tv_profile_bio.setText(ad_list.getString("bio"));
                                tv_profile_followers.setText(ad_list.getString("followers") + "\n دنبال کننده ها");
                                tv_profile_following.setText(ad_list.getString("following") + "\n دنبال شونده ها");
                                //Toast.makeText(getApplicationContext(),ad_list.getString("background"),Toast.LENGTH_SHORT).show();
                                //  new ProfileAndShowToitActivity().LoadBackground(ad_list.getString("background"),
                                //         "background").execute();


                                // Toast.makeText(getBaseContext(), String.valueOf(ad_list.length()), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                if (!ad_list.getString("image").isEmpty()) {
                                    current_image = 0;
                                    Glide.with(getApplicationContext()).load("https://meydane-azadi.ir/photo/photo.php?image_name=" + ad_list.getString("image")).into(profile_image);
                                } else {
                                    profile_image.setImageResource(R.drawable.ic_user);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            skeletonScreen.hide();

                        }
                    });


                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
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
        }
    }

    public class ProfileAndShowToitAdapter2 extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

        public ProfileAndShowToitAdapter2(FragmentManager fragmentManager) {

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
                    return FragmentTweet.newInstance("token", settings.getString("token", ""));
                case 1:
                    return FragmentActivite.newInstance("activites", ad_list.toString());
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
                return "نویسه ها";
            else
                return "";
        }
    }


}

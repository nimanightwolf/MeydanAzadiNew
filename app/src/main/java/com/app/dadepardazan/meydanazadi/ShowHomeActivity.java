package com.app.dadepardazan.meydanazadi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.swipe.SwipeLayout;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.AnimateGifMode;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Bamdad on 12/6/2017.
 */

public class ShowHomeActivity extends MainActivity {

    JSONObject ad;
    JSONObject ad_advertising;
    TextView ad_title_show;
    //TextView ad_description_show;
    TextView ad_category_show;
    TextView ad_manba_show;
    TextView ad_manba_link_show;
    TextView ad_like_show;
    TextView ad_coment_show;
    TextView number_like_home_show;
    TextView number_coment_home_show;
    TextView ad_time_study_show;
    Button send_coment;
    Button btn_link;
    RatingBar ratingBar;
    LinearLayout layout_advertising;
    TextView texview_advertising;
    ReadMoreTextView ad_description_show_more;


    EditText edittext_coment_type;
    Menu menu;
    JSONObject user_contact_details;
    String slug;


    RecyclerView recyclerView;
    ComentShowAdapter2 adapter;
    int last_ad_id = 0;

    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;
    NestedScrollView nesscroll;
    MediaController mediaController;
    VideoView videoView;
    ImageView imageview_advertising;

    private MCrypt mcrypt;
    FloatingActionButton call_bt;
    NestedScrollView nsv;

    int int_stattus_radio_report=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame;

//        content_frame = (FrameLayout) findViewById(R.id.content_frame);
//        getLayoutInflater().inflate(R.layout.show_home, content_frame);
        setContentView(R.layout.show_home2);
        toolbar = (Toolbar) findViewById(R.id.toolbarshow);
        setSupportActionBar(toolbar);


        try {
            ad = new JSONObject(getIntent().getStringExtra("ad"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        IMEI_Number_Holder = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        ad_title_show = (TextView) findViewById(R.id.ad_title_show);
        // ad_description_show = (TextView) findViewById(R.id.ad_description_show);

        ad_category_show = (TextView) findViewById(R.id.ad_category_show);
        ad_manba_show = (TextView) findViewById(R.id.ad_manba_show);
        ad_manba_link_show = (TextView) findViewById(R.id.ad_manba_link_show);
        edittext_coment_type = (EditText) findViewById(R.id.edittext_coment_type);
        ad_like_show = (TextView) findViewById(R.id.ad_like_show);
        ad_coment_show = (TextView) findViewById(R.id.ad_coment_show);
        number_coment_home_show = (TextView) findViewById(R.id.number_coment_home_show);
        number_like_home_show = (TextView) findViewById(R.id.number_like_home_show);
        send_coment = (Button) findViewById(R.id.send_coment);
        layout_advertising = (LinearLayout) findViewById(R.id.layout_advertising);
        imageview_advertising = (ImageView) findViewById(R.id.imageview_advertising);
        texview_advertising = (TextView) findViewById(R.id.texview_advertising);
        nesscroll = (NestedScrollView) findViewById(R.id.nesscroll);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btn_link = (Button) findViewById(R.id.btn_link);
        ad_description_show_more = findViewById(R.id.ad_description_show_more);
        ad_time_study_show = findViewById(R.id.ad_time_study_show);




        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (settings.getInt("user_id", 0) == 0) {
                    Toast.makeText(getApplicationContext(), "برای وارد کردن مقدار علاقه مندی باید به میدان ازادی وارد شوید", Toast.LENGTH_SHORT).show();
                    ratingBar.setRating(Float.valueOf("0"));

                } else {
                    new insert_like().execute();
                }

            }
        });


        nesscroll.setOnTouchListener(new OnSwipeTouchListener(ShowHomeActivity.this) {

            public void onSwipeRight() {

                // Toast.makeText(getApplicationContext(),"left to right  gesture",Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }

        });

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("https://www.aparat.com/v/CUV6A");


        final String VIDEO_PATH = "https://s-v1.tamasha.com/statics/videos_download/57/e1/l23jk_57e106e58ef61897e62ce69ebc9322330a46e96f_n_240.mp4?name=%D9%88%D9%82%D8%AA%DB%8C_%D8%A8%D8%A7%D8%A8%D8%A7_%D9%87%D8%A7_%D8%AE%D9%88%D8%A7%D8%A8%D9%86_auto.mp4";
        videoView = (VideoView) findViewById(R.id.video_view);
        mediaController = new MediaController(ShowHomeActivity.this);
        videoView.setVideoPath(VIDEO_PATH);

        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {


            }
        });


        nesscroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                mediaController.hide();
            }
        });


        ad_coment_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittext_coment_type.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edittext_coment_type, InputMethodManager.SHOW_IMPLICIT);
            }
        });


//        ad_like_show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (settings.getInt("user_id", 0) == 0) {
//
//                    Toast.makeText(getApplicationContext(), "شما به میدان ازادی وارد نشده اید", Toast.LENGTH_SHORT).show();
//
//                } else {
//
//                    new insert_like().execute();
//
//
//                }
//
//
//            }
//        });

        // for hiden navigation bottom
//        BottomNavigationView= (android.support.design.widget.BottomNavigationView) findViewById(R.id.navigationView);
//        BottomNavigationView.setVisibility(View.GONE);


        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view_show);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);

        final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();

        adapter = new ComentShowAdapter2(getApplicationContext(), data_list) {
            @Override
            public void load_more() {
                if (last_ad_id != -1) {
                    new get_coment_list().execute();
                }

            }
        };
        recyclerView.setAdapter(adapter);

        new get_coment_list().execute();


        try {

            ad_title_show.setText(ad.getString("title"));
            //ad_description_show.setText(ad.getString("description").trim());
            ad_description_show_more.setText(ad.getString("description").trim());
            ad_description_show_more.setTrimCollapsedText("توضیحات بیشتر");
            ad_description_show_more.setTrimExpandedText("توضیحات کمتر");

            ad_time_study_show.setText(time_study(ad.getString("description").trim()));


            if (!ad.getString("source").trim().isEmpty()) {
                ad_manba_show.setText("منبع: " + ad.getString("source"));
            }
            if (ad.has("link_source")) {
                if (!ad.getString("link_source").trim().isEmpty()) {
                    ad_manba_link_show.setVisibility(View.VISIBLE);
                    ad_manba_link_show.setText("ادرس منبع: " + ad.getString("link_source"));
                }
            }
            if (!ad.getString("category").trim().isEmpty()) {
                ad_category_show.setText("دسته بندی: " + ad.getString("category"));
            }
            number_coment_home_show.setText(ad.getString("coment"));
            number_like_home_show.setText(ad.getString("avg_rate"));

            ratingBar.setRating((float) ad.getDouble("rat_adv"));
            // Toast.makeText(getApplicationContext(),(ad.getString("rat_adv")),Toast.LENGTH_SHORT).show();
            if (!ad.getString("link").isEmpty()) {
                btn_link.setVisibility(View.VISIBLE);
                btn_link.setText(ad.getString("text_button"));
                btn_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = null;
                        try {
                            url = ad.getString("link");
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Drawable arrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        // getSupportActionBar().setHomeAsUpIndicator(arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowHomeActivity.this.finish();
            }
        });
        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listName = new ArrayList<>();
        com.glide.slider.library.SliderLayout mDemoSlider;
        mDemoSlider = findViewById(R.id.slider);

        listUrl.add("https://www.revive-adserver.com/media/GitHub.jpg");
        listName.add("JPG - Github");

        listUrl.add("https://tctechcrunch2011.files.wordpress.com/2017/02/android-studio-logo.png");
        listName.add("PNG - Android Studio");
        try {
            if (ad.getString("image1").trim().equals("") && ad.getString("image2").trim().equals("") && ad.getString("image3").trim().equals("")) {

                DefaultSliderView sliderView = new DefaultSliderView(this);

                sliderView.image(R.drawable.no_photo);
                mDemoSlider.addSlider(sliderView);

            } else {

                if (!ad.getString("image1").trim().equals("")) {
                    DefaultSliderView sliderView = new DefaultSliderView(this);

                    sliderView.image("https://meydane-azadi.ir/photo/photo_news.php?image_name=" + ad.getString("image1").trim());
                    mDemoSlider.addSlider(sliderView);
                }

                if (!ad.getString("image2").trim().equals("")) {
                    DefaultSliderView sliderView = new DefaultSliderView(this);

                    sliderView.image("https://meydane-azadi.ir/photo/photo_news.php?image_name=" + ad.getString("image2").trim());
                    mDemoSlider.addSlider(sliderView);
                }

                if (!ad.getString("image3").trim().equals("")) {
                    DefaultSliderView sliderView = new DefaultSliderView(this);

                    sliderView.image("https://meydane-azadi.ir/photo/photo_news.php?image_name=" + ad.getString("image3").trim());
                    mDemoSlider.addSlider(sliderView);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        listUrl.add("http://static.tumblr.com/7650edd3fb8f7f2287d79a67b5fec211/3mg2skq/3bdn278j2/tumblr_static_idk_what.gif");
//        listName.add("GIF - Disney");
//
//        listUrl.add("http://www.gstatic.com/webp/gallery/1.webp");
//        listName.add("WEBP - Mountain");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        //.diskCacheStrategy(DiskCacheStrategy.NONE)
        //.placeholder(R.drawable.placeholder)
        //.error(R.drawable.placeholder);
        for (int i = 0; i < listUrl.size(); i++) {
            TextSliderView sliderView = new TextSliderView(ShowHomeActivity.this);
            // if you want show image only / without description text use DefaultSliderView instead
            // initialize SliderLayout
            sliderView
                    .image(listUrl.get(i))
                    .description(listName.get(i))
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {

                        }
                    });

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", listName.get(i));
            // mDemoSlider.addSlider(sliderView);
        }
        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(com.glide.slider.library.SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        //mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopCyclingWhenTouch(false);

//        SliderLayout slider = (SliderLayout) findViewById(R.id.slider);
//        slider.requestFocus();
//        slider.isFocused();
//        slider.clearChildFocus(videoView);
//
//        try {
//            if (ad.getString("image1").trim().equals("") && ad.getString("image2").trim().equals("") && ad.getString("image3").trim().equals("")) {
//
//                DefaultSliderView sliderView = new DefaultSliderView(this);
//
//                sliderView.image(R.drawable.no_photo);
//                slider.addSlider(sliderView);
//
//            } else {
//
//                if (!ad.getString("image1").trim().equals("")) {
//                    DefaultSliderView sliderView = new DefaultSliderView(this);
//
//                    sliderView.image("http://meydane-azadi.ir/photo/photo_news.php?image_name=" + ad.getString("image1").trim());
//                    slider.addSlider(sliderView);
//                }
//
//                if (!ad.getString("image2").trim().equals("")) {
//                    DefaultSliderView sliderView = new DefaultSliderView(this);
//
//                    sliderView.image("http://meydane-azadi.ir/photo/photo_news.php?image_name=" + ad.getString("image2").trim());
//                    slider.addSlider(sliderView);
//                }
//
//                if (!ad.getString("image3").trim().equals("")) {
//                    DefaultSliderView sliderView = new DefaultSliderView(this);
//
//                    sliderView.image("http://meydane-azadi.ir/photo/photo_news.php?image_name=" + ad.getString("image3").trim());
//                    slider.addSlider(sliderView);
//                }
//
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        slider.stopAutoCycle();

        call_bt = (FloatingActionButton) findViewById(R.id.call_bt);
        call_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // new get_contact_details().execute();
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("profile_user2/plain");
//                i.putExtra(Intent.EXTRA_SUBJECT, "میدان ازادی");
//                try {
//                    i.putExtra(Intent.EXTRA_TEXT, ad.getString("title")+"\nبرای مشاهده خبر برنامه میدان ازادی را نصب کنید\n" +
//                            "https://cafebazaar.ir/app/com.app.dadepardazan.meydanazadi");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                startActivity(Intent.createChooser(i, "یک برنامه را انتخاب کنید"));

                String shareBody = null;
                try {
                    shareBody = ad.getString("title") + "\nبرای مشاهده خبر برنامه میدان ازادی را نصب کنید\n" +
                            "https://cafebazaar.ir/app/com.app.dadepardazan.meydanazadi";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "میدان ازادی");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "یک برنامه را انتخاب کنید"));
                /*
                ArrayList<String> list = new ArrayList<String>();
                list.add("ارسال ایمیل");
                list.add("تماس تلفنی");
                list.add("ارسال پیامک");

                AlertDialog.Builder builder = new AlertDialog.Builder(ShowHomeActivity.this);
                builder.setAdapter(new ArrayAdapter<String>(ShowHomeActivity.this, R.layout.row, R.id.mytext, list), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });


                builder.show();
                */
            }
        });
        send_coment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settings.getInt("user_id", 0) == 0) {

                    Toast.makeText(getApplicationContext(), "شما به میدان ازادی وارد نشده اید", Toast.LENGTH_SHORT).show();

                } else {
                    if (!edittext_coment_type.getText().toString().trim().isEmpty()) {
//                                permissionStatus = ShowHomeActivity.this.getSharedPreferences("permissionStatus",ShowHomeActivity.this.MODE_PRIVATE);

//                                if(ActivityCompat.checkSelfPermission(ShowHomeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                                    if (ActivityCompat.shouldShowRequestPermissionRationale(ShowHomeActivity.this, Manifest.permission.READ_PHONE_STATE)) {

                        new send_coment_list().execute();
//                                    }
//                                }else {
//                                    get_permistion();
//
//                                }

                    }


                }


            }


        });
        ad_title_show.requestFocus();
//        AudioManager am = (AudioManager) super.getSystemService(Context.AUDIO_SERVICE);
//        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);


        nsv = findViewById(R.id.nesscroll);
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    call_bt.hide();

//                    if(temp==1){
//                        temp=0;
//                        TutoShowcase.from(ShowHomeActivity.this)
//                                .setContentView(R.layout.tuto_sample)
//
//                                .on(toolbar) //a view in actionbar
//                                .addCircle()
//
//
//                                .on(nesscroll)
//                                .displaySwipableRight()
//
//                                .show();
//                    }


                } else {
                    call_bt.show();
                }
            }
        });
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(edittext_coment_type.getWindowToken(), 0);
//        edittext_coment_type.clearFocus();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


    }

    private String time_study(String str_description) {
        try {
            int dec_length=str_description.length();
            float time_need_to_study_for_each_char=0.06f;
            float time_study=(int)dec_length*time_need_to_study_for_each_char;
            return calculateTime((long) time_study);


        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    public static String calculateTime(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

        System.out.println("Day " + day + " Hour " + hours + " Minute " + minute + " Seconds " + second);
        return "زمان مطالعه "+minute+" دقیقه و"+second+" ثانیه";

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        try {
            if (settings.getInt("user_id", 0) == ad.getInt("user_id")) {
                getMenuInflater().inflate(R.menu.edit_like_share_menu, menu);


            } else {

                getMenuInflater().inflate(R.menu.like_share_menu, menu);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.menu = menu;

        //            if (ad.getBoolean("bookmark")) {
//
//                menu.findItem(R.id.bookmark).setIcon(R.drawable.bookmark1);
//
//            }

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.editic) {
            Intent i = new Intent(getApplicationContext(), EditActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            i.putExtra("ad", ad.toString());

            getApplicationContext().startActivity(i);
            ShowHomeActivity.this.finish();

        }


        if (item.getItemId() == R.id.share) {
//            try {
//                Toast.makeText(getApplicationContext(), ad.getString("url_id").toString(), Toast.LENGTH_SHORT).show();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("profile_user2/plain");
            try {

                i.putExtra(Intent.EXTRA_SUBJECT, ad.getString("title"));
                slug = ad.getString("title");
                slug = slug.replace(' ', '-');
                // Toast.makeText(getApplicationContext(), ad.getString("url_id").toString(), Toast.LENGTH_SHORT).show();
                i.putExtra(Intent.EXTRA_TEXT, "\n http://ghafas.net/ads/" + slug + "/" + ad.getString("url_id") + "\n  این اگهی رو توی قفس ببین " + ad.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            startActivity(Intent.createChooser(i, "یک برنامه را انتخاب کنید"));


        }
        if (item.getItemId() == R.id.bookmark) {

            if (settings.getInt("user_id", 0) == 0) {

                Toast.makeText(getApplicationContext(), "شما به قفس وارد نشده اید", Toast.LENGTH_SHORT).show();

            } else {

                //  new insert_like().execute();


            }
        }

        return super.onOptionsItemSelected(item);

    }


    public class insert_like extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(ShowHomeActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {


                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("insert_like")));

                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));

                get_ad_list.put("news_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(ad.getInt("id")))));
                get_ad_list.put("like", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(ratingBar.getRating()))));
                get_ad_list.put("imei", MCrypt.bytesToHex(mcrypt.encrypt(IMEI_Number_Holder)));

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

                if (response.trim().startsWith("<azadi>") && response.trim().endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    //       Toast.makeText(getBaseContext(),response, Toast.LENGTH_SHORT).show();

//                    if (response.equals("ok")) {

//                        if (ad.getBoolean("is_like")) {
//                            ad.put("is_like", false);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//
//
//                                    Toast.makeText(getBaseContext(), "خبر ان لایک شد", Toast.LENGTH_SHORT).show();
//
//                                    //menu.findItem(R.id.bookmark).setIcon(R.drawable.bookmark0);
//                                    ad_like_show.setBackgroundResource(R.drawable.ic_action_unlike);
//                                }
//                            });
//
//
//                        } else {
//                            ad.put("bookmark", true);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//
//
//                                    Toast.makeText(getBaseContext(), "خبر لایک شد", Toast.LENGTH_SHORT).show();
//
//
//                                  //  menu.findItem(R.id.bookmark).setIcon(R.drawable.bookmark1);
//                                    ad_like_show.setBackgroundResource(R.drawable.ic_action_like);
//
//                                }
//                            });
//
//
//                        }


//                    } else {
//
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//
//                                Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//
//
//                    }


                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                    }
                });


            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }


    public class get_contact_details extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(ShowHomeActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("get_contact_details")));

                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(ad.getInt("user_id")))));

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));


            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ghafas.net/app/command.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<ghafas>") && response.endsWith("</ghafas>")) {//response is valid

                    response = response.replace("<ghafas>", "").replace("</ghafas>", "");


                    if (!response.equals("error")) {

                        final String finalResponse = response;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String email = "";
                                String mobile = "";

                                try {
                                    user_contact_details = new JSONObject(finalResponse);

                                    if (!user_contact_details.getString("email").equals("")) {

                                        email = " ( " + user_contact_details.getString("email") + " ) ";

                                    }
                                    mobile = " ( " + user_contact_details.getString("mobile") + " ) ";


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                final ArrayList<String> list = new ArrayList<String>();

                                list.add("تماس تلفنی" + mobile);
                                list.add("ارسال پیامک" + mobile);
                                if (!email.equals(""))
                                    list.add("ارسال ایمیل" + email);

                                AlertDialog.Builder builder = new AlertDialog.Builder(ShowHomeActivity.this);
                                builder.setAdapter(new ArrayAdapter<String>(ShowHomeActivity.this, R.layout.row, R.id.mytext, list), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if (i == 0) {//call

                                            Intent calldail = null;
                                            try {
                                                calldail = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + user_contact_details.getString("mobile")));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            startActivity(calldail);


//
//                                            if (ActivityCompat.checkSelfPermission(ShowHomeActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
//                                                if (ActivityCompat.shouldShowRequestPermissionRationale(ShowHomeActivity.this, permissionsRequired[0])) {
//                                                    //Show Information about why you need the permission
//                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowHomeActivity.this);
//                                                    builder.setTitle("درخواست مجوز ");
//                                                    builder.setMessage("این نرم افزار نیاز به مجوز تماس دارد تایید را بزنید.");
//                                                    builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//                                                            dialog.cancel();
//                                                            ActivityCompat.requestPermissions(ShowHomeActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
//                                                        }
//                                                    });
//                                                    builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//                                                            dialog.cancel();
//                                                        }
//                                                    });
//                                                    builder.show();
//                                                } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
//                                                    //Previously Permission Request was cancelled with 'Dont Ask Again',
//                                                    // Redirect to Settings after showing Information about why you need the permission
//                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowHomeActivity.this);
//                                                    builder.setTitle("درخواست مجوز ");
//                                                    builder.setMessage("این نرم افزار نیاز به مجوز تماس دارد تایید را بزنید.");
//                                                    builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//                                                            dialog.cancel();
//                                                            sentToSettings = true;
//                                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
//                                                            intent.setData(uri);
//                                                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
//                                                            Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera ", Toast.LENGTH_LONG).show();
//                                                        }
//                                                    });
//                                                    builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//                                                            dialog.cancel();
//                                                        }
//                                                    });
//                                                    builder.show();
//                                                } else {
//                                                    //just request the permission
//                                                    ActivityCompat.requestPermissions(ShowHomeActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
//                                                }
//
//
//                                                SharedPreferences.Editor editor = permissionStatus.edit();
//                                                editor.putBoolean(permissionsRequired[0], true);
//                                                editor.commit();
//                                            } else {
//                                                //You already have the permission, just go ahead.
//                                                proceedAfterPermission();
//                                            }
//
//


                                            /*
                                            try {
                                                Intent callintent=new Intent(Intent.ACTION_CALL);
                                                callintent.setData(Uri.parse("tel:"+user_contact_details.getString("mobile")));
                                                startActivity(callintent);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }*/
                                        }


                                        if (i == 1) {//sms


                                            try {
                                                Intent smsintent = new Intent(Intent.ACTION_SENDTO);
                                                smsintent.setData(Uri.parse("sms:" + user_contact_details.getString("mobile")));
                                                startActivity(smsintent);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }

                                        if (i == 2) {//email


                                            try {
                                                Intent emailintent = new Intent(Intent.ACTION_SENDTO);
                                                emailintent.setData(Uri.parse("mailto:" + user_contact_details.getString("email")));
                                                startActivity(emailintent);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }


                                    }
                                });


                                builder.show();


                            }
                        });


                    } else {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }


                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                    }
                });


            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
//            //check if all permissions are granted
//            boolean allgranted = false;
//            for (int i = 0; i < grantResults.length; i++) {
//                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                    allgranted = true;
//                } else {
//                    allgranted = false;
//                    break;
//                }
//            }
//
//            if (allgranted) {
//                proceedAfterPermission();
//            } else if (ActivityCompat.shouldShowRequestPermissionRationale(ShowHomeActivity.this, permissionsRequired[0])) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(ShowHomeActivity.this);
//                builder.setTitle("درخواست مجوز ");
//                builder.setMessage("این نرم افزار نیاز به مجوز تماس دارد تایید را بزنید.");
//                builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        ActivityCompat.requestPermissions(ShowHomeActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
//                    }
//                });
//                builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                builder.show();
//            } else {
//                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_PERMISSION_SETTING) {
//            if (ActivityCompat.checkSelfPermission(ShowHomeActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
//                //Got Permission
//                proceedAfterPermission();
//            }
//        }
//    }

    //    private void proceedAfterPermission() {
//
//        // Toast.makeText(getBaseContext(), "همه ی مجوز ها گرفته شد", Toast.LENGTH_LONG).show();
//        try {
//            Intent callintent = new Intent(Intent.ACTION_CALL);
//            callintent.setData(Uri.parse("tel:" + user_contact_details.getString("mobile")));
//            startActivity(callintent);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        if (sentToSettings) {
//            if (ActivityCompat.checkSelfPermission(ShowHomeActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
//                //Got Permission
//                proceedAfterPermission();
//            }
//        }
//    }
    public class get_coment_list extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(ShowHomeActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//        pd.setMessage("در حال دریافت اطلاعات");
//        pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("read_coment_advertising")));


                //get_ad_list.put("location","");
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));

                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("news_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(ad.getString("newsid")))));

                get_ad_list.put("last_ad_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(last_ad_id))));
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


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {


                                JSONObject ad_all_date = new JSONObject(finalResponse);
                                JSONArray ad_list = new JSONArray();
                                ad_list = ad_all_date.getJSONArray("one");
                                ad_advertising = new JSONObject();

                                // Toast.makeText(getApplicationContext(),ad_advertising.getString("link").toString(),Toast.LENGTH_LONG).show();


                                if (ad_list.length() == 0 && last_ad_id == 0) {


                                }

                                if (ad_list.length() != 0) {

                                    last_ad_id = ad_list.getJSONObject(ad_list.length() - 1).getInt("id");


                                    if (ad_list.length() != 10) {
                                        last_ad_id = -1;
                                    }


                                } else {

                                    last_ad_id = -1;
                                }


                                adapter.insert(adapter.getItemCount(), ad_list);
                                recyclerView.setAdapter(adapter);

                                // Toast.makeText(getBaseContext(), String.valueOf(ad_list.length()), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {


                                JSONObject ad_all_date = new JSONObject(finalResponse);
                                ad_advertising = ad_all_date.getJSONObject("two");
                                if (!ad_advertising.getString("baner").isEmpty()) {
                                    layout_advertising.setVisibility(View.VISIBLE);
                                    layout_advertising.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String url = null;
                                            try {
                                                url = ad_advertising.getString("link");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(url));
                                            startActivity(i);
                                        }
                                    });
                                    Ion.with(imageview_advertising)
                                            .error(R.drawable.no_photo)
                                            .animateGif(AnimateGifMode.ANIMATE)
                                            .load(ad_advertising.getString("baner"));
                                    if (!ad_advertising.getString("text").isEmpty()) {
                                        texview_advertising.setText(ad_advertising.getString("text"));
                                    }


                                }

                                // Toast.makeText(getBaseContext(), String.valueOf(ad_list.length()), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                    }
                });


            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//        pd.hide();
//        pd.dismiss();
        }
    }

    public class send_coment_list extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(ShowHomeActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();

            try {
                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("insert_coment")));
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));


                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("news_id", MCrypt.bytesToHex(mcrypt.encrypt(ad.getString("id"))));

                String text1 = Base64.encodeToString(edittext_coment_type.getText().toString().trim().getBytes("UTF-8"), Base64.DEFAULT);
                get_ad_list.put("text", MCrypt.bytesToHex(mcrypt.encrypt(text1)));
                get_ad_list.put("imei", MCrypt.bytesToHex(mcrypt.encrypt(IMEI_Number_Holder)));

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
                final String finalResponse1 = response;


                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    response = new String(mcrypt.decrypt(response)).trim();


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Toast.makeText(getBaseContext(), finalResponse, Toast.LENGTH_SHORT).show();
                            if (!finalResponse.trim().isEmpty()) {
                                edittext_coment_type.setText("");
                                try {
                                    // JSONArray ad_list_ob=new JSONArray("[{"+edittext_coment_type.getText().toString()+"}]");

                                    JSONArray ad_list = new JSONArray(finalResponse);

                                    hideKeyboard(ShowHomeActivity.this);
//
//
//                                if (ad_list.length() == 0 && last_ad_id == 0) {
//
//
//
//                                }
//
//                                if (ad_list.length() != 0) {
//
//                                    last_ad_id = ad_list.getJSONObject(ad_list.length() - 1).getInt("id");
//
//
//                                    if (ad_list.length() != 10) {
//                                        last_ad_id = -1;
//                                    }
//
//
//                                } else {
//
//                                    last_ad_id = -1;
//                                }
//
//
                                    adapter.insert(adapter.getItemCount(), ad_list);
                                    recyclerView.setAdapter(adapter);
//
//                                // Toast.makeText(getBaseContext(), String.valueOf(ad_list.length()), Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });


                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                    }
                });


            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
            nesscroll.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }


//    private void proceedAfterPermission() {
//        //We've got the permission, now we can proceed further
//        telephonyManager = (TelephonyManager) this.getSystemService(getApplicationContext().TELEPHONY_SERVICE);
//        IMEI_Number_Holder = telephonyManager.getDeviceId();
//        IMEI_Number_Holder = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

//    }

    public abstract class ComentShowAdapter2 extends RecyclerView.Adapter<ShowHomeActivity.ComentShowAdapter2.ViewHolder> {
        ArrayList<JSONObject> data_list;
        Context context;
        CardView cardv;

        public ComentShowAdapter2(Context context, ArrayList<JSONObject> data_list) {
            this.context = context;
            this.data_list = data_list;
        }

        @Override
        public ShowHomeActivity.ComentShowAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coment_show, parent, false);
            return new ComentShowAdapter2.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ShowHomeActivity.ComentShowAdapter2.ViewHolder holder, final int position) {
            try {
                holder.coment_user_title.setText(data_list.get(position).getString("name"));
                holder.coment_description.setText(data_list.get(position).getString("text"));

                int seconds = (int) ((System.currentTimeMillis() / 1000) - data_list.get(position).getInt("date"));
                String temp = "";
                if (seconds < 60) {
                    temp = "چند ثانیه پیش";


                } else if (seconds >= 60 && seconds < 3600) {

                    temp = (seconds / 60) + " دقیقه پیش ";

                } else if (seconds >= 3600 && seconds < 86400) {

                    temp = (seconds / 3600) + " ساعت پیش ";


                } else if (seconds >= 86400 && seconds < 2629743) {

                    temp = (seconds / 86400) + " روز پیش ";


                } else if (seconds >= 2629743 && seconds < 31556926) {

                    temp = (seconds / 2629743) + " ماه پیش ";


                } else {
                    temp = (seconds / 31556926) + " سال پیش ";
                }
                holder.coment_date.setText(temp);

                holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)


                holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                    @Override
                    public void onClose(SwipeLayout layout) {
                        //when the SurfaceView totally cover the BottomView.
                    }

                    @Override
                    public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                        //you are swiping.
                    }

                    @Override
                    public void onStartOpen(SwipeLayout layout) {

                    }

                    @Override
                    public void onOpen(SwipeLayout layout) {
                        //when the BottomView totally show.
                        Toast.makeText(context, "وارد کردن پیام", Toast.LENGTH_SHORT).show();
                        holder.swipeLayout.close();
                        edittext_coment_type.setText("@" + holder.coment_user_title.getText() + "\n");
                        edittext_coment_type.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edittext_coment_type, InputMethodManager.SHOW_IMPLICIT);
                        edittext_coment_type.setSelection(edittext_coment_type.getText().length());


                    }

                    @Override
                    public void onStartClose(SwipeLayout layout) {

                    }

                    @Override
                    public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                        //when user's hand released.
                        // Toast.makeText(context, "وارد کردن پیام2", Toast.LENGTH_SHORT).show();
                        holder.swipeLayout.close();
//                    edittext_coment_type.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(edittext_coment_type, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                holder.swipeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.swipeLayout.close();
                    }
                });


                holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        //will show popup menu here
                        PopupMenu popup = new PopupMenu(ShowHomeActivity.this, holder.buttonViewOption);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.comment_menu);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.mnu_answer:

                                        edittext_coment_type.setText("@" + holder.coment_user_title.getText() + "\n");
                                        edittext_coment_type.requestFocus();
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.showSoftInput(edittext_coment_type, InputMethodManager.SHOW_IMPLICIT);
                                        edittext_coment_type.setSelection(edittext_coment_type.getText().length());
                                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                                        break;
                                    case R.id.mnu_report:
//                                        new LovelyStandardDialog(ShowHomeActivity.this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
//                                                .setTopColorRes(R.color.colorAccent)
//                                                .setButtonsColor(Color.RED)
//                                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                                .setTitle("ریپورت")
//                                                .setMessage("ایا می خواهید این کامنت را ریپورت کنید")
//                                                .setPositiveButton("بله", new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        //Toast.makeText(context, "positive clicked", Toast.LENGTH_SHORT).show();
//                                                        new send_report_coment().execute();
//                                                    }
//                                                })
//                                                .setNegativeButton("لغو", null)
//                                                .show();
                                        dialog_report(data_list.get(position),new Button_dialog() {
                                            @Override
                                            public void onasync(String asyncc_call_back) {

                                            }
                                        });
                                        //handle menu2 click
                                        break;

                                }
                                return false;
                            }
                        });
                        //displaying the popup
                        popup.show();

                    }
                });
//
//
//            holder.ad_date.setText( data_list.get(position).getString("date"));


                //  holder.ad_price.setText(data_list.get(position).ad_price);
                //  holder.ad_location.setText(data_list.get(position).ad_location);
                //  Picasso.with(context).load(data_list.get(position).ad_image).resize(128,128).into(holder.img);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.tv_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new send_report_coment().execute();
                }
            });


            if (position >= getItemCount() - 1) {

                load_more();
            }


        }

        public abstract void load_more();


        @Override
        public int getItemCount() {
            return data_list.size();
        }

        public void insert(int position, JSONArray ad_list) {


            try {


                for (int i = 0; i < ad_list.length(); i++) {


                    data_list.add(ad_list.getJSONObject(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            notifyItemInserted(position);


        }


        public void clear_list() {

            int size = data_list.size();
            data_list.clear();
            notifyItemRangeRemoved(0, size);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView coment_user_title;
            TextView coment_description;
            TextView coment_date;
            TextView tv_report;
            SwipeLayout swipeLayout;
            TextView buttonViewOption;


            ImageView img_home;

            // TextView ad_location;
            CardView cardv;
            TextView ad_poblisher;


            public ViewHolder(View item) {
                super(item);
                coment_user_title = (TextView) item.findViewById(R.id.coment_user_title);
                coment_description = (TextView) item.findViewById(R.id.coment_description);
                coment_date = (TextView) item.findViewById(R.id.coment_date);
                tv_report = (TextView) item.findViewById(R.id.tv_report);
                swipeLayout = (SwipeLayout) item.findViewById(R.id.sample1);
                buttonViewOption = item.findViewById(R.id.textViewOptions);


                //status.setBackgroundColor(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                //status.setVisibility(View.VISIBLE);
            }
        }
    }

    ////////////////////////////////
//    public abstract class ComentReplayAdapter extends RecyclerView.Adapter<ShowHomeActivity.ComentReplayAdapter.ViewHolder> {
//        ArrayList<JSONObject> data_list_item;
//        Context context;
//
//        public ComentReplayAdapter(Context context, ArrayList<JSONObject> data_list_item) {
//            this.context = context;
//            this.data_list_item = data_list_item;
//        }
//
//
//        @Override
//        public ShowHomeActivity.ComentReplayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_formula_show, parent, false);
//            return new ComentReplayAdapter.ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(ShowHomeActivity.ComentReplayAdapter.ViewHolder holder, final int position) {
//            try {
//                holder.show_item_item_name_item.setText(data_list_item.get(position).getString("name"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            if (position >= getItemCount() - 1) {
//
//                load_more();
//            }
//
//
//        }
//
//        public abstract void load_more();
//
//
//        @Override
//        public int getItemCount() {
//            return data_list_item.size();
//        }
//
//        public void insert(int position, JSONArray ad_list) {
//
//
//            try {
//
//
//                for (int i = 0; i < ad_list.length(); i++) {
//
//
//                    data_list_item.add(ad_list.getJSONObject(i));
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            notifyItemInserted(position);
//
//
//        }
//
//
//        public void clear_list() {
//
//            int size = data_list_item.size();
//            data_list_item.clear();
//            notifyItemRangeRemoved(0, size);
//        }
//
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            TextView show_item_item_name_item;
//            RecyclerView recycler_view_item_item_formula;
//            Button btn_more_description_formula_show;
//
//
//            public ViewHolder(View item) {
//                super(item);
//                show_item_item_name_item = (TextView) item.findViewById(R.id.show_item_item_name_item);
////                recycler_view_item_item_formula=(RecyclerView)item.findViewById(R.id.recycler_view_item_item_formula) ;
////                btn_more_description_formula_show=(Button)item.findViewById(R.id.btn_more_description_formula_show);
//            }
//        }
//    }
    public void dialog_report(final JSONObject jsonObject, final Button_dialog bt_dialog) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view_dialog = inflater.inflate(R.layout.dialog_report_detaile, null);

        RadioButton radio_report_aklaghi = view_dialog.findViewById(R.id.radio_report_aklaghi);
        RadioButton radio_report_tohin = view_dialog.findViewById(R.id.radio_report_tohin);
        radio_report_aklaghi.setChecked(true);
        int_stattus_radio_report = 0;
        radio_report_aklaghi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int_stattus_radio_report = 0;
            }
        });
        radio_report_tohin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int_stattus_radio_report = 1;
            }
        });
        SweetAlertDialog alertDialog = new SweetAlertDialog(ShowHomeActivity.this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText("باز خورد");
        //                                                    .setContentText("Won't be able to recover this file!")
        alertDialog.setConfirmText("ارسال");
        alertDialog.setCustomView(view_dialog);
        alertDialog.setCustomImage(R.drawable.icon_retweet_dialog);
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                try {
                    Log.e("data",jsonObject.toString());
                    if (!new send_report_coment().execute(jsonObject).get().equals("error")) {
                        sDialog.cancel();
                        SweetAlertDialog sucess_dialog = new SweetAlertDialog(ShowHomeActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        sucess_dialog.setTitleText("گزارش شما با موفقیت ثبت شد");
                        sucess_dialog.setConfirmText("متوجه شدم");
                        sucess_dialog.setConfirmClickListener(null);
                        sucess_dialog.show();
                        Button btn = (Button) sucess_dialog.findViewById(R.id.confirm_button);
                        btn.setBackground(getResources().getDrawable(R.drawable.background_button_sweetalert));
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        })
                .show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackground(getResources().getDrawable(R.drawable.background_button_sweetalert));
    }

    public class send_report_coment extends AsyncTask<JSONObject, Void, String> {
        ProgressDialog pd = new ProgressDialog(ShowHomeActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.show();
        }

        @Override
        protected String doInBackground(JSONObject... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();

            try {
                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("report_comment")));

                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("news_id", MCrypt.bytesToHex(mcrypt.encrypt(params[0].getString("id_coment"))));

                get_ad_list.put("imei", MCrypt.bytesToHex(mcrypt.encrypt(IMEI_Number_Holder)));

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


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Toast.makeText(getBaseContext(), finalResponse, Toast.LENGTH_SHORT).show();
                            if (!finalResponse.trim().equals("ok")) {
//
//                            final AlertDialog.Builder builder = new AlertDialog.Builder(ShowHomeActivity.this,R.style.DialogeTheme);
//                            builder.setTitle("گزارش شما با موفقیت ارسال شد");
//                            //   builder.setMessage("این نرم افزار نیاز به مجوز تماس دارد تایید را بزنید.");
//                            builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//
//                                }
//
//                            });
//                            builder.show();
                                Snackbar.make(ad_like_show, "گزارش شما با موفقیت ارسال شد", Snackbar.LENGTH_LONG).show();


                            } else {


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }


                        }
                    });
                    return  finalResponse;

                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                    }
                });


            }


            return "error";
        }

        //////////////////////////////////
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
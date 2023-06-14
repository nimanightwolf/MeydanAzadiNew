package com.app.dadepardazan.meydanazadi.show_tweet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aghajari.zoomhelper.ZoomHelper;
import com.app.dadepardazan.meydanazadi.MainActivity;
import com.app.dadepardazan.meydanazadi.R;
import com.app.dadepardazan.meydanazadi.all_tweet.AllTweetActivity;
import com.app.dadepardazan.meydanazadi.view_pager.post.ActiviteAndShowTweeetActivity;
import com.app.dadepardazan.meydanazadi.view_pager.profile.ProfileAndShowToitActivity;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.swipe.SwipeLayout;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.material.snackbar.Snackbar;
import com.hendraanggrian.socialview.SocialView;
import com.hendraanggrian.widget.SocialTextView;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/**
 * Created by Bamdad on 5/20/2020.
 */

public class ShowTweetActivity extends MainActivity implements ViewPagerEx.OnPageChangeListener {
    ComentShowAdapter2 adapter;
    RecyclerView recyclerview;
    JSONObject ad;
    String str_position = "0";
    String str_id_post_like;

    TextView tv_profile_name_fragment;
    TextView tv_profile_user_name_fragment;
    ImageView image_profile_image_fragment;
    TextView tv_time_and_date_fragment;
    SocialTextView tv_text_profile_fragment;
    TextView tv_number_like_profile;
    TextView tv_number_comment_profile;
    TextView tv_number_retweet_profile;
    ImageView img_like;
    ImageView img_comment;
    SliderLayout mDemoSlider;
    LinearLayout linear_profile;
    LinearLayout linear_retweet_profile;
    EditText edittext_message;
    int last_ad_id = 0;
    int int_id_replay = 0;
    ImageButton btn_send_message;
    TextView tv_reweeted;
    String str_post_id = "";

    String str_android_id = "";
    SkeletonScreen skeletonScreen;
    TextView textViewOptions_main_view;
    String Id_item_deleted = "";
    int int_stattus_radio_report = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout content_frame;

        str_android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        //AdListActivity.this.setTitle("میدان ازادی");
//        content_frame = (FrameLayout) findViewById(R.id.content_frame);
//        getLayoutInflater().inflate(R.layout.show_tweet, content_frame);
        setContentView(R.layout.show_tweet);
        findviewbyId();
        CoordinatorLayout coordinatorlayout = findViewById(R.id.coordinatorlayout);

        skeletonScreen = Skeleton.bind(coordinatorlayout)
                .load(R.layout.show_tweet)
                .show();
//        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
//        toolbar_title.setVisibility(View.VISIBLE);
//        toolbar_title.setText("میدان ازادی");
        BottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        try {
            if (getIntent().hasExtra("post_id")) {
                str_post_id = getIntent().getStringExtra("post_id");
                new get_post_by_post_id().execute();

            }

            if (getIntent().hasExtra("ad")) {
                ad = new JSONObject(getIntent().getStringExtra("ad"));
                str_position = getIntent().getStringExtra("position");
                setValue();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();

        adapter = new ComentShowAdapter2(getApplicationContext(), data_list) {


            @Override
            public void load_more() {

            }
        };
        recyclerview.setHasFixedSize(false);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(lm);
        recyclerview.setAdapter(adapter);
        try {
            if (ad.getJSONObject("user").getString("user_token").equals(settings.getString("token", ""))) {
                textViewOptions_main_view.setVisibility(View.VISIBLE);
                textViewOptions_main_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        PopupMenu popup = new PopupMenu(ShowTweetActivity.this, textViewOptions_main_view);
                        popup.inflate(R.menu.option_item_tweet);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.mnu_delete:

                                        dialog_delete_tweet(ad, new MainActivity.After_delete_listener() {
                                            @Override
                                            public void delete_completed(SweetAlertDialog alertDialog) {
                                                // delete_item(data_list.get(position), position);
                                                try {
                                                    Id_item_deleted = ad.getString("id_post");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Intent i = new Intent();
                                                i.putExtra("id_item_deleted", Id_item_deleted);
                                                i.putExtra("position", str_position);
                                                setResult(RESULT_OK, i);
                                                alertDialog.cancel();
                                                finish();
                                            }
                                        });

//                                        new LovelyStandardDialog(ShowTweetActivity.this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
//                                                .setTopColorRes(R.color.colorAccent)
//                                                .setButtonsColor(Color.RED)
//                                                .setIcon(android.R.drawable.ic_menu_delete)
//                                                .setTitle("حذف")
//                                                .setMessage("ایا می خواهید این نویسه را پاک کنید")
//                                                .setPositiveButton("بله", new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        //Toast.makeText(context, "positive clicked", Toast.LENGTH_SHORT).show();
//                                                        JSONObject get_ad_list = new JSONObject();
//                                                        try {
//                                                            get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("delete_post")));
//                                                            get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
//                                                            Id_item_deleted = ad.getString("id_post");
//                                                            get_ad_list.put("id_post",  mcrypt.bytesToHex(mcrypt.encrypt(Id_item_deleted)));
//
//
//                                                            if (new send_data().execute(get_ad_list).get().equals("\"ok\"")) {
//                                                                Toast.makeText(ShowTweetActivity.this, "نویسه شما با موفقیت حذف شد", Toast.LENGTH_SHORT).show();
//                                                                Snackbar.make(view, "نویسه شما با موفقیت حذف شد", Snackbar.LENGTH_SHORT).show();
//                                                                Intent i = new Intent();
//                                                                i.putExtra("id_item_deleted", Id_item_deleted);
//                                                                i.putExtra("position", str_position);
//                                                                setResult(RESULT_OK, i);
//                                                                finish();
//                                                                //   delete_item(data_list.get(position),position);
//                                                            }
//
//                                                        } catch (Exception e) {
//                                                            e.printStackTrace();
//                                                        }
//                                                    }
//                                                })
//                                                .setNegativeButton("لغو", null)
//                                                .show();
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new get_commend().execute();
        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edittext_message.getText().toString().isEmpty()) {
                    edittext_message.setError("لطفا متن کامنت را پر کنید");
                } else {
                    new send_commend().execute();
                    edittext_message.setText("");
                }
            }
        });
        ZoomHelper.Companion.addZoomableView(mDemoSlider);

//ZoomHelper.Companion.addZoomableView(view,tag);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return ZoomHelper.Companion.getInstance().dispatchTouchEvent(ev, this) || super.dispatchTouchEvent(ev);
    }

    private void setValue() {
        try {
            img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    try {

                        str_id_post_like = ad.getString("id_post");
                        if (ad.getString("is_like_post").equals("0")) {
                            // you going to like post
                            String a = new send_like().execute().get();
                            Log.e("post like", a);
                            img_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_heart));
                            tv_number_like_profile.setText(String.valueOf(ad.getInt("like_number") + 1));
                            ad.put("is_like_post", "1");
                            ad.put("like_number", tv_number_like_profile.getText().toString());
                        } else {
                            String a = new send_like().execute().get();
                            Log.e("post like", a);
                            img_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_empty_heart));
                            tv_number_like_profile.setText(String.valueOf(ad.getInt("like_number") - 1));
                            ad.put("is_like_post", "0");
                            ad.put("like_number", tv_number_like_profile.getText().toString());


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
            if (ad.getString("repost_id").equals("0")) {
                tv_reweeted.setVisibility(View.GONE);
            } else {
                tv_reweeted.setVisibility(View.VISIBLE);
                tv_reweeted.setText("ریتویت شده از " + ad.getString("user_name_repost"));
            }
            if (ad.getString("is_like_post").equals("1")) {
                img_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_heart));
            } else {
                img_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_empty_heart));
            }
            tv_time_and_date_fragment.setText(ad.getString("time"));
            tv_text_profile_fragment.setText(ad.getString("text"));
            tv_text_profile_fragment.setHashtagColor(Color.BLUE);
            tv_text_profile_fragment.setMentionColor(Color.BLUE);
            tv_text_profile_fragment.setOnHashtagClickListener(new Function2<SocialView, String, Unit>() {
                @Override
                public Unit invoke(SocialView socialView, String s) {
                    // do something
                    Intent i = new Intent(ShowTweetActivity.this, AllTweetActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("hashtag", s);
                    startActivity(i);
                    return null;
                }
            });
            tv_text_profile_fragment.setOnMentionClickListener(new Function2<SocialView, String, Unit>() {
                @Override
                public Unit invoke(SocialView socialView, String s) {
                    // do something
                    Intent i = new Intent(ShowTweetActivity.this, ActiviteAndShowTweeetActivity.class);
                    if (s.equals(settings.getString("username", ""))) {
                        i = new Intent(ShowTweetActivity.this, ProfileAndShowToitActivity.class);
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //   i.putExtra("ad", data_list.get(position).getString("user"));
                    // i.putExtra("user_token", data_list.get(position).getJSONObject("user").getString("user_token"));
                    i.putExtra("username", s);
                    startActivity(i);

                    return null;
                }
            });

            tv_number_like_profile.setText(ad.getString("like_number"));
            tv_number_comment_profile.setText(ad.getString("coment_number"));
            tv_number_retweet_profile.setText(ad.getString("repost_number"));


            linear_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //go to profile
                        Intent i = new Intent(ShowTweetActivity.this, ActiviteAndShowTweeetActivity.class);
                        i.putExtra("ad", ad.getString("user"));
                        i.putExtra("user_token", ad.getString("user_token"));
                        //image
                        image_profile_image_fragment.buildDrawingCache();
                        Bitmap image = image_profile_image_fragment.getDrawingCache();
                        Bundle extras = new Bundle();
                        extras.putParcelable("imagebitmap", image);
                        i.putExtras(extras);

                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            tv_reweeted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(ShowTweetActivity.this, ActiviteAndShowTweeetActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("username", ad.getString("user_name_repost"));
                        if (ad.getString("user_name_repost").equals(settings.getString("username", ""))) {
                            i = new Intent(ShowTweetActivity.this, ProfileAndShowToitActivity.class);
                        }
                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            linear_retweet_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i = new Intent(AllTweetActivity.this, SendNewTweetActivity.class);
//                    i.putExtra("retweet", data_list.get(position).toString());
//                    startActivity(i);
                    try {
                        if (!ad.getJSONObject("user").getString("user_token").equals(settings.getString("token", ""))) {
                            dialog_retweet(0, ad, new Button_dialog() {
                                @Override
                                public void onasync(String asyncc_call_back) {
                                    Toast.makeText(ShowTweetActivity.this, "اطلاعات شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                                    // new get_list_tweet().execute();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            });

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listName = new ArrayList<>();
        try {
            JSONArray jarr_image = ad.getJSONArray("media");
            if (jarr_image.length() == 0) {
                mDemoSlider.setVisibility(View.GONE);
            } else {
                mDemoSlider.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < jarr_image.length(); i++) {
                listUrl.add(jarr_image.getJSONObject(i).getString("url"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        //.diskCacheStrategy(DiskCacheStrategy.NONE)
        //.placeholder(R.drawable.placeholder)
        //.error(R.drawable.placeholder);
        for (int i = 0; i < listUrl.size(); i++) {
            //TextSliderView sliderView = new TextSliderView(ShowTweetActivity.this);
            DefaultSliderView sliderView = new DefaultSliderView(ShowTweetActivity.this);
            // if you want show image only / without description text use DefaultSliderView instead
            // initialize SliderLayout
            sliderView
                    .image(listUrl.get(i))
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                        }
                    });

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", "");
            mDemoSlider.addSlider(sliderView);
        }
        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(5000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopCyclingWhenTouch(true);
        if (listUrl.size() <= 1) {
            mDemoSlider.stopAutoCycle();
        }


        try {
            JSONObject job_user = new JSONObject(ad.getString("user"));
            tv_profile_name_fragment.setText(job_user.getString("name"));
            tv_profile_user_name_fragment.setText(job_user.getString("username"));

            if (!ad.getJSONObject("user").getString("image").equals("")) {

                Picasso.get().load(ad.getJSONObject("user").getString("image")).resize(128, 128).into(image_profile_image_fragment);


            } else {

                image_profile_image_fragment.setImageResource(R.drawable.no_photo);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        skeletonScreen.hide();
    }


    private void findviewbyId() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mDemoSlider = findViewById(R.id.slider);
        tv_profile_name_fragment = (TextView) findViewById(R.id.tv_profile_name_fragment);
        tv_profile_user_name_fragment = (TextView) findViewById(R.id.tv_profile_user_name_fragment);
        image_profile_image_fragment = (ImageView) findViewById(R.id.image_profile_image_fragment);
        tv_time_and_date_fragment = (TextView) findViewById(R.id.tv_time_and_date_fragment);
        tv_text_profile_fragment = findViewById(R.id.tv_text_profile_fragment);
        tv_number_like_profile = findViewById(R.id.tv_number_like_profile);
        tv_number_comment_profile = findViewById(R.id.tv_number_comment_profile);
        tv_number_retweet_profile = findViewById(R.id.tv_number_retweet_profile);
        img_like = findViewById(R.id.img_like);
        img_comment = findViewById(R.id.img_comment);
        mDemoSlider = findViewById(R.id.slider);
        linear_profile = findViewById(R.id.linear_profile);
        linear_retweet_profile = findViewById(R.id.linear_retweet_profile);
        edittext_message = findViewById(R.id.edittext_message);
        btn_send_message = findViewById(R.id.btn_send_message);
        tv_reweeted = findViewById(R.id.tv_reweeted);
        textViewOptions_main_view = findViewById(R.id.textViewOptions_main_view);
    }


    public void dialog_retweet(final int pos, final JSONObject jsonObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowTweetActivity.this, R.style.DialogeTheme);
        builder.setTitle("ایا می خواهید این پست را ریتویت کنید");
        //builder.setIcon(R.drawable.ic_action_retweet);
        builder.setMessage("با کلید بر روی تایید این پست در صفحه شخصی شما به صورت ریتویت نمایش داده می شود");
        builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final JSONObject get_ad_list = new JSONObject();
                mcrypt = new MainActivity.MCrypt();
                try {

                    get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("insert_post")));
                    //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));
                    get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                    get_ad_list.put("text", mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(jsonObject.getString("text").getBytes("UTF-8"), Base64.DEFAULT))));
                    get_ad_list.put("android_id", mcrypt.bytesToHex(mcrypt.encrypt(str_android_id)));
                    get_ad_list.put("repost_id", mcrypt.bytesToHex(mcrypt.encrypt(jsonObject.getString("id_post"))));
                    // get_ad_list.put("repost_id", mcrypt.bytesToHex(mcrypt.encrypt(str_repost_id)));


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (new send_retweet().execute(get_ad_list).equals("ok")) {

                }


            }
        });
        builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog dialog = builder.create();
        //baray zamani k mikhahim dialog bejoz click roy butten az bin naravad
        dialog.setCancelable(true);
        dialog.show();


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public abstract class ComentShowAdapter2 extends RecyclerView.Adapter<ComentShowAdapter2.ViewHolder> {
        ArrayList<JSONObject> data_list;
        Context context;
        CardView cardv;

        public ComentShowAdapter2(Context context, ArrayList<JSONObject> data_list) {
            this.context = context;
            this.data_list = data_list;
        }

        @Override
        public ComentShowAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coment_show, parent, false);
            return new ComentShowAdapter2.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ComentShowAdapter2.ViewHolder holder, final int position) {
            try {
                holder.coment_user_title.setText(data_list.get(position).getString("username"));
                holder.coment_description.setText(data_list.get(position).getString("text"));
                holder.coment_description.setHashtagColor(Color.BLUE);
                holder.coment_description.setMentionColor(Color.BLUE);
                holder.coment_description.setOnHashtagClickListener(new Function2<SocialView, String, Unit>() {
                    @Override
                    public Unit invoke(SocialView socialView, String s) {
                        // do something
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ShowTweetActivity.this, AllTweetActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("hashtag", s);
                        startActivity(i);
                        return null;
                    }
                });
                holder.coment_description.setOnMentionClickListener(new Function2<SocialView, String, Unit>() {
                    @Override
                    public Unit invoke(SocialView socialView, String s) {
                        // do something
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ShowTweetActivity.this, ActiviteAndShowTweeetActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //   i.putExtra("ad", data_list.get(position).getString("user"));
                        // i.putExtra("user_token", data_list.get(position).getJSONObject("user").getString("user_token"));
                        i.putExtra("username", s);
                        if (s.equals(settings.getString("username", ""))) {
                            i = new Intent(ShowTweetActivity.this, ProfileAndShowToitActivity.class);
                        }
                        startActivity(i);

                        return null;
                    }
                });

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

                        Toast.makeText(ShowTweetActivity.this, "وارد کردن پیام", Toast.LENGTH_SHORT).show();
                        holder.swipeLayout.close();
                        edittext_message.setText("@" + holder.coment_user_title.getText() + "\n");
                        edittext_message.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edittext_message, InputMethodManager.SHOW_IMPLICIT);
                        edittext_message.setSelection(edittext_message.getText().length());


                    }

                    @Override
                    public void onStartClose(SwipeLayout layout) {

                    }

                    @Override
                    public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                        //when user's hand released.
                        //Toast.makeText(context, "وارد کردن پیام2", Toast.LENGTH_SHORT).show();
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
                        PopupMenu popup = new PopupMenu(ShowTweetActivity.this, holder.buttonViewOption);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.comment_menu);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.mnu_answer:

                                        edittext_message.setText("@" + holder.coment_user_title.getText() + "\n");
                                        edittext_message.requestFocus();
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.showSoftInput(edittext_message, InputMethodManager.SHOW_IMPLICIT);
                                        edittext_message.setSelection(edittext_message.getText().length());
                                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                                        break;
                                    case R.id.mnu_report:
//                                        new LovelyStandardDialog(ShowTweetActivity.this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
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
//                                        //handle menu2 click
                                        dialog_report(data_list.get(position),new Button_dialog() {
                                            @Override
                                            public void onasync(String asyncc_call_back) {

                                            }
                                        });
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
            SocialTextView coment_description;
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
                coment_description = item.findViewById(R.id.coment_description);
                coment_date = (TextView) item.findViewById(R.id.coment_date);
                tv_report = (TextView) item.findViewById(R.id.tv_report);
                swipeLayout = (SwipeLayout) item.findViewById(R.id.sample1);
                buttonViewOption = item.findViewById(R.id.textViewOptions);


                //status.setBackgroundColor(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                //status.setVisibility(View.VISIBLE);
            }
        }
    }

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
        SweetAlertDialog alertDialog = new SweetAlertDialog(ShowTweetActivity.this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText("باز خورد");
        //                                                    .setContentText("Won't be able to recover this file!")
        alertDialog.setConfirmText("ارسال");
        alertDialog.setCustomView(view_dialog);
        alertDialog.setCustomImage(R.drawable.icon_retweet_dialog);
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                try {

                    if (!new send_report_coment().execute(jsonObject).get().equals("error")) {
                        sDialog.cancel();
                        SweetAlertDialog sucess_dialog = new SweetAlertDialog(ShowTweetActivity.this, SweetAlertDialog.SUCCESS_TYPE);
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

    public class get_post_by_post_id extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(ShowTweetActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //   pd.setMessage("در حال دریافت اطلاعات");
            //  pd.setCancelable(false);
            //   pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MainActivity.MCrypt();
            try {

                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("get_post_by_post_id")));
                get_ad_list.put("token", mcrypt.bytesToHex(mcrypt.encrypt(settings.getString("token", ""))));
                get_ad_list.put("post_id", mcrypt.bytesToHex(mcrypt.encrypt(str_post_id)));
                get_ad_list.put("android_id", mcrypt.bytesToHex(mcrypt.encrypt(str_android_id)));


                // get_ad_list.put("text",mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(search_key.getBytes("UTF-8"), Base64.DEFAULT))));


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));
            Log.e("send_post_by_post_id", get_ad_list.toString());

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/ecxa.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());

                Log.e("get_post_by_post_id", response);
                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    // response = new String(mcrypt.decrypt(response)).trim();


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ad = new JSONObject(finalResponse);
                                setValue();
                                skeletonScreen.hide();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(ShowTweetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            // finish();

                        }
                    });


                }

                return response;
            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(ShowTweetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        //finish();
                    }
                });
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.hide();
//            pd.dismiss();

        }
    }

    public class send_like extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(ShowTweetActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //   pd.setMessage("در حال دریافت اطلاعات");
            //  pd.setCancelable(false);
            //   pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MainActivity.MCrypt();
            try {

                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("insert_like_post")));
                get_ad_list.put("token", mcrypt.bytesToHex(mcrypt.encrypt(settings.getString("token", ""))));
                get_ad_list.put("post_id", mcrypt.bytesToHex(mcrypt.encrypt(str_id_post_like)));
                get_ad_list.put("android_id", mcrypt.bytesToHex(mcrypt.encrypt(str_android_id)));


                // get_ad_list.put("text",mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(search_key.getBytes("UTF-8"), Base64.DEFAULT))));


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

                Log.e("data respose", response);
                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    // response = new String(mcrypt.decrypt(response)).trim();


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });

                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(ShowTweetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            // finish();

                        }
                    });


                }

                return response;
            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(ShowTweetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        //finish();
                    }
                });
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.hide();
//            pd.dismiss();

        }
    }

    public class get_commend extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(ShowTweetActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.setCancelable(false);
            progress_ball.setVisibility(View.VISIBLE);
            progress_ball.show();
            // pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("read_comment_post")));
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));
                // get_ad_list.put("post_id", MCrypt.bytesToHex(mcrypt.encrypt(ad.getString("id_post"))));
                if (str_post_id.isEmpty()) {
                    get_ad_list.put("post_id", mcrypt.bytesToHex(mcrypt.encrypt(ad.getString("id_post"))));
                } else {
                    get_ad_list.put("post_id", mcrypt.bytesToHex(mcrypt.encrypt(str_post_id)));
                }
                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));
            Log.e("send commend", get_ad_list.toString());

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/ecxa.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    //  response = new String(mcrypt.decrypt(response)).trim();
                    Log.e("data commend", response);


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray ad_list = new JSONArray(finalResponse);
                                //not_found.setVisibility(View.GONE);

                                if (ad_list.length() == 0 & last_ad_id == 0) {

                                    //   not_found.setVisibility(View.VISIBLE);

                                }

                                if (ad_list.length() != 0) {

                                    last_ad_id = ad_list.getJSONObject(ad_list.length() - 1).getInt("id_comment");


                                    if (ad_list.length() != 10) {
                                        last_ad_id = -1;
                                    }


                                } else {

                                    last_ad_id = -1;
                                }

                                adapter.clear_list();
                                adapter.insert(adapter.getItemCount(), ad_list);

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
                            //finish();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        // finish();

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


    public class send_commend extends AsyncTask<JSONObject, Void, String> {
        ProgressDialog pd = new ProgressDialog(ShowTweetActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.setCancelable(false);
            progress_ball.setVisibility(View.VISIBLE);
            // pd.show();
        }

        @Override
        protected String doInBackground(JSONObject... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("insert_coment_post")));
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));
                get_ad_list.put("id_post", MCrypt.bytesToHex(mcrypt.encrypt(ad.getString("id_post"))));
                get_ad_list.put("text", mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(edittext_message.getText().toString().getBytes("UTF-8"), Base64.DEFAULT))));

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
                    Log.e("data commend", response);


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalResponse.equals("1")) {
                                hideKeyboard(ShowTweetActivity.this);
                                new get_commend().execute();
                            }
//                                JSONArray ad_list = new JSONArray(finalResponse);
//                                //not_found.setVisibility(View.GONE);
//
//                                if (ad_list.length() == 0 & last_ad_id == 0) {
//
//                                    //   not_found.setVisibility(View.VISIBLE);
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
//                                adapter.insert(adapter.getItemCount(), ad_list);


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
            progress_ball.setVisibility(View.GONE);
        }
    }

    public class send_report_coment extends AsyncTask<JSONObject, Void, String> {
        ProgressDialog pd = new ProgressDialog(ShowTweetActivity.this);

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
                get_ad_list.put("news_id", MCrypt.bytesToHex(mcrypt.encrypt(params[0].getString("id_comment"))));

                get_ad_list.put("imei", MCrypt.bytesToHex(mcrypt.encrypt(str_android_id)));
                get_ad_list.put("stattus_report", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(int_stattus_radio_report))));

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
                                Snackbar.make(edittext_message, "گزارش شما با موفقیت ارسال شد", Snackbar.LENGTH_LONG).show();


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

                    return finalResponse;
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

        //////////////////////////////////
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        //handel back button
        try {
            Log.e("show tweet is like", ad.getString("is_like_post"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent i = new Intent();
        i.putExtra("ad", ad.toString());
        i.putExtra("position", str_position);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

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

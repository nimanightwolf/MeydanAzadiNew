package com.app.dadepardazan.meydanazadi.all_tweet;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.dadepardazan.meydanazadi.MainActivity;
import com.app.dadepardazan.meydanazadi.R;
import com.app.dadepardazan.meydanazadi.SendNewTweetActivity;
import com.app.dadepardazan.meydanazadi.show_tweet.ShowTweetActivity;
import com.app.dadepardazan.meydanazadi.view_pager.post.ActiviteAndShowTweeetActivity;
import com.app.dadepardazan.meydanazadi.view_pager.profile.ProfileAndShowToitActivity;
import com.bumptech.glide.request.RequestOptions;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class AllTweetActivity extends MainActivity {

    public static final int RESULT_DATA_USER_CHANGE = 458;

    public TweetAdapter adapter;
    int last_ad_id = 0;
    TextView not_found;
    RecyclerView recyclerView;
    RecyclerView recycler_suggested;
    String str_id_post_like = "";
    JSONArray ad_list;
    String str_android_id = "";
    boolean flag_click = true;
    String str_hashtag = "";
    SkeletonScreen skeletonScreen;
    boolean flag_oneTime = true;
    AllTweetViewModel allTweetViewModel;
    FloatingActionButton fb_send_new_tweet;
    SuggestFollowAdapter suggestFollowAdapter;
    LinearLayout linear_no_follower;
    int int_stattus_radio_report;

    private static final int RESULT_CODE_SHOW_TWEET = 529;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout content_frame;
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_all_tweet, content_frame);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("میدان آزادی");
//        toolbar_title.setVisibility(View.VISIBLE);
        if (getIntent().hasExtra("hashtag")) {
            str_hashtag = getIntent().getStringExtra("hashtag");
            toolbar_title.setText("#" + str_hashtag);
            toolbar_title.setVisibility(View.VISIBLE);
            ImageView img_toolbar = findViewById(R.id.img_toolbar);
            img_toolbar.setVisibility(View.GONE);

        }
        BottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        str_android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);


        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_cnontainer);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                not_found.setVisibility(View.GONE);
                last_ad_id = 0;
                adapter.clear_list();
                new get_list_tweet().execute();
                swipe.setRefreshing(false);


            }
        });

        linear_no_follower = findViewById(R.id.linear_no_follower);
        not_found = (TextView) findViewById(R.id.not_found);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recycler_suggested = (RecyclerView) findViewById(R.id.recycler_suggested);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);

        recycler_suggested.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recycler_suggested.setLayoutManager(gridLayoutManager);

        fb_send_new_tweet = findViewById(R.id.fb_send_new_tweet);
        fb_send_new_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SendNewTweetActivity.class);
                startActivity(i);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //  Toast.makeText(AdListActivity.this, dx+"_"+dy, Toast.LENGTH_SHORT).show();
                if (dy > 0) {
                    fb_send_new_tweet.hide();
                } else {
                    fb_send_new_tweet.show();
                }
            }
        });


        final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();

        adapter = new TweetAdapter(getApplicationContext(), data_list) {
            @Override
            public void load_more() {
                new get_list_tweet().execute();
            }
        };
        final ArrayList<JSONObject> data_list2 = new ArrayList<JSONObject>();
        suggestFollowAdapter = new SuggestFollowAdapter(AllTweetActivity.this, data_list2);
        // recyclerView.setAdapter(adapter);

        skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(adapter)
                .load(R.layout.item_show_tweet)
                .show();
        try {
            String b = "hi";
            String a = new get_list_tweet().execute(b).get();
            //Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recycler_suggested.setAdapter(suggestFollowAdapter);

        try {
            JSONArray jar_temp = new JSONArray();
            for (int i = 0; i < 4; i++) {
                String str_temp = "{\"id\":\"" + i + "\"}";
                JSONObject job_temp = new JSONObject(str_temp);
                jar_temp.put(job_temp);
            }
            suggestFollowAdapter.insert(suggestFollowAdapter.getItemCount(), jar_temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        FloatingActionButton fb_test = findViewById(R.id.fb_test);
//        fb_test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new SweetAlertDialog(AllTweetActivity.this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("ایا مطمئنید?")
//                        .setContentText("این فایل دیگر قابل بازیانی نیست!")
//                        .setConfirmText("بله پاکش کن!")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.dismissWithAnimation();
//                                new SweetAlertDialog(AllTweetActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                                        .setTitleText("Good job!")
//                                        .setContentText("You clicked the button!")
//                                        .show();
//                            }
//                        })
//                        .show();
//                new KAlertDialog(AllTweetActivity.this, KAlertDialog.ERROR_TYPE)
//                        .setTitleText("Are you sure?")
//                        .setContentText("Won't be able to recover this file!")
//
//                        .setCancelText("خیر")
//                        .setConfirmText("بله")
//                        .confirmButtonColor(R.color.colorPrimary) // you can change the color of confirm button
//                        .cancelButtonColor(R.color.colorAccent) // you can change the color of cancel button
//                        .showCancelButton(true)
//                        .setCancelClickListener(new KAlertDialog.KAlertClickListener() {
//                            @Override
//                            public void onClick(KAlertDialog sDialog) {
//                                sDialog.cancel();
//                            }
//                        })
//                        .show();


    }
//        });
//
//    }


    public void dialog_retweet45654(final int pos, final JSONObject jsonObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AllTweetActivity.this, R.style.DialogeTheme);
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
                    Toast.makeText(AllTweetActivity.this, "اطلاعات شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                    new get_list_tweet().execute();

                    setResult(RESULT_OK);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE_SHOW_TWEET & resultCode == RESULT_OK) {
            JSONObject job = null;
            try {
                if (data.hasExtra("id_item_deleted")) {
                    adapter.delete_item_with_Id(data.getStringExtra("id_item_deleted"));
                } else {
                    Log.e("data back", data.getStringExtra("ad"));
                    job = new JSONObject(data.getStringExtra("ad"));
                    int position = Integer.parseInt(data.getStringExtra("position"));
                    adapter.update_item(position, job);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        flag_click = true;
//        if (resultCode == RESULT_OK) {
//            new get_list_tweet().execute();
//        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        flag_click = true;
        BottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);

    }


    public abstract class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> implements
            ViewPagerEx.OnPageChangeListener {
        ArrayList<JSONObject> data_list;
        Context context;


        public TweetAdapter(Context context, ArrayList<JSONObject> data_list) {
            this.context = context;
            this.data_list = data_list;

        }

        @Override
        public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_tweet, parent, false);
            return new TweetAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final TweetAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            try {
                holder.img_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        try {
                            str_id_post_like = data_list.get(position).getString("id_post");
                            if (data_list.get(position).getString("is_like_post").equals("0")) {
                                // you going to like post
                                String a = new send_like().execute().get();
                                Log.e("post like", a);
                                holder.img_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_heart));
                                holder.tv_number_like_profile.setText(String.valueOf(data_list.get(position).getInt("like_number") + 1));
                                data_list.get(position).put("is_like_post", "1");
                                data_list.get(position).put("like_number", holder.tv_number_like_profile.getText().toString());
                            } else {
                                String a = new send_like().execute().get();
                                Log.e("post like", a);
                                holder.img_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_empty_heart));
                                holder.tv_number_like_profile.setText(String.valueOf(data_list.get(position).getInt("like_number") - 1));
                                data_list.get(position).put("is_like_post", "0");
                                data_list.get(position).put("like_number", holder.tv_number_like_profile.getText().toString());


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
                if (data_list.get(position).getString("repost_id").equals("0")) {
                    holder.tv_reweeted.setVisibility(View.GONE);
                } else {
                    holder.tv_reweeted.setVisibility(View.VISIBLE);
                    holder.tv_reweeted.setText("ریتویت شده از " + data_list.get(position).getString("user_name_repost"));
                }
                if (data_list.get(position).getString("is_like_post").equals("1")) {
                    holder.img_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_heart));
                } else {
                    holder.img_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_empty_heart));
                }
                JSONObject job_user = new JSONObject(data_list.get(position).getString("user"));
                holder.tv_profile_name_fragment.setText(job_user.getString("name"));

                holder.tv_profile_user_name_fragment.setText(job_user.getString("username"));
                if (!job_user.getString("image").equals("")) {
                    Picasso.get().load(job_user.getString("image")).resize(512, 512).into(holder.image_profile_image_fragment);
                    //   Picasso.with(context).load("https://meydane-azadi.ir/photo/photo.php?image_name=" + job_user.getString("image")).resize(512, 512).into(holder.image_profile_image_fragment);
                } else {
                    holder.image_profile_image_fragment.setImageResource(R.drawable.icon_user);
                }


                holder.tv_time_and_date_fragment.setText(data_list.get(position).getString("time"));
                holder.tv_text_profile_fragment.setText(data_list.get(position).getString("text"));

                holder.tv_text_profile_fragment.setHyperlinkEnabled(true);
                holder.tv_text_profile_fragment.setHashtagColor(Color.BLUE);
                holder.tv_text_profile_fragment.setMentionColor(Color.BLUE);
                holder.tv_text_profile_fragment.setOnHashtagClickListener(new Function2<SocialView, String, Unit>() {
                    @Override
                    public Unit invoke(SocialView socialView, String s) {
                        // do something
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        flag_click = false;
                        Intent i = new Intent(AllTweetActivity.this, AllTweetActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("hashtag", s);
                        startActivity(i);
                        return null;
                    }
                });
                holder.tv_text_profile_fragment.setOnMentionClickListener(new Function2<SocialView, String, Unit>() {
                    @Override
                    public Unit invoke(SocialView socialView, String s) {
                        // do something
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AllTweetActivity.this, ActiviteAndShowTweeetActivity.class);
                        if (s.equals(settings.getString("username", ""))) {
                            i = new Intent(AllTweetActivity.this, ProfileAndShowToitActivity.class);
                        }
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //   i.putExtra("ad", data_list.get(position).getString("user"));
                        // i.putExtra("user_token", data_list.get(position).getJSONObject("user").getString("user_token"));
                        i.putExtra("username", s);
                        startActivity(i);
                        flag_click = false;
                        return null;
                    }
                });

                holder.tv_text_profile_fragment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("data general:", data_list.get(position).toString());
                        if (flag_click) {
                            function_intent_show_tweet(data_list.get(position), position + "");


                        }
                        flag_click = true;
                    }
                });


//                String a="";
//                a.indexOf("@");

                // tv.setText(text);
//                String content = text_tweet.substring(text_tweet.indexOf('@') + 1);
//                content=content.substring(0, content.indexOf(' '));
//                Toast.makeText(context, nickname[1]+"___"+ nickname.length+content, Toast.LENGTH_SHORT).show();
//
//                ClickableSpan clickableSpan = new ClickableSpan() {
//                    @Override
//                    public void onClick(View textView) {
//                     //   startActivity(new Intent(AllTweetActivity.this, NextActivity.class));
//                        Toast.makeText(context, "hiiii", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
//                        ds.setUnderlineText(false);
//                       // Toast.makeText(context, "bnyyyyy", Toast.LENGTH_SHORT).show();
//                    }
//                };
//                ss.setSpan(clickableSpan, text_tweet.indexOf(content)-1, text_tweet.indexOf(content)+content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                //holder.tv_text_profile_fragment.setText(text_tweet);
//                holder.tv_text_profile_fragment.setMovementMethod(LinkMovementMethod.getInstance());
//                holder.tv_text_profile_fragment.setHighlightColor(Color.TRANSPARENT);


                holder.tv_number_like_profile.setText(data_list.get(position).getString("like_number"));
                holder.tv_number_comment_profile.setText(data_list.get(position).getString("coment_number"));
                holder.tv_number_retweet_profile.setText(data_list.get(position).getString("repost_number"));


            } catch (JSONException e) {
                e.printStackTrace();
            }


            ArrayList<String> listUrl = new ArrayList<>();
            ArrayList<String> listName = new ArrayList<>();
            try {
                JSONArray jarr_image = data_list.get(position).getJSONArray("media");
                if (jarr_image.length() == 0) {
                    holder.mDemoSlider.setVisibility(View.GONE);
                } else {
                    holder.mDemoSlider.setVisibility(View.VISIBLE);
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
            holder.mDemoSlider.removeAllSliders();
            for (int i = 0; i < listUrl.size(); i++) {
                //TextSliderView sliderView = new TextSliderView(AllTweetActivity.this);
                DefaultSliderView sliderView = new DefaultSliderView(AllTweetActivity.this);
                // if you want show image only / without description text use DefaultSliderView instead
                // initialize SliderLayout
                sliderView
                        .image(listUrl.get(i))
                        .setRequestOption(requestOptions)
                        .setProgressBarVisible(true)
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                function_intent_show_tweet(data_list.get(position), position + "");
                            }
                        });

                //add your extra information
                sliderView.bundle(new Bundle());
                sliderView.getBundle().putString("extra", "");
                holder.mDemoSlider.addSlider(sliderView);
            }
            // set Slider Transition Animation
            // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
            holder.mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            holder.mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            holder.mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            holder.mDemoSlider.setDuration(5000);
            holder.mDemoSlider.addOnPageChangeListener(this);
            holder.mDemoSlider.stopCyclingWhenTouch(true);
            if (listUrl.size() <= 1) {
                holder.mDemoSlider.stopAutoCycle();
            }


            if (position >= getItemCount() - 1) {

                load_more();
            }


            holder.linear_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("data general:", data_list.get(position).toString());
                    function_intent_show_tweet(data_list.get(position), position + "");


                }
            });
            holder.linear_retweet_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i = new Intent(AllTweetActivity.this, SendNewTweetActivity.class);
//                    i.putExtra("retweet", data_list.get(position).toString());
//                    startActivity(i);
                    try {
                        if (!data_list.get(position).getJSONObject("user").getString("user_token").equals(settings.getString("token", ""))) {
                            // dialog_retweet(position, data_list.get(position));
                            dialog_retweet(position, data_list.get(position), new Button_dialog() {
                                @Override
                                public void onasync(String asyncc_call_back) {
                                    adapter.clear_list();
                                    last_ad_id = 0;
                                    new get_list_tweet().execute();
                                }
                            });

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.tv_reweeted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(AllTweetActivity.this, ActiviteAndShowTweeetActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // i.putExtra("user_token", data_list.get(position).getString("token_repost"));
                        i.putExtra("username", data_list.get(position).getString("user_name_repost"));
                        if (data_list.get(position).getString("user_name_repost").equals(settings.getString("username", ""))) {
                            i = new Intent(AllTweetActivity.this, ProfileAndShowToitActivity.class);
                        }
                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.linear_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //go to profile
                        if (data_list.get(position).getJSONObject("user").getString("user_token").equals(settings.getString("token", ""))) {
                            Intent i = new Intent(AllTweetActivity.this, ProfileAndShowToitActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else {
                            Log.e("ad", data_list.get(position).getJSONObject("user").toString());
                            Intent i = new Intent(AllTweetActivity.this, ActiviteAndShowTweeetActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("ad", data_list.get(position).getJSONObject("user").toString());
                            // i.putExtra("user_token", data_list.get(position).getJSONObject("user").getString("user_token"));
                            i.putExtra("username", data_list.get(position).getJSONObject("user").getString("username"));
                            Log.e("username_send", data_list.get(position).getJSONObject("user").getString("username"));
                            //image
                            holder.image_profile_image_fragment.buildDrawingCache();
                            Bitmap image = holder.image_profile_image_fragment.getDrawingCache();
                            Bundle extras = new Bundle();
                            extras.putParcelable("imagebitmap", image);
                            i.putExtras(extras);
                            startActivityForResult(i, RESULT_DATA_USER_CHANGE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            try {
                if (data_list.get(position).getJSONObject("user").getString("user_token").equals(settings.getString("token", ""))) {
                    holder.textViewOptions.setVisibility(View.VISIBLE);
                    holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            PopupMenu popup = new PopupMenu(AllTweetActivity.this, holder.textViewOptions);
                            popup.inflate(R.menu.option_item_tweet);
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.mnu_delete:
//                                            new LovelyStandardDialog(AllTweetActivity.this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
//                                                    .setTopColorRes(R.color.colorAccent)
//                                                    .setButtonsColor(Color.RED)
//                                                    .setIcon(android.R.drawable.ic_menu_delete)
//                                                    .setTitle("حذف")
//                                                    .setMessage("ایا می خواهید این نویسه را پاک کنید")
//                                                    .setPositiveButton("بله", new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View v) {
//                                                            //Toast.makeText(context, "positive clicked", Toast.LENGTH_SHORT).show();
//                                                            JSONObject get_ad_list = new JSONObject();
//                                                            try {
//                                                                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("delete_post")));
//                                                                get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
//                                                                get_ad_list.put("id_post", mcrypt.bytesToHex(mcrypt.encrypt(data_list.get(position).getString("id_post"))));
//
//                                                                if (new send_data().execute(get_ad_list).get().equals("\"ok\"")) {
//                                                                   // Snackbar.make(view, R.string.nevise+" شما با موفقیت حذف شد", Snackbar.LENGTH_SHORT).show();
//                                                                    Toast.makeText(AllTweetActivity.this, "نویسه شما با موفقیت حذف شد", Toast.LENGTH_SHORT).show();
//                                                                    delete_item(data_list.get(position), position);
//                                                                }
//
//                                                            } catch (Exception e) {
//                                                                e.printStackTrace();
//                                                            }
//                                                        }
//                                                    })
//                                                    .setNegativeButton("لغو", null)
//                                                    .show();
                                            //handle menu2 click
                                            dialog_delete_tweet(data_list.get(position), new After_delete_listener() {
                                                @Override
                                                public void delete_completed(SweetAlertDialog alertDialog) {
                                                    delete_item(data_list.get(position), position);
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
                } else {
                    holder.textViewOptions.setVisibility(View.GONE);
                    holder.textViewOptions.setVisibility(View.VISIBLE);
                    holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            PopupMenu popup = new PopupMenu(AllTweetActivity.this, holder.textViewOptions);
                            popup.inflate(R.menu.report_tweet_menu);
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.mnu_report:
                                            try {
                                                //todo send id post
                                                dialog_tweet_report(data_list.get(position).getString("id_post"), AllTweetActivity.this, new Button_dialog() {
                                                    @Override
                                                    public void onasync(String asyncc_call_back) {

                                                    }
                                                });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

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


//            try {
//
//
//                //String text_tweet = "سلام من @nima_heydari هستم و از @mehdi دعوت به همکاری می کنم";
//                String text_tweet=data_list.get(position).getString("text");
//
//                SpannableString ss = new SpannableString(text_tweet);
//                final ArrayList<String> nickname = new ArrayList<>();
//                if (text_tweet.indexOf("@") == -1) {
//
//                    nickname.add(text_tweet);
//                    return;
//
//                } else {
//                    // nickname = Arrays.asList(text_tweet.split("@"));
//                    String[] temp = text_tweet.split("@");
//                    for (int i = 1; i < temp.length; i++) {
//                        Log.e("print data",temp[i]);
//                        if (temp[i].indexOf(' ') != -1) {
//                            nickname.add(temp[i].substring(0, temp[i].indexOf(' ')));
//                        }else{
//                            nickname.add(text_tweet.replace("@","").trim());
//                            text_tweet=text_tweet+" ";
//                        }
//
//                    }
//
//
//                }
//                for (String str:nickname){
//                    if (str.length()<3){
//                        nickname.remove(str);
//                    }
//                }
//                // if (nickname.size() > 1) {
////                    for (String temp : nickname) {
////
////                    }
//
//                for (int i = 0; i < nickname.size(); i++) {
//                    // if (i > 0) {
////                    if (nickname.get(i).indexOf(' ') != -1) {
////                        nickname.set(i, nickname.get(i).substring(0, nickname.get(i).indexOf(' ')));
////                    }
//                    nickname.set(i, "@" + nickname.get(i)+" ");
//                    final int finalI = i;
//                    ClickableSpan clickableSpan = new ClickableSpan() {
//                        @Override
//                        public void onClick(View textView) {
//                            //   startActivity(new Intent(AllTweetActivity.this, NextActivity.class));
//                            TextView textView2 = new TextView(context);
//                            if (textView instanceof TextView) {
//                                textView2 = (TextView) textView;
//
//
//                                //Do your stuff
//                            }
//                            Toast.makeText(context, textView2.getText().toString() + finalI, Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void updateDrawState(TextPaint ds) {
//                            //super.updateDrawState(' ');
//                            // ds.setUnderlineText(true);
//                            ds.setColor(Color.BLUE);
//
//
//                            // Toast.makeText(context, "bnyyyyy", Toast.LENGTH_SHORT).show();
//                        }
//                    };
//                    ClickableSpan clickableSpan2 = new ClickableSpan() {
//                        @Override
//                        public void onClick(View textView2) {
//                            //   startActivity(new Intent(AllTweetActivity.this, NextActivity.class));
//                            TextView textView3 = new TextView(context);
//                            if (textView2 instanceof TextView) {
//                                textView3 = (TextView) textView2;
//
//
//                                //Do your stuff
//                            }
//                            Toast.makeText(context, nickname.get(finalI) + finalI, Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void updateDrawState(TextPaint ds) {
//                            //super.updateDrawState(' ');
//                            // ds.setUnderlineText(true);
//
//                            ds.setColor(Color.GREEN);
//
//
//                            // Toast.makeText(context, "bnyyyyy", Toast.LENGTH_SHORT).show();
//                        }
//                    };
//                    Log.e("nikname", nickname.get(i));
//                    // ss.setSpan(clickableSpan, 0, text_tweet.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    int start = 0;
//                    int end = 0;
//                    end = text_tweet.indexOf(nickname.get(i));
//
//                    if (end >0) {
//                        end = end - 1;
//                    }
//
//                    if (i == 0) {
//                        start = 0;
//
//                    } else {
//                        start = text_tweet.indexOf(nickname.get(i - 1)) + nickname.get(i - 1).length()-1;
//                    }
//                  //  ss.setSpan(clickableSpan2, text_tweet.indexOf(nickname.get(i)), text_tweet.indexOf(nickname.get(i)) + nickname.get(i).length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    if (i == nickname.size() - 1) {
//                        if (text_tweet.length() != text_tweet.indexOf(nickname.get(nickname.size() - 1)) + nickname.get(nickname.size() - 1).length()) {
//                            int start2 = text_tweet.indexOf(nickname.get(nickname.size() - 1))+nickname.get(nickname.size()-1).length()-1;
//                            int end2 = text_tweet.length();
//                            ClickableSpan clickableSpan3 = new ClickableSpan() {
//                                @Override
//                                public void onClick(View textView) {
//                                    //   startActivity(new Intent(AllTweetActivity.this, NextActivity.class));
//                                    TextView textView2 = new TextView(context);
//                                    if (textView instanceof TextView) {
//                                        textView2 = (TextView) textView;
//                                        //Do your stuff
//                                    }
//                                    Toast.makeText(context, textView2.getText().toString() + finalI, Toast.LENGTH_SHORT).show();
//                                }
//
//                                @Override
//                                public void updateDrawState(TextPaint ds) {
//                                    //super.updateDrawState(' ');
//                                    // ds.setUnderlineText(true);
//                                    ds.setColor(Color.BLUE);
//
//                                }
//                            };
//
//                            ss.setSpan(clickableSpan3, start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        }
//                    }
//                }
//
//                //  }
//                // }
//                holder.tv_text_profile_fragment.setText(ss);
//            }catch (Exception e){
//
//                try {
//                    holder.tv_text_profile_fragment.setText(data_list.get(position).getString("text"));
////                    Log.e("error",e.getMessage());
//                    e.printStackTrace();
//                } catch (JSONException ex) {
//                    ex.printStackTrace();
//                }
//                holder.tv_text_profile_fragment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(context, "felan", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }


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

        public void delete_item(JSONObject job, int posi) {
            data_list.remove(job);
            notifyItemRemoved(posi);
        }

        public void delete_item_with_Id(String Id) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                try {
                    if (data_list.get(i).getString("id_post").equals(Id)) {
                        data_list.remove(data_list.get(i));
                    }
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public void update_item(int pos, JSONObject job) {
            data_list.set(pos, job);
            notifyItemChanged(pos);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_profile_name_fragment;
            TextView tv_profile_user_name_fragment;
            ImageView image_profile_image_fragment;
            TextView tv_time_and_date_fragment;
            SocialTextView tv_text_profile_fragment;
            TextView tv_number_like_profile;
            TextView tv_number_comment_profile;
            TextView tv_number_retweet_profile;
            // SliderLayout slider_profile_fragment;
            ImageView img_like;
            ImageView img_comment;
            LinearLayout linear_card;
            LinearLayout linear_retweet_profile;
            SliderLayout mDemoSlider;
            TextView tv_reweeted;
            LinearLayout linear_profile;
            TextView textViewOptions;


            //  com.smarteist.autoimageslider.SliderLayout sliderLayout;


            public ViewHolder(View item) {
                super(item);
                tv_profile_name_fragment = (TextView) item.findViewById(R.id.tv_profile_name_fragment);
                tv_profile_user_name_fragment = (TextView) item.findViewById(R.id.tv_profile_user_name_fragment);
                //ad_location = (TextView) item.findViewById(R.id.ad_location);
                image_profile_image_fragment = (ImageView) item.findViewById(R.id.image_profile_image_fragment);
                tv_time_and_date_fragment = (TextView) item.findViewById(R.id.tv_time_and_date_fragment);
                tv_text_profile_fragment = item.findViewById(R.id.tv_text_profile_fragment);
                // slider_profile_fragment = item.findViewById(R.id.slider_profile_fragment);
                tv_number_like_profile = item.findViewById(R.id.tv_number_like_profile);
                tv_number_comment_profile = item.findViewById(R.id.tv_number_comment_profile);
                tv_number_retweet_profile = item.findViewById(R.id.tv_number_retweet_profile);
                img_like = item.findViewById(R.id.img_like);
                img_comment = item.findViewById(R.id.img_comment);
                linear_card = item.findViewById(R.id.linear_card);
                mDemoSlider = item.findViewById(R.id.slider);
                linear_retweet_profile = item.findViewById(R.id.linear_retweet_profile);
                // sliderLayout=item.findViewById(R.id.imageSlider);
                tv_reweeted = item.findViewById(R.id.tv_reweeted);
                linear_profile = item.findViewById(R.id.linear_profile);
                textViewOptions = item.findViewById(R.id.textViewOptions);


            }
        }
    }


    public class SuggestFollowAdapter extends RecyclerView.Adapter<SuggestFollowAdapter.ViewHolder> {
        ArrayList<JSONObject> data_list;
        Context context;


        public SuggestFollowAdapter(Context context, ArrayList<JSONObject> data_list) {
            this.context = context;
            this.data_list = data_list;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggest_follow, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            try {

                if (!data_list.get(position).getString("image").equals("")) {
                    Picasso.get().load("https://meydane-azadi.ir/photo/photo.php?image_name=" + data_list.get(position).getString("image")).resize(512, 512).into(holder.image_user);
                } else {
                    holder.image_user.setImageResource(R.drawable.icon_user);
                }
                holder.tv_name.setText(data_list.get(position).getString("name"));
                holder.tv_user_name.setText(data_list.get(position).getString("username"));
                holder.tv_bio.setText(data_list.get(position).getString("bio"));
                //todo این بخشو به صورت دستی مخفی کردم هنگام تکمیل شدن ای پی ای کامل شود
                holder.image_check.setVisibility(View.GONE);
                if (data_list.get(position).has("is_follow")) {
                    if (data_list.get(position).getString("is_follow").equals("0")) {
                        holder.btn_followed.setVisibility(View.GONE);
                        holder.btn_followering.setVisibility(View.VISIBLE);
                    } else {
                        holder.btn_followed.setVisibility(View.VISIBLE);
                        holder.btn_followering.setVisibility(View.GONE);
                    }
                } else {
                    holder.btn_followed.setVisibility(View.GONE);
                    holder.btn_followering.setVisibility(View.VISIBLE);
                }

                holder.btn_followering.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int temp_position = position;

                            if (new send_follow().execute(data_list.get(temp_position).getString("username")).get().equals("follow")) {
                                // tv_profile_followering.setText("دنبال کرده اید");
                                holder.btn_followed.setVisibility(View.VISIBLE);
                                holder.btn_followering.setVisibility(View.GONE);
                                data_list.get(temp_position).put("is_follow", "1");

                            } else {
                                // tv_profile_followering.setText("دنبال کنید");
                                holder.btn_followed.setVisibility(View.GONE);
                                holder.btn_followering.setVisibility(View.VISIBLE);
                                data_list.get(temp_position).put("is_follow", "0");
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                holder.btn_followed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int temp_position = position;

                            if (new send_follow().execute(data_list.get(temp_position).getString("username")).get().equals("follow")) {
                                // tv_profile_followering.setText("دنبال کرده اید");
                                holder.btn_followed.setVisibility(View.VISIBLE);
                                holder.btn_followering.setVisibility(View.GONE);
                                data_list.get(temp_position).put("is_follow", "1");

                            } else {
                                // tv_profile_followering.setText("دنبال کنید");
                                holder.btn_followed.setVisibility(View.GONE);
                                holder.btn_followering.setVisibility(View.VISIBLE);
                                data_list.get(temp_position).put("is_follow", "0");
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
                holder.cardv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent i = new Intent(getApplicationContext(), ActiviteAndShowTweeetActivity.class);

                            if (data_list.get(position).getString("username").equals(settings.getString("username", ""))) {
                                i = new Intent(AllTweetActivity.this, ProfileAndShowToitActivity.class);
                            }

                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //   i.putExtra("ad", data_list.get(position).getString("user"));
                            // i.putExtra("user_token", data_list.get(position).getJSONObject("user").getString("user_token"));
                            i.putExtra("username", data_list.get(position).getString("username"));
                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


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

        public void delete_item(JSONObject ad_list) {


            int delet_position = data_list.indexOf(ad_list);
            data_list.remove(ad_list);


            notifyItemInserted(delet_position);


        }

        public void clear_list() {

            int size = data_list.size();
            data_list.clear();
            notifyItemRangeRemoved(0, size);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image_check;
            ImageView image_user;
            TextView tv_name;
            TextView tv_bio;
            TextView tv_user_name;
            Button btn_followed;
            Button btn_followering;

            CardView cardv;


            public ViewHolder(View item) {
                super(item);

                image_check = item.findViewById(R.id.image_check);
                image_user = item.findViewById(R.id.image_user);
                tv_name = item.findViewById(R.id.tv_name);
                tv_bio = item.findViewById(R.id.tv_bio);
                tv_user_name = item.findViewById(R.id.tv_user_name);
                btn_followed = item.findViewById(R.id.btn_followed);
                btn_followering = item.findViewById(R.id.btn_followering);
                cardv = item.findViewById(R.id.cardv);
            }


        }


    }

    public void dialog_tweet_report(final String str_id_post, final Context context, final Button_dialog bt_dialog) {
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
        final SweetAlertDialog alertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText("باز خورد");
        //                                                    .setContentText("Won't be able to recover this file!")
        alertDialog.setConfirmText("ارسال");
        alertDialog.setCustomView(view_dialog);
        alertDialog.setCustomImage(R.drawable.icon_retweet_dialog);
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(final SweetAlertDialog sDialog) {
                try {
                    mcrypt = new MainActivity.MCrypt();
                    final JSONObject get_ad_list = new JSONObject();
                    get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("report_post")));

                    get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                    get_ad_list.put("news_id", MCrypt.bytesToHex(mcrypt.encrypt(str_id_post)));

                    get_ad_list.put("imei", MCrypt.bytesToHex(mcrypt.encrypt(str_android_id)));
                    get_ad_list.put("stattus_report", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(int_stattus_radio_report))));


                    if (!new send_data().execute(get_ad_list).get().equals("error")) {
                        bt_dialog.onasync("");
                        sDialog.cancel();
                        sDialog.dismiss();
                        SweetAlertDialog sucess_dialog = new SweetAlertDialog(AllTweetActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        sucess_dialog.setTitleText("گزارش شما با موفقیت ثبت شد");
                        sucess_dialog.setConfirmText("متوجه شدم");
                        sucess_dialog.setConfirmClickListener(null);
                        sucess_dialog.show();
                        Button btn = (Button) sucess_dialog.findViewById(R.id.confirm_button);
                        btn.setBackground(getResources().getDrawable(R.drawable.background_button_sweetalert));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })
                .show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackground(getResources().getDrawable(R.drawable.background_button_sweetalert));
    }


    public class get_list_tweet extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(AllTweetActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.setCancelable(false);
            progress_ball.setVisibility(View.VISIBLE);
            //pd.show();
        }

        @Override
        protected String doInBackground(final String... params) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//
//                    Toast.makeText(AllTweetActivity.this, params[0].toString(), Toast.LENGTH_SHORT).show();
//                    // finish();
//
//                }
//            });
            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MainActivity.MCrypt();
            try {

                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("post_read_home")));
                get_ad_list.put("last_ad_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(last_ad_id))));
                get_ad_list.put("token", mcrypt.bytesToHex(mcrypt.encrypt(settings.getString("token", ""))));

                get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("search", mcrypt.bytesToHex(mcrypt.encrypt("")));
                get_ad_list.put("hashtag", mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(str_hashtag.getBytes("UTF-8"), Base64.DEFAULT))));


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

                            try {
                                JSONObject job_all_data = new JSONObject(finalResponse);
                                ad_list = job_all_data.getJSONArray("post");
                                JSONArray jar_suggest = job_all_data.getJSONArray("suggest");
                                not_found.setVisibility(View.GONE);

                                if (ad_list.length() == 0 & last_ad_id == 0) {

                                    not_found.setVisibility(View.VISIBLE);

                                }

                                if (ad_list.length() != 0) {

                                    last_ad_id = ad_list.getJSONObject(ad_list.length() - 1).getInt("id");


                                    if (ad_list.length() != 10) {
                                        last_ad_id = -1;
                                    }


                                } else {

                                    last_ad_id = -1;
                                }

                                if (flag_oneTime) {
                                    flag_oneTime = false;
                                    skeletonScreen.hide();
                                }
                                adapter.insert(adapter.getItemCount(), ad_list);
                                if (job_all_data.getString("is_show").equals("1") && adapter.getItemCount() == 0) {
                                    linear_no_follower.setVisibility(View.VISIBLE);
                                    suggestFollowAdapter.clear_list();
                                    suggestFollowAdapter.insert(suggestFollowAdapter.getItemCount(), jar_suggest);
                                    not_found.setVisibility(View.GONE);
                                } else {
                                    linear_no_follower.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(AllTweetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
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


                        Toast.makeText(AllTweetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
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
            progress_ball.setVisibility(View.GONE);
        }
    }


    public class send_like extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(AllTweetActivity.this);

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


                            Toast.makeText(AllTweetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
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


                        Toast.makeText(AllTweetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
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

    public void function_intent_show_tweet(JSONObject job, String position) {
        Intent i = new Intent(AllTweetActivity.this, ShowTweetActivity.class);
        i.putExtra("ad", job.toString());
        i.putExtra("position", position);
        // startActivityForResult(i, RESULT_CODE_SHOW_TWEET);
        startActivityForResult(i, RESULT_CODE_SHOW_TWEET);
    }


    //    public class AllTweetViewModel extends AndroidViewModel {
//        LiveData<ArrayList<JSONObject>> listLiveData;
//        public AllTweetViewModel(@NonNull Application application) throws JSONException {
//            super(application);
//            ArrayList<JSONObject>temp=new ArrayList<>();
//            for (int i=0;i<ad_list.length();i++){
//                temp.add(ad_list.getJSONObject(i));
//            }
//            listLiveData=temp;
//
//        }
//        public void update(JSONObject job){
//
//        }
//        public LiveData<ArrayList<JSONObject>> getListLiveData(){
//            return listLiveData;
//        }
//
//    }
    public class AllTweetViewModel extends ViewModel {

        private MutableLiveData<ArrayList<JSONObject>> data_list_live;


    }

    public class send_follow extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(getApplicationContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // pd.setMessage("در حال دریافت اطلاعات");
            // pd.setCancelable(false);
            //    pd.show();
            //progress_ball.setVisibility(View.VISIBLE);
            //progress_ball.show();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MainActivity.MCrypt();
            try {

                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("follow_user_by_username")));
                get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                // get_ad_list.put("token", mcrypt.bytesToHex(mcrypt.encrypt(str_user_token)));
                get_ad_list.put("username", mcrypt.bytesToHex(mcrypt.encrypt(params[0])));


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

                    return response;


                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getApplicationContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });
                    return "error";

                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getApplicationContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
                return "error";

            }


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.hide();
////            pd.dismiss();
            //progress_ball.hide();
            //progress_ball.setVisibility(View.GONE);


        }
    }


}

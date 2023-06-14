package com.app.dadepardazan.meydanazadi.view_pager.post;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dadepardazan.meydanazadi.MainActivity;
import com.app.dadepardazan.meydanazadi.R;
import com.app.dadepardazan.meydanazadi.all_tweet.AllTweetActivity;
import com.app.dadepardazan.meydanazadi.show_tweet.ShowTweetActivity;
import com.app.dadepardazan.meydanazadi.view_pager.profile.ProfileAndShowToitActivity;
import com.bumptech.glide.request.RequestOptions;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
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

import static android.app.Activity.RESULT_OK;
import static com.app.dadepardazan.meydanazadi.all_tweet.AllTweetActivity.RESULT_DATA_USER_CHANGE;

public class FragmentTweet extends Fragment {

    private String title;
    //private JSONObject job_data;
    SharedPreferences settings;
    RecyclerView recyclerview;
    TweetFragmentAdapter adapter;
    MainActivity.MCrypt mcrypt;
    JSONObject ad = new JSONObject();
    private int last_ad_id = 0;
    boolean is_like = false;
    TextView not_found;
    String str_id_post_like;
    String str_android_id = "";
    String str_user_token = "";
    String str_user_username = "";
    boolean flag_click = true;
    SkeletonScreen skeletonScreen;
    private static final int RESULT_CODE_SHOW_TWEET = 360;


    public static FragmentTweet newInstance(String title, String data) {

        FragmentTweet fragment = new FragmentTweet();

        Bundle args = new Bundle();

        args.putString("data", data);

        args.putString("title", title);

        fragment.setArguments(args);

        return fragment;

    }

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //just need user_token from json
        //  str_user_token =(getArguments().getString("data"));
        title = getArguments().getString("title");
        if (title.equals("token")) {
            str_user_token = (getArguments().getString("data"));
            Log.e("username", str_user_username);
        } else {
            str_user_username = (getArguments().getString("data"));
        }

        //Toast.makeText(getActivity(), job_data.toString(), Toast.LENGTH_SHORT).show();


    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().setTheme(R.style.NoActionBarDark);

        View view_main = inflater.inflate(R.layout.fragmant_search, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

//        try {
//            ad = new JSONObject(title);
//            Log.e("data json", ad.toString());
//            ad = ad.getJSONObject("user");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        str_android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        recyclerview = view_main.findViewById(R.id.recyclerview_fragmant_search);
        not_found = view_main.findViewById(R.id.not_found);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();
//        for (int i = 0; i < 4; i++) {
//            JSONObject a = new JSONObject();
//            data_list.add(a);
//        }
        adapter = new TweetFragmentAdapter(getContext(), data_list) {
            @Override
            public void load_more() {

            }
        };
        // recyclerview.setAdapter(adapter);
        skeletonScreen = Skeleton.bind(recyclerview)
                .adapter(adapter)
                .load(R.layout.item_show_tweet)
                .show();


        if (title.equals("token")) {
            new get_list_tweet_token().execute();
        } else {
            new get_list_tweet().execute();
        }


        setHasOptionsMenu(true);
        return view_main;

    }



    @Override
    public void onResume() {
        super.onResume();
        // Toast.makeText(getContext(),"2222",Toast.LENGTH_SHORT).show();
//        last_ad_id = 0;
//        adapter.clear_list();
//        new get_my_ad_list().execute();
//        new get_add().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_CODE_SHOW_TWEET & resultCode == RESULT_OK) {
//            JSONObject job = null;
//            try {
//                Log.e("data back", data.getStringExtra("ad"));
//                job = new JSONObject(data.getStringExtra("ad"));
//                int position = Integer.parseInt(data.getStringExtra("position"));
//
//                adapter.update_item(position, job);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
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
    }

    public void dialog_retweet(final int pos, final JSONObject jsonObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogeTheme);

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
                    Toast.makeText(getActivity(), "اطلاعات شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                    if (title.equals("token")) {
                        new get_list_tweet_token().execute();
                    } else {
                        new get_list_tweet().execute();
                    }

                    getActivity().setResult(RESULT_OK);
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

    public abstract class TweetFragmentAdapter extends RecyclerView.Adapter<TweetFragmentAdapter.ViewHolder> implements
            ViewPagerEx.OnPageChangeListener {
        ArrayList<JSONObject> data_list;
        Context context;


        public TweetFragmentAdapter(Context context, ArrayList<JSONObject> data_list) {
            this.context = context;
            this.data_list = data_list;
        }

        @Override
        public TweetFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_tweet, parent, false);
            return new TweetFragmentAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final TweetFragmentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
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
                    // "https://meydane-azadi.ir/photo/photo.php?image_name="
                } else {
                    holder.image_profile_image_fragment.setImageResource(R.drawable.icon_user);
                }


                holder.tv_time_and_date_fragment.setText(data_list.get(position).getString("time"));
                holder.tv_text_profile_fragment.setText(data_list.get(position).getString("text"));
                holder.tv_text_profile_fragment.setHashtagColor(Color.BLUE);
                holder.tv_text_profile_fragment.setMentionColor(Color.BLUE);
                holder.tv_text_profile_fragment.setOnHashtagClickListener(new Function2<SocialView, String, Unit>() {
                    @Override
                    public Unit invoke(SocialView socialView, String s) {
                        // do something
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        flag_click = false;
                        Intent i = new Intent(getActivity(), AllTweetActivity.class);
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
                        Intent i = new Intent(getActivity(), ActiviteAndShowTweeetActivity.class);
                        if (s.equals(settings.getString("username", ""))) {
                            i = new Intent(getActivity(), ProfileAndShowToitActivity.class);
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
                DefaultSliderView sliderView = new DefaultSliderView(getActivity());
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
                          //  dialog_retweet(position, data_list.get(position));
                            ((MainActivity) getActivity()).dialog_retweet(position, ad, new MainActivity.Button_dialog() {
                                @Override
                                public void onasync(String asyncc_call_back) {

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
                        Intent i = new Intent(getActivity(), ActiviteAndShowTweeetActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // i.putExtra("user_token", data_list.get(position).getString("token_repost"));
                        i.putExtra("username", data_list.get(position).getString("user_name_repost"));
                        if (data_list.get(position).getString("user_name_repost").equals(settings.getString("username", ""))) {
                            i = new Intent(getActivity(), ProfileAndShowToitActivity.class);
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
                        //go to profile
                        if (data_list.get(position).getJSONObject("user").getString("user_token").equals(settings.getString("token", ""))) {
                            Intent i = new Intent(getActivity(), ProfileAndShowToitActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(getActivity(), ActiviteAndShowTweeetActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //  i.putExtra("ad", data_list.get(position).getString("user"));
                            // i.putExtra("user_token", data_list.get(position).getJSONObject("user").getString("user_token"));
                            i.putExtra("username", data_list.get(position).getJSONObject("user").getString("username"));
                            Log.e("username_send", data_list.get(position).getJSONObject("user").getString("username"));
                            //image
                            holder.image_profile_image_fragment.buildDrawingCache();
                            Bitmap image = holder.image_profile_image_fragment.getDrawingCache();
                            Bundle extras = new Bundle();
                            extras.putParcelable("imagebitmap", image);
                            i.putExtras(extras);
                            getActivity().startActivityForResult(i, RESULT_DATA_USER_CHANGE);

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
                            PopupMenu popup = new PopupMenu(getActivity(), holder.textViewOptions);
                            popup.inflate(R.menu.option_item_tweet);
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.mnu_delete:
                                          ((MainActivity) getActivity()).dialog_delete_tweet(data_list.get(position), new MainActivity.After_delete_listener() {
                                                @Override
                                                public void delete_completed(SweetAlertDialog alertDialog) {
                                                    delete_item(data_list.get(position), position);
                                                }
                                            });
//                                            new LovelyStandardDialog(getActivity(), LovelyStandardDialog.ButtonLayout.HORIZONTAL)
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
//                                                                if(((MainActivity) getActivity()).someMethodToCallAsyncTaskInAddActivity(get_ad_list).equals("\"ok\"")){
////                                                                if (new MainActivity.send_data().execute(get_ad_list).get().equals("\"ok\"")) {
//                                                                    // Snackbar.make(view, R.string.nevise+" شما با موفقیت حذف شد", Snackbar.LENGTH_SHORT).show();
//                                                                    Toast.makeText(getActivity(), "نویسه شما با موفقیت حذف شد", Toast.LENGTH_SHORT).show();
//                                                                   // delete_item(data_list.get(position), position);
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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

    public class get_list_tweet extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(getActivity());

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
            mcrypt = new MainActivity.MCrypt();
            try {

                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("post_read_user_by_username")));
                get_ad_list.put("last_ad_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(last_ad_id))));

                //    get_ad_list.put("token", mcrypt.bytesToHex(mcrypt.encrypt(str_user_token)));
                get_ad_list.put("username", mcrypt.bytesToHex(mcrypt.encrypt(str_user_username)));
                get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));

                // get_ad_list.put("text",mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(search_key.getBytes("UTF-8"), Base64.DEFAULT))));


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));
            Log.e("data read_user", get_ad_list.toString());

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/ecxa.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    //  response = new String(mcrypt.decrypt(response)).trim();
                    Log.e("data respose", response);


                    final String finalResponse = response;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try { JSONObject job_all_data = new JSONObject(finalResponse);
                                JSONArray ad_list = job_all_data.getJSONArray("post");
                                not_found.setVisibility(View.GONE);


                                if (ad_list.length() == 0 && last_ad_id == 0) {

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


                                adapter.insert(adapter.getItemCount(), ad_list);
                                skeletonScreen.hide();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } else {


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getActivity(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            getActivity().finish();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getActivity(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.hide();
//            pd.dismiss();
        }
    }

    public class get_list_tweet_token extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(getActivity());

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
            mcrypt = new MainActivity.MCrypt();
            try {

                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("post_read_user")));
                get_ad_list.put("last_ad_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(last_ad_id))));

                get_ad_list.put("token", mcrypt.bytesToHex(mcrypt.encrypt(str_user_token)));
                //  get_ad_list.put("username", mcrypt.bytesToHex(mcrypt.encrypt(str_user_username)));
                get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));

                // get_ad_list.put("text",mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(search_key.getBytes("UTF-8"), Base64.DEFAULT))));


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));
            Log.e("data read_user", get_ad_list.toString());

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/ecxa.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    // response = new String(mcrypt.decrypt(response)).trim();
                    Log.e("data respose", response);


                    final String finalResponse = response;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject job_all_data = new JSONObject(finalResponse);
                                JSONArray  ad_list = job_all_data.getJSONArray("post");
                                not_found.setVisibility(View.GONE);

                                if (ad_list.length() == 0 && last_ad_id == 0) {

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


                                adapter.insert(adapter.getItemCount(), ad_list);
                                skeletonScreen.hide();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } else {


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getActivity(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            getActivity().finish();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getActivity(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.hide();
//            pd.dismiss();
        }
    }

    public class send_like extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(getActivity());

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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });

                } else {


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getActivity(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            // finish();

                        }
                    });


                }

                return response;
            } catch (Exception e) {
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getActivity(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
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

    public class send_retweet extends AsyncTask<JSONObject, Void, String> {
        ProgressDialog pd = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(JSONObject... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            JSONObject get_ad_list = new JSONObject();
            mcrypt = new MainActivity.MCrypt();
            try {
                get_ad_list = params[0];


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

                Log.e("data respose with code", response);
                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "").replace("\"", "");
                    response = new String(mcrypt.decrypt(response)).trim();
                    response = response.replace("\"", "");
                    Log.e("data respose", response);


                    final String finalResponse = response;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalResponse.equals("ok")) {
                                Toast.makeText(getActivity(), "اطلاعات شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                                getActivity().setResult(RESULT_OK);
                            }
                            // jarr_tag = job_all_data.getJSONArray("tag");


                        }
                    });

                } else {


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getActivity(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            getActivity().finish();

                        }
                    });


                }
                return response;

            } catch (Exception e) {
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getActivity(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        getActivity().finish();

                    }
                });
                return null;

            }


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }

    public void function_intent_show_tweet(JSONObject job, String position) {
        Intent i = new Intent(getActivity(), ShowTweetActivity.class);
        i.putExtra("ad", job.toString());
        i.putExtra("position", position);
        // startActivityForResult(i, RESULT_CODE_SHOW_TWEET);
        startActivityForResult(i, RESULT_CODE_SHOW_TWEET);
    }
}
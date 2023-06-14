package com.app.dadepardazan.meydanazadi.view_pager.post;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dadepardazan.meydanazadi.MainActivity;
import com.app.dadepardazan.meydanazadi.R;
import com.app.dadepardazan.meydanazadi.show_tweet.ShowTweetActivity;
import com.app.dadepardazan.meydanazadi.view_pager.profile.ProfileAndShowToitActivity;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;

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

public class FragmentActivite extends Fragment {

    private String title;
    private JSONObject job_data;
    SharedPreferences settings;
    RecyclerView recyclerview;
    ActiviteFragmentAdapter adapter;
    MainActivity.MCrypt mcrypt;
    JSONObject ad = new JSONObject();
    String str_android_id = "";
    private int last_ad_id = 0;
    boolean is_like = false;
    SkeletonScreen skeletonScreen;

    public static FragmentActivite newInstance(String title, String data) {

        FragmentActivite fragment = new FragmentActivite();

        Bundle args = new Bundle();

        args.putString("data", data);

        args.putString("title", title);

        fragment.setArguments(args);

        return fragment;

    }

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try {
            job_data = new JSONObject(getArguments().getString("data"));
            //Toast.makeText(getActivity(), job_data.toString(), Toast.LENGTH_SHORT).show();

            title = getArguments().getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().setTheme(R.style.NoActionBarDark);

        View view_main = inflater.inflate(R.layout.fragmant_search, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());


        recyclerview = view_main.findViewById(R.id.recyclerview_fragmant_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();
//        for (int i = 0; i < 4; i++) {
//            JSONObject a = new JSONObject();
//            data_list.add(a);
//        }
        adapter = new ActiviteFragmentAdapter(getContext(), data_list) {
            @Override
            public void load_more() {

            }
        };
       // recyclerview.setAdapter(adapter);
        skeletonScreen = Skeleton.bind(recyclerview)
                .adapter(adapter)
                .load(R.layout.item_activite)
                .show();
        str_android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        new get_list_activite().execute();
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


    public abstract class ActiviteFragmentAdapter extends RecyclerView.Adapter<ActiviteFragmentAdapter.ViewHolder> {
        ArrayList<JSONObject> data_list;
        Context context;


        public ActiviteFragmentAdapter(Context context, ArrayList<JSONObject> data_list) {
            this.context = context;
            this.data_list = data_list;
        }

        @Override
        public ActiviteFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activite, parent, false);
            return new ActiviteFragmentAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ActiviteFragmentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            try {
                int seconds = (int) ((System.currentTimeMillis() / 1000) - data_list.get(position).getInt("date"));
                String temp = "";
                if (seconds < 60) {
                    temp = seconds + "s";
                } else if (seconds >= 60 && seconds < 3600) {
                    temp = (seconds / 60) + "m";
                } else if (seconds >= 3600 && seconds < 86400) {
                    temp = (seconds / 3600) + "h";
                } else if (seconds >= 86400 && seconds < 604800) {
                    temp = (seconds / 86400) + "d";
                } else if (seconds >= 604800 && seconds < 2592000) {
                    temp = (seconds / 604800) + "w";
                } else {
                    temp = (seconds / 2592000) + "m";

                }
                String normalBOLD = data_list.get(position).getString("username");
                String normalBefore = " " + data_list.get(position).getString("text");
                String normalAfter = temp;
                String finalString = normalBOLD + normalBefore + normalAfter;
                SpannableString sb = new SpannableString(finalString);
                sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, normalBOLD.length(), 0); //bold
                sb.setSpan(new AbsoluteSizeSpan(18), normalBOLD.length() + normalBefore.length(), finalString.length(), 0);//resize size
                holder.tv_activite.setMovementMethod(LinkMovementMethod.getInstance());
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView2) {
                        try {
                            // Toast.makeText(context, "hiiiii", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getActivity(), ActiviteAndShowTweeetActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            // i.putExtra("user_token", data_list.get(position).getString("token_repost"));
                            i.putExtra("username", data_list.get(position).getString("username"));
                            if (data_list.get(position).getString("username").equals(settings.getString("username",""))){
                                i = new Intent(getActivity(), ProfileAndShowToitActivity.class);
                            }
                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //send username
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        //super.updateDrawState(' ');
                        // ds.setUnderlineText(true);
                        ds.setColor(Color.BLACK);
                    }
                };
                ClickableSpan clickableSpan2 = new ClickableSpan() {
                    @Override
                    public void onClick(View textView2) {
                        //send post_id to show post
                        try {
                            if (data_list.get(position).getString("type").equals("follow")) {
                                Intent i = new Intent(getActivity(), ActiviteAndShowTweeetActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                // i.putExtra("user_token", data_list.get(position).getString("token_repost"));
                                i.putExtra("username", data_list.get(position).getString("username"));
                                startActivity(i);

                            } else {
                                //Toast.makeText(context, "byyyyyy", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getActivity(), ShowTweetActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                // i.putExtra("user_token", data_list.get(position).getString("token_repost"));
                                i.putExtra("post_id", data_list.get(position).getString("post_id"));
                                startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        //super.updateDrawState(' ');
                        // ds.setUnderlineText(true);
                        ds.setColor(Color.BLACK);
                    }
                };

                sb.setSpan(clickableSpan, 0, normalBOLD.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.setSpan(clickableSpan2, normalBOLD.length(), finalString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                holder.tv_activite.setText(sb, TextView.BufferType.SPANNABLE);


                if (position >= getItemCount() - 1) {

                    load_more();
                }

            } catch (JSONException e) {
                e.printStackTrace();
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
            TextView tv_activite;
            LinearLayout item_layout;
            CardView cardv;

            public ViewHolder(View item) {
                super(item);
                tv_activite = item.findViewById(R.id.tv_activite);
                item_layout = item.findViewById(R.id.item_layout);
                cardv = item.findViewById(R.id.cardv);


            }
        }
    }

    public class get_list_activite extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.setCancelable(false);
            ((MainActivity)getActivity()). progress_ball.setVisibility(View.VISIBLE);
           // pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MainActivity.MCrypt();
            try {

                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("list_activity")));
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));
                // get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("token", mcrypt.bytesToHex(mcrypt.encrypt(settings.getString("token", ""))));
                get_ad_list.put("android_id", mcrypt.bytesToHex(mcrypt.encrypt(str_android_id)));
                // get_ad_list.put("text",mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(search_key.getBytes("UTF-8"), Base64.DEFAULT))));


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));
            Log.e("data sended activite", get_ad_list.toString());

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/ecxa.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    response = new String(mcrypt.decrypt(response)).trim();
                    Log.e("data respose activite", response);


                    final String finalResponse = response;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONArray ad_list = new JSONArray(finalResponse);
                                //not_found.setVisibility(View.GONE);

                                if (ad_list.length() == 0 && last_ad_id == 0) {

                                    // not_found.setVisibility(View.VISIBLE);

                                }

                                if (ad_list.length() != 0) {

                                    last_ad_id = ad_list.getJSONObject(ad_list.length() - 1).getInt("id_activity");


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
         //   pd.hide();
          //  pd.dismiss();
            ((MainActivity)getActivity()). progress_ball.setVisibility(View.GONE);
        }
    }


}
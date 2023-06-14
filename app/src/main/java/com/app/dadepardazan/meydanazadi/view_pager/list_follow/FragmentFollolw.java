package com.app.dadepardazan.meydanazadi.view_pager.list_follow;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.dadepardazan.meydanazadi.MainActivity;
import com.app.dadepardazan.meydanazadi.R;
import com.app.dadepardazan.meydanazadi.view_pager.post.ActiviteAndShowTweeetActivity;
import com.app.dadepardazan.meydanazadi.view_pager.profile.ProfileAndShowToitActivity;
import com.glide.slider.library.tricks.ViewPagerEx;
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

public class FragmentFollolw extends Fragment {

    private String title;
    SharedPreferences settings;
    RecyclerView recyclerview;
    FollowFragmentAdapter adapter;
    MainActivity.MCrypt mcrypt;
    private int last_ad_id = 0;
    TextView not_found;
    String str_android_id = "";
    String str_user_token = "";
    String str_user_username = "";
    String str_id_post_like = "";


    public static FragmentFollolw newInstance(String title, String data) {

        FragmentFollolw fragment = new FragmentFollolw();

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

        str_user_token = (getArguments().getString("data"));
        //  Log.e("username", str_user_username);


        //Toast.makeText(getActivity(), job_data.toString(), Toast.LENGTH_SHORT).show();


    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().setTheme(R.style.NoActionBarDark);

        View view_main = inflater.inflate(R.layout.fragmant_follow, container, false);
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
        adapter = new FollowFragmentAdapter(getContext(), data_list) {
            @Override
            public void load_more() {

            }
        };
        recyclerview.setAdapter(adapter);
        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) view_main.findViewById(R.id.swipe_follow);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                not_found.setVisibility(View.GONE);
                last_ad_id = 0;
                adapter.clear_list();
                new get_list_follow().execute();
                swipe.setRefreshing(false);

            }
        });

        new get_list_follow().execute();


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


    public abstract class FollowFragmentAdapter extends RecyclerView.Adapter<FollowFragmentAdapter.ViewHolder> implements
            ViewPagerEx.OnPageChangeListener {
        ArrayList<JSONObject> data_list;
        Context context;


        public FollowFragmentAdapter(Context context, ArrayList<JSONObject> data_list) {
            this.context = context;
            this.data_list = data_list;
        }

        @Override
        public FollowFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follow_fragment, parent, false);
            return new FollowFragmentAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final FollowFragmentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            try {

                if (!data_list.get(position).getString("image").equals("")) {
                    Picasso.get().load("https://meydane-azadi.ir/photo/photo.php?image_name=" + data_list.get(position).getString("image")).resize(512, 512).into(holder.image_user_follow);
                } else {
                    holder.image_user_follow.setImageResource(R.drawable.icon_user);
                }
                holder.tv_name_follow.setText(data_list.get(position).getString("name"));
                holder.tv_user_name_follow.setText(data_list.get(position).getString("username"));

                if (data_list.get(position).getString("is_follow").equals("0")) {
                    holder.btn_followed.setVisibility(View.GONE);
                    holder.btn_followering.setVisibility(View.VISIBLE);
                } else {
                    holder.btn_followed.setVisibility(View.VISIBLE);
                    holder.btn_followering.setVisibility(View.GONE);
                }
                if (data_list.get(position).getString("username").equals(settings.getString("username", ""))) {
                    holder.linear_follow_btn.setVisibility(View.GONE);

                }else {
                    holder.linear_follow_btn.setVisibility(View.VISIBLE);
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
                holder.card_follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {try {
                        Intent i = new Intent(getActivity(), ActiviteAndShowTweeetActivity.class);

                        if (data_list.get(position).getString("username").equals(settings.getString("username", ""))) {
                            i = new Intent(getActivity(), ProfileAndShowToitActivity.class);
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


        public class ViewHolder extends RecyclerView.ViewHolder {

            CardView card_follow;
            ImageView image_user_follow;
            Button btn_followed;
            Button btn_followering;
            TextView tv_name_follow;
            TextView tv_user_name_follow;
            LinearLayout linear_follow_btn;


            public ViewHolder(View item) {
                super(item);

                card_follow = item.findViewById(R.id.card_follow);
                image_user_follow = item.findViewById(R.id.image_user_follow);
                btn_followed = item.findViewById(R.id.btn_followed);
                btn_followering = item.findViewById(R.id.btn_followering);
                tv_name_follow = item.findViewById(R.id.tv_name_follow);
                tv_user_name_follow = item.findViewById(R.id.tv_user_name_follow);
                linear_follow_btn = item.findViewById(R.id.linear_follow_btn);

            }
        }
    }

    public class get_list_follow extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MainActivity.MCrypt();
            try {

                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt(title)));
                get_ad_list.put("last_ad_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(last_ad_id))));
                get_ad_list.put("token_person", mcrypt.bytesToHex(mcrypt.encrypt(str_user_token)));
                get_ad_list.put("token", mcrypt.bytesToHex(mcrypt.encrypt(((FollowActivity)getActivity()).settings.getString("token",""))));
                // get_ad_list.put("username", mcrypt.bytesToHex(mcrypt.encrypt(str_user_username)));
                // get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));

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
                    response = new String(mcrypt.decrypt(response)).trim();
                    response=response.replace("null,","");
                    Log.e("data respose", response);


                    final String finalResponse = response;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONArray ad_list = new JSONArray(finalResponse);
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
            pd.hide();
            pd.dismiss();
        }
    }

    public class send_follow extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(getActivity());

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


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getActivity(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            getActivity().finish();

                        }
                    });
                    return "error";

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
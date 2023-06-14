package com.app.dadepardazan.meydanazadi.view_pager.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.app.dadepardazan.meydanazadi.MainActivity;
import com.app.dadepardazan.meydanazadi.R;
import com.app.dadepardazan.meydanazadi.ShowHomeActivity;
import com.app.dadepardazan.meydanazadi.all_tweet.AllTweetActivity;
import com.app.dadepardazan.meydanazadi.view_pager.post.ActiviteAndShowTweeetActivity;
import com.app.dadepardazan.meydanazadi.view_pager.profile.ProfileAndShowToitActivity;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Bamdad on 7/17/2019.
 */

public class SearchActivity extends MainActivity {
    SearchAdapter2 searchAdapter;
    TabLayout mtabTabLayout;
    TextView toolbar_title;
    static SharedPreferences settings;
    ImageView imageview_icon_search;
    String search_key = "";
    SearchView searchview;
    JSONArray jarr_person = new JSONArray();
    JSONArray jarr_hashtag = new JSONArray();
    ViewPager vpPager;
     static SearchFragmentAdapter adapter_item_search_person;
     static SearchFragmentAdapter adapter_item_search_tag;
    boolean is_tag;

    @Override

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        // setContentView(R.layout.listbuy_and_hestory);
        FrameLayout content_frame;

        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.search_activity, content_frame);
//        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
//        toolbar_title.setText("میدان آزادی");
//        toolbar_title.setTextSize(24);
//        toolbar_title.setVisibility(View.VISIBLE);
        imageview_icon_search = findViewById(R.id.imageview_icon_search);


        imageview_icon_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCrypt mcrypt = new MainActivity.MCrypt();
                try {
                    // Toast.makeText(SearchActivity.this,mcrypt.bytesToHex(mcrypt.encrypt("سلاااام")), Toast.LENGTH_SHORT).show();
                    new get_list_search().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        searchview = findViewById(R.id.search_view);
        TextView searchtext = (TextView) searchview.findViewById(R.id.search_src_text);

        searchview.setQueryHint("جستجو....");
        searchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                search_key = query;
//                adapter.clear_list();
                new get_list_search().execute();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                search_key = newText;
                if (newText.length()>1){
                    new get_list_search().execute();
                }
                if (newText.equals("")) {
                    search_key = "";
                    adapter_item_search_person.clear_list();
                    adapter_item_search_tag.clear_list();
                    new get_list_search().execute();

                }

                return false;
            }
        });


        vpPager = (ViewPager) findViewById(R.id.vpPager_search);
        mtabTabLayout = (TabLayout) findViewById(R.id.tablayout_search);
        mtabTabLayout.setupWithViewPager(vpPager);

        // mtabTabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#00000")));
        //mtabTabLayout.setTabTextColors(getResources().getColor(R.color.color_hint_white), getResources().getColor(R.color.colorblack));
//
        searchAdapter = new SearchAdapter2(getSupportFragmentManager());
        vpPager.setAdapter(searchAdapter);
        vpPager.setPageTransformer(true, new RotateUpTransformer());
        if (vpPager.getCurrentItem() == 0) {
            //     navigationView.getMenu().findItem(R.id.mnu_category).setChecked(true);
        } else if (vpPager.getCurrentItem() == 1) {
            //    navigationView.getMenu().findItem(R.id.mnu_category).setChecked(true);
        }

        //برای تغییر صفحه با کد
        vpPager.setCurrentItem(1);
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
//    }

    public abstract class SearchFragmentAdapter2 extends RecyclerView.Adapter<SearchFragmentAdapter2.ViewHolder> {
        ArrayList<JSONObject> data_list;
        Context context;

        public SearchFragmentAdapter2(Context context, ArrayList<JSONObject> data_list) {
            this.context = context;
            this.data_list = data_list;
        }

        @Override
        public SearchFragmentAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_fragment, parent, false);
            return new SearchFragmentAdapter2.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchFragmentAdapter2.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            try {
                holder.tv_name_user_search.setText(data_list.get(position).getString("name"));
                holder.tv_bio_user_search.setText(data_list.get(position).getString("bio"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (!data_list.get(position).getString("image1").equals("")) {

                    Picasso.get().load(data_list.get(position).getString("image1")).resize(128, 128).into(holder.image_user_search);


                } else {

                    holder.image_user_search.setImageResource(R.drawable.no_photo);


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            //  holder.ad_price.setText(data_list.get(position).ad_price);
            //  holder.ad_location.setText(data_list.get(position).ad_location);
            //  Picasso.with(context).load(data_list.get(position).ad_image).resize(128,128).into(holder.img);


            if (position >= getItemCount() - 1) {

                load_more();
            }


            holder.card_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(context, ShowHomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        i.putExtra("ad", data_list.get(position).toString());

                        i.putExtra("username", data_list.get(position).getString("username"));

                        context.startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

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
            TextView tv_name_user_search;
            TextView tv_bio_user_search;
            ImageView image_user_search;
            LinearLayout card_search;


            public ViewHolder(View item) {
                super(item);
                tv_name_user_search = (TextView) item.findViewById(R.id.tv_name_user_search);
                tv_bio_user_search = (TextView) item.findViewById(R.id.tv_bio_user_search);
                //ad_location = (TextView) item.findViewById(R.id.ad_location);
                image_user_search = (ImageView) item.findViewById(R.id.image_user_search);
                card_search = (LinearLayout) item.findViewById(R.id.card_search);

            }
        }
    }

    public class get_list_search extends AsyncTask<Void, Void, String> {
      //  ProgressDialog pd = new ProgressDialog(SearchActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pd.setMessage("در حال دریافت اطلاعات");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MainActivity.MCrypt();
            try {

                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("search_in")));
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));
                get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("type", mcrypt.bytesToHex(mcrypt.encrypt("1")));
                get_ad_list.put("text", mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(search_key.getBytes("UTF-8"), Base64.DEFAULT))));


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
                    Log.e("data respose", response);


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject job_all_data = new JSONObject(finalResponse);

                                jarr_person = job_all_data.getJSONArray("person");
                                jarr_hashtag = job_all_data.getJSONArray("tag");
                                adapter_item_search_person.clear_list();
                                adapter_item_search_person.insert(adapter_item_search_person.getItemCount(), jarr_person);
                                adapter_item_search_tag.clear_list();
                                adapter_item_search_tag.insert(adapter_item_search_tag.getItemCount(), jarr_hashtag);


                                Log.e("jarr_person0:", jarr_person.toString());
                                // jarr_tag = job_all_data.getJSONArray("tag");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(SearchActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(SearchActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
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
//            pd.dismiss();
        }
    }

    public class SearchAdapter2 extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

        public SearchAdapter2(FragmentManager fragmentManager) {

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

                    return FragmentSearch2.newInstance("tag", R.drawable.logo);

                case 1:
                    Log.e("jarr_person:", jarr_person.toString());
                    return FragmentSearch2.newInstance("person", R.drawable.logo);

                case 2:

                    return FragmentSearch.newInstance("near", R.drawable.logo);
                case 3:

                    return FragmentSearch.newInstance("near", R.drawable.logo);


                default:

                    return null;

            }

        }

        // Returns the page title for the top indicator

        @Override

        public CharSequence getPageTitle(int position) {

            if (position == 0)
                return "تگ ها";
            if (position == 1)
                return "افراد";
            else
                return "";


        }

    }


    @SuppressLint("ValidFragment")
    public static class FragmentSearch2 extends Fragment {

        private String title = "";
        private int image;
        SharedPreferences settings;
        RecyclerView recyclerview_fragmant_search;

        MainActivity.MCrypt mcrypt;


        public static FragmentSearch2 newInstance(String title, int resImage) {

            FragmentSearch2 fragment = new FragmentSearch2();

            Bundle args = new Bundle();

            args.putInt("image", resImage);

            args.putString("title", title);

            fragment.setArguments(args);

            return fragment;

        }

        @Override

        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            image = getArguments().getInt("image", 0);

            title = getArguments().getString("title");

        }

        @Override

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //getActivity().setTheme(R.style.NoActionBarDark);

            View view_main = inflater.inflate(R.layout.fragmant_search, container, false);
            settings = PreferenceManager.getDefaultSharedPreferences(getActivity());


            recyclerview_fragmant_search = view_main.findViewById(R.id.recyclerview_fragmant_search);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerview_fragmant_search.setLayoutManager(layoutManager);

            final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();
//            for (int i = 0; i < 4; i++) {
//                JSONObject a = new JSONObject();
//                data_list.add(a);
//            }
            if (title.equals("person")) {
                adapter_item_search_person = new SearchFragmentAdapter(getContext(), data_list,false) {
                    @Override
                    public void load_more() {

                    }
                };

                recyclerview_fragmant_search.setAdapter(adapter_item_search_person);
            } else {
                adapter_item_search_tag = new SearchFragmentAdapter(getContext(), data_list,true) {
                    @Override
                    public void load_more() {

                    }
                };

                recyclerview_fragmant_search.setAdapter(adapter_item_search_tag);
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


    }

    public abstract static class FtagmentAdapter extends RecyclerView.Adapter<FtagmentAdapter.ViewHolder> {
        ArrayList<JSONObject> data_list;
        Context context;
        boolean is_tag;


        public FtagmentAdapter(Context context, ArrayList<JSONObject> data_list, boolean is_tag) {
            this.context = context;
            this.data_list = data_list;
            this.is_tag = is_tag;

        }

        @Override
        public FtagmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_tweet, parent, false);
            return new FtagmentAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final FtagmentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            try {
                if (is_tag) {
                    holder.image_user_search.setImageResource(R.drawable.icon_hashtag);
                    holder.tv_name_user_search.setText(data_list.get(position).getString("name"));
                    //  holder.tv_bio_user_search.setText(data_list.get(position).getString("number")+"پست");
                    holder.tv_bio_user_search.setText("");
                } else {
                    holder.tv_name_user_search.setText(data_list.get(position).getString("name"));
                    holder.tv_bio_user_search.setText(data_list.get(position).getString("username"));
                    if (!data_list.get(position).getString("image_address").equals("")) {
                        Picasso.get().load(data_list.get(position).getString("image_address")).resize(512, 512).into(holder.image_user_search);
                        //  https://meydane-azadi.ir/photo/photo.php?image_name="+
                        //  Picasso.with(context).load(data_list.get(position).getString("image_address")).resize(512, 512).into(holder.image_user_search);
                    } else {

                        holder.image_user_search.setImageResource(R.drawable.icon_user);


                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (position >= getItemCount() - 1) {

                load_more();
            }


            holder.card_search.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        if (is_tag) {
                            Intent i = new Intent(context, AllTweetActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("hashtag", data_list.get(position).getString("name"));
                            context.startActivity(i);

                        } else {
                            Log.e("data general:", data_list.get(position).toString());
                            Intent i = new Intent(context, ActiviteAndShowTweeetActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("ad", data_list.get(position).toString());
                            i.putExtra("user_token", data_list.get(position).getString("user_token"));
                            i.putExtra("username", data_list.get(position).getString("username"));
                            ////
                            holder.image_user_search.buildDrawingCache();
                            Bitmap image = holder.image_user_search.getDrawingCache();
                            Bundle extras = new Bundle();
                            extras.putParcelable("imagebitmap", image);
                            i.putExtras(extras);
                            if (data_list.get(position).getString("user_token").equals(settings.getString("token", ""))) {

                                i = new Intent(context, ProfileAndShowToitActivity.class);
                            }

                            //                BitmapDrawable bd = ((BitmapDrawable) holder.image_user_search.getDrawable());
                            //                Bitmap bm = bd.getBitmap();
                            //                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            //                bm.compress(Bitmap.CompressFormat.PNG, 90, bao);
                            //                String image_base64 = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);
                            //
                            //                i.putExtras("image",image_base64);

                            
                            context.startActivity(i);
                        }
                    } catch (
                            JSONException e) {
                        e.printStackTrace();
                    }

                }

            });

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


        public void update_item(int pos, JSONObject job) {
            data_list.set(pos, job);
            notifyItemChanged(pos);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_name_user_search;
            TextView tv_bio_user_search;
            ImageView image_user_search;
            LinearLayout card_search;


            public ViewHolder(View item) {
                super(item);
                tv_name_user_search = (TextView) item.findViewById(R.id.tv_name_user_search);
                tv_bio_user_search = (TextView) item.findViewById(R.id.tv_bio_user_search);
                //ad_location = (TextView) item.findViewById(R.id.ad_location);
                image_user_search = (ImageView) item.findViewById(R.id.image_user_search);
                card_search = (LinearLayout) item.findViewById(R.id.card_search);


            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new get_list_search().execute();
    }
}

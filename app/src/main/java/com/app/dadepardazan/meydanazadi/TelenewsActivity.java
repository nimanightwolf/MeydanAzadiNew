package com.app.dadepardazan.meydanazadi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.dadepardazan.meydanazadi.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by Bamdad on 12/6/2017.
 */

public class TelenewsActivity extends MainActivity {



    TeleAdapter adapter;
    RecyclerView recyclerView;
    int last_ad_id = 0;
    TextView not_found;
    String search_key = "";
    TextView toolbar_title;
    TextView toolbar_text;
    private MCrypt mcrypt;
    float initialX=0.0f;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FloatingActionButton fb;

        super.onCreate(savedInstanceState);
        FrameLayout content_frame;
        toolbar.setTitle("ssssss");
        toolbar.setBackgroundColor(Color.parseColor("#dfe6e9"));

        //AdListActivity.this.setTitle("میدان ازادی");

        //setContentView(R.layout.telenews);
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.telenews, content_frame);
       // navigationView.getMenu().findItem(R.id.mnu_main).setChecked(true);
        not_found = (TextView) findViewById(R.id.not_found);
        BottomNavigationView.getMenu().findItem(R.id.navigation_today_news).setChecked(true);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_title= (TextView) findViewById(R.id.toolbar_title);
//        toolbar_text= (TextView) findViewById(R.id.toolbar_text);
//        toolbar_text.setText("تله نیوز");
       // toolbar_text.setVisibility(View.VISIBLE);
        toolbar_title.setText("تله نیوز");
        toolbar_title.setVisibility(View.VISIBLE);
        fb = (FloatingActionButton) findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (settings.getInt("user_id", 0) != 0) {


                    Intent i = new Intent(getApplicationContext(), NewAdActivity.class);
                    startActivity(i);

                } else

                {

                    Toast.makeText(getBaseContext(), "برای درج اگهی ابتدا باید وارد شوید", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);


                }


            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        SlideInLeftAnimator animator=new SlideInLeftAnimator(new OvershootInterpolator(1f));
        recyclerView.setItemAnimator(animator) ;
        recyclerView.getItemAnimator().setAddDuration(1200);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();

                        return true;
                    case MotionEvent.ACTION_MOVE:

                        return false;
                    case MotionEvent.ACTION_UP:
                        if (initialX > event.getX() && (Math.abs(initialX - event.getX()) > 100)) {
                           // Toast.makeText(getApplicationContext(),"Left to right gesture",Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        return true;


                }


                return false;
            }
        });



        final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();


        adapter = new TeleAdapter(getApplicationContext(), data_list) {
            @Override
            public void load_more() {
                if (last_ad_id != -1) {
                    new get_ad_list().execute();
                }

            }
        };
        recyclerView.setAdapter(adapter);


        new get_ad_list().execute();


        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_cnontainer);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                not_found.setVisibility(View.GONE);

                last_ad_id = 0;


                adapter.clear_list();
                new get_ad_list().execute();
                swipe.setRefreshing(false);

            }
        });

        BottomNavigationView.setOnNavigationItemSelectedListener(new com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id = item.getItemId()) {
                    case R.id.navigation_today_news:
                        //Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),TelenewsActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);


                        break;

                    case R.id.navigation_home:
                        i = new Intent(getApplicationContext(), AdListActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                        break;
                    case R.id.navigation_account:
                        if (settings.getInt("user_id", 0) != 0) {

                            i = new Intent(getApplicationContext(), ProFileUser.class);
                            startActivity(i);



                        } else {
                            i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);


                        }
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
//                    case R.id.navigation_book:
//                        // Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
//                        i = new Intent(getApplicationContext(),BookActivity.class);
//                        startActivity(i);
//
//                        break;
                }

                return true;
            }
        });


        //




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchitem = menu.findItem(R.id.action_search);
        if (searchitem != null) {

            SearchView searchview = (SearchView) MenuItemCompat.getActionView(searchitem);
            TextView searchtext = (TextView) searchview.findViewById(R.id.search_src_text);
            searchview.setQueryHint("جستجو...");

            searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {


                    search_key = query;

                    last_ad_id = 0;


                    adapter.clear_list();
                    new get_ad_list().execute();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {


                    if (newText.equals("")) {
                        search_key = "";

                        last_ad_id = 0;


                        adapter.clear_list();
                        new get_ad_list().execute();

                    }

                    return false;
                }
            });


        }

        return true;

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        BottomNavigationView.getMenu().findItem(R.id.navigation_today_news).setChecked(true);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public class get_ad_list extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(TelenewsActivity.this);

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

                get_ad_list.put("command", MCrypt.bytesToHex( mcrypt.encrypt("tele_news") ));


               // get_ad_list.put("hot","1");


                get_ad_list.put("user_id", MCrypt.bytesToHex( mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("last_ad_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(last_ad_id))));
                search_key= Base64.encodeToString(search_key.getBytes("UTF-8"), Base64.DEFAULT);
                get_ad_list.put("search_key",MCrypt.bytesToHex(mcrypt.encrypt(search_key)));
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


                                JSONArray ad_list = new JSONArray(finalResponse);


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
            pd.hide();
            pd.dismiss();
        }
    }


}

package com.app.dadepardazan.meydanazadi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by Bamdad on 12/6/2017.
 */

public class FavoriteActivity extends MainActivity  {


    DataAdapter adapter;
    RecyclerView recyclerView;
    TextView not_found;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame;

        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.favorite, content_frame);




        not_found= (TextView) findViewById(R.id.not_found);

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);

        //
//        recyclerView.setOnTouchListener(this);{
//
//        }
       //

        final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();


        adapter = new DataAdapter(getApplicationContext(), data_list) {
            @Override
            public void load_more() {

            }
        };
        recyclerView.setAdapter(adapter);



        new get_bookmark_ad_list().execute();





        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_cnontainer);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                not_found.setVisibility(View.GONE);

                adapter.clear_list();
                new get_bookmark_ad_list().execute();
                swipe.setRefreshing(false);

            }
        });






        toolbar.setTitle("علاقه مندی ها");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.help_menu,menu);

        return true;

    }
    //
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.help) {

            show_help_dialog();


        }


        return super.onOptionsItemSelected(item);

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }

    //
    public void show_help_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteActivity.this);
        builder.setMessage("شما می توانید با استفاده از ایکون ستاره ی بالای هر اگهی ان را به لیست علاقه مندی های خود اضاف کنید");
        builder.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });



        AlertDialog dialog = builder.create();
        dialog.show();


    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                initialX = event.getX();
//                initialY = event.getY();
//
//
//                return false;
//            case MotionEvent.ACTION_UP:
//                if (initialY > event.getY() && (Math.abs(initialY - event.getY()) > 200)) {
//                    Toast.makeText(this,"nshon bede1 ",Toast.LENGTH_SHORT).show();
//                    toolbar.setVisibility(View.GONE);
//
//                }
//                if (initialY < event.getY() && (Math.abs(initialY - event.getY()) > 200)) {
//                    Toast.makeText(this,"nshon bede2 ",Toast.LENGTH_SHORT).show();
//                    toolbar.setVisibility(View.VISIBLE);
//
//                }
//
//                return true;
//
//        }
//        return false;
//    }


    //









    public class get_bookmark_ad_list extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(FavoriteActivity.this);

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
            try {

                get_ad_list.put("command","get_bookmark_ad_list");

                get_ad_list.put("user_id",settings.getInt("user_id",0));

            } catch (JSONException e) {
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


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONArray ad_list = new JSONArray(finalResponse);


                                if (ad_list.length()==0)
                                {

                                    not_found.setVisibility(View.VISIBLE);

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

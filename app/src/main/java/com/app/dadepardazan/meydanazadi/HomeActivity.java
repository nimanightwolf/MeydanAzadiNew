package com.app.dadepardazan.meydanazadi;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

/**
 * Created by Bamdad on 12/6/2017.
 */

public class HomeActivity extends MainActivity {



    HomeAdapter adapter;
    RecyclerView recyclerView;
    int last_ad_id = 0;
    TextView not_found;
    String search_key = "";
    String category_filter;
    TextView toolbar_title;
    private MCrypt mcrypt;


    @Override
    protected void onResume() {
        super.onResume();
        not_found = (TextView) findViewById(R.id.not_found);
        BottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FloatingActionButton fb;

        super.onCreate(savedInstanceState);
        FrameLayout content_frame;
        toolbar.setTitle("ssssss");
        //AdListActivity.this.setTitle("میدان ازادی");

        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.home, content_frame);
       // navigationView.getMenu().findItem(R.id.mnu_main).setChecked(true);
        not_found = (TextView) findViewById(R.id.not_found);
        BottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);

        toolbar_title= (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("میدان ازادی");
        toolbar_title.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);



        final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();


        adapter = new HomeAdapter(getApplicationContext(), data_list) {
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



        //
//        if(settings.getInt("start",0)==0){
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//            builder.setTitle("استان محل زندگی خود را انتخاب کنید");
//            builder.setMessage("لطفا استان مورد نظر خود را انتخاب کنید تا فقط اگهی های همان استان به شما نشان داده شود ");
//            builder.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    final ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.province)));
//                    list.set(0, "همه ی استان ها");
//                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//                    builder.setAdapter(new ArrayAdapter<String>(HomeActivity.this, R.layout.row, R.id.mytext, list)
//                            , new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int i) {
//
//
//                                    if (!location_filter.getText().toString().equals(list.get(i))) {
//
//
//                                        location_filter.setText(list.get(i));
//
//                                        SharedPreferences.Editor editor = settings.edit();
//                                        editor.putInt("location_filter", i);
//                                        editor.putInt("start",1);
//                                        editor.commit();
//
//                                        last_ad_id = 0;
//
//
//
//                                        adapter.clear_list();
//
//                                        not_found.setVisibility(View.GONE);
//
//                                        new get_ad_list().execute();
//
//                                    }
//
//                                }
//                            });
//                    builder.show();
//
//
//
//                }
//            });
//
//            builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    SharedPreferences.Editor editor = settings.edit();
//                    editor.putInt("start",1);
//                    editor.commit();
//
//                }
//            });
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
//
//
//
//
//
//
//        }

        //



//        location_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.province)));
//                list.set(0, "همه ی استان ها");
//                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//                builder.setAdapter(new ArrayAdapter<String>(HomeActivity.this, R.layout.row, R.id.mytext, list)
//                        , new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int i) {
//
//
//                                if (!location_filter.getText().toString().equals(list.get(i))) {
//
//
//                                    location_filter.setText(list.get(i));
//
//                                    SharedPreferences.Editor editor = settings.edit();
//                                    editor.putInt("location_filter", i);
//                                    editor.commit();
//
//                                    last_ad_id = 0;
//
//
//                                    adapter.clear_list();
//
//                                    not_found.setVisibility(View.GONE);
//
//                                    new get_ad_list().execute();
//
//                                }
//
//                            }
//                        });
//                builder.show();
//            }
//        });
//        category_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.category)));
//                list.set(0, "همه ی گروه ها");
//                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//                builder.setAdapter(new ArrayAdapter<String>(HomeActivity.this, R.layout.row, R.id.mytext, list)
//                        , new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int i) {
//
//                                if (!category_filter.getText().toString().equals(list.get(i))) {
//
//                                    category_filter.setText(list.get(i));
//
//                                    SharedPreferences.Editor editor = settings.edit();
//                                    editor.putInt("category_filter", i);
//                                    editor.commit();
//
//                                    last_ad_id = 0;
//
//
//                                    adapter.clear_list();
//
//                                    not_found.setVisibility(View.GONE);
//
//                                    new get_ad_list().execute();
//                                }
//
//
//                            }
//                        });
//                builder.show();
//            }
//        });


    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        BottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);

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



    public void filter_ostan_dialog() {



    }





    public class get_ad_list extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(HomeActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            //pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex( mcrypt.encrypt("news_home")));


                //get_ad_list.put("location","");
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));

                get_ad_list.put("user_id", MCrypt.bytesToHex( mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));

                get_ad_list.put("last_ad_id",MCrypt.bytesToHex( mcrypt.encrypt( String.valueOf(last_ad_id))));
                search_key= Base64.encodeToString(search_key.getBytes("UTF-8"), Base64.DEFAULT);
                get_ad_list.put("search_key", MCrypt.bytesToHex( mcrypt.encrypt(search_key)));
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

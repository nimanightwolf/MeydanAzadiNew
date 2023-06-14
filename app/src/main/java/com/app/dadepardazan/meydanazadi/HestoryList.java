package com.app.dadepardazan.meydanazadi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by Bamdad on 12/6/2017.
 */

public class HestoryList extends MainActivity  {


    HestorylistDataAdapter2 adapter;
    RecyclerView recyclerView;
    TextView not_found;
    TextView toolbar_title;
    private MCrypt mcrypt;
    JSONArray ad_list;
    int id_order_int=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame;
        toolbar.setTitle("ssssss");
        toolbar.setBackgroundColor(Color.parseColor("#dfe6e9"));

        //AdListActivity.this.setTitle("میدان ازادی");

        //setContentView(R.layout.telenews);
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.hestory_list, content_frame);
        BottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);


        Intent in = getIntent();
        Uri data = in.getData();
        if (data != null) {
            String rdata = data.toString().replace("myapp://com.app.dadepardazan.MeydanAzadi:", "");

            if (rdata.equals("1")) {
                Toast.makeText(getBaseContext(), "پرداخت موفقیت امیز بود", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "پرداخت نا موفق بود", Toast.LENGTH_LONG).show();
            }
        }


        not_found= (TextView) findViewById(R.id.not_found);

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        SlideInLeftAnimator animator=new SlideInLeftAnimator(new OvershootInterpolator(1f));
        recyclerView.setItemAnimator(animator) ;
        recyclerView.getItemAnimator().setAddDuration(1200);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);

        //
//        recyclerView.setOnTouchListener(this);{
//
//        }
       //
        adapter=new HestorylistDataAdapter2(getApplicationContext(),new ArrayList<JSONObject>() );
        recyclerView.setAdapter(adapter);

        new get_hestory().execute();





        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_cnontainer);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                not_found.setVisibility(View.GONE);

                new get_hestory().execute();

                swipe.setRefreshing(false);

            }
        });






        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Drawable arrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        // getSupportActionBar().setHomeAsUpIndicator(arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HestoryList.this.finish();
            }
        });
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }

    //
    public  void show_help_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HestoryList.this);
        builder.setMessage("شما می توانید با استفاده از ایکون ستاره ی بالای هر اگهی ان را به لیست علاقه مندی های خود اضاف کنید");
        builder.setPositiveButton("باشه متوجه شدم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });



        AlertDialog dialog = builder.create();
        //baray zamani k mikhahim dialog bejoz click roy butten az bin naravad
    //    dialog.setCancelable(false);
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


    @Override
    protected void onResume() {
        super.onResume();
        new get_hestory().execute();
    }

    public  class get_hestory extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(HestoryList.this);

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

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("get_history")));


                //get_ad_list.put("location","");
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));

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
                final String finalResponse1 = response;

                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    response = new String(mcrypt.decrypt(response)).trim();
                    final String finalResponse = response;


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                ad_list = new JSONArray(finalResponse);


                                       // Toast.makeText(getApplicationContext(), ad_list.toString(),Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();





                                if (ad_list.length()==0)
                                {

                                    not_found.setVisibility(View.VISIBLE);

                                }else{
                                    not_found.setVisibility(View.GONE);
                                }

//                                final ArrayList<JSONObject> data_list2 = new ArrayList<JSONObject>();
//                                try {
//                                    for (int i = 0; i < ad_list.length(); i++) {
//
//
//
//                                        data_list2.add(ad_list.getJSONObject(i));
//
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                                adapter.addHestoryList(ad_list);
                               // adapter = new HestorylistDataAdapter2(getApplicationContext(), data_list2);
                               // recyclerView.setAdapter(adapter);



                               // Toast.makeText(getApplicationContext(),ad_list.toString(),Toast.LENGTH_SHORT).show();
                                //adapter.insert(adapter.getItemCount(), ad_list);

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


    public class HestorylistDataAdapter2 extends RecyclerView.Adapter<com.app.dadepardazan.meydanazadi.HestorylistDataAdapter.BookViewHolder> {
        Context context = null;
        ArrayList<JSONObject> data_list = null;
        public static final String ACTIVITY_PACKING_RESULT_CODE = "result_code";


        public HestorylistDataAdapter2(Context context, ArrayList<JSONObject> data_list) {
            this.context = context;
            this.data_list = data_list;
        }

        @Override
        public com.app.dadepardazan.meydanazadi.HestorylistDataAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // View item = LayoutInflater.from(context).inflate(R.layout.item_hess, null);
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hess, parent, false);
            TextView hes_date;
            TextView time_ago;
            TextView hes_status;
            TextView hes_adress;
            TextView hes_price;
            TextView hes_sefarsh;
            TextView hes_shomare_tamas;
            TextView hes_name_book;
            CardView cardv;
            LinearLayout item_layout=(LinearLayout) item.findViewById(R.id.item_layout);
            hes_date = (TextView) item.findViewById(R.id.hes_date);
            hes_status = (TextView) item.findViewById(R.id.hes_status);
            hes_adress = (TextView) item.findViewById(R.id.hes_adress);
            hes_price = (TextView) item.findViewById(R.id.hes_price);
            time_ago = (TextView) item.findViewById(R.id.time_ago);
            hes_sefarsh = (TextView) item.findViewById(R.id.hes_sefarsh);
            hes_shomare_tamas = (TextView) item.findViewById(R.id.hes_shomare_tamas);
            hes_name_book = (TextView) item.findViewById(R.id.hes_name_book);
            cardv = (CardView) item.findViewById(R.id.cardv_hestory);
            com.app.dadepardazan.meydanazadi.HestorylistDataAdapter.BookViewHolder viewHolder = new com.app.dadepardazan.meydanazadi.HestorylistDataAdapter.BookViewHolder(item);
            viewHolder.hes_date=hes_date;
            viewHolder.hes_status = hes_status;
            viewHolder.time_ago = time_ago;
            viewHolder.hes_adress=hes_adress;
            viewHolder.hes_price=hes_price;
            viewHolder.hes_sefarsh=hes_sefarsh;
            viewHolder.hes_shomare_tamas=hes_shomare_tamas;
            viewHolder.hes_name_book=hes_name_book;
            viewHolder.cardv=cardv;
            viewHolder.item_layout=item_layout;


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final com.app.dadepardazan.meydanazadi.HestorylistDataAdapter.BookViewHolder holder, final int position) {
            //BookEntity bookItem = daro.get(position);
            try {
                int statous=data_list.get(position).getInt("status");
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
                holder.hes_date.setText("تاریخ: " + data_list.get(position).getString("date_show"));
                holder.time_ago.setText(temp);
                if (data_list.get(position).getString("number_order").toString().trim().equals("null")) {
                    holder.hes_sefarsh.setVisibility(View.GONE);
                } else {
                    holder.hes_sefarsh.setVisibility(View.VISIBLE);
                    holder.hes_sefarsh.setText("شماره سفارش: " + data_list.get(position).getString("number_order"));
                }


                holder.hes_status.setText("وضعیت: " + data_list.get(position).getString("status_text"));
                if (data_list.get(position).getString("adress").trim().isEmpty()) {
                    holder.hes_adress.setVisibility(View.GONE);
                } else {
                    holder.hes_adress.setVisibility(View.VISIBLE);
                    holder.hes_adress.setText("ادرس: " + data_list.get(position).getString("adress"));

                }
                NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
                String price_string_formatag = (numberformat.format(data_list.get(position).getInt("price_final"))) + "ریال";
                holder.hes_price.setText("قیمت: " + price_string_formatag);

                //holder.hes_price.setText("قیمت: "+ data_list.get(position).getString("price"));
                if (data_list.get(position).getInt("status") == 0) {
//
                    holder.cardv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext(), R.style.DialogeTheme);
                            alertbox.setMessage("ایا می خواهید کتاب را خریداری کنید");
                            alertbox.setTitle("پرداخت مبلغ کتاب");
                            //alertbox.setIcon(R.drawable.trn_03);

                            alertbox.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = null;
                                    try {
                                        url = "http://meydane-azadi.ir/paying/pay.php?id_order=" + data_list.get(position).getString("number_order");

                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        context.startActivity(i);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            alertbox.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    new get_hestory().execute();

                                }
                            });
                            alertbox.setNeutralButton("لغو خرید", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        id_order_int=data_list.get(position).getInt("id_order");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                   // Toast.makeText(getApplicationContext(),String.valueOf(id_order_int),Toast.LENGTH_LONG).show();

                                    new send_delete_shop().execute();

                                }
                            });

                            alertbox.show();
                        }
                    });
                }

//                 if (statous==0) {
//                    // holder.relative_item.setBackgroundColor(Color.BLUE);
//                     Toast.makeText(getApplicationContext(),"sjkahkfj",Toast.LENGTH_LONG).show();
//                }
                holder.hes_name_book.setText("نام کتاب: " + data_list.get(position).getString("name_book"));

                if (data_list.get(position).getString("telephone").trim().isEmpty()) {
                    holder.hes_shomare_tamas.setVisibility(View.GONE);
                } else {
                    holder.hes_shomare_tamas.setVisibility(View.VISIBLE);
                    holder.hes_shomare_tamas.setText("شماره تماس: " + data_list.get(position).getString("telephone"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }






        }
        public void addHestoryList( JSONArray ad_list) {

            data_list.clear();

            try {


                for (int i = 0; i < ad_list.length(); i++) {


                    data_list.add(ad_list.getJSONObject(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            notifyItemInserted(data_list.size());
//            notifyDataSetChanged();


        }
        @Override
        public int getItemCount() {
            return data_list.size();
        }

        public class BookViewHolder extends RecyclerView.ViewHolder {
            TextView hes_date;
            TextView time_ago;
            TextView hes_status;
            TextView hes_adress;
            TextView hes_price;
            TextView hes_sefarsh;
            TextView hes_shomare_tamas;
            TextView hes_name_book;
            CardView cardv;
            LinearLayout item_layout;

            public BookViewHolder(View itemView) {
                super(itemView);

            }
        }


    }



    public  class send_delete_shop extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(HestoryList.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
           // pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("del_shoping")));
                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("id_order", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(id_order_int))));


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

                    response = response.replace("<azadi>", "").replace("</azadi>","").replace("\"ok\"", "ok");
                    response = new String(mcrypt.decrypt(response)).trim();
                    response = response.replace("\"", "");
                    final String finalResponse = response;


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //    ad_list = new JSONArray(finalResponse);


                         //   Toast.makeText(getApplicationContext(),finalResponse,Toast.LENGTH_SHORT).show();
                            if (finalResponse.trim().equals("ok")){
                                new get_hestory().execute();
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
//            pd.hide();
//            pd.dismiss();
        }
    }

}

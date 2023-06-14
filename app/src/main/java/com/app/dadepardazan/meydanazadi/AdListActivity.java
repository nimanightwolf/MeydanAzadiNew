package com.app.dadepardazan.meydanazadi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

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
import java.util.Arrays;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by Bamdad on 12/6/2017.
 */

public class AdListActivity extends MainActivity {


    HomeAdapter adapter;
    CategoryRecyclerViewAdapter adapter_cat;
    RecyclerView recycler_cat = null;
    RecyclerView recyclerView;
    int last_ad_id = 0;
    TextView not_found;
    String search_key = "";
    String category_filter = "همه دسته ها";
    TextView toolbar_title;
    TextView location_filter;
    String str_location_filter2 = "";
    JSONArray jsonArray_category;
    int c = 2;
    private MCrypt mcrypt;
    int temp_update = 1;
    int i = 0;
    Handler handler;
    String a = "";
    ImageView imageView_left_recicler;
    ImageView imageView_right_recicler;
    float initialX = 0.0f;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FloatingActionButton fb;

        super.onCreate(savedInstanceState);
        FrameLayout content_frame;
        toolbar.setTitle("ssssss");
        //AdListActivity.this.setTitle("میدان ازادی");

        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.ad_list, content_frame);
        // navigationView.getMenu().findItem(R.id.mnu_main).setChecked(true);
        not_found = (TextView) findViewById(R.id.not_found);
        ImageView img_toolbar_with_less_margin = findViewById(R.id.img_toolbar_with_less_margin);
        img_toolbar_with_less_margin.setVisibility(View.VISIBLE);
        img_toolbar.setVisibility(View.GONE);
        BottomNavigationView.getMenu().findItem(R.id.navigation_today_news).setChecked(true);
        imageView_left_recicler = findViewById(R.id.imageView_left_recicler);
        imageView_right_recicler = findViewById(R.id.imageView_right_recicler);


//        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
//        toolbar_title.setText("میدان آزادی");
//        toolbar_title.setVisibility(View.VISIBLE);

        location_filter = (TextView) findViewById(R.id.location_filter);
        location_filter.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_action_open_spinner), null, null, null);


//        TutoShowcase.from(this)
//                .setContentView(R.layout.tuto_sample)
//
//                .on(content_frame)
//                .displayScrollable()
//
//                .show();


        // List<String> myList = new ArrayList<String>(Arrays.asList(a.split(",")));


        location_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), String.valueOf( Arrays.asList(getResources().getStringArray(R.array.province))),Toast.LENGTH_SHORT).show();
                final ArrayList<String> list = new ArrayList<String>(Arrays.asList(a.split(",")));

                //list.set(0, "همه ی استان ها");
//                AlertDialog.Builder builder = new AlertDialog.Builder(AdListActivity.this);
//                builder.setAdapter(new ArrayAdapter<String>(AdListActivity.this, R.layout.row, R.id.mytext, list)
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
//                                    str_location_filter2 = location_filter.getText().toString();
//                                    new get_ad_list().execute();
//
//                                }
//
//                            }
//                        });
//                builder.show();
                DialogPlus dialog = DialogPlus.newDialog(AdListActivity.this)
                        .setAdapter(new ArrayAdapter<String>(AdListActivity.this, R.layout.row, R.id.mytext, list))
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                if (!location_filter.getText().toString().equals(list.get(position))) {
                                    location_filter.setText(list.get(position));
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putInt("location_filter", position);
                                    editor.commit();
                                    last_ad_id = 0;

                                    adapter.clear_list();
                                    not_found.setVisibility(View.GONE);
                                    str_location_filter2 = location_filter.getText().toString();
                                    new get_ad_list().execute();
//
                                }
                                dialog.dismiss();

                            }
                        })
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .setHeader(R.layout.header_dialog_pluse)
                        .create();


                dialog.show();
            }
        });

        if (settings.getInt("start", 0) == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(AdListActivity.this, R.style.DialogeTheme);
            builder.setTitle("استان محل زندگی خود را انتخاب کنید");
            builder.setMessage("لطفا استان مورد نظر خود را انتخاب کنید تا فقط اگهی های همان استان به شما نشان داده شود ");
            builder.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ArrayList<String> list = new ArrayList<String>(Arrays.asList(a.split(",")));
                    // list.set(0, "همه ی استان ها");
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdListActivity.this);
                    builder.setAdapter(new ArrayAdapter<String>(AdListActivity.this, R.layout.row, R.id.mytext, list)
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    if (!location_filter.getText().toString().equals(list.get(i))) {

                                        location_filter.setText(list.get(i));

                                        // location_filter.setText(list.get(i));
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putInt("location_filter", i);
                                        editor.putInt("start", 1);
                                        editor.commit();
                                        last_ad_id = 0;
                                        adapter.clear_list();
                                        not_found.setVisibility(View.GONE);

                                        str_location_filter2 = location_filter.getText().toString();
                                        new get_ad_list().execute();
                                    }
                                }
                            });
                    builder.show();
                }
            });

            builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("start", 1);
                    editor.commit();

                }
            });
            AlertDialog dialog = builder.create();
//            dialog.show();
        }


//        category_filter = new String(getIntent().getStringExtra(PackingActivity.ACTIVITY_PACKING_RESULT_CODE));
//        if (category_filter.trim().isEmpty()) {
        category_filter = "همه دسته ها";
//        }
//        Toast.makeText(getApplicationContext(), category_filter, Toast.LENGTH_SHORT).show();

        recycler_cat = (RecyclerView) findViewById(R.id.recyclerview_cat);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        // gridLayoutManager.setSpanCount(2);
        // StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        // StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        recycler_cat.setLayoutManager(gridLayoutManager);
        recycler_cat.setHasFixedSize(true);


        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        SlideInLeftAnimator animator = new SlideInLeftAnimator(new OvershootInterpolator(1f));
        recyclerView.setItemAnimator(animator);
        recyclerView.getItemAnimator().setAddDuration(1200);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);


//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                //  Toast.makeText(AdListActivity.this, dx+"_"+dy, Toast.LENGTH_SHORT).show();
//                if (dy > 0) {
//                    fb_send_new_tweet.hide();
//                } else {
//                    fb_send_new_tweet.show();
//                }
//            }
//        });
//

//        recyclerView.setOnTouchListener(new OnSwipeTouchListener(AdListActivity.this) {
//
//            public void onSwipeRight() {
//
//                // Toast.makeText(getApplicationContext(),"left to right  gesture",Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(getApplicationContext(), TelenewsActivity.class);
//                startActivity(i);
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//            }
//
//            public void onSwipeLeft() {
//                //Toast.makeText(getApplicationContext(), "right to left gesture", Toast.LENGTH_SHORT).show();
//                if (settings.getInt("user_id", 0) != 0) {
//
//                    Intent i = new Intent(getApplicationContext(), ProFileUser.class);
//                    startActivity(i);
//
//
//                } else {
//                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                    startActivity(i);
//
//
//                }
//                //startActivity(i);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//
//
//        });
        new MaterialShowcaseView.Builder(this)
                .setTarget(recycler_cat)
                .setDismissText("متوجه شدم")
                .setContentText("از این قسمت شما می توانید فیلتر اخبار خود را انتخاب کنید ")
                .setDelay(200) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("nima2") // provide a unique ID used to ensure it is only shown once
                .show();
//        ShowcaseConfig config = new ShowcaseConfig();
//        config.setDelay(500); // half second between each showcase view
//
//        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "SHOWCASE_ID");
//
//        sequence.setConfig(config);
//
//        sequence.addSequenceItem(mButtonOne,
//                "This is button one", "GOT IT");
//
//        sequence.addSequenceItem(mButtonTwo,
//                "This is button two", "GOT IT");
//
//        sequence.addSequenceItem(mButtonThree,
//                "This is button three", "GOT IT");
//
//        sequence.start();
//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        initialX = event.getX();
//
//                        return false;
//
//                    case MotionEvent.ACTION_MOVE:
//
//                        return false;
//                    case MotionEvent.ACTION_UP:
//                        if (initialX < event.getX() && (Math.abs(initialX - event.getX()) > 200)) {
//                             Toast.makeText(getApplicationContext(),"left to right  gesture",Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(getApplicationContext(), TelenewsActivity.class);
//                            startActivity(i);
//                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//
//
//                        }
//                        if (initialX > event.getX() && (Math.abs(initialX - event.getX()) > 200)) {
//                            Toast.makeText(getApplicationContext(), "right to left gesture", Toast.LENGTH_SHORT).show();
//                            if (settings.getInt("user_id", 0) != 0) {
//
//                                Intent i = new Intent(getApplicationContext(), ProFileUser.class);
//                                startActivity(i);
//
//
//                            } else {
//                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                                startActivity(i);
//
//
//                            }
//                            //startActivity(i);
//                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//                            break;
//                        }
//
//
//                }
//
//
//                return false;
//            }
//        });
//        final ArrayList<JSONObject> data_list2 = new ArrayList<JSONObject>();
//        adapter_cat = new CategoryRecyclerViewAdapter(getApplicationContext(), data_list2){
//            @Override
//            public void onBindViewHolder(BookViewHolder holder, final int position) {
//                super.onBindViewHolder(holder, position);
//                holder.liner_item.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        try {
//                            category_filter=daro.get(position).getString("title");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        Toast.makeText(getApplicationContext(),category_filter,Toast.LENGTH_SHORT).show();
//                        temp_update=2;
//                          new get_ad_list().execute();
//                    }
//                });
//            }
//        };
//
//        recycler_cat.setAdapter(adapter_cat);


        final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();


        adapter = new HomeAdapter(getApplicationContext(), data_list) {
            @Override
            public void load_more() {
                if (last_ad_id != -1) {

                    str_location_filter2 = location_filter.getText().toString();
                    new get_ad_list().execute();
                }

            }
        };
        recyclerView.setAdapter(adapter);

        str_location_filter2 = location_filter.getText().toString();
        new get_ad_list().execute();
        adapter_cat = new CategoryRecyclerViewAdapter(getApplicationContext(), data_list) {
            @Override
            public void onBindViewHolder(final BookViewHolder holder, final int position) {
                super.onBindViewHolder(holder, position);
                holder.liner_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            category_filter = daro.get(position).getString("title");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), category_filter, Toast.LENGTH_SHORT).show();
                        str_location_filter2 = location_filter.getText().toString();
                        new get_ad_list().execute();
                    }
                });
            }
        };

        recycler_cat.setAdapter(adapter_cat);
//        BottomNavigationView.setOnNavigationItemSelectedListener(new com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                switch (id = item.getItemId()) {
//                    case R.id.navigation_today_news:
//                        //Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(getApplicationContext(), TelenewsActivity.class);
//                        startActivity(i);
//                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//
//
//                        break;
//
//                    case R.id.navigation_home:
//                        i = new Intent(getApplicationContext(), AdListActivity.class);
//                        startActivity(i);
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//                        break;
//                    case R.id.navigation_account:
//                        if (settings.getInt("user_id", 0) != 0) {
//
//                            i = new Intent(getApplicationContext(), ProFileUser.class);
//                            startActivity(i);
//
//
//                        } else {
//                            i = new Intent(getApplicationContext(), LoginActivity.class);
//                            startActivity(i);
//
//
//                        }
//                        startActivity(i);
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                        break;
////                    case R.id.navigation_book:
////                        // Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
////                        i = new Intent(getApplicationContext(),BookActivity.class);
////                        startActivity(i);
////
////                        break;
//                }
//
//                return true;
//            }
//        });

        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_cnontainer);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                not_found.setVisibility(View.GONE);

                last_ad_id = 0;


                adapter.clear_list();

                str_location_filter2 = location_filter.getText().toString();
                new get_ad_list().execute();
                swipe.setRefreshing(false);


//                JSONArray jason_for_search = new JSONArray();
//
//
//                for (int i = 0; i < jsonArray_category.length(); i++) {
//
//                    try {
//                        JSONObject obj = jsonArray_category.getJSONObject(i);
//
//                        if ((obj.getString("title").indexOf(category_filter) != -1)) {
//
//                            jason_for_search.put(obj);
//                            c = obj.getInt("id");
//
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }

                //Toast.makeText(getApplicationContext(), String.valueOf(c), Toast.LENGTH_SHORT).show();

            }
        });


        //
//        if(settings.getInt("start",0)==0){
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(AdListActivity.this);
//            builder.setTitle("استان محل زندگی خود را انتخاب کنید");
//            builder.setMessage("لطفا استان مورد نظر خود را انتخاب کنید تا فقط اگهی های همان استان به شما نشان داده شود ");
//            builder.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    final ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.province)));
//                    list.set(0, "همه ی استان ها");
//                    AlertDialog.Builder builder = new AlertDialog.Builder(AdListActivity.this);
//                    builder.setAdapter(new ArrayAdapter<String>(AdListActivity.this, R.layout.row, R.id.mytext, list)
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


//        }

        //


//        location_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final ArrayList<String> list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.province)));
//                list.set(0, "همه ی استان ها");
//                AlertDialog.Builder builder = new AlertDialog.Builder(AdListActivity.this);
//                builder.setAdapter(new ArrayAdapter<String>(AdListActivity.this, R.layout.row, R.id.mytext, list)
//                        , new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int i) {
//                                if (!location_filter.getText().toString().equals(list.get(i))) {
//                                    location_filter.setText(list.get(i));
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
//                AlertDialog.Builder builder = new AlertDialog.Builder(AdListActivity.this);
//                builder.setAdapter(new ArrayAdapter<String>(AdListActivity.this, R.layout.row, R.id.mytext, list)
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
//                            }
//                        });
//                builder.show();
//            }
//        });
//       i =jsonArray_category.length();

        recycler_cat.scrollToPosition(0);
//        handler=new Handler(getMainLooper());
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 1000);

        handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                foo();

            }
        });
        imageView_left_recicler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i != 0) {
                    i = i - 1;
                }
                recycler_cat.scrollToPosition(i);
                adapter_cat.notifyItemChanged(i);

            }
        });
        imageView_right_recicler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = i + 1;
                if (jsonArray_category != null) {
                    if (i >= jsonArray_category.length()) {
                        i = 0;
                    }
                }
                recycler_cat.scrollToPosition(i);
                adapter_cat.notifyItemChanged(i);


            }
        });

    }

    public void foo() {


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                foo();
                i = i + 1;
                //Toast.makeText(getApplicationContext(),String.valueOf(i),Toast.LENGTH_SHORT).show();
                if (jsonArray_category != null) {
                    if (i == jsonArray_category.length()) {
                        i = 0;
                    }
                }
                recycler_cat.scrollToPosition(i);
                adapter_cat.notifyItemChanged(i);
                recycler_cat.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        handler.removeCallbacksAndMessages(null);
                        return false;

                    }
                });
            }
        }, 2000);


    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        BottomNavigationView.getMenu().findItem(R.id.navigation_today_news).setChecked(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchitem = menu.findItem(R.id.action_search);
        if (searchitem != null) {

            SearchView searchview = (SearchView) MenuItemCompat.getActionView(searchitem);
            TextView searchtext = (TextView) searchview.findViewById(R.id.search_src_text);

            searchview.setQueryHint("جستجو....             ");
            searchview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            searchview.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (img_toolbar.getVisibility() == View.VISIBLE) {
//                        img_toolbar.setVisibility(View.GONE);
//                    } else {
//                        img_toolbar.setVisibility(View.VISIBLE);
//                    }
                }
            });


            searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {


                    search_key = query;

                    last_ad_id = 0;


                    adapter.clear_list();

                    str_location_filter2 = location_filter.getText().toString();
                    new get_ad_list().execute();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {


                    if (newText.equals("")) {
                        search_key = "";

                        last_ad_id = 0;


                        adapter.clear_list();

                        str_location_filter2 = location_filter.getText().toString();
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
        ProgressDialog pd = new ProgressDialog(AdListActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            progress_ball.setVisibility(View.VISIBLE);
           // pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {
                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("read_category")));
                // get_ad_list.put("category","سیاسی");
                String category_filter2 = Base64.encodeToString(category_filter.getBytes("UTF-8"), Base64.DEFAULT);
                get_ad_list.put("category", MCrypt.bytesToHex(mcrypt.encrypt(category_filter2)));
                if (str_location_filter2.isEmpty()) {
                    String st_location_filter = Base64.encodeToString("همه ی استان ها".getBytes("UTF-8"), Base64.DEFAULT);
                    get_ad_list.put("location_filter", MCrypt.bytesToHex(mcrypt.encrypt(st_location_filter)));
                } else {
                    String st_location_filter = Base64.encodeToString(str_location_filter2.getBytes("UTF-8"), Base64.DEFAULT);
                    get_ad_list.put("location_filter", MCrypt.bytesToHex(mcrypt.encrypt(st_location_filter)));
                    //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));
                }
                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("last_ad_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(last_ad_id))));
                String str_search = Base64.encodeToString(search_key.getBytes("UTF-8"), Base64.DEFAULT);
                get_ad_list.put("search", MCrypt.bytesToHex(mcrypt.encrypt(str_search)));
                Log.e("data sended",get_ad_list.toString());
                //search_key = Base64.encodeToString(search_key.getBytes("UTF-8"), Base64.DEFAULT);
                // get_ad_list.put("search_key", MCrypt.bytesToHex(mcrypt.encrypt(search_key)));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//
//                    try {
//                        String temp = new String(mcrypt.decrypt(get_ad_list.getString("location_filter").toString()));
//                        Toast.makeText(getBaseContext(), a, Toast.LENGTH_SHORT).show();
//                        String temp2 = new String(mcrypt.decrypt(get_ad_list.getString("location_filter").toString())).trim();
//                        Snackbar.make(location_filter, temp2, Snackbar.LENGTH_LONG).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });

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
                                JSONObject data_all = new JSONObject(finalResponse);


                                JSONArray ad_list = new JSONArray();
                                ad_list = (JSONArray) data_all.get("row");

                                jsonArray_category = (JSONArray) data_all.get("cat");
                                a = data_all.get("ostan").toString();
                                a = a.replace("\"", "");
                                String[] province_list = a.split(",");


                                province_list[0] = "همه ی استان ها";
                                location_filter.setText(province_list[settings.getInt("location_filter", 0)]);

                                //Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();


                                if (ad_list.length() == 0 && last_ad_id == 0) {

                                    not_found.setVisibility(View.VISIBLE);

                                }else{
                                    not_found.setVisibility(View.GONE);
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

                                //
                                final ArrayList<JSONObject> data_list2 = new ArrayList<JSONObject>();

                                for (int i = 0; i < jsonArray_category.length(); i++) {


                                    data_list2.add(jsonArray_category.getJSONObject(i));
                                }

                                //Toast.makeText(getApplicationContext(),data_list2.toString(),Toast.LENGTH_SHORT).show();

                                adapter_cat = new CategoryRecyclerViewAdapter(getApplicationContext(), data_list2) {
                                    @Override
                                    public void onBindViewHolder(final BookViewHolder holder, final int position) {
                                        super.onBindViewHolder(holder, position);

                                        holder.liner_item.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                try {
                                                    category_filter = daro.get(position).getString("title");
                                                    adapter_cat.notifyItemChanged(position);
                                                    //  holder.tv_item_cat.setTextColor(Color.BLACK);
                                                    notifyDataSetChanged();

                                                    //notifyDataSetChanged();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Toast.makeText(getApplicationContext(), category_filter, Toast.LENGTH_SHORT).show();
                                                temp_update = 2;
                                                not_found.setVisibility(View.GONE);

                                                last_ad_id = 0;


                                                adapter.clear_list();
                                                new get_just_data_for_cat().execute();
//                                                runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//
//                                                        new get_ad_cat().execute();
//
//                                                    }
//                                                });
                                            }
                                        });
                                        if (holder.tv_item_cat.getText().toString().equals(category_filter)) {
                                            holder.tv_item_cat.setTextColor(Color.BLUE);
                                        } else {
                                            holder.tv_item_cat.setTextColor(Color.BLACK);
                                        }

                                    }
                                };

                                recycler_cat.setAdapter(adapter_cat);
                                try {
                                    JSONObject jsonupdate = new JSONObject();
                                    if (data_all.has("update")) {
                                        jsonupdate = data_all.getJSONObject("update");
                                        int int_update_ejbari = 0;
                                        int int_update_ekhtiyari = 0;
                                        int int_update_focus = 0;

                                        if (jsonupdate.has("update_ejbari")) {
                                            int_update_ejbari = jsonupdate.getInt("update_ejbari");
                                        }
                                        if (jsonupdate.has("update_ekhtiyari")) {
                                            int_update_ekhtiyari = jsonupdate.getInt("update_ekhtiyari");
                                        }
                                        if (jsonupdate.has("update_focus")) {
                                            int_update_focus = jsonupdate.getInt("update_focus");
                                        }

                                        int version_app = 0;

                                        version_app = getAppVersion();

                                        //if (permis_fill){
                                        if (int_update_ejbari > version_app) {
                                            show_update_ejbari_dialog();

                                        } else if (int_update_ekhtiyari > version_app) {
                                            show_update_ekhtiaary_dialog();

                                        }
                                        if (int_update_focus == version_app) {
                                            show_update_ejbari_dialog();

                                        }


                                    }
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }


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
//            pd.hide();
//            pd.dismiss();
           progress_ball.setVisibility(View.GONE);

        }
    }


    public class get_just_data_for_cat extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(AdListActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
          progress_ball.setVisibility(View.VISIBLE);
          //  pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("read_category")));

                // get_ad_list.put("category","سیاسی");
                String category_filter2 = Base64.encodeToString(category_filter.getBytes("UTF-8"), Base64.DEFAULT);

                get_ad_list.put("category", MCrypt.bytesToHex(mcrypt.encrypt(category_filter2)));

                String st_location_filter = Base64.encodeToString(str_location_filter2.getBytes("UTF-8"), Base64.DEFAULT);
                get_ad_list.put("location_filter", MCrypt.bytesToHex(mcrypt.encrypt(st_location_filter)));

                // get_ad_list.put("location_filter",0);
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));

                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("last_ad_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(last_ad_id))));

                //search_key = Base64.encodeToString(search_key.getBytes("UTF-8"), Base64.DEFAULT);
                // get_ad_list.put("search_key", MCrypt.bytesToHex(mcrypt.encrypt(search_key)));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//
//                    try {
//                        Toast.makeText(getBaseContext(),get_ad_list.getString("category").toString(), Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });

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
                                JSONObject data_all = new JSONObject(finalResponse);


                                JSONArray ad_list = new JSONArray();
                                ad_list = (JSONArray) data_all.get("row");


                                if (ad_list.length() == 0 && last_ad_id == 0) {

                                    not_found.setVisibility(View.VISIBLE);

                                }else {
                                    not_found.setVisibility(View.GONE);
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
//            pd.hide();
//            pd.dismiss();
             progress_ball.setVisibility(View.GONE);


        }
    }

    // update
    public void show_update_ekhtiaary_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdListActivity.this, R.style.DialogeTheme);
        builder.setTitle("نسخه جدید");
        builder.setIcon(R.mipmap.ic_update);
        builder.setMessage("نسخه جدیدی از برنامه موجود است , مایل به دریافت نسخه جدید هستید");
        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent_udpdate_app();

            }
        });
        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog dialog = builder.create();
        //baray zamani k mikhahim dialog bejoz click roy butten az bin naravad
        dialog.setCancelable(false);
        dialog.show();


    }

    public void show_update_ejbari_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdListActivity.this, R.style.DialogeTheme);
        builder.setTitle("بروز رسانی اجباری");
        builder.setIcon(R.mipmap.ic_update);
        builder.setMessage("نسخه ی مورد استفاده شما خیلی قدیمی است, برای استفاده از نرم افزار باید ان را اپدیت کنید");
        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent_udpdate_app();
            }
        });
        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        //baray zamani k mikhahim dialog bejoz click roy butten az bin naravad
        dialog.setCancelable(false);
        dialog.show();


    }

    public void intent_udpdate_app() {

        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo("com.farsitel.bazaar", PackageManager.GET_ACTIVITIES);
            app_installed = true;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("bazaar://details?id=" + "com.app.dadepardazan.meydanazadi"));
            intent.setPackage("com.farsitel.bazaar");
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://cafebazaar.ir/app/com.app.dadepardazan.meydanazadi"));
            startActivity(i);
        }


    }

    public int getAppVersion() throws PackageManager.NameNotFoundException {
        PackageManager manager = getApplicationContext().getPackageManager();
        PackageInfo info = manager.getPackageInfo(
                getApplicationContext().getPackageName(),
                0);

        return info.versionCode;
    }
}

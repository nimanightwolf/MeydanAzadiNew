package com.app.dadepardazan.meydanazadi.view_pager.profile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.app.dadepardazan.meydanazadi.MainActivity;
import com.app.dadepardazan.meydanazadi.R;
import com.app.dadepardazan.meydanazadi.TasviehActivity;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;

/**
 * Created by Bamdad on 1/29/2019.
 */

public class ProFileUser2Activity extends MainActivity {
    public static final String ACTIVITY_PACKING_RESULT_CODE = "result_code";

    TextView tv_show_tasvieh;
    TextView profile_name;
    TextView profile_number_davatSode;
    TextView profile_number_taiidshode;
    TextView profile_number_emtiaz;
    TextView profile_code_davat;
    TextView tv_profile_share;
    TextView tv_profile_code_davat;
    //edite
    EditText edit_profile_name_family;
    EditText edit_profile_username;
    EditText edit_profile_bio;
    EditText edit_profile_education;
    EditText edit_profile_job;
    TextView tv_profile_day;
    TextView tv_profile_month;
    TextView tv_profile_year;

    JSONObject ad_list;
    private MCrypt mcrypt;
    Uri uri;
    int current_image;
    Boolean fill_image = false;
    String[] address_images = new String[3];
    ArrayList<ImageView> images = new ArrayList<ImageView>();
    String image_base64;
    String code_davat;
    RelativeLayout layout_profile_bg;
    RelativeLayout profile_hestorylist_layout;
    Bitmap res;
    BitmapDrawable background;
    TextView tv_profile_user_name;
    EditText edittext_ID;
    String string_id = "";
    String string_gettext = "";
    ScrollView scroll_main;
    CardView btn_save;
    float initialX = 0.0f;
    ImageView image_back;
    PersianCalendar initDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user2);
        toolbar = findViewById(R.id.toolbarprofile);
        toolbar.setTitle("ssssss");
        initDate = new PersianCalendar();

        findviewbyid();
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_profile_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show_dialog_input_ID();
            }
        });
        cheackInputValue();

//        profile_hestorylist_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), HestoryList.class);
//                startActivity(i);
//            }
//        });

        images.add((ImageView) findViewById(R.id.profile_image));


        tv_profile_code_davat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_help_dialog();
            }
        });

        tv_show_tasvieh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TasviehActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("ad", ad_list.toString());
                startActivity(i);
            }
        });

        tv_profile_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("profile_user2/plain");
//                i.putExtra(Intent.EXTRA_SUBJECT, "میدان ازادی");
//                i.putExtra(Intent.EXTRA_TEXT, "برنامه ی میدان ازادی را نصب کنید با این کد دعوت \n  " + "کد دعوت:" + code_davat + "\n" +
//                        "https://cafebazaar.ir/app/com.app.dadepardazan.meydanazadi");
//                startActivity(Intent.createChooser(i, "یک برنامه را انتخاب کنید"));


                String shareBody = "برنامه ی میدان ازادی را نصب کنید با این کد دعوت \n  " + "کد دعوت:" + code_davat + "\n" +
                        "https://cafebazaar.ir/app/com.app.dadepardazan.meydanazadi";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "میدان ازادی");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "یک برنامه را انتخاب کنید"));

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(ProFileUser2Activity.this, "کلییییک", Toast.LENGTH_SHORT).show();
                new send_edit_profile().execute();
            }
        });
        tv_profile_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_calender();
            }
        });
        tv_profile_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_calender();
            }
        });
        tv_profile_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_calender();
            }
        });

        images.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_image = 0;
                if (!fill_image) {
                    show_dialog();
                } else {
                    show_delete_dialog();
                }
            }
        });


        Typeface bnazanin = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");

        tv_show_tasvieh.setTypeface(bnazanin);
    //    tv_show_tasvieh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_share_drawable), null,null, null);
      //  tv_show_tasvieh.setCompoundDrawables(getResources().getDrawable(R.drawable.icon_share),null,null,null);
        new get_user_info().execute();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    private void cheackInputValue() {
        edit_profile_bio.setOnKeyListener(new View.OnKeyListener() {
int num_enter=0;
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode== KeyEvent.KEYCODE_ENTER){
                    num_enter+=1;
                    if (num_enter>4){
                        if (edit_profile_bio.getText().toString().isEmpty()){
                            num_enter=0;
                        }
                       // Toast.makeText(ProFileUser2Activity.this, "دوست عزیز زیاد اینتر نزن", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }

                // if enter is pressed start calculating
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_UP) {

                    // get EditText text
                    String text = ((EditText) v).getText().toString();

                    // find how many rows it cointains
                    int editTextRowCount = text.split("\\n").length;

                    // user has input more than limited - lets do something
                    // about that
                    if (editTextRowCount >= 7) {

                        // find the last break
                        int lastBreakIndex = text.lastIndexOf("\n");

                        // compose new text
                        String newText = text.substring(0, lastBreakIndex);

                        // add new text - delete old one and append new one
                        // (append because I want the cursor to be at the end)
                        ((EditText) v).setText("");
                        ((EditText) v).append(newText);

                    }
                }

                return false;
            }
        });

    }

    private void findviewbyid() {
        tv_show_tasvieh = (TextView) findViewById(R.id.tv_show_tasvieh);
        profile_name = (TextView) findViewById(R.id.tv_profile_name);
        profile_number_davatSode = (TextView) findViewById(R.id.profile_number_davatSode);
        profile_number_taiidshode = (TextView) findViewById(R.id.profile_number_taiidshode);
        profile_number_emtiaz = (TextView) findViewById(R.id.profile_number_emtiaz);
        tv_profile_share = (TextView) findViewById(R.id.tv_profile_share);
        tv_profile_code_davat = (TextView) findViewById(R.id.tv_profile_code_davat);
        tv_profile_user_name = (TextView) findViewById(R.id.tv_profile_user_name);
        edit_profile_name_family = findViewById(R.id.edit_profile_name_family);
        edit_profile_username = findViewById(R.id.edit_profile_username);
        edit_profile_bio = findViewById(R.id.edit_profile_bio);
        edit_profile_education = findViewById(R.id.edit_profile_education);
        edit_profile_job = findViewById(R.id.edit_profile_job);
        tv_profile_day = findViewById(R.id.tv_profile_day);
        tv_profile_month = findViewById(R.id.tv_profile_month);
        tv_profile_year = findViewById(R.id.tv_profile_year);
        btn_save=findViewById(R.id.btn_save);
        image_back=findViewById(R.id.image_back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_menu, menu);


        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_back) {

            finish();
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();

                return true;
            case MotionEvent.ACTION_MOVE:

                return false;
            case MotionEvent.ACTION_UP:
                if (initialX < event.getX() && (Math.abs(initialX - event.getX()) > 100)) {
                    Toast.makeText(this, "Left to right gesture", Toast.LENGTH_SHORT).show();

                }
                return true;


        }


        return super.onTouchEvent(event);
    }

//    private class LoadBackground extends AsyncTask<String, Void, Drawable> {
//
//        private String imageUrl, imageName;
//
//        public LoadBackground(String url, String file_name) {
//            this.imageUrl = url;
//            this.imageName = file_name;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Drawable doInBackground(String... urls) {
//
//            try {
//                InputStream is = (InputStream) this.fetch(this.imageUrl);
//                Drawable d = Drawable.createFromStream(is, this.imageName);
//                return d;
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                return null;
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        private Object fetch(String address) throws MalformedURLException, IOException {
//            URL url = new URL(address);
//            Object content = url.getContent();
//            return content;
//        }
//
//        @Override
//        protected void onPostExecute(Drawable result) {
//            super.onPostExecute(result);
//            //layout_profile_bg.setBackgroundDrawable(result);
//        }
//    }

    public void show_dialog_input_ID() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProFileUser2Activity.this, R.style.DialogeTheme);
        alertDialog.setTitle("لطفا نام کاربری خود را در کادر زیر وارد کنید");
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_input_id, null);
        edittext_ID = (EditText) view.findViewById(R.id.edittext_ID);
        edittext_ID.setText(string_id);

        alertDialog.setView(view);
        //alertDialog.setView(input2);

        alertDialog.setPositiveButton("تایید",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (edittext_ID.getText().toString().trim().isEmpty()) {
                            dialog.cancel();
                        } else if (edittext_ID.getText().toString().trim().equals(string_id)) {
                            dialog.cancel();
                        } else {
                            string_gettext = edittext_ID.getText().toString();
                            new send_username_id().execute();
                        }
//

                    }


                });

        alertDialog.setNegativeButton("لغو",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        customdialog();

                    }
                });

//        if (!tv_add.getText().toString().trim().equals("0")) {
        alertDialog.show();
//
//        }
    }

    public void customdialog() {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.dialog_show_dialog, null);
//        final Dialog dialog = new Dialog(ProFileUser.this, R.style.DialogeThemeWhite);
//        dialog.setContentView(view);
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();


    }


    public void show_help_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProFileUser2Activity.this, R.style.DialogeTheme);
        builder.setTitle("راهنمای دعوت");
        builder.setMessage(" ثبت نام در میدان آزادی و ایجاد صفحه ی کاربری به شما یک کد معرف اختصاص داده خواهد شد که میتوانید با معرفی این نرم افزار به دوستان خود و ارائه این کد معرف و ثبت آن در هنگام نصب امتیاز دریافت کنید و در ادامه با جمع آوری این امتیازات از امکاناتی که به شما ارائه میشود استفاده کنید و یا این امتیازات را به تومان نقد و یا به ارز دیجیتال تبدیل کنید.\n" +
                "\n" +
                "لازم به ذکر است با نصب این نرافزار با کد معرف شما نیز امتیاز اولیه ای به کاربر دعوت شده از طرف شما نیز تعلق خواهد گرفت");
        builder.setPositiveButton("متوجه شدم!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();


    }


    public class get_user_info extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(ProFileUser2Activity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.setCancelable(false);
            progress_ball.setVisibility(View.VISIBLE);
           // pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("get_user_info")));
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


                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    response = new String(mcrypt.decrypt(response)).trim();
                    Log.e("data response", response);


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

//                                Toast.makeText(getApplicationContext(), String.valueOf(settings.getInt("user_id", 0)),Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(),ad_list.toString(),Toast.LENGTH_SHORT).show();
                                ad_list = new JSONObject(finalResponse);
                                profile_name.setText(ad_list.getString("name"));
                                profile_number_davatSode.setText("دعوت شده:\n" + ad_list.getString("moferi"));
                                profile_number_taiidshode.setText("تایید شده:\n" + ad_list.getString("moferi_taeed"));
                                profile_number_emtiaz.setText("امتیاز:\n" + ad_list.getString("points"));
                                code_davat = ad_list.getString("moaref_code");
                                tv_profile_code_davat.setText("کد دعوت: " + code_davat);
                                setValueEdite(ad_list);

                                string_id = ad_list.getString("username");
                                if (!string_id.isEmpty()) {
                                    tv_profile_user_name.setText(string_id);
                                }
                                //Toast.makeText(getApplicationContext(),ad_list.getString("background"),Toast.LENGTH_SHORT).show();
//                                new LoadBackground(ad_list.getString("background"),
//                                        "background").execute();
//                                Bitmap myImage = getBitmapFromURL("http://looksok.files.wordpress.com/2011/12/me.jpg");
//
//                                RelativeLayout rLayout=(RelativeLayout)findViewById(R.id.relativeLayout);
//
//                                //BitmapDrawable(obj) convert Bitmap object into drawable object.
//                                Drawable dr = new BitmapDrawable(myImage);

//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                    layout_profile_bg.setBackground(Drawable.createFromPath("afawf"));
//                                }
                                // Toast.makeText(getApplicationContext(),ad_list.getString("image"),Toast.LENGTH_SHORT).show();


//                                if (!ad_list.getString("image").equals("")) {
//
//                                    Picasso.with(getApplicationContext()).load("http://meydane-azadi.ir/photo/photo.php?image_name=" + ad_list.getString("image")).into(images.get(0));
//
//                                }


                                // Toast.makeText(getBaseContext(), String.valueOf(ad_list.length()), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                if (!ad_list.getString("image").isEmpty()) {
                                    current_image = 0;
                                    Glide.with(getApplicationContext()).load("https://meydane-azadi.ir/photo/photo.php?image_name=" + ad_list.getString("image")).into(images.get(0));
                                    fill_image = true;
                                } else {
                                    images.get(current_image).setImageResource(R.drawable.ic_user);
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


                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                          //  finish();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                       // finish();

                    }
                });


            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.hide();
////            pd.dismiss();
            progress_ball.setVisibility(View.GONE);
        }
    }
    public class send_edit_profile extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(ProFileUser2Activity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.setCancelable(false);
            progress_ball.setVisibility(View.VISIBLE);
            // pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("edit_profile")));
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));

                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("token", MCrypt.bytesToHex(mcrypt.encrypt(settings.getString("token", ""))));
                get_ad_list.put("name",mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(edit_profile_name_family.getText().toString().getBytes("UTF-8"), Base64.DEFAULT))));
                get_ad_list.put("username", MCrypt.bytesToHex(mcrypt.encrypt(edit_profile_username.getText().toString())));
                get_ad_list.put("bio",mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(edit_profile_bio.getText().toString().getBytes("UTF-8"), Base64.DEFAULT))));
                get_ad_list.put("education",mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(edit_profile_education.getText().toString().getBytes("UTF-8"), Base64.DEFAULT))));
                get_ad_list.put("job",mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(edit_profile_job.getText().toString().getBytes("UTF-8"), Base64.DEFAULT))));
                get_ad_list.put("day", MCrypt.bytesToHex(mcrypt.encrypt(tv_profile_day.getText().toString())));
                get_ad_list.put("month", MCrypt.bytesToHex(mcrypt.encrypt(tv_profile_month.getText().toString())));
                get_ad_list.put("year", MCrypt.bytesToHex(mcrypt.encrypt(tv_profile_year.getText().toString())));



            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));
            Log.e("data send_edit", get_ad_list.toString());


            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/ecxa.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "").replace("\"","");
                    response = new String(mcrypt.decrypt(response)).trim();
                    response=response.replace("\"","");
                    Log.e("data response", response);


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalResponse.equals("ok")) {
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("username",edit_profile_username.getText().toString());
                                editor.commit();
                                Toast.makeText(ProFileUser2Activity.this, "اطلاعات ذخیره شد", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            }else {
                                Toast.makeText(ProFileUser2Activity.this, "نام کاربری تکراری است", Toast.LENGTH_SHORT).show();
                            }



                        }
                    });


                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
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
////            pd.dismiss();
            progress_ball.setVisibility(View.GONE);
        }
    }

    private void setValueEdite(JSONObject ad_list) {
        try {
            edit_profile_name_family.setText(ad_list.getString("name"));
            edit_profile_username.setText(ad_list.getString("username"));
            edit_profile_bio.setText(ad_list.getString("bio"));
            edit_profile_education.setText(ad_list.getString("education"));
            edit_profile_job.setText(ad_list.getString("job"));
            tv_profile_day.setText(ad_list.getString("day"));
            tv_profile_month.setText(ad_list.getString("month"));
            tv_profile_year.setText(ad_list.getString("year"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void show_calender() {

        initDate.setPersianDate(initDate.getPersianYear(), initDate.getPersianMonth(), initDate.getPersianDay());
        PersianDatePickerDialog picker = new PersianDatePickerDialog(this)
                .setPositiveButtonString("باشه")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(1300)
                .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                .setInitDate(initDate)
                .setActionTextColor(Color.GRAY)
                //.setTypeFace(typeface)
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setShowInBottomSheet(true)
                .setListener(new Listener() {
                    @Override
                    public void onDateSelected(PersianCalendar persianCalendar) {
                        String TAG = "calender";
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getGregorianChange());//Fri Oct 15 03:25:44 GMT+04:30 1582
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getTimeInMillis());//1583253636577
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getTime());//Tue Mar 03 20:10:36 GMT+03:30 2020
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getDelimiter());//  /
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianLongDate());// سه‌شنبه  13  اسفند  1398
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianLongDateAndTime()); //سه‌شنبه  13  اسفند  1398 ساعت 20:10:36
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianMonthName()); //اسفند
                        Log.d(TAG, "onDateSelected: " + persianCalendar.isPersianLeapYear());//false
                        //  Toast.makeText(getApplicationContext(), persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay(), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(DateActivity.this,  persianCalendar.getTimeInMillis()+"", Toast.LENGTH_SHORT).show();
                        Log.e("date_milie", String.valueOf(persianCalendar.getTimeInMillis()));

                        //  String temp = persianCalendar.getPersianYear() + "/" + str_mounth + "/" + str_day;

                        tv_profile_day.setText(persianCalendar.getPersianDay()+"");
                        tv_profile_month.setText(persianCalendar.getPersianMonth()+"");
                        tv_profile_year.setText(persianCalendar.getPersianYear()+"");
                    }

                    @Override
                    public void onDismissed() {

                    }
                });

        picker.show();
    }

    public void show_dialog() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("انتخاب عکس از گالری");
        list.add("گرفتن عکس با دوربین");

        AlertDialog.Builder builder = new AlertDialog.Builder(ProFileUser2Activity.this);
        builder.setAdapter(new ArrayAdapter<String>(ProFileUser2Activity.this, R.layout.row, R.id.mytext, list), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {//galerry

                    Intent galerry_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(galerry_intent, "لطفا یک عکس را انتخاب کنید"), 2);

                } else if (i == 1) {//camera
                    // CropImage.activity(uri).setAspectRatio(1,1).setRequestedSize(512,512).start(ProFileUser.this);
                    CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(512, 512).setCropShape(CropImageView.CropShape.OVAL).start(ProFileUser2Activity.this);
                    ;


                    //  Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    //File file=new File(Environment.getExternalStorageDirectory(),"file"+String.valueOf(System.currentTimeMillis()+".jpg"));

                    // uri= Uri.fromFile(file);

                    //  camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);

                    //  camera_intent.putExtra("return-data",true);

                    //  startActivityForResult(camera_intent,1);


                }

            }
        });

        builder.show();
    }


    public void show_delete_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProFileUser2Activity.this, R.style.DialogeTheme);

        // AlertDialog.Builder builder = new AlertDialog.Builder(ProFileUser.this,R.style.Theme_AppCompat_DayNight
        // );
        builder.setMessage("ایا مطمئن به حدف عکس هستید");
        builder.setTitle("");
        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                images.get(current_image).setImageResource(R.drawable.ic_user);
                fill_image = false;

            }
        });

        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {//camera


            //  CropImage.activity(uri).setAspectRatio(1,1).setRequestedSize(512,512).start(this);


        } else if (requestCode == 2 && resultCode == RESULT_OK) {//gallery

            uri = data.getData();

            CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(512, 512).setCropShape(CropImageView.CropShape.OVAL).start(this);
            ;


        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            Uri resutlUri = result.getUri();

            images.get(current_image).setImageURI(resutlUri);


            fill_image = true;
            resutlUri = null;


            BitmapDrawable bd = ((BitmapDrawable) images.get(current_image).getDrawable());
            Bitmap bm = bd.getBitmap();

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);

            image_base64 = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);


            new upload_image().execute();


        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    public class upload_image extends AsyncTask<Void, Void, String> {

        ProgressDialog pd = new ProgressDialog(ProFileUser2Activity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("لطفا منتظر بمانید");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... Void) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();

            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("images_upload_profile")));
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));

                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));

                get_ad_list.put("image", MCrypt.bytesToHex(mcrypt.encrypt(image_base64)));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));

//            namevaluepairs.add(new BasicNameValuePair("image", image_base64));


            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/image_up.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    final String finalResponse = response;
                    //response=new String( mcrypt.decrypt(response)).trim();


                    if (!response.trim().equals("0")) {//upload ok

                        address_images[current_image] = response.trim();

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Toast.makeText(getBaseContext(), "خطا در اپلود تصویر", Toast.LENGTH_SHORT).show();

                                images.get(current_image).setImageResource(R.drawable.ic_user);
                                fill_image = false;
                            }
                        });

                    }

                } else {//error

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در اپلود تصویر", Toast.LENGTH_SHORT).show();

                            images.get(current_image).setImageResource(R.drawable.ic_user);
                            fill_image = false;
                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در اپلود تصویر", Toast.LENGTH_SHORT).show();

                        images.get(current_image).setImageResource(R.drawable.ic_user);
                        fill_image = false;
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

    public class send_username_id extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(ProFileUser2Activity.this);

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
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("username_update")));
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));

                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("username", MCrypt.bytesToHex(mcrypt.encrypt(string_gettext)));


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

                    response = response.replace("<azadi>", "").replace("</azadi>", "").replace("\"", "");

                    //  response = new String(mcrypt.decrypt(response)).trim();


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (finalResponse.equals("error")) {
                                Toast.makeText(getBaseContext(), " خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                                show_dialog_input_ID();
                            } else if (finalResponse.equals("exist")) {
                                edittext_ID.setError("نام کاربری تکراری می باشد");
                                Toast.makeText(getBaseContext(), "نام کاربری تکراری می باشد", Toast.LENGTH_SHORT).show();
                                show_dialog_input_ID();
                            } else {
                                string_id = finalResponse;
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("username",string_id);
                                editor.commit();
                                tv_profile_user_name.setText(string_id);
                            }

                            // Toast.makeText(getBaseContext(), String.valueOf(ad_list.length()), Toast.LENGTH_SHORT).show();


                        }
                    });


                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            // finish();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        finish();

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

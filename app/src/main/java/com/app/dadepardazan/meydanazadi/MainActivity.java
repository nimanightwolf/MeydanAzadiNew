package com.app.dadepardazan.meydanazadi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.app.dadepardazan.meydanazadi.all_tweet.AllTweetActivity;
import com.app.dadepardazan.meydanazadi.login.LoginActivity;
import com.app.dadepardazan.meydanazadi.view_pager.profile.ProfileAndShowToitActivity;
import com.app.dadepardazan.meydanazadi.view_pager.search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.wang.avi.AVLoadingIndicatorView;

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

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String ACTIVITY_PACKING_RESULT_CODE = "result_code";

    public Toolbar toolbar;
    DrawerLayout drawer;
    public SharedPreferences settings;
    int other = 0;
    int one_click_intent = 1;
    public MCrypt mcrypt;
    public AVLoadingIndicatorView progress_ball;

    public com.google.android.material.bottomnavigation.BottomNavigationView BottomNavigationView;
    public ImageView img_toolbar;
    public String str_android_id = "";
    int int_stattus_radio_report;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        settings = PreferenceManager.getDefaultSharedPreferences(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("میدان ازادی");

        progress_ball = (AVLoadingIndicatorView) findViewById(R.id.progress_ball);
        img_toolbar = findViewById(R.id.img_toolbar);

        //


        BottomNavigationView = (com.google.android.material.bottomnavigation.BottomNavigationView) findViewById(R.id.navigationView);
        BottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id = item.getItemId()) {


                    case R.id.navigation_search:
                        //Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;

                    case R.id.navigation_today_news:
                        //Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
                        i = new Intent(getApplicationContext(), AdListActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                        break;

                    case R.id.navigation_home:
                        i = new Intent(getApplicationContext(), AllTweetActivity.class);
                        startActivity(i);
                        break;
                    case R.id.navigation_account:
                        if (settings.getInt("user_id", 0) != 0) {

                            i = new Intent(getApplicationContext(), ProfileAndShowToitActivity.class);
                            startActivity(i);


                        } else {
                            i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);


                        }
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;


                    case R.id.navigation_send_post:
                        // Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
                        i = new Intent(getApplicationContext(), SendNewTweetActivity.class);
                        startActivity(i);

                        break;
                }

                return true;
            }
        });

        str_android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        toolbar.setNavigationOnClickListener(this);


//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/iransans.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build());
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/IRANSans.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // getSupportActionBar().setElevation(0);


        return true;
    }


    public class foo extends MainActivity {

    }

    @Override
    public void onClick(View v) {
    }

    //    @Override
//    protected void attachBaseContext(Context newBase) {
//       // super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    public static class MCrypt {

        private String iv = "#$7rdwq,pmxsegyn";//Dummy iv (CHANGE IT!)
        private IvParameterSpec ivspec;
        private SecretKeySpec keyspec;
        private Cipher cipher;

        private String SecretKey = "#&^!mlotrqa*lpedtbvfrujikp,mnhyt";

        public MCrypt() {
            ivspec = new IvParameterSpec(iv.getBytes());

            keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

            try {
                cipher = Cipher.getInstance("AES/CBC/NoPadding");
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public byte[] encrypt(String text) throws Exception {
            String temp = "";
            if (text == null || text.length() == 0) {
                // throw new Exception("Empty string");
                return "".getBytes();
            }

            byte[] encrypted = null;

            try {
                cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

                encrypted = cipher.doFinal(padString(text).getBytes("UTF-8"));
            } catch (Exception e) {

                encrypted = (text).getBytes("UTF-8");
                //throw new Exception("[encrypt] " + e.getMessage());


            }
            //encrypted = temp.getBytes("UTF-8");
            return encrypted;
        }

        public byte[] decrypt(String code) throws Exception {
            if (code == null || code.length() == 0)
                throw new Exception("Empty string");

            byte[] decrypted = null;

            try {
                cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

                decrypted = cipher.doFinal(hexToBytes(code));
            } catch (Exception e) {
                throw new Exception("[decrypt] " + e.getMessage());
            }
            return decrypted;
        }


        public static String bytesToHex(byte[] data) {
            if (data == null) {
                return "";
            }

            int len = data.length;
            String str = "";
            for (int i = 0; i < len; i++) {
                if ((data[i] & 0xFF) < 16)
                    str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
                else
                    str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
            }
            return str;
        }


        public static byte[] hexToBytes(String str) {
            if (str == null) {
                return "".getBytes();
            } else if (str.length() < 2) {
                return null;
            } else {
                int len = str.length() / 2;
                byte[] buffer = new byte[len];
                for (int i = 0; i < len; i++) {
                    buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
                }
                return buffer;
            }
        }


        private static String padString(String source) {
            char paddingChar = ' ';
            int size = 16;
            int x = source.length() % size;
            int padLength = size - x;

            for (int i = 0; i < padLength; i++) {
                source += paddingChar;
            }

            return source;
        }
    }

    public class send_retweet extends AsyncTask<JSONObject, Void, String> {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);

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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalResponse.equals("ok")) {
                                Toast.makeText(MainActivity.this, "اطلاعات شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                // finish();
                            }
                            // jarr_tag = job_all_data.getJSONArray("tag");


                        }
                    });

                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(MainActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });


                }
                return response;

            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(MainActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        finish();

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

    public String someMethodToCallAsyncTaskInAddActivity(JSONObject get_ad_list) {

        try {
            return new send_data().execute(get_ad_list).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";

    }

    public class send_data extends AsyncTask<JSONObject, Void, String> {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);

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

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    response = new String(mcrypt.decrypt(response)).trim();
                    // response=response.replace("\"","");
                    Log.e("data respose", response);

                    return response;
//                    final String finalResponse = response;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (finalResponse.equals("ok")) {
//                                Toast.makeText(MainActivity.this, "اطلاعات شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
//                                setResult(RESULT_OK);
//                                finish();
//                            }
//                            // jarr_tag = job_all_data.getJSONArray("tag");
//
//
//                        }
//                    });

                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(MainActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });


                }
                return response;

            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(MainActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
                return "error";

            }


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();

        }
    }

    public void dialog_retweet(final int pos, final JSONObject jsonObject, final Button_dialog bt_dialog) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText("آیا می خواهید این نویسه را باز نشر کنید");

        alertDialog.setContentText("با کلید بر روی تایید این پست در صفحه شخصی شما به صورت ریتویت نمایش داده می شود");
        alertDialog.setConfirmText("بله");
        alertDialog.setCancelButton("خیر", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        });

        //.setCustomView(view_dialog)
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
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
                try {
                    if (new send_retweet().execute(get_ad_list).get().equals("ok")) {
                        bt_dialog.onasync("");
                        sDialog.cancel();
                        SweetAlertDialog sucess_dialog =new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        sucess_dialog .setTitleText("نویسه ی شما با موفقیت بازنشر شد!");
                        sucess_dialog.setConfirmText("متوجه شدم");
                        sucess_dialog .setConfirmClickListener(null);
                        sucess_dialog.show();
                        Button btn = (Button) sucess_dialog.findViewById(R.id.confirm_button);
                        btn.setBackground(getResources().getDrawable(R.drawable.background_button_sweetalert));
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


//


            }
        });
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackground(getResources().getDrawable(R.drawable.background_button_sweetalert));
    }

    public void dialog_delete_tweet(final JSONObject jsonObject, final After_delete_listener after_delete_listener) {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText("آیا می خواهید این نویسه را حذف کنید");
//                                                    .setContentText("Won't be able to recover this file!")
        alertDialog.setConfirmText("بله حذف کن!");


        //.setCustomView(view_dialog)
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                mcrypt = new MainActivity.MCrypt();
                JSONObject get_ad_list = new JSONObject();
                try {
                    get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("delete_post")));
                    get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                    get_ad_list.put("id_post", mcrypt.bytesToHex(mcrypt.encrypt(jsonObject.getString("id_post"))));

                    if (new send_data().execute(get_ad_list).get().equals("\"ok\"")) {
                        after_delete_listener.delete_completed(alertDialog);
                        // Snackbar.make(view, R.string.nevise+" شما با موفقیت حذف شد", Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(AllTweetActivity.this, "نویسه شما با موفقیت حذف شد", Toast.LENGTH_SHORT).show();
                        //
                        sDialog
                                .setTitleText("نویسه ی شما با موفقیت حذف شد!")
                                .setConfirmText("متوجه شدم")
                                .setConfirmClickListener(null)

                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        Button btn = (Button) sDialog.findViewById(R.id.confirm_button);
                        btn.setBackground(getResources().getDrawable(R.drawable.background_button_sweetalert));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackground(getResources().getDrawable(R.drawable.background_button_sweetalert));

    }


    public void dialog_delete_tweet(final int pos, final JSONObject jsonObject, final Button_dialog bt_dialog) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view_dialog = inflater.inflate(R.layout.dialog_report_detaile, null);

        SweetAlertDialog alertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        //.setCustomView(view_dialog)
        alertDialog.setCustomImage(R.drawable.icon_retweet_dialog);

        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                JSONObject get_ad_list = new JSONObject();
                try {
                    get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("delete_post")));
                    get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                    get_ad_list.put("id_post", mcrypt.bytesToHex(mcrypt.encrypt(jsonObject.getString("id_post"))));

                    if (new send_data().execute(get_ad_list).get().equals("\"ok\"")) {
                        // Snackbar.make(view, R.string.nevise+" شما با موفقیت حذف شد", Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(AllTweetActivity.this, "نویسه شما با موفقیت حذف شد", Toast.LENGTH_SHORT).show();
                        // delete_item(jsonObject, pos);
                        sDialog
                                .setTitleText("نویسه ی شما با موفقیت حذف شد!")

                                .setConfirmText("متوجه شدم")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        })
                .show();

    }


    public interface Button_dialog {
        public void onasync(String asyncc_call_back);

    }

    public interface After_delete_listener {
        public void delete_completed(SweetAlertDialog alertDialog);


    }


}

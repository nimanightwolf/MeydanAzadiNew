package com.app.dadepardazan.meydanazadi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;

import com.google.android.material.textfield.TextInputLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Bamdad on 12/6/2017.
 */

public class RegisterActivty extends MainActivity {


    String mobile;
    String password;

    EditText sbt_name_family;
    TextInputLayout sbt_name_family_layout;
    EditText sbt_phone;
    // TextInputLayout sbt_phone_layout;
    EditText sbt_email;
    // TextInputLayout sbt_email_layout;
    EditText sbt_password;
    //  TextInputLayout sbt_password_layout;
    EditText sbt_password_again;
    // TextInputLayout sbt_password_again_layout;
    EditText sbt_code_moaref;
    //  TextInputLayout sbt_code_moaref_layout;
    Button sbt_btnLogin;
    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;

    TextView toolbar_title;
    ViewPager viewPager;

    private MCrypt mcrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout content_frame;
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.register_view_pager, content_frame);

//        telephonyManager = (TelephonyManager) this.getSystemService(getApplicationContext().TELEPHONY_SERVICE);
//        IMEI_Number_Holder = telephonyManager.getDeviceId();
        //navigationView.getMenu().findItem(R.id.mnu_account).setChecked(true);
        BottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);
        toolbar.setTitle("ثبت نام");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("ثبت نام");
        toolbar_title.setVisibility(View.VISIBLE);

        IMEI_Number_Holder = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        viewPager=(ViewPager)findViewById(R.id.viewPager);
        MyViewPagerAdapter adapter=new MyViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        //viewPager.setCurrentItem(num, true);




//        sbt_name_family = (EditText) findViewById(R.id.sbt_name_family);
//        //sbt_name_family_layout= (TextInputLayout) findViewById(R.id.sbt_name_family_layout);
//        sbt_phone = (EditText) findViewById(R.id.sbt_phone);
//        // sbt_phone_layout= (TextInputLayout) findViewById(R.id.sbt_phone_layout);
//
//        sbt_email = (EditText) findViewById(R.id.sbt_email);
//        // sbt_email_layout= (TextInputLayout) findViewById(R.id.sbt_email_layout);
//
//        sbt_password = (EditText) findViewById(R.id.sbt_password);
//        // sbt_password_layout= (TextInputLayout) findViewById(R.id.sbt_password_layout);
//
//        sbt_password_again = (EditText) findViewById(R.id.sbt_password_again);
//        // sbt_password_again_layout= (TextInputLayout) findViewById(R.id.sbt_password_again_layout);
//
//        sbt_code_moaref = (EditText) findViewById(R.id.sbt_code_moaref);
//        // sbt_code_moaref_layout= (TextInputLayout) findViewById(R.id.sbt_code_moaref_layout);
//
//
//        sbt_btnLogin = (Button) findViewById(R.id.sbt_btnLogin);


//        mcrypt = new MCrypt();
///* Encrypt */
//        try {
//          //  String encrypted = MCrypt.bytesToHex( mcrypt.encrypt("1") );
//
//            String decrypted = new String( mcrypt.decrypt(text) );
//            Toast.makeText(getApplicationContext(),decrypted,Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        sbt_btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Boolean is_valid = true;
//
//                if (sbt_name_family.getText().toString().isEmpty()) {
//                    sbt_name_family.setError("لطفا نام و نام خانوادگی را وارد کنید");
//                    // sbt_name_family_layout.setErrorEnabled(true);
//                } else {
//                    // sbt_name_family_layout.setErrorEnabled(false);
//                }
//
//
//                if (!sbt_email.getText().toString().trim().equals("")) {
//
//                    if (sbt_email.getText().toString().indexOf(".") == -1 || sbt_email.getText().toString().indexOf("@") == -1) {
//                        sbt_email.setError("ایمیل وارد شده صحیح نمی باشد");
//
//                        is_valid = false;
//                    }
//
//                } else {
//
//                    // sbt_email_layout.setErrorEnabled(false);
//
//                }
//                //sbt_email.setError("لطفا شماره تلن یا ایمیل را وارد کنید");
//                if (!sbt_phone.getText().toString().startsWith("09") || sbt_phone.getText().length() != 11) {
//                    sbt_phone.setError("شماره تلفن را به صورت 11 رقمی همراه با 0 وارد کنید");
//                    //sbt_phone_layout.setErrorEnabled(true);
//                    // Toast.makeText(getApplicationContext(), "لطفا شماره تلن یا ایمیل را وارد کنید", Toast.LENGTH_SHORT).show();
//                    // sbt_email_layout.setErrorEnabled(true);
//                    is_valid = false;
//                }
//
//
//                if (sbt_password.getText().length() > 22 || sbt_password.getText().length() < 6) {
//                    sbt_password.setError("لطفا پسورد را به صورت 6 تا 22 رقمی وارد کنید");
//                    is_valid = false;
//
//
//                }
//
//                if (!sbt_password_again.getText().toString().equals(sbt_password.getText().toString())) {
//                    sbt_password_again.setError("پسورد های وارد شده با یکدیگر تطابق ندارند");
//                    is_valid = false;
//                }
////                    if(sbt_password_again.getText().toString()!=sbt_password.getText().toString()){
////                    sbt_password_again.setError("پسورد های وارد شده یکسان نیستن");
////                    sbt_email_layout.setErrorEnabled(true);
////                    is_valid=false;
////
////
////                    }else{
////                    sbt_email_layout.setErrorEnabled(false);
////                }
//
//                if (is_valid) {
//                    mobile = sbt_phone.getText().toString();
//                    password = sbt_password.getText().toString();
//
//                    new apply_activation_key().execute();
//
//                } else {
//
//                    // Toast.makeText(getApplicationContext(), "شماره موبایل وارد شده صحیح نمی باشد", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        });

//
//
//        et_password= (EditText) findViewById(R.id.password);
//        btnLinkToRegisterScreen= (Button) findViewById(R.id.btnLinkToRegisterScreen);
//
//        btnLinkToRegisterScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),RegisterActivty.class);
//                startActivity(i);
//                finish();
//
//            }
//        });
//
//
//        mobail_text = (EditText) findViewById(R.id.mobail_text);
//        // email_text = (EditText) findViewById(R.id.email_text);
//        btnLogin = (Button) findViewById(R.id.btnLogin);
//        // submit2_bt = (Button) findViewById(R.id.submit2_bt);
//        // activation_code = (EditText) findViewById(R.id.activation_code);
//
//        mobail_text.requestFocus();
//
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Boolean is_valid = true;
//                if (!mobail_text.getText().toString().startsWith("09") || mobail_text.getText().length() != 11) {
//
//                    is_valid = false;
//
//                }
//
//                if (is_valid) {
//                    mobile = mobail_text.getText().toString();
//                    password = et_password.getText().toString();
//
//                    new apply_activation_key().execute();
//
//                } else {
//
//                    Toast.makeText(getApplicationContext(), "شماره موبایل وارد شده صحیح نمی باشد", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        });
//
//


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.help_menu, menu);

        return true;

    }

    //
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.help) {

            show_help_dialog();


        }


        return super.onOptionsItemSelected(item);

    }

    //


    public void show_help_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivty.this);
        builder.setMessage("بعد از وارد کردن شماره تماس منتظر بمانید که پیامک تایید برای شما ارسال شود\n لطفا حداقل یک دقیقه منتظر بمانید! و در صورت عدم دریافت پیامک دوباره امتحان کنید");
        builder.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();


    }


    public class send_activation_key extends AsyncTask<Void, Void, String> {


        ProgressDialog pd = new ProgressDialog(RegisterActivty.this);


        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.show();


        }


        @Override
        protected String doInBackground(Void... voids) {


            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();


            JSONObject get_ad_list = new JSONObject();

            try {

                get_ad_list.put("command", "send_activation_key");


                get_ad_list.put("mobile", mobile);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));


            try {


                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ghafas.net/api/sms/active_app.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);


                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<ghafas>") && response.endsWith("</ghafas>")) {//response is valid

                    response = response.replace("<ghafas>", "").replace("</ghafas>", "");


                    if (response.trim().equals("ok")) {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                LinearLayout login_layout = (LinearLayout) findViewById(R.id.login_layout);
                                LinearLayout activation_layout = (LinearLayout) findViewById(R.id.activation_layout);


                                login_layout.setVisibility(View.GONE);
                                activation_layout.setVisibility(View.VISIBLE);

                                // activation_code.requestFocus();


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

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.hide();
            pd.dismiss();

        }


    }


    public class apply_activation_key extends AsyncTask<Void, Void, String> {


        ProgressDialog pd = new ProgressDialog(RegisterActivty.this);


        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.show();


        }

        @Override
        protected String doInBackground(Void... voids) {


            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();


            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();

            try {
                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("rigster_user")));

                String name2 = Base64.encodeToString(sbt_name_family.getText().toString().getBytes("UTF-8"), Base64.DEFAULT);

                get_ad_list.put("name", MCrypt.bytesToHex(mcrypt.encrypt(name2)));
                get_ad_list.put("mobile", MCrypt.bytesToHex(mcrypt.encrypt(sbt_phone.getText().toString().trim())));
                if (sbt_email.getText().toString().trim().isEmpty()) {
                    get_ad_list.put("email", MCrypt.bytesToHex(mcrypt.encrypt("null")));

                } else {
                    get_ad_list.put("email", MCrypt.bytesToHex(mcrypt.encrypt(sbt_email.getText().toString().trim())));

                }
                get_ad_list.put("password", MCrypt.bytesToHex(mcrypt.encrypt(sbt_password.getText().toString())));
                if (sbt_code_moaref.getText().toString().trim().isEmpty()) {
                    get_ad_list.put("moaref_code", MCrypt.bytesToHex(mcrypt.encrypt("0")));
                } else {
                    get_ad_list.put("moaref_code", MCrypt.bytesToHex(mcrypt.encrypt(sbt_code_moaref.getText().toString().trim())));
                }
                get_ad_list.put("imei", MCrypt.bytesToHex(mcrypt.encrypt(IMEI_Number_Holder)));


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
//                    Toast.makeText(getBaseContext(), get_ad_list.toString(), Toast.LENGTH_SHORT).show();
//
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

                    response = response.replace("<azadi>", "").replace("</azadi>", "").replace("\"", "");
                    response = new String(mcrypt.decrypt(response)).trim();

                    if (!response.trim().equals("mobile")) {
                        if (!response.trim().equals("false")) {
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt("user_id", Integer.parseInt(response));
                            editor.commit();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    Toast.makeText(getBaseContext(), "شما به میدان ازادی وارد شدید", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ProFileUser.class);
                                    startActivity(i);
                                    finish();

                                }
                            });
                        }


                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getBaseContext(), "شماره تلفن وارد شده تکراری است", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }


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

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.hide();
            pd.dismiss();

        }


    }


}


package com.app.dadepardazan.meydanazadi.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.app.dadepardazan.meydanazadi.AdListActivity;
import com.app.dadepardazan.meydanazadi.MainActivity;
import com.app.dadepardazan.meydanazadi.ProFileUser;
import com.app.dadepardazan.meydanazadi.R;
import com.app.dadepardazan.meydanazadi.TelenewsActivity;
import com.app.dadepardazan.meydanazadi.view_pager.profile.ProfileAndShowToitActivity;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

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

public class LoginActivity extends MainActivity {

    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;

    EditText mobail_text;

    EditText activation_code;

    String mobile;
    String password;
    String name_family;
    String code_mortf_str="";
    String b = null;


    EditText et_password;
    Button btnLogin;
    Button btnLinkToRegisterScreen;
    TextView toolbar_title;
    TextView tv_zaman_baghi;
    private MCrypt mcrypt;
    EditText et_mobail_text;
    EditText et_name_user;
    EditText et_code_moarf;
    PinEntryEditText et_password_sender;
    TextView btn_send_mobile;
    TextView btn_send_pass;
    TextView btn_send_again_pass;
    LinearLayout layout_name_code_moref;
    LinearLayout layout_pass_btn_send;
    boolean islogin_later = false;
    int time_secend = 240;
    Handler handler;
    EditText edittext_ID;
    String string_gettext = "";
    String str_temp_username = "";
    JSONObject ad;
    String str_temp_user_id = "";
    CardView card_dark;
    CardView card_small;
    ImageView image_icon;
    ImageView img_text_wellcome;
    LinearLayout linear_edittext;
    TextView tv_text_pass_sended_to_number;
    CardView card_send_code;
    ImageView img_back;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame;
//        content_frame = (FrameLayout) findViewById(R.id.content_frame);
//        getLayoutInflater().inflate(R.layout.login3, content_frame);
        setContentView(R.layout.login4);

        //navigationView.getMenu().findItem(R.id.mnu_account).setChecked(true);
//        toolbar.setTitle("ورود به میدان ازادی ");
//        BottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);
//        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
//        toolbar_title.setText("ورود به میدان ازادی");
//        toolbar_title.setVisibility(View.VISIBLE);

        IMEI_Number_Holder = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        findviewbyid();
        //  et_password = (EditText) findViewById(R.id.password);
        //  btnLinkToRegisterScreen = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        final ScaleAnimation scaleAnimation = new ScaleAnimation(1, 3f, 1, 4, Animation.RELATIVE_TO_SELF, 0.15f, Animation.RELATIVE_TO_SELF, 1);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation.setFillAfter(true);


        final Animation frombottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_splash);

        card_dark.setVisibility(View.VISIBLE);
        card_small.setVisibility(View.VISIBLE);
        card_dark.startAnimation(frombottom);
        card_small.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_small_circle));
        frombottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image_icon.setVisibility(View.VISIBLE);
                img_text_wellcome.setVisibility(View.VISIBLE);
                image_icon.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_imag_icon));
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setFillAfter(true);
                alphaAnimation.setDuration(1200);
                img_text_wellcome.startAnimation(alphaAnimation);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        linear_edittext.setVisibility(View.VISIBLE);
                        final Animation transanim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_linear_edittext);
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);

                        AnimationSet animationSet = new AnimationSet(true);
                        animationSet.setDuration(1200);
                        animationSet.setInterpolator(new AccelerateInterpolator());
                        animationSet.setFillAfter(true);
                        animationSet.addAnimation(transanim);
                        animationSet.addAnimation(alphaAnimation);
                        linear_edittext.startAnimation(animationSet);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        btn_send_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean is_valid = true;
                if (!et_mobail_text.getText().toString().startsWith("09") || et_mobail_text.getText().length() != 11) {

                    is_valid = false;

                }

                if (is_valid) {

                    mobile = et_mobail_text.getText().toString();
                    //password = et_password.getText().toString();

                    // new apply_activation_key().execute();
                    card_small.startAnimation(scaleAnimation);
                    scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            final Handler h = new Handler(getMainLooper());
                            h.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    card_send_code.setVisibility(View.VISIBLE);
                                    et_password_sender.setVisibility(View.VISIBLE);
                                    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                                    alphaAnimation.setFillAfter(true);
                                    alphaAnimation.setDuration(1200);
                                    card_send_code.startAnimation(alphaAnimation);
                                    h.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            et_password_sender.requestFocus();
                                            InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                        }
                                    },2000);
                                }
                            }, 700);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    new send_sms().execute();


                } else {

                    Toast.makeText(getApplicationContext(), "شماره موبایل وارد شده صحیح نمی باشد", Toast.LENGTH_SHORT).show();
                    et_mobail_text.setError("شماره موبایل وارد شده صحیح نمی باشد");

                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(LoginActivity.this,LoginActivity.class));
              finish();
            }
        });
        btn_send_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islogin_later == false) {
                    Boolean is_valid = true;
                    if (et_name_user.getText().toString().isEmpty()) {

                        is_valid = false;

                    }
                    if (et_password_sender.getText().toString().isEmpty()) {

                        is_valid = false;

                    }

                    if (is_valid) {
                        mobile = et_mobail_text.getText().toString();
                        password = et_password_sender.getText().toString();
                        name_family = et_name_user.getText().toString();
                        code_mortf_str = et_code_moarf.getText().toString();


                        // new apply_activation_key().execute();
                        new send_password().execute();


                    } else {

                        Toast.makeText(getApplicationContext(), "لطفا فیلد های خواسته شده را به صورت درست و کامل  وارد کنید ", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    password = et_password_sender.getText().toString();
                    new send_password().execute();

                }


            }
        });
        btn_send_again_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_secend = 240;
                new send_sms().execute();
            }
        });


//        btnLinkToRegisterScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), RegisterActivty.class);
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
//
//                    mobile = mobail_text.getText().toString();
//                    password = et_password.getText().toString();
//
//                    new apply_activation_key().execute();
//
//
//                } else {
//
//                    Toast.makeText(getApplicationContext(), "شماره موبایل وارد شده صحیح نمی باشد", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        });
        BottomNavigationView.setOnNavigationItemSelectedListener(new com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id = item.getItemId()) {
                    case R.id.navigation_today_news:
                        //Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), TelenewsActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                        break;

                    case R.id.navigation_home:
                        i = new Intent(getApplicationContext(), AdListActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

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
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    }

    private void findviewbyid() {
        et_mobail_text = (EditText) findViewById(R.id.et_mobail_text);
        et_name_user = (EditText) findViewById(R.id.et_name_user);
        et_code_moarf = (EditText) findViewById(R.id.et_code_moarf);
        et_password_sender = findViewById(R.id.et_password_sender);
        btn_send_mobile = findViewById(R.id.btn_send_mobile);
        btn_send_pass = findViewById(R.id.btn_send_pass);
        btn_send_again_pass = (TextView) findViewById(R.id.btn_send_again_pass);
        layout_name_code_moref = (LinearLayout) findViewById(R.id.layout_name_code_moref);
        layout_pass_btn_send = (LinearLayout) findViewById(R.id.layout_pass_btn_send);
        tv_zaman_baghi = (TextView) findViewById(R.id.tv_zaman_baghi);
        card_dark = findViewById(R.id.card_dark);
        card_small = findViewById(R.id.card_small);
        image_icon = findViewById(R.id.image_icon);
        img_text_wellcome = findViewById(R.id.img_text_wellcome);
        linear_edittext = findViewById(R.id.linear_edittext);
        tv_text_pass_sended_to_number = findViewById(R.id.tv_text_pass_sended_to_number);
        card_send_code = findViewById(R.id.card_send_code);
        img_back = findViewById(R.id.img_back);
    }

    public void foo() {


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (time_secend > 0) {
                    time_secend = time_secend - 1;
                    btn_send_again_pass.setClickable(false);
                    btn_send_again_pass.setVisibility(View.GONE);
                } else {
                    btn_send_again_pass.setClickable(true);
                    btn_send_again_pass.setVisibility(View.VISIBLE);
                    return;

                }
                int minutes = (int) (time_secend / 60);
                int seconds = (int) (time_secend - minutes * 60);

                if (seconds > 9) {
                    tv_zaman_baghi.setText("" + minutes + ":" + seconds);
                } else {
                    tv_zaman_baghi.setText("" + minutes + ":0" + seconds);
                }

                foo();
            }
        }, 1000);
    }


    @Override
    protected void onPause() {
        super.onPause();
        BottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("بعد از وارد کردن شماره تماس منتظر بمانید که پیامک تایید برای شما ارسال شود\n لطفا حداقل یک دقیقه منتظر بمانید! و در صورت عدم دریافت پیامک دوباره امتحان کنید");
        builder.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();


    }


    public class apply_activation_key extends AsyncTask<Void, Void, String> {


        ProgressDialog pd = new ProgressDialog(LoginActivity.this);


        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.show();


        }

        @Override
        protected String doInBackground(Void... voids) {


            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();


            JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {
                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("login_sms")));

                get_ad_list.put("mobile", MCrypt.bytesToHex(mcrypt.encrypt(mobile)));

                //get_ad_list.put("password", MCrypt.bytesToHex(mcrypt.encrypt(password)));
                get_ad_list.put("imei", MCrypt.bytesToHex(mcrypt.encrypt(IMEI_Number_Holder)));


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

                    response = new String(mcrypt.decrypt(response)).trim();


                    if (!response.trim().equals("error")) {
                        if (response.trim().equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    layout_pass_btn_send.setVisibility(View.VISIBLE);
                                    layout_name_code_moref.setVisibility(View.VISIBLE);
                                    // et_name_user.setVisibility(View.VISIBLE);
                                    btn_send_pass.setVisibility(View.VISIBLE);
                                    btn_send_mobile.setVisibility(View.GONE);


                                }
                            });


                        } else if (response.trim().equals("1")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    et_password_sender.setVisibility(View.VISIBLE);
                                    et_name_user.setVisibility(View.VISIBLE);
                                    btn_send_pass.setVisibility(View.VISIBLE);
                                    btn_send_mobile.setVisibility(View.GONE);

                                }
                            });

                        }
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putInt("user_id", Integer.parseInt(response));
//                        editor.commit();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//
//                                Toast.makeText(getBaseContext(), "شما به میدان ازادی وارد شدید", Toast.LENGTH_SHORT).show();
//
//                                Intent i = new Intent(getApplicationContext(), ProFileUser.class);
//                                startActivity(i);
//                                finish();
//                            }
//                        });


                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getBaseContext(), "شماره یا رمز وارد شده اشتباه می باشد", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات1", Toast.LENGTH_SHORT).show();


                        }
                    });
                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات2", Toast.LENGTH_SHORT).show();


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


//    public void get_permistion() {
//        permissionStatus = LoginActivity.this.getSharedPreferences("permissionStatus",LoginActivity.this.MODE_PRIVATE);
//
//        if(ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
//            if(ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, android.Manifest.permission.READ_PHONE_STATE)){
//                //Show Information about why you need the permission
//                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this,R.style.DialogeTheme);
//                builder.setTitle("در خواست مجوز");
//                builder.setMessage("این نرم افزار برای ثبت نام به این مجوز دارد.");
//                builder.setPositiveButton("قبول کردن", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE},PERMISSION_CALLBACK_CONSTANT);
//                        }
//                    }
//                });
//                builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        finish();
//                    }
//                });
//                builder.show();
//
//            } else if (permissionStatus.getBoolean(android.Manifest.permission.READ_PHONE_STATE,false)) {
//                //Previously Permission Request was cancelled with 'Dont Ask Again',
//                // Redirect to Settings after showing Information about why you need the permission
//                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this,R.style.DialogeTheme);
//                builder.setTitle("در خواست مجوز");
//                builder.setMessage("این نرم افزار برای ثبت نام نیاز به این مجوز دارد.");
//                builder.setPositiveButton("قبول کردن", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        sentToSettings = true;
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        Uri uri = Uri.fromParts("package", LoginActivity.this.getPackageName(), null);
//                        intent.setData(uri);
//                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
//                        Toast.makeText(LoginActivity.this, "رفتن به تنظیمات نرم افزار", Toast.LENGTH_LONG).show();
//                    }
//                });
//                builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        finish();
//                    }
//                });
//                builder.show();
//            }  else {
//                //just request the permission
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE},PERMISSION_CALLBACK_CONSTANT);
//                }
//            }
//
//            SharedPreferences.Editor editor = permissionStatus.edit();
//            editor.putBoolean(android.Manifest.permission.READ_PHONE_STATE,true);
//            editor.commit();
//        } else {
//            //You already have the permission, just go ahead.
//            proceedAfterPermission();
//        }
//
//
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
//            //check if all permissions are granted
//            boolean allgranted = false;
//            for(int i=0;i<grantResults.length;i++){
//                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
//                    allgranted = true;
//                } else {
//                    allgranted = false;
//                    break;
//                }
//            }
//
//            if(allgranted){
//                proceedAfterPermission();
//            } else if(ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, android.Manifest.permission.READ_PHONE_STATE)){
//                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this,R.style.DialogeTheme);
//                builder.setTitle("در خواست مجوز");
//                builder.setMessage("این نرم افزار برای ثبت نام به این مجوز دارد");
//                builder.setPositiveButton("قبول کردن", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE},PERMISSION_CALLBACK_CONSTANT);
//                        }
//                    }
//                });
//                builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        finish();
//                    }
//                });
//                builder.show();
//            } else {
//
//            }
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_PERMISSION_SETTING) {
//            if (ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                //Got Permission
//                proceedAfterPermission();
//            }
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (sentToSettings) {
//            if (ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                //Got Permission
//                proceedAfterPermission();
//            }
//        }
//    }


//    private void proceedAfterPermission() {
//        //We've got the permission, now we can proceed further
//        telephonyManager = (TelephonyManager) this.getSystemService(getApplicationContext().TELEPHONY_SERVICE);
//        IMEI_Number_Holder = telephonyManager.getDeviceId();
//         IMEI_Number_Holder = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//      //  Toast.makeText(getBaseContext(), IMEI_Number_Holder, Toast.LENGTH_SHORT).show();
//
//    }

    public class send_sms extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv_text_pass_sended_to_number.setText("کد امنیتی ارسال شده به شماره " + mobile + " را وارد کنید");
//            pd.setMessage("در حال دریافت اطلاعات");
//            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("login_sms")));

                get_ad_list.put("mobile", MCrypt.bytesToHex(mcrypt.encrypt(mobile)));
                get_ad_list.put("imei", MCrypt.bytesToHex(mcrypt.encrypt(IMEI_Number_Holder)));


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


                final String finalResponse = response;
                final String finalResponse1 = response;
                b = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            b = finalResponse.trim();

                            b = b.replace("<azadi>", "").replace("</azadi>", "");
                            b = new String(mcrypt.decrypt(b)).trim();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //  Toast.makeText(getBaseContext(), b, Toast.LENGTH_SHORT).show();

                    }
                });
                // if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                //     response = response.replace("<azadi>", "").replace("</azadi>", "");
                // response = new String(mcrypt.decrypt(response)).trim();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!b.trim().equals("error")) {
                            if (b.trim().equals("0")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        layout_pass_btn_send.setVisibility(View.VISIBLE);
                                        layout_name_code_moref.setVisibility(View.VISIBLE);
                                        et_name_user.setVisibility(View.VISIBLE);
                                        btn_send_mobile.setVisibility(View.GONE);
                                        et_mobail_text.setVisibility(View.GONE);
                                        tv_zaman_baghi.setVisibility(View.VISIBLE);
                                        islogin_later = false;
                                        handler = new Handler(getMainLooper());
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                foo();

                                            }
                                        });

                                    }
                                });


                            } else if (b.trim().equals("1")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        et_mobail_text.setVisibility(View.GONE);
                                       // layout_pass_btn_send.setVisibility(View.VISIBLE);
                                        btn_send_mobile.setVisibility(View.GONE);
                                        tv_zaman_baghi.setVisibility(View.VISIBLE);

//                                        et_password_sender.setVisibility(View.VISIBLE);
//                                        et_name_user.setVisibility(View.VISIBLE);
//                                        btn_send_pass.setVisibility(View.VISIBLE);
//                                        btn_send_mobile.setVisibility(View.GONE);
                                        islogin_later = true;
                                        handler = new Handler(getMainLooper());
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                foo();

                                            }
                                        });

                                    }
                                });

                            }
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putInt("user_id", Integer.parseInt(response));
//                        editor.commit();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//
//                                Toast.makeText(getBaseContext(), "شما به میدان ازادی وارد شدید", Toast.LENGTH_SHORT).show();
//
//                                Intent i = new Intent(getApplicationContext(), ProFileUser.class);
//                                startActivity(i);
//                                finish();
//                            }
//                        });


                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(getBaseContext(), "شماره یا رمز وارد شده اشتباه می باشد", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                        //Toast.makeText(getBaseContext(), finalResponse2, Toast.LENGTH_SHORT).show();


                    }
                });


//                } else {
//
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//
//                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//
//
//                }


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

        }
    }

    public class send_password extends AsyncTask<Void, Void, String> {


        ProgressDialog pd = new ProgressDialog(LoginActivity.this);


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
                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("login_code")));


                String mobile2 = Base64.encodeToString(mobile.getBytes("UTF-8"), Base64.DEFAULT);
                get_ad_list.put("mobile", MCrypt.bytesToHex(mcrypt.encrypt(mobile2)));
                get_ad_list.put("password", MCrypt.bytesToHex(mcrypt.encrypt(password)));
                if (code_mortf_str.isEmpty()) {
                    get_ad_list.put("moaref_code", MCrypt.bytesToHex(mcrypt.encrypt("0")));
                } else {
                    get_ad_list.put("moaref_code", MCrypt.bytesToHex(mcrypt.encrypt(code_mortf_str.trim())));
                }
                String name2 = Base64.encodeToString(name_family.getBytes("UTF-8"), Base64.DEFAULT);
                if (name_family.trim().isEmpty()) {
                    get_ad_list.put("name", MCrypt.bytesToHex(mcrypt.encrypt("0")));
                } else {
                    get_ad_list.put("name", MCrypt.bytesToHex(mcrypt.encrypt(name2)));
                }
                get_ad_list.put("imei", MCrypt.bytesToHex(mcrypt.encrypt(IMEI_Number_Holder)));


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

                Log.e("data response", response);

                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    response = new String(mcrypt.decrypt(response)).trim();
                    Log.e("data response decode", response);

                    if (!response.trim().equals("false")) {

                        final String finalResponse = response;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (finalResponse=="\"error\""){
                                        Toast.makeText(LoginActivity.this, "رمز وارد شده اشتباه است", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    ad = new JSONObject(finalResponse);
                                    //  setFinalSharePrefrence();
                                    str_temp_user_id = ad.getString("user_id");
                                    if (ad.getString("username").isEmpty()) {
                                        show_dialog_set_username();
                                    } else {
                                        setFinalSharePrefrence();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(LoginActivity.this, "خطا در دریافت اطلاعات ارسالی", Toast.LENGTH_SHORT).show();
                                }


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

    private void setFinalSharePrefrence() {
        try {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("user_id", ad.getInt("user_id"));
            editor.putString("token", ad.getString("token"));
            editor.putString("username", ad.getString("username"));
            editor.commit();
            Toast.makeText(getBaseContext(), "شما به میدان ازادی وارد شدید", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), ProfileAndShowToitActivity.class);
            startActivity(i);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void show_dialog_set_username() {
//        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this, R.style.DialogeTheme);
//        alertDialog.setTitle("لطفا نام کاربری خود را در کادر زیر وارد کنید");
//        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_input_id, null);
//        edittext_ID = (EditText) view.findViewById(R.id.edittext_ID);
//        edittext_ID.setText(str_temp_username);
//
//        alertDialog.setView(view);
//        //alertDialog.setView(input2);
//
//        alertDialog.setPositiveButton("تایید",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (edittext_ID.getText().toString().trim().isEmpty()) {
//                            dialog.cancel();
//                        } else if (edittext_ID.getText().toString().trim().equals(str_temp_username)) {
//                            dialog.cancel();
//                        } else {
//                            string_gettext = edittext_ID.getText().toString();
//                            new send_username_id().execute();
//                        }
////
//
//                    }
//
//
//                });
//
//        alertDialog.setNegativeButton("لغو",
//
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//
//
//                    }
//                });
//
////        if (!tv_add.getText().toString().trim().equals("0")) {
//        alertDialog.show();
        new EditText(getApplicationContext()).setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        new LovelyTextInputDialog(LoginActivity.this)
                .setTopColorRes(R.color.colorAccent)
                .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                .setTitle("لطفا نام کاربری خود را در کادر زیر وارد کنید")
                .setMessage("لطفا یوزرنیم مورد نظر خود را در کادر زیر وارد کنید(به صورت انگلیسی)")
                .setHint("وارد کردن یوزر نیم")
                .setIcon(R.drawable.icon_username)
                .setInputFilter("لطفا کادر بالا را پر کنید", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("\\w+");
                    }
                })
                .setConfirmButton("تایید", new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        try {

                            if (edittext_ID.getText().toString().trim().equals(str_temp_username)) {

                            } else {
                                string_gettext = edittext_ID.getText().toString();
                                new send_username_id().execute();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("لغو", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .show();
//
//        }
    }

    public class send_username_id extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(LoginActivity.this);

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

                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(str_temp_user_id)));
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
                                show_dialog_set_username();
                            } else if (finalResponse.equals("exist")) {
                                edittext_ID.setError("نام کاربری تکراری می باشد");
                                Toast.makeText(getBaseContext(), "نام کاربری تکراری می باشد", Toast.LENGTH_SHORT).show();
                                show_dialog_set_username();
                            } else {
                                str_temp_username = finalResponse;
                                setFinalSharePrefrence();
                                //tv_profile_user_name.setText(str_temp_username);
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

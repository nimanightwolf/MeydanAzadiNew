package com.app.dadepardazan.meydanazadi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Bamdad on 12/6/2017.
 */

public class BuyingBookActivity extends MainActivity {

    JSONObject ad;
    Menu menu;
    JSONObject user_contact_details;

    TextView buy_name_book;
    Button bn_add;
    Button bn_sub;
    TextView tv_show_number;
    TextView buy_mojodi;
    int number_show = 1;
    TextView buy_price_book;
    TextView buy_price_ghablepardakht;
    EditText buy_codeposti;
    EditText buy_addres;
    EditText buy_telephon;
    Button bt_buy;

    CheckBox checkbox_buy;

    RecyclerView recyclerView;
    ComentShowAdapter adapter;
    int last_ad_id = 0;

    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;

    private MCrypt mcrypt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame;

//        content_frame = (FrameLayout) findViewById(R.id.content_frame);
//        getLayoutInflater().inflate(R.layout.show_home, content_frame);
        setContentView(R.layout.buying_book);
        toolbar = (Toolbar) findViewById(R.id.toolbarshow);
        setSupportActionBar(toolbar);

        buy_name_book = (TextView) findViewById(R.id.buy_name_book);
        tv_show_number= (TextView) findViewById(R.id.tv_show_number);
        buy_mojodi= (TextView) findViewById(R.id.buy_mojodi);
        buy_price_book= (TextView) findViewById(R.id.buy_price_book);
        buy_price_ghablepardakht= (TextView) findViewById(R.id.buy_price_ghablepardakht);
        checkbox_buy= (CheckBox) findViewById(R.id.checkbox_buy);
        buy_codeposti= (EditText) findViewById(R.id.buy_codeposti);
        buy_addres= (EditText) findViewById(R.id.buy_addres);
        buy_telephon= (EditText) findViewById(R.id.buy_telephon);
        bt_buy= (Button) findViewById(R.id.bt_buy);

        bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buy_codeposti.getText().toString().isEmpty()||(buy_addres.getText().toString().isEmpty())||buy_telephon.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"لطفا تمام فیلد های خواسته شده را پر کنید",Toast.LENGTH_SHORT).show();

                }else {
                    if (!buy_telephon.getText().toString().startsWith("09") || buy_telephon.getText().length() != 11) {
                        Toast.makeText(getApplicationContext(), "شماره موبایل وارد شده صحیح نمی باشد", Toast.LENGTH_SHORT).show();

                    }else {
                        new send_book_shop().execute();
                    }

                }
            }
        });

        checkbox_buy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settext_in_texview_descrinpt ();
            }
        });


        bn_add = (Button) findViewById(R.id.bn_add);
        bn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_show++;
                tv_show_number.setText(String.valueOf(number_show));
                settext_in_texview_descrinpt();


            }
        });
        bn_sub = (Button) findViewById(R.id.bn_sub);
        bn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number_show > 1) {
                    number_show--;
                }
                tv_show_number.setText(String.valueOf(number_show));
                settext_in_texview_descrinpt();
            }
        });


        try {
            ad = new JSONObject(getIntent().getStringExtra("ad"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
            buy_name_book.setText((ad.getString("name")));
            buy_mojodi.setText(PersianDigitConverter.PerisanNumber(numberformat.format(ad.getInt("points"))));
            buy_price_book.setText(PersianDigitConverter.PerisanNumber(numberformat.format(ad.getInt("price_off"))));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        settext_in_texview_descrinpt();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Drawable arrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        // getSupportActionBar().setHomeAsUpIndicator(arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BuyingBookActivity.this.finish();
            }
        });
    }

        public void settext_in_texview_descrinpt () {
            NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
            // ad_price_koli.setText(numberformat.format(a) + " ریال ");
            try {
                int price_ghablepardakht=ad.getInt("price_off")*number_show;
                if (checkbox_buy.isChecked()){

                    int price_ghablepardakht_menha=price_ghablepardakht-ad.getInt("points");
                    buy_price_ghablepardakht.setText(PersianDigitConverter.PerisanNumber(numberformat.format(price_ghablepardakht_menha)));
                }else {
                    buy_price_ghablepardakht.setText(PersianDigitConverter.PerisanNumber(numberformat.format(price_ghablepardakht)));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){

            return true;

        }


        @Override
        public boolean onOptionsItemSelected (MenuItem item){

            return super.onOptionsItemSelected(item);

        }


        public class send_book_shop extends AsyncTask<Void, Void, String> {
            ProgressDialog pd = new ProgressDialog(BuyingBookActivity.this);

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

                    get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("book_insert_order")));


                    //get_ad_list.put("location","");
                    //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));

                    get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                    get_ad_list.put("number", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(number_show))));

                    get_ad_list.put("id_book", MCrypt.bytesToHex(mcrypt.encrypt(ad.getString("id_book"))));

                    String name_book2 = Base64.encodeToString(ad.getString("name").getBytes("UTF-8"), Base64.DEFAULT);

                    get_ad_list.put("name_book", MCrypt.bytesToHex(mcrypt.encrypt(name_book2)));
                    if (checkbox_buy.isChecked()){
                        get_ad_list.put("use_points", MCrypt.bytesToHex(mcrypt.encrypt("1")));
                    }else{
                        get_ad_list.put("use_points", MCrypt.bytesToHex(mcrypt.encrypt("0")));
                    }

                    get_ad_list.put("zipcode", MCrypt.bytesToHex(mcrypt.encrypt(buy_codeposti.getText().toString())));

                    String address2 = Base64.encodeToString(buy_addres.getText().toString().getBytes("UTF-8"), Base64.DEFAULT);
                    get_ad_list.put("address", MCrypt.bytesToHex(mcrypt.encrypt(address2)));
                    get_ad_list.put("tell", MCrypt.bytesToHex(mcrypt.encrypt(buy_telephon.getText().toString())));

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
                        response=new String( mcrypt.decrypt(response)).trim();
                        final String finalResponse1 = response;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                String url = "http://meydane-azadi.ir/paying/pay.php?id_order="+ finalResponse1;
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);

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

        public class send_coment_list extends AsyncTask<Void, Void, String> {
            ProgressDialog pd = new ProgressDialog(BuyingBookActivity.this);

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

                    get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("insert_coment")));
                    //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));

//                get_ad_list.put("user_id", MCrypt.bytesToHex( mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
//                get_ad_list.put("news_id", MCrypt.bytesToHex( mcrypt.encrypt(ad.getString("news_id"))));
//                get_ad_list.put("text",MCrypt.bytesToHex( mcrypt.encrypt(edittext_coment_type.getText().toString().trim())));
//                get_ad_list.put("imei",MCrypt.bytesToHex( mcrypt.encrypt(IMEI_Number_Holder)));

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
                                    recyclerView.setAdapter(adapter);

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

    public void get_permistion() {
        permissionStatus = BuyingBookActivity.this.getSharedPreferences("permissionStatus", BuyingBookActivity.this.MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(BuyingBookActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(BuyingBookActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(BuyingBookActivity.this, R.style.DialogeTheme);
                builder.setTitle("در خواست مجوز");
                builder.setMessage("این نرم افزار برای ارسال کامنت نیاز به این مجوز دارد.");
                builder.setPositiveButton("قبول کردن", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_CALLBACK_CONSTANT);
                        }
                    }
                });
                builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            } else if (permissionStatus.getBoolean(Manifest.permission.READ_PHONE_STATE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(BuyingBookActivity.this, R.style.DialogeTheme);
                builder.setTitle("در خواست مجوز");
                builder.setMessage("این نرم افزار برای ارسال کامنت نیاز به این مجوز دارد.");
                builder.setPositiveButton("قبول کردن", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", BuyingBookActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(BuyingBookActivity.this, "رفتن به تنظیمات نرم افزار", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_CALLBACK_CONSTANT);
                }
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.READ_PHONE_STATE, true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(BuyingBookActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BuyingBookActivity.this, R.style.DialogeTheme);
                builder.setTitle("در خواست مجوز");
                builder.setMessage("این نرم افزار برای ارسال کامنت نیاز به این مجوز دارد");
                builder.setPositiveButton("قبول کردن", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_CALLBACK_CONSTANT);
                        }
                    }
                });
                builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(BuyingBookActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(BuyingBookActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }


    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        telephonyManager = (TelephonyManager) this.getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        IMEI_Number_Holder = telephonyManager.getDeviceId();
        // IMEI_Number_Holder = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

    }


}